package info.chenliang.ds;

import info.chenliang.debug.Assert;

public class Vector3d {
	private float x=0.0f,y=0.0f,z=0.0f;

	public Vector3d(float x, float y, float z) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
	}
	
	public Vector3d()
	{
	}
	
	public Vector3d(Vector3d from, Vector3d to)
	{
		copy(to.minus(from));
	}
	
	public Vector3d(Vector3d v)
	{
		copy(v);
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
	
	public float dot(Vector3d v)
	{
		return x*v.x + y*v.y + z*v.z;
	}
	
	public Vector3d minus(Vector3d v)
	{
		Vector3d result = new Vector3d(v);
		result.scale(-1);
		return add(result);
	}
	
	public Vector3d add(Vector3d v)
	{
		Vector3d result = new Vector3d(this);
		result.x += v.x;
		result.y += v.y;
		result.z += v.z;
		return result;
	}
	
	public Vector3d cross(Vector3d v)
	{
		Vector3d result = new Vector3d();
		result.x = y*v.z - z*v.y;
		result.y = z*v.x - x*v.z;
		result.z = x*v.y - y*v.x;
		return result;
	}
}
