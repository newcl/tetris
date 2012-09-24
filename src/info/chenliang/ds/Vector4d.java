package info.chenliang.ds;

public class Vector4d {
	public float x, y, z, w;

	public Vector4d(float x, float y, float z, float w) {
		super();
		this.x = x;
		this.y = y;
		this.z = z;
		this.w = w;
	}
	
	public Vector4d(Vector3d v, float w)
	{
		this(v.x, v.y, v.z, w);
	}
	
	public float dot(Vector4d v)
	{
		return x*v.x + y*v.y + z*v.z + w*v.w;
	}
}
