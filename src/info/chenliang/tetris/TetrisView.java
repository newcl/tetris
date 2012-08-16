package info.chenliang.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnTouchListener;

public class TetrisView extends SurfaceView implements Runnable, Callback, OnTouchListener{

	private Paint paint = new Paint();
	private Thread paintThread;
	private SurfaceHolder surfaceHolder;
	private BlockContainer blockContainer;
	private BlockGenerator blockGenerator;
	
	private BlockControlAction currentAction;
	
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
		
		currentAction = BlockControlAction.NONE;
		
		setClickable(true);
		setOnTouchListener(this);
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

	private void gameTick(int timeElapsed){
		testBlockDownTime += timeElapsed;
		
		Block block = blockGenerator.getCurrentBlock();
		switch(currentAction){
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
			break;
		case NONE:
		case MOVE_DOWN:
			/*
			boolean testDown = currentAction == BlockControlAction.MOVE_DOWN || testBlockDownTime >= testBlockDownInterval;
			if(testDown){
				if(blockContainer.canMoveDown(block)){
					block.translate(0, 1);
				}else{
					blockContainer.fixBlock(block);
					blockGenerator.nextRound();
				}				
			}
			*/
			break;
		}
		
		boolean testDown = currentAction == BlockControlAction.MOVE_DOWN || testBlockDownTime >= testBlockDownInterval;
		if(testDown){
			if(blockContainer.canMoveDown(block)){
				block.translate(0, 1);
			}else{
				blockContainer.fixBlock(block);
				blockGenerator.nextRound();
			}				
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
	
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		if(v == this){
			int action = event.getAction();
			if(action == MotionEvent.ACTION_DOWN){

				int x = (int)event.getX(0);
				int y = (int)event.getY(0);
				
				int screenWidth = getWidth();
				int screenHeight = getHeight();
				
				if(y < screenHeight / 2){
					//instant down
					currentAction = BlockControlAction.INSTANT_DOWN;
				}else if(y < screenHeight * 3 / 4){
					//transform
					currentAction = BlockControlAction.TRANSFORM;
				}else if(x < screenWidth / 3){
					//left
					currentAction = BlockControlAction.MOVE_LEFT;
				}else if(x < screenWidth * 2 / 3){
					//down
					currentAction = BlockControlAction.MOVE_DOWN;
				}else{
					//right
					currentAction = BlockControlAction.MOVE_RIGHT;
				}
			}else{
				currentAction = BlockControlAction.NONE;
			}
			
			
			return true;
		}
		
		return false;
	}
}
