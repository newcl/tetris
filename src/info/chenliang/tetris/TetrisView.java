package info.chenliang.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TetrisView extends SurfaceView implements Runnable, Callback{

	private Paint paint = new Paint();
	private Thread paintThread;
	private SurfaceHolder surfaceHolder;
	private BlockContainer blockContainer;
	private int keyCode;
	private BlockGenerator blockGenerator;
	
	private int testBlockDownInterval;
	private int testBlockDownTime;
	
	private long lastTickTime;
	
	private int xMargin = 30;
	private int yMargin = 30;
	private int cellSize;
	
	public TetrisView(Context context, BlockContainer blockContainer, BlockGenerator blockGenerator) {
		super(context);
		setKeepScreenOn(true);
		
		this.blockContainer = blockContainer;
		this.blockGenerator = blockGenerator;
		
		paintThread = new Thread(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		testBlockDownInterval = 800;
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

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		this.keyCode = keyCode;
		return translateKeyToAction(keyCode) != BlockControlAction.NONE;
	}

	private BlockControlAction translateKeyToAction(int keyCode){
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

	@Override
	public boolean onKeyLongPress(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyLongPress(keyCode, event);
	}



	@Override
	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyMultiple(keyCode, repeatCount, event);
	}



	@Override
	public boolean onKeyPreIme(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyPreIme(keyCode, event);
	}



	@Override
	public boolean onKeyShortcut(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return super.onKeyShortcut(keyCode, event);
	}



	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		keyCode = -1;
		return super.onKeyUp(keyCode, event);
	}



	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		paint.setColor(0xff0000);
		canvas.drawRect(0, 0, 100, 100, paint);
	}

	private void gameTick(int timeElapsed){
		testBlockDownTime += timeElapsed;
		
		Block block = blockGenerator.getCurrentBlock();
		
		BlockControlAction action = translateKeyToAction(keyCode);
		switch(action){
		case TRANSFORM:
			if(blockContainer.canRotate(block)){
				block.rotate();
			}
			break;
		case MOVE_LEFT:
			if(blockContainer.canMoveLeft(block)){
				block.translate(-1, 0);
			}
			break;
		case MOVE_RIGHT:
			if(blockContainer.canMoveRight(block)){
				block.translate(1, 0);
			}
		case NONE:
		case MOVE_DOWN:
			boolean testDown = action == BlockControlAction.MOVE_DOWN || testBlockDownTime >= testBlockDownInterval;
			if(testDown){
				if(blockContainer.canMoveDown(block)){
					block.translate(0, 1);
				}else{
					blockContainer.fixBlock(block);
					blockGenerator.nextRound();
				}				
			}
			break;
		}
		
		if(testBlockDownTime >= testBlockDownInterval){
			//testBlockDownTime -= testBlockDownInterval;
			testBlockDownTime = 0;
		}
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		lastTickTime = System.currentTimeMillis();
		while(true)
		{
			long currentTime = System.currentTimeMillis();
			int timeElapsed = (int)(currentTime - lastTickTime);
			
			gameTick(timeElapsed);
			gameDraw();
			
			lastTickTime = currentTime;
			try {
				Thread.sleep(500);
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
			
			BlockContainerCell[][] containerCells = blockContainer.getContainerCells();
			
			for(int row=0; row < blockContainer.getNumRows(); row++){
				for(int col=0; col < blockContainer.getNumCols(); col++){
					BlockContainerCell cell = containerCells[row][col];
					if(cell.status == BlockContainerCellStatus.OCCUPIED){
						drawBlockCell(canvas, col, row, cell.color);
					}
				}
			}
			
			Block block = blockGenerator.getCurrentBlock();
			drawBlock(canvas, block);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();	
		}
		
		if(canvas != null)
		{
			try {
				surfaceHolder.unlockCanvasAndPost(canvas);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		// TODO Auto-generated method stub
		//throw new RuntimeException("wtf~~~");
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		initViewParams();
		paintThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
