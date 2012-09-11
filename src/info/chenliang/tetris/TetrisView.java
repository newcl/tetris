package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TetrisView extends SurfaceView implements Runnable, Callback{

	private Paint paint = new Paint();
	private Thread paintThread;
	private SurfaceHolder surfaceHolder;
	private BlockContainer blockContainer;
	private BlockGenerator blockGenerator;
	
	private BlockControlAction currentAction;
	
	private final static int REFRESH_INVERVAL = 30;
	
	public BlockControlAction getCurrentAction() {
		return currentAction;
	}

	public void setCurrentAction(BlockControlAction currentAction) {
		this.currentAction = currentAction;
		if(this.currentAction == BlockControlAction.NONE)
		{
			lastActionTime = 0;
			lastAction = BlockControlAction.NONE;
		}
	}

	private int testBlockDownInterval;
	private int testBlockDownTime;
	
	private long lastTickTime;
	
	private int xMargin = 30;
	private int yMargin = 30;
	private int cellSize;
	
	private Block currentBlock;
	private Block nextBlock;
	
	private int inputCheckInterval = 80;
	private long lastActionTime;
	private BlockControlAction lastAction = BlockControlAction.NONE;
	
	private Block shadowBlock;
	
	private List<GameObject> gameObjects = new ArrayList<GameObject>(); 
	
	public TetrisView(Context context, BlockContainer blockContainer, BlockGenerator blockGenerator) {
		super(context);
		setKeepScreenOn(true);
		
		this.blockContainer = blockContainer;
		this.blockGenerator = blockGenerator;
		
		paintThread = new Thread(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		testBlockDownInterval = 1000;
		
		currentAction = BlockControlAction.NONE;
	}
	
	private void initViewParams(){
		final int screenWidth = getWidth();
		final int screenHeight = getHeight();
		
		final int numCols = blockContainer.getNumCols();
		final int numRows = blockContainer.getNumRows();
		
		int xSize = (screenWidth - xMargin*2) / numCols;
		int ySize = (screenHeight - yMargin*2) / numRows;
		
		cellSize = Math.min(xSize, ySize);
		xMargin = (screenWidth - cellSize*numCols) / 2;
		yMargin = (screenHeight - cellSize*numRows) / 2;
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
	
	private void gameTick(int timeElapsed){
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
			/*
			List<BlockContainerRow> fullRows = blockContainer.getFullRows();
			for(int i=0;i < fullRows.size() ;i++)
			{
				BlockContainerRow containerRow = fullRows.get(i);
				int xOffset = xMargin;
				int yOffset = yMargin + containerRow.getRow()*cellSize;
				for(int col=0; col < blockContainer.getNumCols(); col++)
				{
					BlockContainerCell containerCell = containerRow.getColumn(col);
					
					GameObject gameObject = null;
					
					gameObject = new GameObject(xOffset + cellSize/ 4, yOffset + cellSize/ 4, containerCell.getColor());
					gameObject.setShape(new RectangleShape(gameObject.getX(), gameObject.getY(), cellSize, cellSize));
					gameObjects.add(gameObject);
					
					gameObject = new GameObject(xOffset + cellSize/ 2, yOffset + cellSize/ 4, containerCell.getColor());
					gameObject.setShape(new RectangleShape(gameObject.getX(), gameObject.getY(), cellSize, cellSize));
					gameObjects.add(gameObject);
					
					gameObject = new GameObject(xOffset + cellSize/ 4, yOffset + cellSize/ 2, containerCell.getColor());
					gameObject.setShape(new RectangleShape(gameObject.getX(), gameObject.getY(), cellSize, cellSize));
					gameObjects.add(gameObject);
					
					gameObject = new GameObject(xOffset + cellSize/ 2, yOffset + cellSize/ 2, containerCell.getColor());
					gameObject.setShape(new RectangleShape(gameObject.getX(), gameObject.getY(), cellSize, cellSize));
					gameObjects.add(gameObject);
				}
				
			}
			*/
			blockContainer.removeFullRows();		
		}
		
		if(testBlockDownTime >= testBlockDownInterval){
			testBlockDownTime -= testBlockDownInterval;
		}
		
		updateShaowBlock();
		
		while(blockContainer.canMoveDown(shadowBlock))
		{
			shadowBlock.translate(0, 2);
		}
		
		tickGameObjects(timeElapsed);
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
			blockContainer.reset();
		}
		
		return canPutNextBlockInContainer;
	}
														
	private Block generateNextBlock()
	{
		Block block = blockGenerator.generate();
		return block;
	}
	
	private void gameInit()
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
	
	public void run() {
		// TODO Auto-generated method stub
		lastTickTime = System.currentTimeMillis();
		
		gameInit();
		while(true)
		{
			long currentTime = System.currentTimeMillis();
			int timeElapsed = (int)(currentTime - lastTickTime);
			
			gameTick(timeElapsed);
			gameDraw();
			removeDeadGameObjects();
			
			lastTickTime = currentTime;
			try {
				Thread.sleep(REFRESH_INVERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
	
	private void drawBlock(Canvas canvas, Block block){
		BlockCell[] cells = block.getCells();
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			int cellX = (block.getX() + cell.x) / 2;
			int cellY = (block.getY() + cell.y) / 2;
			drawBlockCell(canvas, cellX, cellY, block.getColor());
		}
	}
	
	private void drawBlockCell(Canvas canvas, int cellX, int cellY, int color){
		int x = xMargin + cellX*cellSize;
		int y = yMargin + cellY*cellSize;
		
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		canvas.drawRect(x, y, x + cellSize, y + cellSize, paint);
		
		paint.setStyle(Style.STROKE);
		paint.setColor(0xffffffff);
		canvas.drawRect(x, y, x + cellSize-1, y + cellSize-1, paint);
	}
	
	private void gameDraw(){
		
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
			
			int containerWidth = screenWidth - xMargin*2;
			int containerHeight = screenHeight - yMargin*2;
			
			paint.setStyle(Style.FILL);
			paint.setColor(0xff000000);
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
				/*
				boolean drawRow = true;
				if(containerRow.isFull())
				{
					drawRow = System.currentTimeMillis() % 700 / 350 == 1;
				}
				if(drawRow)
				{
					for(int col=0; col < blockContainer.getNumCols(); col++){
						BlockContainerCell cell = containerRow.getColumn(col);
						if(cell.getStatus() == BlockContainerCellStatus.OCCUPIED){
							drawBlockCell(canvas, col, row, cell.getColor());
						}
					}	
				}
				*/
			}
			
			drawBlock(canvas, shadowBlock);
			drawBlock(canvas, currentBlock);
			drawGameObjects(canvas);
			try {
				surfaceHolder.unlockCanvasAndPost(canvas);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void drawGameObjects(Canvas canvas)
	{
		for(int i=0;i < gameObjects.size();i++)
		{
			GameObject gameObject = gameObjects.get(i);
			gameObject.draw(canvas, paint);
		}
	}
	
	private void drawButton(Canvas canvas, Rect rect, int color){
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		canvas.drawRect(rect, paint);
		
		paint.setStyle(Style.STROKE);
		paint.setColor(0xffffffff);
		canvas.drawRect(rect, paint);
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initViewParams();
		paintThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}
	
}
