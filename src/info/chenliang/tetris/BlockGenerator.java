package info.chenliang.tetris;

import java.util.ArrayList;
import java.util.List;

public class BlockGenerator {

	private List<BlockPrototype> blockPrototypes ;
	private List<List<FixedFrame>> fixedFrames;
	private List<Vector3d> colors ;
	
	public BlockGenerator(){
		initColors();
		initFixedFrames();
		registerBlockPrototypes();
	}
	
	private void initFixedFrames()
	{
		fixedFrames = new ArrayList<List<FixedFrame>>();
		
		List<FixedFrame> frames = null;
		List<BlockCell> cells = null;
		
		//|_____|
		frames = new ArrayList<FixedFrame>();
		
		cells = new ArrayList<BlockCell>();
		cells.add(new BlockCell(-2,-2));
		cells.add(new BlockCell(2,-2));
		cells.add(new BlockCell(-4,0));
		cells.add(new BlockCell(-2,0));
		frames.add(new FixedFrame(cells));
		
		cells = new ArrayList<BlockCell>();
		cells.add(new BlockCell(-2,-4));
		cells.add(new BlockCell(-2,-2));
		cells.add(new BlockCell(0,-2));
		cells.add(new BlockCell(0,0));
		frames.add(new FixedFrame(cells));
		
		fixedFrames.add(frames);
		
		//   __
		//__|
		frames = new ArrayList<FixedFrame>();
		
		cells = new ArrayList<BlockCell>();
		cells.add(new BlockCell(-2,-2));
		cells.add(new BlockCell(0,-2));
		cells.add(new BlockCell(-4,0));
		cells.add(new BlockCell(-2,0));
		frames.add(new FixedFrame(cells));
		
		cells = new ArrayList<BlockCell>();
		cells.add(new BlockCell(-2,-4));
		cells.add(new BlockCell(-2,-2));
		cells.add(new BlockCell(0,-2));
		cells.add(new BlockCell(0,0));
		frames.add(new FixedFrame(cells));
		
		fixedFrames.add(frames);
		
		//__
		//  |__
		frames = new ArrayList<FixedFrame>();
		
		cells = new ArrayList<BlockCell>();
		cells.add(new BlockCell(-2,-2));
		cells.add(new BlockCell(0,-2));
		cells.add(new BlockCell(0,0));
		cells.add(new BlockCell(2,0));
		frames.add(new FixedFrame(cells));
		
		cells = new ArrayList<BlockCell>();
		cells.add(new BlockCell(-2,-2));
		cells.add(new BlockCell(-2,0));
		cells.add(new BlockCell(0,-4));
		cells.add(new BlockCell(0,-2));
		frames.add(new FixedFrame(cells));
		
		fixedFrames.add(frames);
	}
	
	private void initColors()
	{
		colors = new ArrayList<Vector3d>();
		colors.add(new Vector3d(255, 0, 0));
		colors.add(new Vector3d(255, 125, 0));
		colors.add(new Vector3d(0, 255, 0));
		colors.add(new Vector3d(255, 255, 0));
		colors.add(new Vector3d(0, 255, 255));
		colors.add(new Vector3d(255, 0, 0));
		colors.add(new Vector3d(0, 0, 255));
	}
	
	private void registerBlockPrototypes(){
		blockPrototypes = new ArrayList<BlockPrototype>();
		BlockPrototype protoType = null;
		
		protoType = new BlockPrototype("_|_", 255, 0, 0);
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-3,-1));
		protoType.getBlockCells().add(new BlockCell(1,-1));
		
		blockPrototypes.add(protoType);
		
		/*
		protoType = new BlockPrototype("|_____|", 255, 125, 0);
		protoType.getBlockCells().add(new BlockCell(0,-4));
		protoType.getBlockCells().add(new BlockCell(0,-2));
		protoType.getBlockCells().add(new BlockCell(0,0));
		protoType.getBlockCells().add(new BlockCell(0,2));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("__|--", 0, 255, 0);
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(1,-3));
		protoType.getBlockCells().add(new BlockCell(-3,-1));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("--|__", 255 ,255 ,0);
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-3,-3));
		protoType.getBlockCells().add(new BlockCell(1,-1));
		
		blockPrototypes.add(protoType);
		*/
		
		protoType = new BlockPrototype("__|", 0, 255, 255);
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,1));
		protoType.getBlockCells().add(new BlockCell(-3,-1));
		
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("|__", 255, 0, 255);
		protoType.getBlockCells().add(new BlockCell(-1,-1));
		protoType.getBlockCells().add(new BlockCell(-1,-3));
		protoType.getBlockCells().add(new BlockCell(-1,1));
		protoType.getBlockCells().add(new BlockCell(1,1));
		blockPrototypes.add(protoType);
		
		protoType = new BlockPrototype("|+|", 0, 0, 255);
		protoType.getBlockCells().add(new BlockCell(-2,-2));
		protoType.getBlockCells().add(new BlockCell(0,-2));
		protoType.getBlockCells().add(new BlockCell(0,0));
		protoType.getBlockCells().add(new BlockCell(-2,0));
		
		blockPrototypes.add(protoType);
	}
	
	private int getRandomColor()
	{
		int index = (int)(Math.random() * colors.size());
		Vector3d color = colors.get(index);
		return (0xff000000) | (int)(color.getX()) << 16 | (int)(color.getY()) << 8 | (int)(color.getZ());
	}
	
	private Block generateRandomRotatableBlock(){
		int blockType = (int)(Math.random()*blockPrototypes.size());
		BlockPrototype protoType = blockPrototypes.get(blockType);
		return new RotatableBlock(0, 0, getRandomColor(), protoType.getBlockCells().toArray(new BlockCell[0]));
	}
	
	private Block generateRandomFixedFramesBlock(){
		int frameListIndex = (int)(Math.random()*fixedFrames.size());
		return new FixedFramesBlock(0, 0, getRandomColor(), fixedFrames.get(frameListIndex));
	}
	
	public Block generate()
	{
		int blockTypeCount = blockPrototypes.size() + fixedFrames.size();
		int choice = (int)(Math.random()*blockTypeCount);
		choice = 6;
		if(choice < blockPrototypes.size())
		{
			return generateRandomRotatableBlock();
		}
		else
		{
			return generateRandomFixedFramesBlock();
		}
	}
}
