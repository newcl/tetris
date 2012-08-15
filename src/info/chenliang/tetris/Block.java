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
	
	private BlockCell[] cells;
	private int x,y;
	private int color;
	
	public Block(BlockCell[] cells, int x, int y, int color){
		this.cells = cells;
		this.x = x;
		this.y = y;
		this.color = color;
	}
	
	
	public void rotate(){
		
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
