package info.chenliang.tetris;

import info.chenliang.ds.Matrix3x3;
import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.CubeSceneObject;
import info.chenliang.fatrock.Material;
import info.chenliang.fatrock.SceneObject;
import info.chenliang.fatrock.Triangle;
import info.chenliang.fatrock.Vertex3d;
import info.chenliang.fatrock.trianglerenderers.TriangleRenderer;

public class GameObject3d extends GameObject{
	protected float z;
	protected Camera camera;
	protected TriangleRenderer triangleRenderer;
	//protected Vertex3d[] points, transformedPoints;
	
	//protected List<Triangle> triangles;
	private float size;
	private int xOffset, yOffset;
	private GameObject3dPath path;
	private int pathIndex;
	private float xOffsetStep, yOffsetStep;
	private float zStep;
	private Vector3d colorVector;
	protected SceneObject sceneObject;
	private Vector3d n = new Vector3d(1,1,1);	
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
		
		lifeTime = 8000;
		
		path = new GameObject3dPath();
		colorVector = new Vector3d((color&0xff0000)>>16, (color&0xff00)>>8, color&0xff);
		Material material = new Material();
		material.emission = new Vector3d(0, 0, 0);
		material.diffuse = new Vector3d(1.0f, 1.0f, 1.0f);
		material.ambient = new Vector3d(1.0f, 1.0f, 1.0f);
		material.specular = new Vector3d(0, 0, 0);
		sceneObject = new CubeSceneObject(null, new Vector3d(0, 0, z), size, material);
	}
	
	public void initPath()
	{
		float time = lifeTime/path.elements.size();
		for(int i=0;i < path.elements.size();i++)
		{
			GameObject3dPathElement element = path.elements.get(i);
			element.time = time;
		}
		
		initPathSteps();
	}
	
	private void initPathSteps()
	{
		if(path != null & pathIndex < path.elements.size())
		{
			GameObject3dPathElement element = path.elements.get(pathIndex);
			xOffsetStep = (element.position.x - xOffset)/ element.time;
			yOffsetStep = (element.position.y - yOffset)/ element.time;
			
			zStep = (element.position.z - z)/element.time;
		}
	}
	
	int angle;
	public void tick(int timeElapsed) {
		lifeTime -= timeElapsed;

		xOffset += xOffsetStep*timeElapsed;
		yOffset += yOffsetStep*timeElapsed;

		camera.setScreenOffsets(xOffset, yOffset);
		sceneObject.rotate(n, angle);
		//sceneObject.update();
		
		triangleRenderer.resetZBuffer();
		
		for(int i=0; i < sceneObject.mesh.vertices.size(); i ++)
		{
			Vertex3d v = sceneObject.mesh.vertices.get(i);
			v.transformedPosition = sceneObject.transform.transform(v.position); 
			v.transformedPosition = camera.getWorldToCameraTransform().transform(v.transformedPosition);
			Vector4d v2 = camera.getCameraToProjectionTransform().transform(v.transformedPosition);
			v2.x /= v2.w;
			v2.y /= v2.w;
			v2.z /= v2.w;
			
			float z = v2.w;
			v2.w /= v2.w;
			
			v2 = camera.getProjectionToScreenTransform().transform(v2);
			v2.w = z;
			
			v.transformedPosition.copy(v2);
		}
		
		angle += 10;
		angle %= 360;
		
		GameObject3dPathElement element = path.elements.get(pathIndex);
		element.time -= timeElapsed;
		if(element.time <= 0)
		{
			xOffset = (int)element.position.x;
			yOffset = (int)element.position.y;
			z = element.position.z;
			
			pathIndex++;
			initPathSteps();
		}
	}
	
	int count;
	int ct = 0;
	public void draw(GameCanvas canvas) 
	{
		for (int i = 0; i < sceneObject.mesh.triangles.size(); i++) 
		{
			Triangle triangle = sceneObject.mesh.triangles.get(i);
			Vertex3d v1 = triangle.mesh.vertices.get(triangle.v1);
			Vertex3d v2 = triangle.mesh.vertices.get(triangle.v2);
			Vertex3d v3 = triangle.mesh.vertices.get(triangle.v3);
			
			triangleRenderer.fillTriangle(v1, v2, v3);
		}
	}
	
	public GameObject3dPath getPath() {
		return path;
	}
	public void setPath(GameObject3dPath path) {
		this.path = path;
	}
	
	public float getZ() {
		return z;
	}

	public void setZ(float z) {
		this.z = z;
	}

	public int getxOffset() {
		return xOffset;
	}

	public void setxOffset(int xOffset) {
		this.xOffset = xOffset;
	}

	public int getyOffset() {
		return yOffset;
	}

	public void setyOffset(int yOffset) {
		this.yOffset = yOffset;
	}
	
	
}
