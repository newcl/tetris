package info.chenliang.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TetrisView extends SurfaceView implements Runnable, Callback{

	private Paint paint = new Paint();
	private Thread paintThread;
	private SurfaceHolder surfaceHolder;
	
	public TetrisView(Context context) {
		super(context);
		setKeepScreenOn(true);
		paintThread = new Thread(this);
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);
		
		paint.setColor(0xff0000);
		canvas.drawRect(0, 0, 100, 100, paint);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		while(true)
		{
			gameDraw();
			
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private void gameDraw()
	{
		paint.setStyle(Style.FILL);
		
		Canvas canvas = null;
		try {
			canvas = surfaceHolder.lockCanvas();
			paint.setColor(0xffff0000);
			int width = getWidth();
			int height = getHeight();
			canvas.drawRect(0, 0, width, height, paint);
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
		paintThread.start();
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		// TODO Auto-generated method stub
		
	}
}
