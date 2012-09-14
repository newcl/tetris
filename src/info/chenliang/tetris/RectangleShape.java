package info.chenliang.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class RectangleShape extends Shape{
	private float width, height;
	
	public RectangleShape(float width, float height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(Canvas canvas, Paint paint, float x, float y) {
		canvas.drawRect(x-width/2, y-height/2,x+width/2,y+height/2, paint);
		
		paint.setStyle(Style.STROKE);
		paint.setColor(0);
		canvas.drawRect(x-width/2, y-height/2,x+width/2,y+height/2, paint);
	}

}
