package info.chenliang.tetris;

import info.chenliang.debug.Assert;
import info.chenliang.ds.Vector3d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.DynamicZBuffer;
import info.chenliang.fatrock.ProjectionType;
import info.chenliang.fatrock.Vertex3d;
import info.chenliang.fatrock.ZBufferComparerGreaterThan;
import info.chenliang.fatrock.trianglerenderers.TriangleRendererConstant;
import info.chenliang.tetris.sound.SoundManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.KeyEvent;

public class Tetris implements Runnable{
	private static int ROW_COUNT = 20;
	private static int COLUMN_COUNT = 10;
	
	private final static int REFRESH_INVERVAL = 33;
	
	private Thread gameThread;
	private long lastTickTime;
	private boolean paused;
	
	private BlockControlAction currentAction;

	private int testBlockDownInterval;
	private int testBlockDownTime;
	
	private int cellSize;
	
	private Block currentBlock;
	private Block nextBlock;
	
	private int inputCheckInterval = 80;
	private long lastActionTime;
	private BlockControlAction lastAction = BlockControlAction.NONE;
	
	private Block shadowBlock;
	
	private BlockContainer blockContainer;
	private BlockGenerator blockGenerator;
	
	private float gravity = 0.5f;
	private float angleSpeed = 33;
	
	private int leftMarginWidth = 30, topMarginHeight=30;
	private int rightMarginWidth=120, bottomMarginHeight=30;
	
	private int containerWidth;
	private int containerHeight;
	
	private int currentScore, targetScore;
	
	private Block holdBlock;
	
	private boolean hasHeldInThisTurn;
	
	private List<GameObject> gameObjects; 
	
	private GameCanvas gameCanvas;

	private boolean running;
	
	private static final int STATE_NORMAL = 0;
	private static final int STATE_BLOCK_INSTANT_DOWN = 1;
	
	private int state;
	
	private int instanceDownTotal;
	private int instanceDownStep;
	private int instanceDownCount;
	private int instanceDownStartY;
	
	private SoundManager soundManager;
	private Camera camera;
	private TriangleRendererConstant triangleRendererConstant;
	private float near, far;
	private float viewAngle;
	private float cubeZ;
	private float scoreInfoX, scoreInfoY;

	private int cameraScreenWidth, cameraScreenHeight;
	private int cubeSize;
	private int count;
	
	public Tetris(GameCanvas gameCanvas)
	{
		this.gameCanvas = gameCanvas;
	}
	
	public Tetris(GameCanvas gameCanvas, GameData gameData)
	{
		this.gameCanvas = gameCanvas;
		if(gameData != null)
		{
			initGameData(gameData);
		}
	}
	
	private void initGameData(GameData gameData)
	{
		
	}
	
	public void gameInit()
	{
		state = STATE_NORMAL;
		
		blockContainer = new BlockContainer(ROW_COUNT, COLUMN_COUNT);
		blockGenerator = new BlockGenerator();
		gameObjects = new ArrayList<GameObject>();
		currentBlock = generateNextBlock();
		nextBlock = generateNextBlock();
		Assert.judge(findPositionForBlock(currentBlock), "Unabled to init position for the 1st block.");
		updateShaowBlock();
		running = true;
		lastTickTime = System.currentTimeMillis();
		currentAction = BlockControlAction.NONE;
		testBlockDownInterval = 800;
		testBlockDownTime = 0;
		while(!gameCanvas.isReady())
		{
		}
		
		initViewParams();
		soundManager = new SoundManager();
		soundManager.init();
		
		init3dParams();
	}
	
