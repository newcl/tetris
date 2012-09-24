package info.chenliang.tetris;

import info.chenliang.ds.Matrix4x4;
import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.TriangleRenderer;
import info.chenliang.fatrock.Vertex3d;

public class GameObject3d extends GameObject{
	protected float z;
	protected Camera camera;
	protected TriangleRenderer triangleRenderer;
	protected Vertex3d[] points, transformedPoints;
	public GameObject3d(float x, float y, float z, int color, Camera camera, TriangleRenderer triangleRenderer)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.color = color;
		this.camera = camera;
		this.triangleRenderer = triangleRenderer;
		
		points = new Vertex3d[8];
		float length = 26;
		
		points[0] = new Vertex3d(new Vector3d(0, 0, 0), new Vector3d(0, 0,0xff));
		points[1] = new Vertex3d(new Vector3d(length, 0, 0), new Vector3d(0, 0, 0xff));
		points[2] = new Vertex3d(new Vector3d(length, 0, length), new Vector3d(0, 0, 0xff));
		points[3] = new Vertex3d(new Vector3d(0, 0, length), new Vector3d(0, 0, 0xff));
		
		points[4] = new Vertex3d(new Vector3d(0, length, 0), new Vector3d(0, 0, 0xff));
		points[5] = new Vertex3d(new Vector3d(length, length, 0), new Vector3d(0, 0, 0xff));
		points[6] = new Vertex3d(new Vector3d(length, length, length), new Vector3d(0, 0, 0xff));
		points[7] = new Vertex3d(new Vector3d(0, length, length), new Vector3d(0, 0, 0xff));
		
		
		transformedPoints = new Vertex3d[8];
		for(int i=0; i < 8; i++)
		{
			transformedPoints[i] = new Vertex3d();
		}
		
		lifeTime = 200000000;
	}
	
	public void tick(int timeElapsed) {
		lifeTime -= timeElapsed;
		
		for(int i=0; i < 8; i++)
		{
			Vector4d v = camera.getWorldToCameraTransform().transform(new Vector4d(points[i].position, 1));
			v = camera.getCameraToProjectionTransform().transform(v);
			v.x /= v.w;
			v.y /= v.w;
			v.z /= v.w;
			
			v = camera.getProjectionToScreenTransform().transform(new Vector4d(v.x, v.y, v.z, 1));
			transformedPoints[i].position.set(v.x, v.y, v.z);
		}
	}
	
	public void draw(GameCanvas canvas) {
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
	}
}
