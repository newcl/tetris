package info.chenliang.tetris;


public class BlockContainerCell{
	private int color;
	private BlockContainerCellStatus status;
	
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

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public BlockContainerCellStatus getStatus() {
		return status;
	}

	public void setStatus(BlockContainerCellStatus status) {
		this.status = status;
	}
}