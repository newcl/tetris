package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

public class BlockPrototype {
	private List<BlockCell> blockCells = new ArrayList<BlockCell>();
	private String name;
	
	public BlockPrototype(String name){
		this.name = name;
	}
	
	public List<BlockCell> getBlockCells() {
		return blockCells;
	}

	public String getName() {
		return name;
	}
	
	
}
