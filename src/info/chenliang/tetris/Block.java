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
	private BlockCell[] cells;
	
	private int minX, maxX, minY, maxY;
	private boolean oddX;
	private boolean oddY;
	public Block(BlockPrototype protoType, int x, int y, int color){
		this.protoType = protoType;
		this.x = x;
		this.y = y;
		this.color = color;
		
		cells = new BlockCell[protoType.getBlockCells().size()];
		for(int i=0;i < cells.length; i++)
		{
			cells[i] = new BlockCell(protoType.getBlockCells().get(i));
			
			//cells[i].x *= 2;
			//cells[i].y *= 2;
		}
		
		updateBounds();
		/*
		int centerX = minX + (maxX - minX) / 2;
		int centerY = minY + (maxY - minY) / 2;
		for(int i=0;i < cells.length; i++)
		{
			cells[i].x -= centerX;
			cells[i].y -= centerY;
			
			if(cells[i].x % 2 != 0)
			{
				oddX = true;
			}
			
			if(cells[i].y % 2 != 0)
			{
				oddY = true;
			}
		}
		*/
		
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
		
		if(!((oddX && oddY) || ((!oddX)&&(!oddY))))
		{
			throw new RuntimeException("shit");
		}
//		minX -= centerX;
//		maxX -= centerX;
//		minY -= centerY;
//		maxY -= centerY;
	}
	
	private void updateBounds()
	{
		minX = cells[0].x;
		maxX = cells[0].x;
		minY = cells[0].y;
		maxY = cells[0].y;
		for(int i=1; i < cells.length; i++)
		{
			minX = Math.min(minX, cells[i].x);
			maxX = Math.max(maxX, cells[i].x + 2);
			minY = Math.min(minY, cells[i].y);
			maxY = Math.max(maxY, cells[i].y + 2);
		}
	}
	
	public Block(Block block)
	{
		this.protoType = block.protoType;
		this.x = block.x;
		this.y = block.y;
		this.color = block.color;
		
		cells = new BlockCell[block.protoType.getBlockCells().size()];
		for(int i=0;i < cells.length; i++)
		{
			cells[i] = new BlockCell(block.cells[i]);
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
		bounds[1] = new BlockCell(cell.x + 2, cell.y);
		bounds[2] = new BlockCell(cell.x + 2, cell.y + 2);
		bounds[3] = new BlockCell(cell.x, cell.y + 2);
		
		for(int j=0;j < bounds.length; j++){
			BlockCell boundCell = bounds[j];
			rotateBlockPosition(boundCell);
		}
		
		int cellX = bounds[0].x;
		int cellY = bounds[0].y;
		
		for(int j=1;j < bounds.length; j++){
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
		
		updateBounds();
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

	public BlockPrototype getProtoType() {
		return protoType;
	}

	public int getMinX() {
		return minX;
	}

	public int getMaxX() {
		return maxX;
	}

	public int getMinY() {
		return minY;
	}

	public int getMaxY() {
		return maxY;
	}

	public boolean isOddX() {
		return oddX;
	}

	public boolean isOddY() {
		return oddY;
	}

	public BlockCell[] getCells() {
		return cells;
	}
	
}
