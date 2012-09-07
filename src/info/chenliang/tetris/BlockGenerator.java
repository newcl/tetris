package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

public class BlockGenerator {
	private Block currentBlock;
	private Block nextBlock;

	private List<Block> blocks;
	
	public BlockGenerator(){
		blocks = new ArrayList<Block>();
		registerBlocks();
		
		currentBlock = generateBlock();
		nextBlock = generateBlock();
	}
	
	private void registerBlocks(){
		int[][][] blockType1 = {
									{
										{},
										{},
										{},
										{}
									},
							   };
	}
	
	public Block getCurrentBlock() {
		return currentBlock;
	}

	public Block getNextBlock() {
		return nextBlock;
	}
	
	private Block generateBlock(){
		BlockCell[] cells = new BlockCell[4];
		cells[0] = new BlockCell(-2,0);
		cells[1] = new BlockCell(-1,0);
		cells[2] = new BlockCell(-1,-1);
		cells[3] = new BlockCell(0,-1);
		
		Block block = new Block(cells, 5, 2, 0xff00ff00);
		
		return block;
	}
	
	public void nextRound(){
		currentBlock = nextBlock;
		nextBlock = generateBlock();
	}
}
