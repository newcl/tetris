package info.chenliang.tetris;

import android.graphics.Paint;


public interface GameCanvas {
	public static final int ALIGN_CENTER = 0;
	public static final int ALIGN_LEFT = 1;
	public static final int ALIGN_RIGHT = 2;
	
	public boolean startDraw();
	public boolean endDraw();
	public int getWidth();
	public int getHeight();
	public void drawRect(float left, float top, float right, float bottom, int color, int alpha);
	public void fillRect(float left, float top, float right, float bottom, int color, int alpha);
	public void clipRect(float left, float top, float right, float bottom);
	public void drawText(String text, float x, float y, float color, int alignment);
	public int getFontHeight();
	public boolean isReady();
	public Paint getPaint();
}
