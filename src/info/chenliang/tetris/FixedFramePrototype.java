package info.chenliang.tetris;

import java.util.List;

public class FixedFramePrototype {
	private List<FixedFrame> fixedFrames;
	private int color;
	
	
	public FixedFramePrototype(List<FixedFrame> fixedFrames, int color) {
		super();
		this.fixedFrames = fixedFrames;
		this.color = color;
	}


	public List<FixedFrame> getFixedFrames() {
		return fixedFrames;
	}


	public int getColor() {
		return color;
	}
	
	
	
}
