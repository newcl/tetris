package info.chenliang.tetris;

import info.chenliang.fatrock.PixelRenderer;
import android.graphics.Paint;


public interface GameCanvas extends PixelRenderer{
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_RIGHT = 2;
	
	public boolean startDraw();
	public boolean endDraw();
	public int getCanvasWidth();
	public int getCanvasHeight();
	public void drawRect(float left, float top, float right, float bottom, int color, int alpha);
	public void fillRect(float left, float top, float right, float bottom, int color, int alpha);
	public void clipRect(float left, float top, float right, float bottom);
	public void drawLine(float x1, float y1, float x2, float y2, int color);
	public void drawText(String text, float x, float y, int color, int alignment);
	public int getFontHeight();
	public boolean isReady();
	public Paint getPaint();
}
