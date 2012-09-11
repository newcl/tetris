package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;



public class BlockContainer {
	private BlockContainerRow[] containerRows;
	
	private int numRows, numCols;
	
	private boolean fullRowDetected;
	
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
	
	public boolean collideWithContainer(Block block)
	{
		return collideWithContainer(block.getX(), block.getY(), block.getCells());
	}
	
	public boolean collideWithContainer(int x, int y, BlockCell[] cells){
		for(int i=0; i<cells.length;i ++){
			BlockCell cell = cells[i];
			
			int containerX = (x + cell.x) / 2;
			int containerY = (y + cell.y) / 2;
			
			if(containerX < 0 || containerX >= numCols || containerY >= numRows)
			{
				return true;
			}
			
			if(containerY >= 0 && containerY < numRows){
				BlockContainerRow containerRow = containerRows[containerY];
				if(containerX >= 0 && containerX < numCols)
				{
					BlockContainerCell containerCell = containerRow.getColumn(containerX);
					if(containerCell.getStatus() == BlockContainerCellStatus.OCCUPIED){
						return true;
					}	
				}
			}
		}
		return false;
	}
	
	public boolean canMoveLeft(Block block){
		return !collideWithContainer(block.getX() - 2, block.getY(), block.getCells());
	}

	public boolean canMoveRight(Block block){
		return !collideWithContainer(block.getX() + 2, block.getY(), block.getCells());
	}
	
	public boolean canMoveDown(Block block){
		return !collideWithContainer(block.getX(), block.getY() + 2, block.getCells());
	}
	
	public void fixBlock(Block block){
		BlockCell[] cells = block.getCells();
		for(int i=0; i<cells.length; i++){
			BlockCell cell = cells[i];
			int containerX = (block.getX() + cell.x) / 2;
			int containerY = (block.getY() + cell.y) / 2;
			
			if(containerY >= 0 && containerY < numRows){
				BlockContainerRow containerRow = containerRows[containerY];
				if(containerX >= 0 && containerX < numCols)
				{
					containerRow.fixColumn(containerX, block.getColor());
					if(containerRow.isFull())
					{
						fullRowDetected = true;
					}
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
		List<BlockContainerRow> fullRows = new ArrayList<BlockContainerRow>();
		List<BlockContainerRow> fallDownRows = new ArrayList<BlockContainerRow>();
		
		for(int row= numRows -1; row >= 0; row--)
		{
			BlockContainerRow containerRow = containerRows[row];
			if(containerRow.isFull())
			{
				fullRows.add(containerRow);
			}
			else 
			{
				if(!containerRow.isEmpty() && fullRows.size() > 0)
				{
					fallDownRows.add(containerRow);
				}
			}
		}
		
		for(BlockContainerRow containerRow:fullRows)
		{
			containerRow.reset();
		}
		
		for(int row=fullRows.get(0).getRow();fallDownRows.size() > 0;row--)
		{
			BlockContainerRow fallDownRow = fallDownRows.get(0);
			
			BlockContainerRow containerRow = containerRows[row];
			containerRow.copy(fallDownRow);
			
			fallDownRow.reset();
			fallDownRows.remove(0);
		}
		
		fullRowDetected = false;
		
	}
	
	public List<BlockContainerRow> getFullRows()
	{
		List<BlockContainerRow> fullRows = new ArrayList<BlockContainerRow>();
		for(int i=0; i < containerRows.length; i++)
		{
			if(containerRows[i].isFull())
			{
				fullRows.add(containerRows[i]);
			}
		}
		return fullRows;
	}

	public boolean isFullRowDetected() {
		return fullRowDetected;
	}
}
