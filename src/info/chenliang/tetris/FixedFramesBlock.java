package info.chenliang.tetris;

import java.util.List;

public class FixedFramesBlock extends Block {
	private List<FixedFrame> frames;
	private int currentFrame;
	
	public FixedFramesBlock(int x, int y, int color, List<FixedFrame> frames) {
		super(x, y, color);
		this.frames = frames;
		init();
	}

	@Override
	public BlockCell[] getCells() {
		// TODO Auto-generated method stub
		List<BlockCell> l = frames.get(currentFrame).getCells();
		BlockCell[] bcs = l.toArray(new BlockCell[0]);
		return bcs;
	}

	@Override
	public Block duplicate() {
		// TODO Auto-generated method stub
		FixedFramesBlock block = new FixedFramesBlock(x, y, color, frames);
		block.currentFrame = currentFrame;
		return block;
	}

	@Override
	public void rotate() {
		// TODO Auto-generated method stub
		currentFrame = (currentFrame + 1) % frames.size();
	}

	@Override
	public BlockCell[] tryRotate() {
		// TODO Auto-generated method stub
		return nextFrame().getCells().toArray(new BlockCell[0]);
	}
	
	private FixedFrame nextFrame()
	{
		return frames.get((currentFrame+1)%frames.size());
	}
	
}
