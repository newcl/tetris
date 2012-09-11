package info.chenliang.tetris;

public class RotatableBlock extends Block {
	private BlockCell[] cells;
	
	public RotatableBlock(int x, int y, int color, BlockCell[] cells)
	{
		super(x, y, color);
		this.cells = copy(cells);
		updateBounds();
		verifyBlock();
	}
	
	private BlockCell[] copy(BlockCell[] cells)
	{
		BlockCell[] newCells = new BlockCell[cells.length];
		for(int i=0;i < cells.length; i++)
		{
			newCells[i] = new BlockCell(cells[i]);
		}
		
		return newCells;
	}
	
	private void verifyBlock(){
		updateBounds();
		
		for(int i=0;i < cells.length; i++)
		{
			if(cells[i].x % 2 != 0)
			{
				oddX = true;
			}
			
			if(cells[i].y % 2 != 0)
			{
				oddY = true;
			}
		}
		
		Assert.judge((oddX && oddY) || ((!oddX)&&(!oddY)), "vertical & horizontal should be both odd or both even.");
	}

	@Override
	public BlockCell[] getCells() {
		// TODO Auto-generated method stub
		return cells;
	}

	@Override
	public Block duplicate() {
		// TODO Auto-generated method stub
		return new RotatableBlock(x, y, color, copy(cells));
	}
	
	public BlockCell[] tryRotate(){
		BlockCell[] cells = getCells();
		BlockCell[] rotatedCells = new BlockCell[cells.length];
		for(int i=0;i < rotatedCells.length; i++){
			BlockCell cell = cells[i];
			BlockCell rotatedCell = new BlockCell(cell.x, cell.y);
			
			rotatedCells[i] = rotatedCell;
			
			rotateBlockCell(rotatedCell);
		}
		return rotatedCells;
	}
	
	public void rotate(){
		//x' = x*cosk - y*sink 
		//y' = x*sink + y*cosk
		// k = 90
		//x' = -y
		//y' = x
		BlockCell[] cells = getCells();
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			rotateBlockCell(cell);
		}
		
		updateBounds();
	}
}
