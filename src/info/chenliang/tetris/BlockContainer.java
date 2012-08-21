package info.chenliang.tetris;



public class BlockContainer {
	private BlockContainerCell[][] containerCells;
	
	private int numRows, numCols;
	
	public BlockContainer(int numRows, int numCols){
		this.numRows = numRows;
		this.numCols = numCols;
		
		init();
	}
	
	private void init(){
		containerCells = new BlockContainerCell[numRows][];
		for(int row=0; row < numRows;row++){
			containerCells[row] = new BlockContainerCell[numCols];
			for(int col=0; col < numCols; col++){
				containerCells[row][col] = new BlockContainerCell(0, BlockContainerCellStatus.EMPTY);
			}
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
			
			if(containerX < 0 || containerX >= numCols || containerY >= numRows){
				return true;
			}
			
			if(containerY >= 0){
				BlockContainerCell containerCell = containerCells[containerY][containerX];
				if(containerCell.status == BlockContainerCellStatus.OCCUPIED){
					return true;
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
		BlockCell [] cells = block.getBlockCells();
		for(int i=0; i<cells.length; i++){
			BlockCell cell = cells[i];
			int x = block.getX() + cell.x;
			int y = block.getY() + cell.y;
			
			if(y >= 0 && y < containerCells.length && x >= 0 && x < containerCells[y].length){
				BlockContainerCell containerCell = containerCells[y][x];
				containerCell.color = block.getColor();
				containerCell.status = BlockContainerCellStatus.OCCUPIED;				
			}
		}
	}

	public int getNumRows() {
		return numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public BlockContainerCell[][] getContainerCells() {
		return containerCells;
	}
}
