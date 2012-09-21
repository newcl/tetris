package info.chenliang.tetris;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class TetrisActivity extends Activity implements OnTouchListener {

	private TetrisView tetrisView;
	private Window window;
	
	public RelativeLayout relativeLayout;
	
	private final int ID_VIEW = 3000;
	private final int ID_LEFT_BUTTON = 3001;
	private final int ID_RIGHT_BUTTON = 3002;
	private final int ID_TRANSFORM_BUTTON = 3003;
	private final int ID_DOWN_BUTTON = 3004;
	private final int ID_INSTANT_DOWN_BUTTON = 3005;
	private final int ID_PAUSE_BUTTON = 3006;
	private final int ID_HOLD_BUTTON = 3007;
	
	private Tetris tetris;
	
	private Button pauseButton;
	
	public static TetrisActivity instance;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setFullScreen();
        
        initUi();   
        
        instance = this;
    }
    
    private void setFullScreen()
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    private Button createButton(Context context, int id, String text, OnTouchListener onTouchListener)
    {
    	Button button = new Button(context);
    	button.setId(id);
    	button.setText(text);
    	button.setOnTouchListener(onTouchListener);
    	
    	return button;
    }
    
    private void initUi()
    {
    	relativeLayout = new RelativeLayout(this);
    	
        Context context = relativeLayout.getContext();
        tetrisView = new TetrisView(context);
        tetrisView.setId(ID_VIEW);
        
        relativeLayout.addView(tetrisView);
        
        Button leftButton = createButton(context, ID_LEFT_BUTTON, "  <-  ", this);
        relativeLayout.addView(leftButton);
        
        Button rightButton = createButton(context, ID_RIGHT_BUTTON, "  ->  ", this);
        relativeLayout.addView(rightButton);
        
        Button downButton = createButton(context, ID_DOWN_BUTTON, " Down ", this);
        relativeLayout.addView(downButton);
        
        Button transformButton = createButton(context, ID_TRANSFORM_BUTTON, "  _|_  ", this);
        relativeLayout.addView(transformButton);
        
        pauseButton = createButton(context, ID_PAUSE_BUTTON, "Pause", new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					if(tetris.isPaused())
					{
						tetris.setPaused(false);
						pauseButton.setText("Pause");
					}
					else
					{
						tetris.setPaused(true);
						pauseButton.setText("Start");
					}
				}
				
				return true;
			}
		});
        relativeLayout.addView(pauseButton);
        
        final Button holdButton = createButton(context, ID_HOLD_BUTTON, "Hold", new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					if(!tetris.isPaused())
					{
						tetris.hold();						
					}
				}
				
				return true;
			}
		});
        relativeLayout.addView(holdButton);
        
        Button instantDownButton = createButton(context, ID_INSTANT_DOWN_BUTTON, "|_____________|", this);
        relativeLayout.addView(instantDownButton);
        
        LayoutParams tetrisLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tetrisLayoutParam.addRule(RelativeLayout.ABOVE, ID_TRANSFORM_BUTTON);
        tetrisView.setLayoutParams(tetrisLayoutParam);
        
        LayoutParams transformButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        transformButtonLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        transformButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_DOWN_BUTTON);
        transformButton.setLayoutParams(transformButtonLayoutParam);
        
        LayoutParams pauseButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        pauseButtonLayoutParam.addRule(RelativeLayout.LEFT_OF, ID_TRANSFORM_BUTTON);
        pauseButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_DOWN_BUTTON);
        pauseButton.setLayoutParams(pauseButtonLayoutParam);
        
        LayoutParams holdButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        holdButtonLayoutParam.addRule(RelativeLayout.RIGHT_OF, ID_TRANSFORM_BUTTON);
        holdButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_DOWN_BUTTON);
        holdButton.setLayoutParams(holdButtonLayoutParam);
        
        LayoutParams leftButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        leftButtonLayoutParam.addRule(RelativeLayout.LEFT_OF, ID_DOWN_BUTTON);
        leftButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_INSTANT_DOWN_BUTTON);
        leftButton.setLayoutParams(leftButtonLayoutParam);
        
        LayoutParams downButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        downButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_INSTANT_DOWN_BUTTON);
        downButtonLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        downButton.setLayoutParams(downButtonLayoutParam);
        
        LayoutParams rightButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightButtonLayoutParam.addRule(RelativeLayout.RIGHT_OF, ID_DOWN_BUTTON);
        rightButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_INSTANT_DOWN_BUTTON);
        rightButton.setLayoutParams(rightButtonLayoutParam);
        
        
        LayoutParams instantDownButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        instantDownButtonLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        instantDownButtonLayoutParam.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        instantDownButton.setLayoutParams(instantDownButtonLayoutParam);
        
        setContentView(relativeLayout);
    }
    
    public boolean onTouch(View v, MotionEvent event) {
    	if(tetris == null)
    	{
    		return false;
    	}
    	
		int action = event.getAction();
		if(action == MotionEvent.ACTION_DOWN)
		{
			if(v.getId() == ID_DOWN_BUTTON)
			{
				tetris.setCurrentAction(BlockControlAction.MOVE_DOWN);
			}
			else if(v.getId() == ID_LEFT_BUTTON)
			{
				tetris.setCurrentAction(BlockControlAction.MOVE_LEFT);
			}
			else if(v.getId() == ID_RIGHT_BUTTON)
			{
				tetris.setCurrentAction(BlockControlAction.MOVE_RIGHT);
			}
			else if(v.getId() == ID_TRANSFORM_BUTTON)
			{
				tetris.setCurrentAction(BlockControlAction.TRANSFORM);
			}
			else if(v.getId() == ID_INSTANT_DOWN_BUTTON)
			{
				tetris.setCurrentAction(BlockControlAction.INSTANT_DOWN);
			}
			else if(v.getId() == ID_HOLD_BUTTON)
			{
				if(!tetris.isPaused())
				{
					tetris.hold();
				}
			}
			else if(v.getId() == ID_PAUSE_BUTTON)
			{
				tetris.setPaused(true);
			}
		
			return true;
		}else if(action == MotionEvent.ACTION_UP){
			if(v.getId() == ID_DOWN_BUTTON
			|| v.getId() == ID_LEFT_BUTTON
			|| v.getId() == ID_RIGHT_BUTTON 
			|| v.getId() == ID_TRANSFORM_BUTTON
			|| v.getId() == ID_INSTANT_DOWN_BUTTON)
			{
				tetris.setCurrentAction(BlockControlAction.NONE);
				return true;
			}
		}
			
		return false;
	}

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected void onPause() {
		super.onPause();
		tetris.setPaused(true);
	}

	@Override
	protected void onResume() {
		
		super.onResume();
		if(tetris == null)
		{
			tetris = new Tetris(tetrisView, loadGameData());
			tetris.startGame();
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		if(tetris != null)
		{
			tetris.stopGame();
			saveGameData(tetris.toGameData());
			tetris = null;
		}
	}
	
	private void saveGameData(GameData gameData)
	{
		
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}
	
	private GameData loadGameData()
	{
		return null;
	}

	@Override
	protected void onStart() {
		
		super.onStart();
	}

	@Override
	public void finish() {
		
		super.finish();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		
		super.onDestroy();
	}
	
	
}
