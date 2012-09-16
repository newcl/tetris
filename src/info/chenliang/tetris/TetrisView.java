package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TetrisView extends SurfaceView implements Callback{

	private Paint paint;
	
	private SurfaceHolder surfaceHolder;
	private BlockContainer blockContainer;
	private BlockGenerator blockGenerator;
	
	
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
	
	private List<GameObject> gameObjects = new ArrayList<GameObject>(); 
	
	private float gravity = 0.5f;
	private float angleSpeed = 33;
	
	private int leftMarginWidth = 30, topMarginHeight=30;
	private int rightMarginWidth=120, bottomMarginHeight=30;
	
	private int containerWidth;
	private int containerHeight;
	
	private int currentScore, targetScore;
	
	private Block holdBlock;
	
	private boolean hasHeldInThisTurn;
	
	public TetrisView(Context context, BlockContainer blockContainer, BlockGenerator blockGenerator) {
		super(context);
		setKeepScreenOn(true);
		
		this.blockContainer = blockContainer;
		this.blockGenerator = blockGenerator;
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		testBlockDownInterval = 1000;
		
		currentAction = BlockControlAction.NONE;
		paint = new Paint();
		paint.setAntiAlias(true);
	}
	
	private void initViewParams(){
		final int screenWidth = getWidth();
		final int screenHeight = getHeight();
		
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
	
	public void gameTick(int timeElapsed){
		if(!paused)
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
					while(blockContainer.canMoveDown(currentBlock))
					{
						currentBlock.translate(0, 2);
					}
					
					fixCurrentBlock();
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
			
			while(blockContainer.canMoveDown(shadowBlock))
			{
				shadowBlock.translate(0, 2);
			}
			
			tickGameObjects(timeElapsed);
			
			tickScore();
		}
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
			for(int col=0; col < blockContainer.getNumCols(); col++)
			{
				BlockContainerCell containerCell = containerRow.getColumn(col);
				
				GameObject gameObject = null;
				
				gameObject = new GameObject(xOffset+cellSize/4, yOffset+cellSize/4, containerCell.getColor());
				gameObject.setShape(new RectangleShape(cellSize/2, cellSize/2));
				gameObject.setSpeedParams(floatRandom(8), floatRandom(8), 0, gravity, angleSpeed*randomSign());
				gameObjects.add(gameObject);
				
				gameObject = new GameObject(xOffset+cellSize/2, yOffset+cellSize/4, containerCell.getColor());
				gameObject.setShape(new RectangleShape(cellSize/2, cellSize/2));
				gameObject.setSpeedParams(floatRandom(8), floatRandom(8), 0, gravity, angleSpeed*randomSign());
				gameObjects.add(gameObject);
				
				gameObject = new GameObject(xOffset+cellSize/4, yOffset+cellSize/2, containerCell.getColor());
				gameObject.setShape(new RectangleShape(cellSize/2, cellSize/2));
				gameObject.setSpeedParams(floatRandom(8), floatRandom(8), 0, gravity, angleSpeed*randomSign());
				gameObjects.add(gameObject);
				
				gameObject = new GameObject(xOffset+cellSize/2, yOffset+cellSize/2, containerCell.getColor());
				gameObject.setShape(new RectangleShape(cellSize/2, cellSize/2));
				gameObject.setSpeedParams(floatRandom(8), floatRandom(8), 0, gravity, angleSpeed*randomSign());
				gameObjects.add(gameObject);
				
				
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
		return canPutNextBlockInContainer;
	}
	
	private void clearScore()
	{
		currentScore = 0;
		targetScore = 0;
	}
														
	private Block generateNextBlock()
	{
		Block block = blockGenerator.generate();
		return block;
	}
	
	public void gameInit()
	{
		currentBlock = generateNextBlock();
		nextBlock = generateNextBlock();
		Assert.judge(findPositionForBlock(currentBlock), "Unabled to init position for the 1st block.");
		updateShaowBlock();
	}
	
	private boolean findPositionForBlock(Block block)
	{
		int x = blockContainer.getNumCols();
		int y = -block.getMinY();
		
		if(block.isOddX())
		{
			x += 1;
		}
		
		if((y + block.getMaxY()) % 2 != 0)
		{
			y += 1;
		}
		
		block.setX(x);
		block.setY(y);
		boolean validPositionFound = false;
		while(block.getY() + block.getMaxY() > 0)
		{
			if(blockContainer.collideWithContainer(block))
			{
				block.setY(block.getY() - 2);
			}
			else
			{
				validPositionFound = true;
				break;
			}
		}
		
		return validPositionFound;
	}
	
	private void updateShaowBlock()
	{
		shadowBlock = currentBlock.duplicate();
		shadowBlock.setColor(0xff000000|128<<16|128<<8|128);
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
	
	private void drawBlock(Canvas canvas, Block block){
		BlockCell[] cells = block.getCells();
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			
			Assert.judge((block.getX() + cell.x) % 2 == 0, "block position not right, should be even.");
			Assert.judge((block.getY() + cell.y) % 2 == 0, "block position not right, should be even.");
			int cellX = (block.getX() + cell.x) / 2;
			int cellY = (block.getY() + cell.y) / 2;
			drawBlockCell(canvas, cellX, cellY, block.getColor());
		}
	}
	
	private void drawBlockCell(Canvas canvas, int cellX, int cellY, int color){
		int x = leftMarginWidth+ cellX*cellSize;
		int y = topMarginHeight+ cellY*cellSize;
		
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
		
		paint.setStyle(Style.STROKE);
		paint.setColor(0xffffffff);
		canvas.drawRect(x, y, x + cellSize-1, y + cellSize-1, paint);
	}
	
	public void gameDraw(){
		
		Canvas canvas = null;
		try {
			canvas = surfaceHolder.lockCanvas();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		
		if(canvas != null)
		{
			int screenWidth = getWidth();
			int screenHeight = getHeight();
			
			int xMargin = leftMarginWidth;
			int yMargin = topMarginHeight;
			
			paint.setStyle(Style.FILL);
			paint.setColor(0xff000000);
			canvas.save();
			canvas.drawRect(0, 0, screenWidth, screenHeight, paint);

			canvas.clipRect(xMargin, yMargin, xMargin + containerWidth, yMargin + containerHeight);
			
			paint.setStyle(Style.STROKE);
			paint.setColor(0xffffffff);
			canvas.drawRect(xMargin, yMargin, xMargin + containerWidth-1, yMargin + containerHeight-1, paint);
			
			for(int row=0; row < blockContainer.getNumRows(); row++){
				BlockContainerRow containerRow = blockContainer.getRow(row);
				for(int col=0; col < blockContainer.getNumCols(); col++){
					BlockContainerCell cell = containerRow.getColumn(col);
					if(cell.getStatus() == BlockContainerCellStatus.OCCUPIED){
						drawBlockCell(canvas, col, row, cell.getColor());
					}
				}
			}
			
			drawBlock(canvas, shadowBlock);
			drawBlock(canvas, currentBlock);
			
			drawGameObjects(canvas);
			canvas.restore();
			canvas.clipRect(0, 0, screenWidth, screenHeight);
			drawInfo(canvas);
			
			if(paused)
			{
				paint.setTextSize(40);
				paint.setColor(0xffffffff);
				paint.setTextAlign(Align.CENTER);
				canvas.drawText("Paused", leftMarginWidth + containerWidth/2, topMarginHeight+containerHeight/2, paint);
			}
			try {
				surfaceHolder.unlockCanvasAndPost(canvas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	private int getFontHeight()
	{
		FontMetrics fontMetrics = paint.getFontMetrics();
		return (int)Math.abs(FloatMath.ceil(fontMetrics.descent - fontMetrics.ascent));
				
	}
	
	private void drawInfo(Canvas canvas)
	{
		int yOffset = topMarginHeight;
		int xOffset = leftMarginWidth + containerWidth;
		
		paint.setTextSize(20);
		paint.setColor(Color.WHITE);
		paint.setStyle(Style.FILL);
		paint.setTextAlign(Align.LEFT);

		int fontHeight = getFontHeight();
		canvas.drawText("Next", xOffset + 8, yOffset + fontHeight, paint);
		
		yOffset += getFontHeight() + 4;
		
		drawBlockInUi(canvas, nextBlock, xOffset + rightMarginWidth/2, yOffset + cellSize * 2);
		
		yOffset += cellSize * 4 + 4;
		
		paint.setColor(0xffffffff);
		canvas.drawText("Hold", xOffset + 8, yOffset + fontHeight, paint);
		
		yOffset += getFontHeight() + 4;
		
		drawBlockInUi(canvas, holdBlock, xOffset + rightMarginWidth/2, yOffset + cellSize * 2);
		
		yOffset += cellSize * 4 + 4;
		paint.setColor(0xffffffff);
		canvas.drawText("Score:" + currentScore, xOffset + 8, yOffset + fontHeight, paint);
	}
	
	private void drawBlockInUi(Canvas canvas, Block block, int x, int y)
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
				
				paint.setStyle(Style.FILL);
				paint.setColor(block.getColor());
				canvas.drawRect(bx, by, bx + cellSize, by + cellSize, paint);
				
				paint.setStyle(Style.STROKE);
				paint.setColor(0xffffffff);
				canvas.drawRect(bx, by, bx + cellSize-1, by + cellSize-1, paint);
			}
			
			
		}
	}
	
	private void drawGameObjects(Canvas canvas)
	{
		for(int i=0;i < gameObjects.size();i++)
		{
			GameObject gameObject = gameObjects.get(i);
			if(!gameObject.isFinished())
			{
				gameObject.draw(canvas, paint);				
			}
		}
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		initViewParams();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
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
	
	public void pause()
	{
		
	}
	
	public void start()
	{
		
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

	public Block getCurrentBlock() {
		return currentBlock;
	}

	public Block getNextBlock() {
		return nextBlock;
	}

	public Block getHoldBlock() {
		return holdBlock;
	}
}
