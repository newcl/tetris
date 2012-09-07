package info.chenliang.tetris;

import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class BlockController implements OnTouchListener{
	
	private TetrisView tetrisView;
	private BlockControlAction controlAction;
	
	public BlockController(TetrisView tetrisView, BlockControlAction action){
		this.tetrisView = tetrisView;
		this.controlAction = action;
	}

	public boolean onTouch(View v, MotionEvent event) {
		// TODO Auto-generated method stub
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN)
		{
			tetrisView.setCurrentAction(controlAction);
		}else if(action == MotionEvent.ACTION_UP){
			tetrisView.setCurrentAction(BlockControlAction.NONE);
		}
			
		return true;
	}
	
}
