package info.chenliang.tetris;



public class BlockContainer {
	//private BlockContainerCell[][] containerCells;
	private BlockContainerRow[] containerRows;
	
	private int numRows, numCols;
	
	public BlockContainer(int numRows, int numCols){
		this.numRows = numRows;
		this.numCols = numCols;
		
		init();
	}
	
	private void init(){
		containerRows = new BlockContainerRow[numRows];
		for(int row=0; row < numRows;row++){
			containerRows[row] = new BlockContainerRow(row, numCols);
		}
	}
	
	public boolean canRotate(Block block){
		BlockCell[] cells = block.tryRotate();
		return !collideWithContainer(block.getX(), block.getY(), cells);
	}
	
	private boolean collideWithContainer(int x, int y, BlockCell[] cells){
		for(int i=0; i<cells.length;i ++){
			BlockCell cell = cells[i];
			
			int containerX = x + cell.x;
			int containerY = y + cell.y;
			
			if(containerX < 0 || containerX >= numCols || containerY >= numRows)
			{
				return true;
			}
			
			if(containerY >= 0 && containerY < numRows){
				BlockContainerRow containerRow = containerRows[containerY];
				if(containerX >= 0 && containerX < numCols)
				{
					BlockContainerCell containerCell = containerRow.getColumn(containerX);
					if(containerCell.status == BlockContainerCellStatus.OCCUPIED){
						return true;
					}	
				}
			}
		}
		return false;
	}
	
	public boolean canMoveLeft(Block block){
		return !collideWithContainer(block.getX() - 1, block.getY(), block.getBlockCells());
	}

	public boolean canMoveRight(Block block){
		return !collideWithContainer(block.getX() + 1, block.getY(), block.getBlockCells());
	}
	
	public boolean canMoveDown(Block block){
		return !collideWithContainer(block.getX(), block.getY() + 1, block.getBlockCells());
	}
	
	public void fixBlock(Block block){
		BlockCell[] cells = block.getBlockCells();
		for(int i=0; i<cells.length; i++){
			BlockCell cell = cells[i];
			int containerX = block.getX() + cell.x;
			int containerY = block.getY() + cell.y;
			
			if(containerY >= 0 && containerY < numRows){
				BlockContainerRow containerRow = containerRows[containerY];
				if(containerX >= 0 && containerX < numCols)
				{
					containerRow.fixColumn(containerX, block.getColor());
				}
			}
		}
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}
	
	public BlockContainerRow getRow(int row)
	{
		return containerRows[row];
	}
	
	public void reset()
	{
		for(int i=0; i<containerRows.length;i++)
		{
			containerRows[i].reset();
		}
	}
	
	public void removeFullRows()
	{
		BlockContainerRow lastFullRow = null;
		for(int row= numRows -1; row >= 0; row--)
		{
			BlockContainerRow containerRow = containerRows[row];
			if(containerRow.isFull())
			{
				lastFullRow = containerRow;
			}
			else
			{
				if(lastFullRow != null)
				{
					lastFullRow.copy(containerRow);
					lastFullRow = containerRow;
				}
				
			}
			
		}
		
		if(lastFullRow != null)
		{
			lastFullRow.reset();
		}
	}
}
