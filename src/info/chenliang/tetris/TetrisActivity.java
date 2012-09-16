package info.chenliang.tetris;

import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.view.View.OnTouchListener;

public class TetrisActivity extends Activity {

	private TetrisView tetrisView;
	private Window window;
	private BlockContainer blockContainer;
	private BlockGenerator blockGenerator;
	
	private RelativeLayout relativeLayout;
	
	private final int ID_VIEW = 3000;
	private final int ID_LEFT_BUTTON = 3001;
	private final int ID_RIGHT_BUTTON = 3002;
	private final int ID_TRANSFORM_BUTTON = 3003;
	private final int ID_DOWN_BUTTON = 3004;
	private final int ID_INSTANT_DOWN_BUTTON = 3005;
	private final int ID_PAUSE_BUTTON = 3006;
	private final int ID_HOLD_BUTTON = 3007;
	
	private Thread gameThread;
	private long lastTickTime;
	private boolean paused;
	
	private final static int REFRESH_INVERVAL = 30;
	
	private Tetris tetris;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setFullScreen();
        
        blockContainer = new BlockContainer(20, 10);
        blockGenerator = new BlockGenerator();
        
        initUi();
        
    }
    
    public void run() {
		// TODO Auto-generated method stub
		lastTickTime = System.currentTimeMillis();
		
		tetrisView.gameInit();
		while(true)
		{
			long currentTime = System.currentTimeMillis();
			int timeElapsed = (int)(currentTime - lastTickTime);
			
			if(!paused)
			{
				tetrisView.gameTick(timeElapsed);
				tetrisView.gameDraw();
								
			}
			
			lastTickTime = currentTime;
			try {
				Thread.sleep(REFRESH_INVERVAL);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
    
    private void setFullScreen()
    {
    	requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    
    private void initUi()
    {
    	relativeLayout = new RelativeLayout(this);
        Context context = relativeLayout.getContext();
        tetrisView = new TetrisView(context, blockContainer, blockGenerator);
        tetrisView.setId(ID_VIEW);
        
        relativeLayout.addView(tetrisView);
        
        
        Button leftButton = new Button(context);
        leftButton.setId(ID_LEFT_BUTTON);
        leftButton.setText("  <-  ");
        leftButton.setOnTouchListener(new BlockController(tetrisView, BlockControlAction.MOVE_LEFT));
        relativeLayout.addView(leftButton);
        
        Button rightButton = new Button(context);
        rightButton.setText("  ->  ");
        rightButton.setId(ID_RIGHT_BUTTON);
        rightButton.setOnTouchListener(new BlockController(tetrisView, BlockControlAction.MOVE_RIGHT));
        relativeLayout.addView(rightButton);
        
        Button downButton = new Button(context);
        downButton.setText("down");
        downButton.setId(ID_DOWN_BUTTON);
        downButton.setOnTouchListener(new BlockController(tetrisView, BlockControlAction.MOVE_DOWN));
        relativeLayout.addView(downButton);
        
        Button transformButton = new Button(context);
        transformButton.setText("  _|_  ");
        transformButton.setId(ID_TRANSFORM_BUTTON);
        transformButton.setOnTouchListener(new BlockController(tetrisView, BlockControlAction.TRANSFORM));
        relativeLayout.addView(transformButton);
        
        final Button pauseButton = new Button(context);
        pauseButton.setText("Pause");
        pauseButton.setId(ID_PAUSE_BUTTON);
        pauseButton.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					if(paused)
					{
						paused = false;
						pauseButton.setText("Pause");
					}
					else
					{
						paused = true;
						pauseButton.setText("Start");
					}
				}
				
				return true;
			}
		});
        relativeLayout.addView(pauseButton);
        
        final Button holdButton = new Button(context);
        holdButton.setText("Hold");
        holdButton.setId(ID_HOLD_BUTTON);
        holdButton.setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				int action = event.getAction();
				if(action == MotionEvent.ACTION_UP){
					if(!paused)
					{
						tetrisView.hold();						
					}
				}
				
				return true;
			}
		});
        relativeLayout.addView(holdButton);
        
        
        Button instantDownButton = new Button(context);
        instantDownButton.setText("|________________|");
        instantDownButton.setId(ID_INSTANT_DOWN_BUTTON);
        instantDownButton.setOnTouchListener(new BlockController(tetrisView, BlockControlAction.INSTANT_DOWN));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		
		paused = true;
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		
		loadGameState();
	}
	
	private void loadGameState()
	{
		
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
	}

	@Override
	public void finish() {
		// TODO Auto-generated method stub
		super.finish();
	}
	
	

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onRestoreInstanceState(savedInstanceState);
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		super.onSaveInstanceState(outState);
		
		outState.putByte("testKey", (byte)100);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
	
	
}
