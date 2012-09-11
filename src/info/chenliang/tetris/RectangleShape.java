package info.chenliang.tetris;

import java.util.ArrayList;

import android.graphics.Canvas;
import android.graphics.Paint;

public class RectangleShape extends Shape{
	private Vector2d leftTop = new Vector2d(Float.MAX_VALUE, Float.MAX_VALUE);
	private Vector2d rightBottom = new Vector2d(Float.MIN_VALUE, Float.MIN_VALUE);
	
	private float x,y;
	private float width, height;
	
	public RectangleShape(float x, float y, float width, float height)
	{
		points = new ArrayList<Vector2d>();
		points.add(new Vector2d(x - width/4, y -height/4 ));
		points.add(new Vector2d(x + width/4, y -height/4 ));
		points.add(new Vector2d(x - width/4, y +height/4 ));
		points.add(new Vector2d(x + width/4, y +height/4 ));
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		
		for (Vector2d point:points)
		{
			leftTop.setX(Math.min(leftTop.getX(), point.getX()));
			leftTop.setY(Math.min(leftTop.getY(), point.getY()));
			rightBottom.setX(Math.max(rightBottom.getX(), point.getX()));
			rightBottom.setY(Math.max(rightBottom.getY(), point.getY()));
		}
	}

	@Override
	public void draw(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub
		super.draw(canvas, paint);
		
		canvas.drawRect(x - width/2, y-height/2,x+width/2,y+height/2, paint);
	}

	
	
}
