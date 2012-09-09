package info.chenliang.tetris;

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
	}

	private int testBlockDownInterval;
	private int testBlockDownTime;
	
	private long lastTickTime;
	
	private int xMargin = 30;
	private int yMargin = 30;
	private int cellSize;
	
	private Block currentBlock;
	private Block nextBlock;
	
	private int inputCheckInterval = 60;
	private long lastActionTime;
	
	private int fullRowBlinkCount;
	
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
		return System.currentTimeMillis() - lastActionTime >= inputCheckInterval;
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
					currentBlock.translate(-1, 0);
				}
				break;
			case MOVE_RIGHT:
				if(blockContainer.canMoveRight(currentBlock)){
					currentBlock.translate(1, 0);
				}
				break;
			case NONE:
				break;
			case MOVE_DOWN:
				inputDown = true;
				break;
			case INSTANT_DOWN:
				break;
			default:
				break;
			}	
			
			if(currentAction != BlockControlAction.NONE)
			{
				lastActionTime = System.currentTimeMillis();
			}
		}
		
		
		boolean testDown = inputDown || testBlockDownTime >= testBlockDownInterval;
		if(testDown){
			if(blockContainer.canMoveDown(currentBlock)){
				currentBlock.translate(0, 1);
			}else{
				blockContainer.fixBlock(currentBlock);
				currentBlock = nextBlock;
				nextBlock = generateNextBlock();
				currentAction = BlockControlAction.NONE;
				testBlockDownTime = 0;
			}				
		}
		
		if(fullRowBlinkCount >= 3)
		{
			blockContainer.removeFullRows();
			
			fullRowBlinkCount = 0;
		}
		
		if(testBlockDownTime >= testBlockDownInterval){
			//testBlockDownTime -= testBlockDownInterval;
			testBlockDownTime = 0;
		}
	}
	
	private Block generateNextBlock()
	{
//		int red = 100 + (int)(Math.random()*156);
//		int green = 100 + (int)(Math.random()*156);
//		int blue = 100 + (int)(Math.random()*156);
		
		int red = (int)(Math.random()*256);
		int green = (int)(Math.random()*256);
		int blue = (int)(Math.random()*256);
		
		/*
		int choice = (int)Math.random()*3;
		if(choice == 0)
		{
			red = (int)(Math.random()*256);
		}
		else if(choice == 1)
		{
			green = (int)(Math.random()*256);
		}
		else if(choice == 2)
		{
			blue = (int)(Math.random()*256);
		}
		*/
		Block block = new Block(blockGenerator.getRandomBlockPrototype(), 4, 4, 0xff000000|(red<<16)|(green<<8)|blue);
		
		if(!blockContainer.canMoveDown(block))
		{
			blockContainer.reset();
		}
		
		return block;
	}
	
	private void gameInit()
	{
		currentBlock = generateNextBlock();
		nextBlock = generateNextBlock();
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
			
			lastTickTime = currentTime;
			try {
				Thread.sleep(REFRESH_INVERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void drawBlock(Canvas canvas, Block block){
		BlockCell[] cells = block.getBlockCells();
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			int cellX = block.getX() + cell.x;
			int cellY = block.getY() + cell.y;
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
			canvas.drawRect(xMargin, yMargin, xMargin + containerWidth, yMargin + containerHeight, paint);
			
			paint.setStyle(Style.STROKE);
			paint.setColor(0xffffffff);
			canvas.drawRect(xMargin, yMargin, xMargin + containerWidth-1, yMargin + containerHeight-1, paint);
			
			for(int row=0; row < blockContainer.getNumRows(); row++){
				BlockContainerRow containerRow = blockContainer.getRow(row);
				boolean drawRow = true;
				if(containerRow.isFull())
				{
					drawRow = System.currentTimeMillis() % 300 / 150 == 1;
					fullRowBlinkCount ++;
				}
				if(drawRow)
				{
					for(int col=0; col < blockContainer.getNumCols(); col++){
						BlockContainerCell cell = containerRow.getColumn(col);
						if(cell.status == BlockContainerCellStatus.OCCUPIED){
							drawBlockCell(canvas, col, row, cell.color);
						}
					}	
				}
				
			}
			
			
			drawBlock(canvas, currentBlock);
			
			try {
				surfaceHolder.unlockCanvasAndPost(canvas);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
		// TODO Auto-generated method stub
		//throw new RuntimeException("wtf~~~");
	}

	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initViewParams();
		paintThread.start();
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
	
}
