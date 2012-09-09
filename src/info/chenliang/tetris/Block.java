package info.chenliang.tetris;


public class Block {
	//				|
	//				|
	//				|
	//				|
	//				|
	//   (-1,-1) ---|--- (1,-1)
	//   		|	|   |   	    +
	//--------------|----------------
	//			|	|	|
	//	  (-1,1) ---|--- (1,1)
	//				|
	//				|
	//				|
	//				| +
	
	private BlockPrototype protoType;
	private int x,y;
	private int color;
	BlockCell[] cells;
	public Block(BlockPrototype protoType, int x, int y, int color){
		this.protoType = protoType;
		this.x = x;
		this.y = y;
		this.color = color;
		
		cells = new BlockCell[protoType.getBlockCells().size()];
		for(int i=0;i < cells.length; i++)
		{
			cells[i] = new BlockCell(protoType.getBlockCells().get(i));
		}
	}
	
	private void rotateBlockPosition(BlockCell cell){
		int x = cell.x;
		int y = cell.y;
		
		cell.x = - y;
		cell.y = x;
	}
	
	private void rotateBlockCell(BlockCell cell){
		BlockCell[] bounds = new BlockCell[4];
		bounds[0] = new BlockCell(cell.x, cell.y);
		bounds[1] = new BlockCell(cell.x + 1, cell.y);
		bounds[2] = new BlockCell(cell.x + 1, cell.y + 1);
		bounds[3] = new BlockCell(cell.x, cell.y + 1);
		
		for(int j=0;j < bounds.length; j++){
			BlockCell boundCell = bounds[j];
			rotateBlockPosition(boundCell);
		}
		
		int cellX = bounds[0].x;
		int cellY = bounds[1].y;
		
		for(int j=0;j < bounds.length; j++){
			BlockCell boundCell = bounds[j];
			cellX = Math.min(cellX, boundCell.x);
			cellY = Math.min(cellY, boundCell.y);
		}
		
		cell.x = cellX;
		cell.y = cellY;
	}
	
	
	
	public void rotate(){
		//x' = x*cosk - y*sink 
		//y' = x*sink + y*cosk
		// k = 90
		//x' = -y
		//y' = x
		for (int i = 0; i < cells.length; i++) {
			BlockCell cell = cells[i];
			rotateBlockCell(cell);
		}
		
	}
	
	public BlockCell[] tryRotate(){
		BlockCell[] rotatedCells = new BlockCell[cells.length];
		for(int i=0;i < rotatedCells.length; i++){
			BlockCell cell = cells[i];
			BlockCell rotatedCell = new BlockCell(cell.x, cell.y);
			
			rotatedCells[i] = rotatedCell;
			
			rotateBlockCell(rotatedCell);
		}
		return rotatedCells;
	}
	
	public void translate(int deltaX, int deltaY){
		x += deltaX;
		y += deltaY;
	}
	
	
	public BlockCell[] getBlockCells() {
		return cells;
	}


	public int getX() {
		return x;
	}


	public void setX(int x) {
		this.x = x;
	}


	public int getY() {
		return y;
	}


	public void setY(int y) {
		this.y = y;
	}


	public int getColor() {
		return color;
	}


	public void setColor(int color) {
		this.color = color;
	}
}
