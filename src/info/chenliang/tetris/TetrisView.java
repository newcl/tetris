package info.chenliang.tetris;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Cap;
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
	private Bitmap bitmap;
	private Canvas bc;
	private int[] pixels;
	private boolean ready;
	public TetrisView(Context context) {
		super(context);
		setKeepScreenOn(true);
		
		surfaceHolder = getHolder();
		surfaceHolder.addCallback(this);
		
		paint = new Paint();
		//paint.setAntiAlias(true);
	}
	
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
	}

	public void surfaceCreated(SurfaceHolder holder) {
		bitmap = Bitmap.createBitmap(getWidth(), getHeight(), Config.ARGB_8888);
		canvas = new Canvas(bitmap);
		
		//pixels = new int[getWidth()*getHeight()];
		ready = true;
	}

	public void surfaceDestroyed(SurfaceHolder holder) {
		
	}

	public boolean startDraw() {
		return true;
		
		/*
		boolean success = false;
		try {
			canvas = surfaceHolder.lockCanvas();
			boolean ac = isHardwareAccelerated();
			//bitmap = getDrawingCache();
			success = canvas != null;
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		return success;
		*/
	}

	public boolean endDraw() {
		boolean success = false;
		
		try {
			Canvas viewCanvas = surfaceHolder.lockCanvas();
			viewCanvas.drawBitmap(bitmap, 0, 0, paint);
			surfaceHolder.unlockCanvasAndPost(viewCanvas);
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
			int color, int alpha) {
		paint.setStyle(Style.STROKE);
		paint.setColor(color);
		paint.setAlpha(alpha);
		canvas.drawRect(left, top, right, bottom, paint);
	}

	public void fillRect(float left, float top, float right, float bottom,
			int color, int alpha) {
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		paint.setAlpha(alpha);
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

	public void drawLine(float x1, float y1, float x2, float y2, int color) {
		paint.setColor(color);
		canvas.drawLine(x1, y1, x2, y2, paint);
	}

	public void setPixel(int x, int y, int color) {
		
		paint.setColor(0xff000000|color);
		paint.setStyle(Style.STROKE);
		paint.setStrokeWidth(1);
		paint.setStrokeCap(Cap.SQUARE);
		
		canvas.drawPoint(x, y, paint);
		
		
		//bitmap.setPixel(x, y, 0xff000000|color);
	}
}
