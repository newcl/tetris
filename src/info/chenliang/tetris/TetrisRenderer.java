package info.chenliang.tetris;

import javax.microedition.khronos.egl.EGLConfig;

import javax.microedition.khronos.opengles.GL10;

import android.opengl.GLES20;
import android.opengl.GLSurfaceView.Renderer;

public class TetrisRenderer implements Renderer {

	@Override
	public void onDrawFrame(GL10 arg0) {
		//GLES20.glClearColor(1.0f, 0f, 0f, 1f);
		//GLES20.glClear(0);
		
	}

	@Override
	public void onSurfaceChanged(GL10 arg0, int arg1, int arg2) {
		
	}

	@Override
	public void onSurfaceCreated(GL10 arg0, EGLConfig arg1) {
		
	}

}
