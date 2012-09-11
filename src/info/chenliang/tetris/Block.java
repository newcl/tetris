package info.chenliang.tetris;


public abstract class Block {
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
	
	protected int x,y;
	protected int color;
	
	protected int minX, maxX, minY, maxY;
	protected boolean oddX;
	protected boolean oddY;
	
	public Block(int x, int y, int color)
	{
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	protected void init()
	{
		updateBounds();
		initParity();
	}
	
	protected void updateBounds()
	{
		BlockCell[] cells = getCells();
		
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
	
	protected void initParity(){
		BlockCell[] cells = getCells();
		
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
	
	protected void rotateBlockPosition(BlockCell cell){
		int x = cell.x;
		int y = cell.y;
		
		cell.x = - y;
		cell.y = x;
	}
	
	protected void rotateBlockCell(BlockCell cell){
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

	public abstract BlockCell[] getCells();
	public abstract Block duplicate();
	public abstract void rotate();
	public abstract BlockCell[] tryRotate();
}
