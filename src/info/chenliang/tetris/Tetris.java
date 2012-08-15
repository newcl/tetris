package info.chenliang.tetris;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.Window;
import android.view.WindowManager;

public class Tetris extends Activity {

	private TetrisView tetrisView;
	private Window window;
	private BlockContainer blockContainer;
	private BlockGenerator blockGenerator;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        window = getWindow();
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        
        blockContainer = new BlockContainer(20, 10);
        blockGenerator = new BlockGenerator();
        
        tetrisView = new TetrisView(this, blockContainer, blockGenerator);
        setContentView(tetrisView);
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    
}
