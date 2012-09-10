package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

public class BlockPrototype {
	private List<BlockCell> blockCells = new ArrayList<BlockCell>();
	private String name;
	private int color;
	
	public int getColor() {
		return color;
	}

	public BlockPrototype(String name, int color){
		this.name = name;
		this.color = color;
	}
	
	public BlockPrototype(String name, int red, int green, int blue)
	{
		this(name, 0xff000000|(red<<16)|(green<<8)|blue);
	}
	
	public List<BlockCell> getBlockCells() {
		return blockCells;
	}

	public String getName() {
		return name;
	}
	
	
}
