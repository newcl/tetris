package info.chenliang.tetris;

public class BlockCell {
	public int x;
	public int y;

	public BlockCell(int x, int y) {
		super();
		this.x = x;
		this.y = y;
	}
	
	public BlockCell(BlockCell cell)
	{
		this(cell.x, cell.y);
	}
}