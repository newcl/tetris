package info.chenliang.tetris;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.DrawFilter;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.util.FloatMath;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class TetrisView extends SurfaceView implements Callback, GameCanvas{
	private Paint paint;
	
	private SurfaceHolder surfaceHolder;
	private Canvas canvas;
	private boolean ready;
	public TetrisView(Context context) {
		super(context);
		setKeepScreenOn(true);
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		
		paint = new Paint();
		paint.setAntiAlias(true);
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		ready = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

	public boolean startDraw() {
		boolean success = false;
		try {
			canvas = surfaceHolder.lockCanvas();
			success = canvas != null;
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		return success;
	}

	public boolean endDraw() {
		boolean success = false;
		try {
			surfaceHolder.unlockCanvasAndPost(canvas);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return success;
	}
	
	public int getFontHeight()
	{
		FontMetrics fontMetrics = paint.getFontMetrics();
		return (int)Math.abs(FloatMath.ceil(fontMetrics.descent - fontMetrics.ascent));
				
	}

	public void drawRect(float left, float top, float right, float bottom,
			int color) {
		paint.setStyle(Style.STROKE);
		paint.setColor(color);
		canvas.drawRect(left, top, right, bottom, paint);
	}

	public void fillRect(float left, float top, float right, float bottom,
			int color) {
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		canvas.drawRect(left, top, right, bottom, paint);
	}

	public void clipRect(float left, float top, float right, float bottom) {
		canvas.clipRect(left, top, right, bottom);
	}

	public void drawText(String text, float x, float y, float color,
			int alignment) {
		if(alignment == ALIGN_CENTER)
		{
			paint.setTextAlign(Align.CENTER);
		}
		else if(alignment == ALIGN_LEFT)
		{
			paint.setTextAlign(Align.LEFT);
		}
		else if(alignment == ALIGN_RIGHT)
		{
			paint.setTextAlign(Align.RIGHT);
		}
		
		canvas.drawText(text, x, y, paint);
	}

	public boolean isReady() {
		return ready;
	}

	public Paint getPaint() {
		return paint;
	}
}
