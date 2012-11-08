package info.chenliang.tetris;

import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
import info.chenliang.fatrock.Camera;
import info.chenliang.fatrock.CubeSceneObject;
import info.chenliang.fatrock.Material;
import info.chenliang.fatrock.ProjectionType;
import info.chenliang.fatrock.SceneObject;
import info.chenliang.fatrock.Triangle;
import info.chenliang.fatrock.Vertex3d;
import info.chenliang.fatrock.trianglerenderers.TriangleRendererConstant;

import java.util.ArrayList;
import java.util.List;

public class FloatingCube extends GameObject{
	public float z;
	public Camera camera;
	public TriangleRendererConstant triangleRendererConstant;
	public float size;
	public int xOffset, yOffset;
	public GameObject3dPath path;
	public int pathIndex;
	public float xOffsetStep, yOffsetStep;
	public float zStep;
	public Vector3d pathStep;
	public Vector3d colorVector;
	public SceneObject sceneObject;
	public Vector3d rotateAround;	
	public int angle;
	public Vector3d position;
	public static List<Vector3d> ROTATE_AROUND_VECTORS;
	
	static
	{
		ROTATE_AROUND_VECTORS = new ArrayList<Vector3d>();
//		ROTATE_AROUND_VECTORS.add(new Vector3d(1, 0, 0));
//		ROTATE_AROUND_VECTORS.add(new Vector3d(-1, 0, 0));
//		ROTATE_AROUND_VECTORS.add(new Vector3d(0, 1, 0));
//		ROTATE_AROUND_VECTORS.add(new Vector3d(0, -1, 0));
//		ROTATE_AROUND_VECTORS.add(new Vector3d(0, 0, 1));
//		ROTATE_AROUND_VECTORS.add(new Vector3d(0, 0, -1));
		
		ROTATE_AROUND_VECTORS.add(new Vector3d(1, 1, 0));
		ROTATE_AROUND_VECTORS.add(new Vector3d(1, -1, 0));
		
		ROTATE_AROUND_VECTORS.add(new Vector3d(-1, 1, 0));
		ROTATE_AROUND_VECTORS.add(new Vector3d(-1, -1, 0));
		
		ROTATE_AROUND_VECTORS.add(new Vector3d(1, 0, 1));
		ROTATE_AROUND_VECTORS.add(new Vector3d(1, 0, -1));
		
		ROTATE_AROUND_VECTORS.add(new Vector3d(-1, 0, 1));
		ROTATE_AROUND_VECTORS.add(new Vector3d(-1, 0, -1));
		
		ROTATE_AROUND_VECTORS.add(new Vector3d(0, 1, 1));
		ROTATE_AROUND_VECTORS.add(new Vector3d(0, 1, -1));
		
		ROTATE_AROUND_VECTORS.add(new Vector3d(0, -1, 1));
		ROTATE_AROUND_VECTORS.add(new Vector3d(0, -1, -1));
		
		//----------------------------------------------
		for(int x=-1; x <= 1; x += 2)
		{
			for(int y=-1; y <= 1; y += 2)
			{
				for(int z=-1; z <= 1; z += 2)
				{
					ROTATE_AROUND_VECTORS.add(new Vector3d(x, y, z));			
				}
			}
		}
		
		
		for(Vector3d v:ROTATE_AROUND_VECTORS)
		{
			v.normalize();
		}
		
	}
	
