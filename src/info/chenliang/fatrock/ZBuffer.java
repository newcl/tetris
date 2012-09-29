package info.chenliang.fatrock;

public abstract class ZBuffer {
	protected int width;
	protected int height;
	
	public ZBuffer(int width, int height)
	{
		this.width = width;
		this.height = height;
	}
	
	public abstract float getZ(int x, int y);
	public abstract void setZ(int x, int y, float z);
	public abstract void reset(float z);
}
