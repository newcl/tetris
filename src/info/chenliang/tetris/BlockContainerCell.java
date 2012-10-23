package info.chenliang.tetris;


public class BlockContainerCell{
	public int color;
	public BlockContainerCellStatus status;
	
	public BlockContainerCell(int color, BlockContainerCellStatus status) {
		super();
		this.color = color;
		this.status = status;
	}
	
	public void copy(BlockContainerCell cell)
	{
		color = cell.color;
		status = cell.status;
	}

}