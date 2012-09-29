package info.chenliang.fatrock;

import java.util.HashMap;

public class DynamicZBuffer extends ZBuffer {
	private HashMap<Integer, Float> zBuffer;
	
	public DynamicZBuffer(int width, int height)
	{
		super(width, height);
		zBuffer = new HashMap<Integer, Float>();
	}
	
	@Override
	public float getZ(int x, int y) {
		return x << 16 | y;
	}

	@Override
	public void setZ(int x, int y, float z) {
		zBuffer.put(x << 16 | y , z);
	}

	@Override
	public void reset(float z) {
		zBuffer.clear();
	}

}
