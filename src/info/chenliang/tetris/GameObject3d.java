package info.chenliang.tetris;

import info.chenliang.debug.Logger;
import info.chenliang.ds.Matrix3x3;
import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.Triangle;
import info.chenliang.fatrock.TriangleRenderer;
import info.chenliang.fatrock.Vertex3d;

import java.util.ArrayList;
import java.util.List;

public class GameObject3d extends GameObject{
	protected float z;
	protected Camera camera;
	protected TriangleRenderer triangleRenderer;
	protected Vertex3d[] points, transformedPoints;
	
	protected List<Triangle> triangles;
	private float size;
	private int xOffset, yOffset;
	public GameObject3d(int xOffset, int yOffset, float z, int color, float size, Camera camera, TriangleRenderer triangleRenderer)
	{
		this.x = 0;
		this.y = 0;
		this.z = z;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.color = color;
		this.size = size;
		this.camera = camera;
		this.triangleRenderer = triangleRenderer;
		
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
		
		lifeTime = 200000000;
		
		triangles = new ArrayList<Triangle>();
	}
	
	int angle;
	public void tick(int timeElapsed) {
		Logger.startLog("3d tick");
		
		lifeTime -= timeElapsed;
		
		triangles.clear();
		
		camera.setScreenOffsets(xOffset, yOffset);
		
		Vector3d n = new Vector3d(1,1,1);
		n.normalize();
		Matrix3x3 r = Matrix3x3.buildRotateMatrix(n, angle);
		
		for(int i=0; i < 8; i++)
		{
			Vector3d v = r.transform(points[i].position.degenerate());
			v.x += x;
			v.y += y;
			v.z += z;
			
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
		
		Logger.endLog();
	}
	
	int count;
	int ct = 0;
	public void draw(GameCanvas canvas) {
		/*
		Logger.startLog("3d draw");
		
		for(int i=0; i < triangles.size(); i++)
		{
			Triangle triangle = triangles.get(i);
			triangleRenderer.fillTriangle(triangle.v1, triangle.v2, triangle.v3, triangle.color);
			
			float offset = 100f;
			
			Vertex3d v1 = new Vertex3d(new Vector4d(triangle.v1.position.x-offset, triangle.v1.position.y-offset, triangle.v1.position.z-offset), triangle.v1.color);
			Vertex3d v2 = new Vertex3d(new Vector4d(triangle.v2.position.x-offset, triangle.v2.position.y-offset, triangle.v2.position.z-offset), triangle.v2.color);
			Vertex3d v3 = new Vertex3d(new Vector4d(triangle.v3.position.x-offset, triangle.v3.position.y-offset, triangle.v3.position.z-offset), triangle.v3.color);
			
			triangleRenderer.fillTriangle2(v1, v2, v3, triangle.color);	
		}
		
		Logger.endLog();
		*/
				
		/*
		triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[1], transformedPoints[2], new Vector3d(0xff,0,0));
		triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[2], transformedPoints[3], new Vector3d(0xff,0,0));
		
		triangleRenderer.fillTriangle(transformedPoints[4], transformedPoints[5], transformedPoints[6], new Vector3d(0,0xff,0));
		triangleRenderer.fillTriangle(transformedPoints[5], transformedPoints[6], transformedPoints[7], new Vector3d(0,0xff,0));
		
		triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[1], transformedPoints[5], new Vector3d(0,0,0xff));
		triangleRenderer.fillTriangle(transformedPoints[5], transformedPoints[4], transformedPoints[0], new Vector3d(0,0,0xff));
		
		triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[2], transformedPoints[6], new Vector3d(0xff,0xff,0));
		triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[5], transformedPoints[1], new Vector3d(0xff,0xff,0));
		
		triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[3], transformedPoints[7], new Vector3d(0xff,0,0xff));
		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[6], transformedPoints[2], new Vector3d(0xff,0,0xff));
		
		triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[3], transformedPoints[7], new Vector3d(0,0xff,0xff));
		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[4], transformedPoints[0], new Vector3d(0,0xff,0xff));
		*/
		
		/*
		if(count == 0)
		{
			triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[2], transformedPoints[0], new Vector3d(0xff,0,0));
			triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[0], transformedPoints[2], new Vector3d(0xff,0,0));
		}
		else if(count == 1)
		{
			triangleRenderer.fillTriangle(transformedPoints[5], transformedPoints[4], transformedPoints[6], new Vector3d(0,0xff,0));
			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[6], transformedPoints[4], new Vector3d(0,0xff,0));
		}
		else if(count == 2)
		{
			triangleRenderer.fillTriangle(transformedPoints[4], transformedPoints[5], transformedPoints[0], new Vector3d(0,0,0xff));
			triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[0], transformedPoints[5], new Vector3d(0,0,0xff));
		}
		else if(count == 3)
		{
			triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[1], transformedPoints[6], new Vector3d(0xff,0xff,0));
			triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[5], transformedPoints[1], new Vector3d(0xff,0xff,0));
		}
		else if(count == 4)
		{
			triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[7], transformedPoints[2], new Vector3d(0xff,0,0xff));
			triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[7], transformedPoints[2], new Vector3d(0xff,0,0xff));
		}
		else if(count == 5)
		{
			triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[3], transformedPoints[4], new Vector3d(0,0xff,0xff));
			triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[4], transformedPoints[3], new Vector3d(0,0xff,0xff));
		}
		
		ct += 50;
		if(ct >= 1000)
		{
			count ++;
			count %= 6;
			ct = 0;
		}
		*/
		
		
		triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[2], transformedPoints[0], new Vector3d(0xff,0,0));
		triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[0], transformedPoints[2], new Vector3d(0xff,0,0));
//		triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[1], transformedPoints[3], new Vector3d(0xff,0,0));
//		triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[2], transformedPoints[3], new Vector3d(0xff,0,0));
		
