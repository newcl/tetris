package info.chenliang.tetris;

public class BlockController {
	
	public boolean keyPressed(int keyCode){
		BlockControlAction action = translate(keyCode);
		switch(action){
		case TRANSFORM:
			break;
		case MOVE_LEFT:
			break;
		case MOVE_DOWN:
			break;
		case MOVE_RIGHT:
			break;
		}
		
		return true;
	}
	
	private BlockControlAction translate(int keyCode){
		
		return BlockControlAction.NONE;
	}
}
