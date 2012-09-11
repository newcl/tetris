package info.chenliang.tetris;

public class BlockContainerRow {
	private boolean isFull;
	private boolean isEmpty = true;
	private BlockContainerCell[] cells;
	private int row;
	
	public BlockContainerRow(int row, int numCols) {
		super();
		this.row = row;
		cells = new BlockContainerCell[numCols];
		for(int col=0; col < numCols; col++)
		{
			cells[col] = new BlockContainerCell(0xffffffff, BlockContainerCellStatus.EMPTY); 
		}
	}

	public boolean isFull() {
		return isFull;
	}

	public int getRow() {
		return row;
	}
	
	public BlockContainerCell getColumn(int col)
	{
		return cells[col];
	}
	
	public void fixColumn(int col, int color)
	{
		getColumn(col).setStatus(BlockContainerCellStatus.OCCUPIED);
		getColumn(col).setColor(color);
		
		boolean emptyDetected = false;
		for(int i=0; i<cells.length; i++)
		{
			if(cells[i].getStatus() == BlockContainerCellStatus.EMPTY)
			{
				emptyDetected = true;
				break;
			}
		}
		
		if(!emptyDetected)
		{
			isFull = true;
		}
		
		isEmpty = false;
	}
	
	public void reset()
	{
		for(int i=0; i<cells.length; i++)
		{
			cells[i].setStatus(BlockContainerCellStatus.EMPTY);
		}
		
		isFull = false;
		isEmpty = true;
	}
	
	public void copy(BlockContainerRow row)
	{
		isFull = row.isFull;
		isEmpty = row.isEmpty;
		
		for(int col=0; col<cells.length;col++)
		{
			cells[col].copy(row.getColumn(col));
		}
	}

	public boolean isEmpty() {
		return isEmpty;
	}
}