		triangleRenderer.fillTriangle(transformedPoints[5], transformedPoints[4], transformedPoints[6], new Vector3d(0,0xff,0));
		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[6], transformedPoints[4], new Vector3d(0,0xff,0));
//		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[5], transformedPoints[4], new Vector3d(0,0xff,0));
//		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[6], transformedPoints[5], new Vector3d(0,0xff,0));
		
		triangleRenderer.fillTriangle(transformedPoints[4], transformedPoints[5], transformedPoints[0], new Vector3d(0,0,0xff));
		triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[0], transformedPoints[5], new Vector3d(0,0,0xff));
//		triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[0], transformedPoints[4], new Vector3d(0,0,0xff));
//		triangleRenderer.fillTriangle(transformedPoints[1], transformedPoints[4], transformedPoints[5], new Vector3d(0,0,0xff));
		
		triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[1], transformedPoints[6], new Vector3d(0xff,0xff,0));
		triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[5], transformedPoints[1], new Vector3d(0xff,0xff,0));
//		triangleRenderer.fillTriangle(transformedPoints[2], transformedPoints[1], transformedPoints[5], new Vector3d(0xff,0xff,0));
//		triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[5], transformedPoints[2], new Vector3d(0xff,0xff,0));
		
		triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[7], transformedPoints[2], new Vector3d(0xff,0,0xff));
		triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[7], transformedPoints[2], new Vector3d(0xff,0,0xff));
//		triangleRenderer.fillTriangle(transformedPoints[6], transformedPoints[7], transformedPoints[3], new Vector3d(0xff,0,0xff));
//		triangleRenderer.fillTriangle(transformedPoints[3], transformedPoints[6], transformedPoints[2], new Vector3d(0xff,0,0xff));
		
		triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[3], transformedPoints[4], new Vector3d(0,0xff,0xff));
		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[4], transformedPoints[3], new Vector3d(0,0xff,0xff));
//		triangleRenderer.fillTriangle(transformedPoints[0], transformedPoints[7], transformedPoints[4], new Vector3d(0,0xff,0xff));
//		triangleRenderer.fillTriangle(transformedPoints[7], transformedPoints[0], transformedPoints[3], new Vector3d(0,0xff,0xff));
		
	}
}
