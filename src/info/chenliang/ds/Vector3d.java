package info.chenliang.ds;

import info.chenliang.debug.Assert;

public class Vector3d {
	private float x,y,z;

	public Vector3d(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
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

	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}
	
	public void scale(float factor)
	{
		x *= factor;
		y *= factor;
		z *= factor;
	}
	
	public Vector3d clone()
	{
		return new Vector3d(x, y, z);
	}
	
	public void copy(Vector3d v)
	{
		this.x = v.x;
		this.y = v.y;
		this.z = v.z;
	}
	
	public float length()
	{
		return (float)Math.sqrt(x*x + y*y + z*z);
	}
	
	public void normalize()
	{
		float length = length();
		Assert.judge(length != 0, "Can not normalize a length 0 vector3d.");
		x /= length;
		y /= length;
		z /= length;
	}
}
