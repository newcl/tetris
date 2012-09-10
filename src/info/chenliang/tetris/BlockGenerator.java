package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

public class BlockGenerator {

	private List<BlockPrototype> blockPrototypes;
	
	public BlockGenerator(){
		blockPrototypes = new ArrayList<BlockPrototype>();
		registerBlockPrototypes();
	}
	
	private void registerBlockPrototypes(){
		BlockPrototype protoType = null;
		
		protoType = new BlockPrototype("_|_");
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-3,-1));
		protoType.getBlockCells().add(new BlockCell(1,-1));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("|_____|");
		protoType.getBlockCells().add(new BlockCell(0,-4));
		protoType.getBlockCells().add(new BlockCell(0,-2));
		protoType.getBlockCells().add(new BlockCell(0,0));
		protoType.getBlockCells().add(new BlockCell(0,2));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("__|--");
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(1,-3));
		protoType.getBlockCells().add(new BlockCell(-3,-1));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("--|__");
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-3,-3));
		protoType.getBlockCells().add(new BlockCell(1,-1));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("__|");
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,1));
		protoType.getBlockCells().add(new BlockCell(-3,-1));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("|__");
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-1,1));
		protoType.getBlockCells().add(new BlockCell(1,1));
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("|+|");
		protoType.getBlockCells().add(new BlockCell(-2,-2));
		protoType.getBlockCells().add(new BlockCell(0,-2));
		protoType.getBlockCells().add(new BlockCell(0,0));
		protoType.getBlockCells().add(new BlockCell(-2,0));
		
		blockPrototypes.add(protoType);
	}
	
	public BlockPrototype getRandomBlockPrototype(){
		int blockType = (int)(Math.random()*blockPrototypes.size());
		//blockType = 5;
		return blockPrototypes.get(blockType);
	}
	
}
