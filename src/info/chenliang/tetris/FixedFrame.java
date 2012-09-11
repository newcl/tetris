package info.chenliang.tetris;

import java.util.List;

public class FixedFrame {
	private List<BlockCell> cells;

	public FixedFrame(List<BlockCell> cells) {
		super();
		this.cells = cells;
	}

	public List<BlockCell> getCells() {
		return cells;
	}
	
	
}
