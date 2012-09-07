package info.chenliang.tetris;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class Tetris extends Activity {

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
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        blockContainer = new BlockContainer(20, 10);
        blockGenerator = new BlockGenerator();
        
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
        
        Button instantDownButton = new Button(context);
        instantDownButton.setText("|________________|");
        instantDownButton.setId(ID_INSTANT_DOWN_BUTTON);
        instantDownButton.setOnTouchListener(new BlockController(tetrisView, BlockControlAction.INSTANT_DOWN));
        relativeLayout.addView(instantDownButton);
        
        LayoutParams tetrisLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        tetrisLayoutParam.addRule(RelativeLayout.ABOVE, ID_TRANSFORM_BUTTON);
        //tetrisLayoutParam.addRule(RelativeLayout.ALIGN_TOP, ID_TRANSFORM_BUTTON);
        tetrisView.setLayoutParams(tetrisLayoutParam);
        
        LayoutParams transformButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //transformButtonLayoutParam.addRule(RelativeLayout.BELOW, ID_VIEW);
        /*
        transformButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_DOWN_BUTTON);
        transformButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_LEFT_BUTTON);
        transformButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_RIGHT_BUTTON);
        */
        transformButtonLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        transformButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_DOWN_BUTTON);
        
        transformButton.setLayoutParams(transformButtonLayoutParam);
        
        LayoutParams leftButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //leftButtonLayoutParam.addRule(RelativeLayout.BELOW, ID_TRANSFORM_BUTTON);
        leftButtonLayoutParam.addRule(RelativeLayout.LEFT_OF, ID_DOWN_BUTTON);
        leftButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_INSTANT_DOWN_BUTTON);
        leftButton.setLayoutParams(leftButtonLayoutParam);
        
        LayoutParams downButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //downButtonLayoutParam.addRule(RelativeLayout.RIGHT_OF, ID_LEFT_BUTTON);
        //downButtonLayoutParam.addRule(RelativeLayout.BELOW, ID_TRANSFORM_BUTTON);
        downButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_INSTANT_DOWN_BUTTON);
        downButtonLayoutParam.addRule(RelativeLayout.CENTER_HORIZONTAL);
        downButton.setLayoutParams(downButtonLayoutParam);
        
        LayoutParams rightButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        rightButtonLayoutParam.addRule(RelativeLayout.RIGHT_OF, ID_DOWN_BUTTON);
        rightButtonLayoutParam.addRule(RelativeLayout.ABOVE, ID_INSTANT_DOWN_BUTTON);
        //rightButtonLayoutParam.addRule(RelativeLayout.BELOW, ID_TRANSFORM_BUTTON);
        rightButton.setLayoutParams(rightButtonLayoutParam);
        
        
        LayoutParams instantDownButtonLayoutParam = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        //instantDownButtonLayoutParam.addRule(RelativeLayout.RIGHT_OF, ID_DOWN_BUTTON);
        //instantDownButtonLayoutParam.addRule(RelativeLayout.BELOW, ID_DOWN_BUTTON);
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
}
