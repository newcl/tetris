package info.chenliang.fatrock.showcase;

import info.chenliang.ds.Matrix3x3;
import info.chenliang.ds.Matrix4x4;
import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.CubeSceneObject;
import info.chenliang.fatrock.DirectionLight;
import info.chenliang.fatrock.DotLight;
import info.chenliang.fatrock.FixedSizeZBuffer;
import info.chenliang.fatrock.Light;
import info.chenliang.fatrock.Material;
import info.chenliang.fatrock.PixelRenderer;
import info.chenliang.fatrock.Triangle;
import info.chenliang.fatrock.TriangleRenderer;
import info.chenliang.fatrock.Vertex3d;
import info.chenliang.fatrock.ZBufferComparerGreaterThan;
import info.chenliang.fatrock.ZBufferComparerLessThan;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;

public class FatRockView extends SurfaceView implements Callback, Runnable, PixelRenderer{
	boolean running;
	SurfaceHolder holder;
	Camera camera;
	Paint paint;
	//Vertex3d[] points, transformedPoints;	
	float size = 30;
	int angle;
	TriangleRenderer triangleRenderer;
	TriangleRenderer triangleRenderer2;
	Canvas canvas;
	Vector3d colorVector;
	boolean rotate = true;
	CubeSceneObject cube;
	Vector3d fixedColor;
	Vector3d r = new Vector3d(1, 1, 1);
	DirectionLight light;
	DotLight dotLight;
	public FatRockView(Context context)
	{
		super(context);
		holder = getHolder();
		holder.addCallback(this);
		
		/*
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
		
		*/
		colorVector = new Vector3d(0, 255, 0);
		
		paint = new Paint();
		setOnTouchListener(new OnTouchListener() {
			
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if(event.getAction() == MotionEvent.ACTION_DOWN)
				{
					rotate = !rotate;
				}
				return true;
			}
		});
		
		paint.setStrokeWidth(1);
		paint.setAlpha(255);
		paint.setStyle(Style.STROKE);
		paint.setStrokeCap(Cap.SQUARE);
		
		Material material = new Material();
		material.ambient = new Vector3d(1.0f, 1.0f, 1.0f);
		material.diffuse = new Vector3d(0.8f, 0.8f, 0.8f);
		material.specular = new Vector3d(1.0f, 1.0f, 1.0f);
		material.emission = new Vector3d(1.0f, 1.0f, 1.0f);
		
		cube = new CubeSceneObject(null, new Vector3d(0, 0, 90), 30, material);
		fixedColor = new Vector3d(255.0f, 0.0f, 0.0f);
		
		r = new Vector3d(1, 1, 1);
		r.normalize();
		
		light = new DirectionLight(new Vector3d(0, 0, 0), new Vector3d(255, 255, 255), new Vector3d(0.0f, 0.0f, 0.0f), new Vector3d(0, 0, 1));
		dotLight = new DotLight(new Vector3d(0, 0, 0), new Vector3d(255, 255, 255), new Vector3d(0.0f, 0.0f, 0.0f), new Vector3d(0, 0, 0), 0.1f, 0, 0);
	}
	
	public void run() {
		running = true;
		while(running)
		{
			canvas = holder.lockCanvas();
			if(canvas == null)
			{
				continue;
			}
			canvas.drawColor(0xffffffff);
			Matrix3x3 rm = Matrix3x3.buildRotateMatrix(r, angle);
			for(int i=0; i < cube.mesh.vertices.size(); i ++)
			{
				Vertex3d v = cube.mesh.vertices.get(i);
				v.transformedPosition.set(rm.transform(v.position.degenerate()), 1);
				//v.transformedPosition.copy(v.position);
				v.transformedPosition = cube.translate.transform(v.transformedPosition); 
				v.transformedPosition = camera.getWorldToCameraTransform().transform(v.transformedPosition);
//				Vector4d v2 = camera.getCameraToProjectionTransform().transform(v.transformedPosition);
//				v2.x /= v2.w;
//				v2.y /= v2.w;
//				v2.z /= v2.w;
//				
//				float z = v2.w;
//				v2.w /= v2.w;
//				
//				v2 = camera.getProjectionToScreenTransform().transform(v2);
//				v2.w = z*32767;
//				//v2.w = z;
//				
//				v.transformedPosition.copy(v2);
			}
			
			cube.mesh.updateNormals();
			
			for(int i=0; i < cube.mesh.vertices.size(); i ++)
			{
				Vertex3d v = cube.mesh.vertices.get(i);
				light.light(v);
			}
			
			for(int i=0; i < cube.mesh.vertices.size(); i ++)
			{
				Vertex3d v = cube.mesh.vertices.get(i);
				Vector4d v2 = camera.getCameraToProjectionTransform().transform(v.transformedPosition);
				v2.x /= v2.w;
				v2.y /= v2.w;
				v2.z /= v2.w;
				
				float z = v2.w;
				v2.w /= v2.w;
				
				v2 = camera.getProjectionToScreenTransform().transform(v2);
				v2.w = z*32767;
				//v2.w = z;
				
				v.transformedPosition.copy(v2);
			}
			
			triangleRenderer.resetZBuffer();
			for(int i=0; i < cube.mesh.triangles.size(); i ++)
			{
				Triangle triangle = cube.mesh.triangles.get(i);
				
				triangleRenderer.fillTriangle(triangle.mesh.vertices.get(triangle.v1), triangle.mesh.vertices.get(triangle.v2), triangle.mesh.vertices.get(triangle.v3));
			}
			
			holder.unlockCanvasAndPost(canvas);
			
			angle += 6;
			angle %= 360;
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
		triangleRenderer = new TriangleRenderer(this, new FixedSizeZBuffer(getWidth(), getHeight(), new ZBufferComparerGreaterThan()), true);
		triangleRenderer2 = new TriangleRenderer(this, new FixedSizeZBuffer(getWidth(), getHeight(), new ZBufferComparerLessThan()), false);
		new Thread(this).start();
	}

	public void surfaceDestroyed(SurfaceHolder arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setPixel(int x, int y, int color) {
		paint.setColor(0xff000000|color);
		canvas.drawPoint(x, y, paint);
	}
}
