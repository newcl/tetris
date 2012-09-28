package info.chenliang.fatrock.showcase;

import info.chenliang.ds.Matrix3x3;
import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.PixelRenderer;
import info.chenliang.fatrock.TriangleRenderer;
import info.chenliang.fatrock.Vertex3d;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;

public class FatRockView extends SurfaceView implements Callback, Runnable, PixelRenderer{
	boolean running;
	SurfaceHolder holder;
	Camera camera;
	Paint paint;
	Vertex3d[] points, transformedPoints;	
	float size = 20;
	int angle;
	TriangleRenderer triangleRenderer;
	Canvas canvas;
	Vector3d colorVector;
	public FatRockView(Context context)
	{
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		points = new Vertex3d[8];
		points[0] = new Vertex3d(new Vector4d(0, 0, 0), new Vector3d(0, 0,0xff));
		points[1] = new Vertex3d(new Vector4d(size, 0, 0), new Vector3d(0, 0, 0xff));
		points[2] = new Vertex3d(new Vector4d(size, 0, size), new Vector3d(0, 0, 0xff));
		points[3] = new Vertex3d(new Vector4d(0, 0, size), new Vector3d(0, 0, 0xff));
		
		points[4] = new Vertex3d(new Vector4d(0, size, 0), new Vector3d(0, 0, 0xff));
		points[5] = new Vertex3d(new Vector4d(size, size, 0), new Vector3d(0, 0, 0xff));
		points[6] = new Vertex3d(new Vector4d(size, size, size), new Vector3d(0, 0, 0xff));
		points[7] = new Vertex3d(new Vector4d(0, size, size), new Vector3d(0, 0, 0xff));
		
		transformedPoints = new Vertex3d[8]; 
		for(int i=0; i < 8; i++)
		{
			transformedPoints[i] = new Vertex3d();
			
			points[i].position.x -= size /2;
			points[i].position.y -= size /2;
			points[i].position.z -= size /2;
		}
		
		
		colorVector = new Vector3d(0, 255, 0);
		
		paint = new Paint();
	}

	public void run() {
		// TODO Auto-generated method stub
		running = true;
		while(running)
		{
			canvas = holder.lockCanvas();
			if(canvas == null)
			{
				continue;
			}
			canvas.drawColor(0xffff00ff);
			
			Vector3d n = new Vector3d(1,1,1);
			n.normalize();
			Matrix3x3 r = Matrix3x3.buildRotateMatrix(n, angle);
			
			for(int i=0; i < 8; i++)
			{
				Vector3d v = r.transform(points[i].position.degenerate());
//				v.x += x;
//				v.y += y;
				v.z += 100;
				
				transformedPoints[i].position.set(v, 1.0f);
				
				Vector4d v2 = camera.getWorldToCameraTransform().transform(transformedPoints[i].position);
				v2 = camera.getCameraToProjectionTransform().transform(v2);
				v2.x /= v2.w;
				v2.y /= v2.w;
				v2.z /= v2.w;
				float z = v2.w;
				v2.w /= v2.w;
				v2 = camera.getProjectionToScreenTransform().transform(v2);
				v2.w = z*32767;
				transformedPoints[i].position = v2;
			}
			
			angle += 5;
			angle %= 360;
			
			long st = System.currentTimeMillis();
			triangleRenderer.resetZBuffer();
			triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[2], transformedPoints[0], new Vector3d(0xff,0,0));
			triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[0], transformedPoints[2], new Vector3d(0xff,0,0));
//			triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[1], transformedPoints[3], new Vector3d(0xff,0,0));
//			triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[2], transformedPoints[3], new Vector3d(0xff,0,0));
			
			triangleRenderer.fillTriangle(transformedPoints[5], transformedPoints[4], transformedPoints[6], new Vector3d(0,0xff,0));
			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[6], transformedPoints[4], new Vector3d(0,0xff,0));
//			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[5], transformedPoints[4], new Vector3d(0,0xff,0));
//			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[6], transformedPoints[5], new Vector3d(0,0xff,0));
			
			triangleRenderer.fillTriangle(transformedPoints[4], transformedPoints[5], transformedPoints[0], new Vector3d(0,0,0xff));
			triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[0], transformedPoints[5], new Vector3d(0,0,0xff));
//			triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[0], transformedPoints[4], new Vector3d(0,0,0xff));
//			triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[4], transformedPoints[5], new Vector3d(0,0,0xff));
			
			triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[1], transformedPoints[6], new Vector3d(0xff,0xff,0));
			triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[5], transformedPoints[1], new Vector3d(0xff,0xff,0));
//			triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[1], transformedPoints[5], new Vector3d(0xff,0xff,0));
//			triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[5], transformedPoints[2], new Vector3d(0xff,0xff,0));
			
			triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[7], transformedPoints[2], new Vector3d(128,0,128));
			triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[7], transformedPoints[2], new Vector3d(128,0,128));
//			triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[7], transformedPoints[3], new Vector3d(0xff,0,0xff));
//			triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[6], transformedPoints[2], new Vector3d(0xff,0,0xff));
			
			triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[3], transformedPoints[4], new Vector3d(0,0xff,0xff));
			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[4], transformedPoints[3], new Vector3d(0,0xff,0xff));
//			triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[7], transformedPoints[4], new Vector3d(0,0xff,0xff));
//			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[0], transformedPoints[3], new Vector3d(0,0xff,0xff));
			holder.unlockCanvasAndPost(canvas);
			
			long tt = System.currentTimeMillis() - st;
			Log.d("info.chenliang.fatrock", ""+tt);
			synchronized(this)
			{
				try {
					wait(33);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3) {
		// TODO Auto-generated method stub
		
	}

	public void surfaceCreated(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		camera = new Camera(new Vector3d(0, 0, 0), new Vector3d(0, 0, 1), new Vector3d(0, 1, 0), 90, 10, 150, getWidth(), getHeight(), 0, 0);
		triangleRenderer = new TriangleRenderer(this, getWidth(), getHeight());
		new Thread(this).start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setPixel(int x, int y, int color) {
		// TODO Auto-generated method stub
		paint.setStrokeWidth(1);
		paint.setAlpha(255);
		paint.setStyle(Style.STROKE);
		paint.setStrokeCap(Cap.SQUARE);
		
		paint.setColor(0xff000000|color);
		canvas.drawPoint(x, y, paint);
	}
}
