package info.chenliang.ds;

import info.chenliang.debug.Assert;

public class Vector2d {
	private float x,y;

	public Vector2d(float x, float y) {
		super();
		this.x = x;
		this.y = y;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}
	
	public void scale(float factor)
	{
		x *= factor;
		y *= factor;
	}
	
	public Vector2d clone()
	{
		return new Vector2d(x, y);
	}
	
	public void copy(Vector2d v)
	{
		this.x = v.x;
		this.y = v.y;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y);
	}
	
	public void normalize()
	{
		float length = length();
		Assert.judge(length != 0, "Can not normalize a length 0 vector2d.");
		x /= length;
		y /= length;
	}
}