	private void init3dParams()
	{
		viewAngle = 90;
		near = 10;
		far = 200;
		cubeZ = near + (far - near)/2;
		
		float d = (float)(1 / Math.tan(Math.toRadians(viewAngle/2)));
		cubeSize = cellSize;
		float aspectRatio = 1;
		
		cameraScreenWidth = 2 * cubeSize;
		cameraScreenHeight = 2 * cubeSize;

		cubeZ = cameraScreenHeight*d/2 + cubeSize/2; 
		cubeZ -= 15;
		camera = new Camera(new Vector3d(0, 0, 0), new Vector3d(0, 0, 1), new Vector3d(0, 1, 0), viewAngle, near, far, cameraScreenWidth, cameraScreenHeight, 0, 0, ProjectionType.ORTHOGONALITY);
		triangleRendererConstant = new TriangleRendererConstant(gameCanvas, new DynamicZBuffer(cameraScreenWidth, cameraScreenHeight, new ZBufferComparerGreaterThan()), true, 0xffffffff);
//		triangleRendererConstant = new TriangleRendererConstant(gameCanvas, new DynamicZBuffer(gameCanvas.getCanvasWidth(), gameCanvas.getCanvasHeight(), new ZBufferComparerGreaterThan()), true, 0xffffffff);
	}
	