	public FloatingCube(int xOffset, int yOffset, float z, int color, float size, Camera camera, TriangleRendererConstant triangleRendererConstant)
	{
		this.x = 0;
		this.y = 0;
		this.z = z;
		this.xOffset = xOffset;
		this.yOffset = yOffset;
		this.color = color;
		this.size = size;
		this.camera = camera;
		this.triangleRendererConstant = triangleRendererConstant;
		
		position = new Vector3d(xOffset, yOffset, z);
		
		lifeTime = 1800;
		
		colorVector = new Vector3d((color&0xff0000)>>16, (color&0xff00)>>8, color&0xff);
		Material material = new Material();
		material.emission = new Vector3d(0, 0, 0);
		material.diffuse = new Vector3d(1.0f, 1.0f, 1.0f);
		material.ambient = new Vector3d(1.0f, 1.0f, 1.0f);
		material.specular = new Vector3d(0, 0, 0);
		sceneObject = new CubeSceneObject(null, new Vector3d(0, 0, z), size, material);
		
		path = new GameObject3dPath();
		rotateAround = ROTATE_AROUND_VECTORS.get((int)(Math.random()*ROTATE_AROUND_VECTORS.size()));
		//rotateAround = ROTATE_AROUND_VECTORS.get(4);
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
			pathStep = element.position.minus(new Vector3d(xOffset, yOffset, z));
			pathStep.scale(1.0f/element.time);
		}
	}
	
	public void tick(int timeElapsed) {
		lifeTime -= timeElapsed;

		camera.setScreenOffsets((int)(position.x+0.5), (int)(position.y+0.5));
		sceneObject.rotate(rotateAround, angle);
		
		for(int i=0; i < sceneObject.mesh.vertices.size(); i ++)
		{
			Vertex3d v = sceneObject.mesh.vertices.get(i);
			v.transformedPosition = sceneObject.transform.transform(v.position); 
			v.transformedPosition = camera.getWorldToCameraTransform().transform(v.transformedPosition);
			
//			Vector4d v2 = camera.getCameraToProjectionTransform().transform(v.transformedPosition);
//			v2.x /= v2.w;
//			v2.y /= v2.w;
//			v2.z /= v2.w;
//			
//			float z = v2.w;
//			v2.w /= v2.w;
//			
//			v2 = camera.getProjectionToScreenTransform().transform(v2);
//			v2.w = z;
//			
//			v.transformedPosition.copy(v2);
		}
		
		Vector3d cameraLookAt = new Vector3d(0, 0, 1);
		for(int i=0; i < sceneObject.mesh.triangles.size(); i ++)
		{
			Triangle triangle = sceneObject.mesh.triangles.get(i);
			triangle.updateNormal();
			
			float dot = triangle.normal.dot(cameraLookAt);
			triangle.culled = dot >= 0;
		}
		
		for(int i=0; i < sceneObject.mesh.vertices.size(); i ++)
		{
			Vertex3d v = sceneObject.mesh.vertices.get(i);
//			v.transformedPosition = sceneObject.transform.transform(v.position); 
//			v.transformedPosition = camera.getWorldToCameraTransform().transform(v.transformedPosition);
			
			if(camera.projectionType == ProjectionType.PERSPECTIVE)
			{
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
			else if(camera.projectionType == ProjectionType.ORTHOGONALITY)
			{
				Vector4d v2 = camera.getCameraToProjectionTransform().transform(v.transformedPosition);
				v2 = camera.getProjectionToScreenTransform().transform(v2);
				v.transformedPosition.copy(v2);				
			}
		}
		
		angle += 43;
		angle %= 360;
		position = position.add(pathStep.scale2(timeElapsed));
		GameObject3dPathElement element = path.elements.get(pathIndex);
		element.time -= timeElapsed;
		if(element.time <= 0)
		{			
			pathIndex++;
			initPathSteps();
		}
	}
	
	int count;
	int ct = 0;
	public void draw(GameCanvas canvas) 
	{
		triangleRendererConstant.resetZBuffer();
		triangleRendererConstant.constantColor = color;
		for (int i = 0; i < sceneObject.mesh.triangles.size(); i++) 
		{
			Triangle triangle = sceneObject.mesh.triangles.get(i);
			if(triangle.culled)
			{
				continue;
			}
			
			Vertex3d v1 = triangle.mesh.vertices.get(triangle.v1);
			Vertex3d v2 = triangle.mesh.vertices.get(triangle.v2);
			Vertex3d v3 = triangle.mesh.vertices.get(triangle.v3);
			
			triangleRendererConstant.fillTriangle(v1, v2, v3);
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