	public void run() {
		gameInit();
		while(running)
		{
			long currentTime = System.currentTimeMillis();
			int timeElapsed = (int)(currentTime - lastTickTime);

			gameTick(timeElapsed);
			gameDraw();
			removeDeadGameObjects();
			
			lastTickTime = currentTime;
			synchronized (this) {
				try {
					this.wait(REFRESH_INVERVAL);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public void gameTick(int timeElapsed){
		if(!paused)
		{
			if(state == STATE_NORMAL)
			{
				testBlockDownTime += timeElapsed;
				
				boolean inputDown = false;
				
				if(shouldCheckInputAction())
				{
					switch(currentAction){
					case TRANSFORM:
						if(blockContainer.canRotate(currentBlock)){
							currentBlock.rotate();
						}
						break;
					case MOVE_LEFT:
						if(blockContainer.canMoveLeft(currentBlock)){
							currentBlock.translate(-2, 0);
						}
						break;
					case MOVE_RIGHT:
						if(blockContainer.canMoveRight(currentBlock)){
							currentBlock.translate(2, 0);
						}
						break;
					case NONE:
						break;
					case MOVE_DOWN:
						inputDown = true;
						break;
					case INSTANT_DOWN:
						instanceDownStartY = currentBlock.y;
						
						while(blockContainer.canMoveDown(currentBlock))
						{
							currentBlock.translate(0, 2);
						}
						instanceDownCount = 0;
						instanceDownTotal = (currentBlock.y - instanceDownStartY) / 2;
						instanceDownStep = instanceDownTotal / 3;
						state = STATE_BLOCK_INSTANT_DOWN;
						break;
					default:
						break;
					}	
					
					if(currentAction != BlockControlAction.NONE)
					{
						lastActionTime = System.currentTimeMillis();
						lastAction = currentAction;
					}
				}
				
				
				boolean testDown = (currentAction != BlockControlAction.INSTANT_DOWN) &&  (inputDown || testBlockDownTime >= testBlockDownInterval);
				if(testDown){
					if(blockContainer.canMoveDown(currentBlock)){
						if(count-- <= 0)
						{
							//add3dCubeForBlock(currentBlock);	
							count = 0;
						}
						
						currentBlock.translate(0, 2);
					}else{
						fixCurrentBlock();
					}				
				}
				
				if(blockContainer.isFullRowDetected())
				{
					List<BlockContainerRow> fullRows = blockContainer.getFullRows();
					targetScore = targetScore + calculateScore(fullRows.size());
					addRemoveFullLineEffect(fullRows);
					blockContainer.removeFullRows();		
				}
				
				if(testBlockDownTime >= testBlockDownInterval)
				{
					testBlockDownTime -= testBlockDownInterval;
				}
				
				updateShaowBlock();
				
				tickScore();
			}
			else if(state == STATE_BLOCK_INSTANT_DOWN)
			{
				int step = instanceDownStep;
				if(instanceDownCount + step > instanceDownTotal)
				{
					step = instanceDownTotal - instanceDownCount;
				}
				
				instanceDownCount += step;
				
				if(blockContainer.canMoveDown(currentBlock))
				{
					currentBlock.translate(0, step);
				}
				else
				{
					soundManager.play(R.raw.hit);
					fixCurrentBlock();					
				}
			}
			
			tickGameObjects(timeElapsed);
		}
	}
	
	
	private void drawContainerBackground()
	{
		int gray = compositeColor(128, 128, 128,100);
		
		for(int row=0;row < ROW_COUNT;row++)
		{
			int y = topMarginHeight+row*cellSize;
			gameCanvas.fillRect(leftMarginWidth, y, leftMarginWidth+containerWidth, y+1, gray, 100);
		}
		
		for(int col=0;col < COLUMN_COUNT;col++)
		{
			int x = leftMarginWidth + col*cellSize;
			gameCanvas.fillRect(x, topMarginHeight, x+1, topMarginHeight+containerHeight, gray, 100);
		}
	}
	
	Vertex3d v1, v2,v3;
	Vector3d color;
	
	public void gameDraw(){
		boolean success = gameCanvas.startDraw();
		if(success)
		{
			long st = System.currentTimeMillis();
			Paint paint = gameCanvas.getPaint();
			int screenWidth = gameCanvas.getCanvasWidth();
			int screenHeight = gameCanvas.getCanvasHeight();
			
			int xMargin = leftMarginWidth;
			int yMargin = topMarginHeight;
			
			paint.setStrokeWidth(2);
			gameCanvas.fillRect(0, 0, screenWidth, screenHeight, 0, 0xff);
			
			paint.setStrokeWidth(1);

			gameCanvas.drawRect(xMargin, yMargin, xMargin + containerWidth, yMargin + containerHeight, 0xffffff,0xff);
			//gameCanvas.clipRect(xMargin, yMargin, xMargin + containerWidth, yMargin + containerHeight);
			drawContainerBackground();
			long ct = System.currentTimeMillis();
			st = ct;
			for(int row=0; row < blockContainer.getNumRows(); row++){
				BlockContainerRow containerRow = blockContainer.getRow(row);
				for(int col=0; col < blockContainer.getNumCols(); col++){
					BlockContainerCell cell = containerRow.getColumn(col);
					if(cell.status == BlockContainerCellStatus.OCCUPIED){
						drawBlockCell(col, row, cell.color,0,0,255,true);
					}
				}
			}
			
			ct = System.currentTimeMillis();
			Log.d("info.chenliang.tetris", "p1 " + (ct-st));
			st = ct;
			if(state == STATE_NORMAL)
			{
				drawBlock(shadowBlock, 0, 0,255,true);
			}
			else if(state == STATE_BLOCK_INSTANT_DOWN)
			{
				int alpha = 20;
				for(int y=instanceDownStartY;y<currentBlock.y;y+=2)
				{
					int cellDiff = (currentBlock.y-y) / 2;
					alpha = Math.min(65, alpha + 3) ;
					drawBlock(currentBlock, 0, -cellSize*cellDiff, alpha,false);	
				}
			}
			
			drawBlock(currentBlock, 0, 0,255,true);
			ct = System.currentTimeMillis();
			Log.d("info.chenliang.tetris", "p2 " + (ct-st));
			st = ct;
			
			drawGameObjects();
			
			//gameCanvas.clipRect(0, 0, screenWidth, screenHeight);
			drawInfo();
			
			if(paused)
			{
				gameCanvas.drawText("Paused", leftMarginWidth + containerWidth/2, topMarginHeight+containerHeight/2, 0xffffffff, GameCanvas.ALIGN_CENTER);
			}
			
			//triangleRenderer.fillTriangle(v1, v2, v3, color);
			ct = System.currentTimeMillis();
			Log.d("info.chenliang.tetris", "p3 " + (ct-st));
			st = ct;
			gameCanvas.endDraw();
			ct = System.currentTimeMillis();
			Log.d("info.chenliang.tetris", "p4 " + (ct-st));
		}
	}
	
	private void drawBlock(Block block, float offsetX, float offsetY, int alpha, boolean outline){
		BlockCell[] cells = block.getCells();
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			
			Assert.judge((block.getX() + cell.x) % 2 == 0, "block position not right, should be even.");
			Assert.judge((block.getY() + cell.y) % 2 == 0, "block position not right, should be even.");
			int cellX = (block.getX() + cell.x) / 2;
			int cellY = (block.getY() + cell.y) / 2;
			drawBlockCell(cellX, cellY, block.getColor(), offsetX, offsetY, alpha, outline);
		}
	}
	
	private Rect addEmptyRects(List<Rect> rects, int cellX, int cellY, float offsetX, float offsetY)
	{
		Rect rect = new Rect();
		rect.left = (int)(leftMarginWidth+ cellX*cellSize + offsetX);
		rect.top = (int)(topMarginHeight+ cellY*cellSize + offsetY);
		rect.right = rect.left + cellSize+2;
		rect.bottom = rect.top + cellSize+2;
		
		rects.add(rect);
		
		return rect;
	}
	
	private void glowBlock(Block block)
	{
		float blockOffset = 3f;
		drawBlock(block, -blockOffset, 0,255,false);
		drawBlock(block, 0, -blockOffset,255,false);
		drawBlock(block, +blockOffset, 0,255,false);
		drawBlock(block, 0, +blockOffset,255,false);
		
		drawBlock(block, -blockOffset, -blockOffset,255,false);
		drawBlock(block, +blockOffset, -blockOffset,255,false);
		drawBlock(block, -blockOffset, +blockOffset,255,false);
		drawBlock(block, +blockOffset, +blockOffset,255,false);
		
		float left = leftMarginWidth + (block.getX() + block.minX) / 2 * cellSize - blockOffset;
		float right = leftMarginWidth + (block.getX() + block.maxX) / 2 * cellSize + blockOffset;
		float top = topMarginHeight + (block.getY() + block.minY) / 2 * cellSize - blockOffset;
		float bottom = topMarginHeight + (block.getY() + block.maxY) / 2 * cellSize + blockOffset;
		
		int blurSize = 3;
		
		int color = block.getColor();
		int blockRed = (color & 0xff0000) >> 16;
		int blockGreen = (color & 0xff00) >> 8;
		int blockBlue = (color & 0xff0000);
		
		Map<String, String> cellMark = new HashMap<String, String>();
		BlockCell[] clone = block.getCells(); 
		for(int i=0; i < clone.length ;i ++)
		{
			cellMark.put(clone[i].x+""+clone[i].y, "");
		}
		
		List<Rect> rects = new ArrayList<Rect>();
		for(int x=block.minX; x < block.maxX; x+=2)
		{
			for(int y=block.minY; y < block.maxY;y+=2)
			{
				if(!cellMark.containsKey(x+""+y))
				{
					int cellX = (block.getX() + x) / 2;
					int cellY = (block.getY() + y) / 2;
					
					addEmptyRects(rects, cellX, cellY, -blockOffset, 0);
					addEmptyRects(rects, cellX, cellY, 0, -blockOffset);
					addEmptyRects(rects, cellX, cellY, +blockOffset, 0);
					addEmptyRects(rects, cellX, cellY, 0, +blockOffset);
					
					addEmptyRects(rects, cellX, cellY, -blockOffset, -blockOffset);
					addEmptyRects(rects, cellX, cellY, +blockOffset, -blockOffset);
					addEmptyRects(rects, cellX, cellY, -blockOffset, +blockOffset);
					addEmptyRects(rects, cellX, cellY, +blockOffset, +blockOffset);
					
				}
			}
		}
		
		for(int x=(int)left;x<=right;x++)
		{
			nextPixel:for(int y=(int)top;y <=bottom;y++)
			{
				for(Rect rect:rects)
				{
					if(rect.contains(x, y))
					{
						continue nextPixel;
					}
				}
				
				int blockColorCount = 0;
				
				int red = 0, green = 0, blue = 0;
				if(y-1>=top)
				{
					blockColorCount ++;
				}
				
				if(x - 1 >= left)
				{
					blockColorCount ++;
				}
				
				if(x + 1 <= right)
				{
					blockColorCount ++;
				}
				
				if(y + 1 <= top)
				{
					blockColorCount ++;
				}
				
				red += blockColorCount*blockRed;
				green+= blockColorCount*blockGreen;
				blue += blockColorCount*blockBlue;
				
				red /= 9;
				green /= 9;
				blue /= 9;
				
				gameCanvas.fillRect(x, y, x+1, y+1, compositeColor(red, green, blue,0xff),0xff);
			}
		}
		
		drawBlock(block, 0, 0,255,true);
	}
	
	private int compositeColor(int red, int green, int blue, int alpha)
	{
		return alpha<<24|red<<16|green<<8|blue;
	}
	
	private void drawBlockCell(float cellX, float cellY, int color, float offsetX, float offsetY, int alpha, boolean outline){
		float x = leftMarginWidth+ cellX*cellSize + offsetX;
		float y = topMarginHeight+ cellY*cellSize + offsetY;
		
		gameCanvas.fillRect(x, y, x + cellSize, y + cellSize, color, alpha);
		if(outline)
		{
			gameCanvas.drawRect(x, y, x + cellSize-1, y + cellSize-1, 0xffffff, 0xff);			
		}
	}
	
	private void drawInfo()
	{
		int yOffset = topMarginHeight;
		int xOffset = leftMarginWidth + containerWidth;

		int fontHeight = gameCanvas.getFontHeight();
		gameCanvas.drawText("Next", xOffset + 8, yOffset + fontHeight, 0xffffffff, GameCanvas.ALIGN_LEFT);
		
		yOffset += fontHeight + 4;
		
		drawBlockInUi(nextBlock, xOffset + rightMarginWidth/2, yOffset + cellSize * 2);
		
		yOffset += cellSize * 4 + 4;
		
		gameCanvas.drawText("Hold", xOffset + 8, yOffset + fontHeight, 0xffffffff, GameCanvas.ALIGN_LEFT);
		
		yOffset += fontHeight + 4;
		
		drawBlockInUi(holdBlock, xOffset + rightMarginWidth/2, yOffset + cellSize * 2);
		
		yOffset += cellSize * 4 + 4;
		gameCanvas.drawText("Score:" + currentScore, xOffset + 8, yOffset + fontHeight, 0xffffffff, GameCanvas.ALIGN_LEFT);
		scoreInfoX = xOffset + 8;
		scoreInfoY = yOffset + fontHeight;
	}
	
	private void drawBlockInUi(Block block, int x, int y)
	{
		if(block != null)
		{
			Vector2d size = block.getSize();
			BlockCell[] cells = block.getCells();
			for(int i=0; i < cells.length; i++)
			{
				float cx = (cells[i].x - block.minX)/2 - size.getX()/2;
				float cy = (cells[i].y - block.minY)/2 - size.getY()/2;
				
				float bx = x + cx*cellSize;
				float by = y + cy*cellSize;
				
				gameCanvas.fillRect(bx, by, bx + cellSize, by + cellSize, block.getColor(), 0xff);
				gameCanvas.drawRect(bx, by, bx + cellSize-1, by + cellSize-1, 0xffffffff, 0xff);
			}
		}
	}
	
	private void add3dCube(float xOffset, float yOffset, int color)
	{
		int dx = camera.getScreenWidth()/2-(int)cubeSize/2;
		int dy = camera.getScreenHeight()/2-(int)cubeSize/2;
		
		float finalX = scoreInfoX;//-cubeSize/2; 
		float finalY = scoreInfoY;//-cubeSize/2;
		FloatingCube gameObject = new FloatingCube((int)(xOffset-dx), (int)(yOffset-dy), cubeZ, color, cubeSize, camera, triangleRendererConstant);
		
		float middleX = gameObject.getxOffset() + (finalX - gameObject.getxOffset())/2+randomSign()*((float)Math.random()*10);
		float middleY = gameObject.getyOffset() + (finalY - gameObject.getyOffset())/2+randomSign()*((float)Math.random()*10);
		
		//gameObject.getPath().addPosition(new Vector3d(middleX, middleY, gameObject.getZ() - (float)Math.random()*20));
		gameObject.getPath().addPosition(new Vector3d(finalX, finalY, gameObject.getZ()));
		
		gameObject.initPath();
		
		gameObjects.add(gameObject);
	}
	
	private void add3dCubeForBlock(Block block)
	{
		BlockCell[] cells = block.getCells();
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			
			Assert.judge((block.getX() + cell.x) % 2 == 0, "block position not right, should be even.");
			Assert.judge((block.getY() + cell.y) % 2 == 0, "block position not right, should be even.");
			int cellX = (block.getX() + cell.x) / 2;
			int cellY = (block.getY() + cell.y) / 2;
			
			float x = leftMarginWidth+ cellX*cellSize;
			float y = topMarginHeight+ cellY*cellSize;
			
			add3dCube(x, y, block.color);
//			break;
		}
	}
	
	private void drawGameObjects()
	{
		for(int i=0;i < gameObjects.size();i++)
		{
			GameObject gameObject = gameObjects.get(i);
			if(!gameObject.isFinished())
			{
				gameObject.draw(gameCanvas);				
			}
		}
	}
	
	public boolean isPaused() {
		return paused;
	}

	public void setPaused(boolean paused) {
		this.paused = paused;
	}
	
	private Block generateNextBlock()
	{
		Block block = blockGenerator.generate();
		return block;
	}
	
	private boolean findPositionForBlock(Block block)
	{
		int x = blockContainer.getNumCols();
		int y = -block.minY;
		
		if(block.isOddX())
		{
			x += 1;
		}
		
		block.setX(x);
		block.setY(y);
		
		while(blockContainer.collideWithContainer(block))
		{
			block.setY(block.getY() - 2);
		}
		
		return (block.getY() + block.maxY) / 2 > 0;
	}
	
	public void hold()
	{
		
		if(hasHeldInThisTurn)
		{
			
		}
		else
		{
			if(holdBlock == null)
			{
				holdBlock = currentBlock;
				currentBlock = blockGenerator.generate();
			}
			else
			{
				Block temp = currentBlock;
				currentBlock = holdBlock;
				holdBlock = temp;	
				currentBlock.setX(holdBlock.getX());
				currentBlock.setY(holdBlock.getY());
			}
			
			boolean found = findPositionForBlock(currentBlock);
			if(!found)
			{
				blockContainer.reset();
			}
			
			hasHeldInThisTurn = true;
		}
	}
	
	private void initViewParams(){
		final int screenWidth = gameCanvas.getCanvasWidth();
		final int screenHeight = gameCanvas.getCanvasHeight();
		
		final int numCols = blockContainer.getNumCols();
		final int numRows = blockContainer.getNumRows();
		
		containerWidth = screenWidth - leftMarginWidth - rightMarginWidth;
		containerHeight = screenHeight - topMarginHeight - bottomMarginHeight;
		
		int xSize = containerWidth / numCols;
		int ySize = containerHeight / numRows;
		
		cellSize = Math.min(xSize, ySize);
		
		containerWidth = cellSize * numCols;
		containerHeight = cellSize * numRows;
		
		topMarginHeight = bottomMarginHeight = (screenHeight - containerHeight) / 2;
		rightMarginWidth = screenWidth - leftMarginWidth - containerWidth;
	}

	public BlockControlAction translateKeyToAction(int keyCode){
		BlockControlAction action = BlockControlAction.NONE;
		
		if(keyCode == KeyEvent.KEYCODE_DPAD_UP){
			action = BlockControlAction.TRANSFORM;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_DOWN){
			action = BlockControlAction.MOVE_DOWN;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_LEFT){
			action = BlockControlAction.MOVE_LEFT;
		}else if(keyCode == KeyEvent.KEYCODE_DPAD_RIGHT){
			action = BlockControlAction.MOVE_RIGHT;
		}else if(keyCode == KeyEvent.KEYCODE_SPACE){
			action = BlockControlAction.INSTANT_DOWN;
		}
		
		return action;
	}

	private boolean shouldCheckInputAction()
	{
		return currentAction != lastAction || System.currentTimeMillis() - lastActionTime >= inputCheckInterval;
	}
	
	private float floatRandom(int scalar)
	{
		return (float)(Math.random()*scalar)*randomSign();
	}
	
	private float randomSign()
	{
		int r = (int)(Math.random()*2);
		return r == 0 ? -1 : 1;
	}
	
	
	
	private int calculateScore(int numFullRows)
	{
		return numFullRows * 500;
	}
	
	private void tickScore()
	{
		if(targetScore > currentScore)
		{
			int deltaScore = (targetScore - currentScore) / 2;
			if(deltaScore <= 1)
			{
				currentScore = targetScore;
			}
			else
			{
				currentScore += deltaScore;
			}
		}
	}
	
	private void addRemoveFullLineEffect(List<BlockContainerRow> fullRows)
	{
		for(int i=0;i < fullRows.size() ;i++)
		{
			BlockContainerRow containerRow = fullRows.get(i);
			int xOffset = leftMarginWidth;
			int yOffset = topMarginHeight + containerRow.getRow()*cellSize;
			
			//int floatingCubeCount = 3 + (int)(Math.random() * 4);
			for(int col=0; col < blockContainer.getNumCols(); col++)
			{
				BlockContainerCell containerCell = containerRow.getColumn(col);
				
				if(Math.random()*4 < 1.0f)
				{
					add3dCube(xOffset, yOffset, containerCell.color);
				}
				
				xOffset += cellSize; 
			}
			
		}	
	}
	
	private void tickGameObjects(int timeElapsed)
	{
		for(int i=0;i < gameObjects.size();i++)
		{
			GameObject gameObject = gameObjects.get(i);
			gameObject.tick(timeElapsed);
		}
	}
	
	
	private boolean fixCurrentBlock()
	{
		blockContainer.fixBlock(currentBlock);
		currentBlock = nextBlock;
		boolean canPutNextBlockInContainer = findPositionForBlock(currentBlock);
		if(canPutNextBlockInContainer)
		{
			nextBlock = generateNextBlock();
			currentAction = BlockControlAction.NONE;
			testBlockDownTime = 0;
			
			updateShaowBlock();	
		}
		else
		{
			clearScore();
			blockContainer.reset();
		}
		hasHeldInThisTurn = false;
		state = STATE_NORMAL;
		return canPutNextBlockInContainer;
	}
	
	private void clearScore()
	{
		currentScore = 0;
		targetScore = 0;
	}
														
	private void updateShaowBlock()
	{
		shadowBlock = currentBlock.duplicate();
		shadowBlock.setColor(0xff000000|128<<16|128<<8|128);
		while(blockContainer.canMoveDown(shadowBlock))
		{
			shadowBlock.translate(0, 2);
		}
	}
	
	private void removeDeadGameObjects()
	{
		for(int i=gameObjects.size()-1;i >= 0;i--)
		{
			GameObject gameObject = gameObjects.get(i);
			if(gameObject.isFinished())
			{
				gameObjects.remove(i);
			}
		}
	}

	public BlockControlAction getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(BlockControlAction currentAction) {
		if(!isPaused())
		{
			this.currentAction = currentAction;
			if(this.currentAction == BlockControlAction.NONE)
			{
				lastActionTime = 0;
				lastAction = BlockControlAction.NONE;
			}			
		}
	}

	public Block getCurrentBlock() {
		return currentBlock;
	}

	public Block getNextBlock() {
		return nextBlock;
	}
	
	public Block getHoldBlock() {
		return holdBlock;
	}
	
	public GameData toGameData()
	{
		return null;
	}
	
	public void startGame()
	{
		if(gameThread == null){
			gameThread = new Thread(this);
			gameThread.start();
		}
	}
	
	public void stopGame()
	{
		running = false;
		synchronized (this) {
			notifyAll();
		}
		gameThread = null;
	}
	
}
