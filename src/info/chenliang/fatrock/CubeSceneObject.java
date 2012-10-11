package info.chenliang.fatrock;

import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;

public class CubeSceneObject extends SceneObject {
	public float size;
	public Material material;
	
	public CubeSceneObject(SceneObject parent, Vector3d position, float size) {
		super(parent, position);
		this.size = size;
		
		translate.m03 = position.x;
		translate.m13 = position.y;
		translate.m23 = position.z;
		
		material = new Material();
		material.ambient = new Vector3d(1.0f, 1.0f, 1.0f);
		material.diffuse = new Vector3d(0.5f, 0.5f, 0.5f);
		material.specular = new Vector3d(1.0f, 1.0f, 1.0f);
		material.emission = new Vector3d(1.0f, 1.0f, 1.0f);
		
		float sizeHalf = size*.5f;
		
		///*
		// 0 ~ 3
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, -sizeHalf, -sizeHalf), new Vector3d(255, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, -sizeHalf, -sizeHalf), new Vector3d(0, 255, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, -sizeHalf, sizeHalf), new Vector3d(0, 0, 255)));
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, -sizeHalf, sizeHalf), new Vector3d(255, 255, 0)));
		
		// 4 ~ 7
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, sizeHalf, -sizeHalf), new Vector3d(255, 0, 255)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, sizeHalf, -sizeHalf), new Vector3d(0, 255, 255)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, sizeHalf, sizeHalf), new Vector3d(125, 125, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, sizeHalf, sizeHalf), new Vector3d(0, 125, 125)));
		//*/
		
		/*
		// 0 ~ 3
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, -sizeHalf, -sizeHalf), new Vector3d(0, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, -sizeHalf, -sizeHalf), new Vector3d(0, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, -sizeHalf, sizeHalf), new Vector3d(0, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, -sizeHalf, sizeHalf), new Vector3d(0, 0, 0)));
				
		// 4 ~ 7
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, sizeHalf, -sizeHalf), new Vector3d(0, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, sizeHalf, -sizeHalf), new Vector3d(0, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(sizeHalf, sizeHalf, sizeHalf), new Vector3d(0, 0, 0)));
		mesh.vertices.add(new Vertex3d(new Vector4d(-sizeHalf, sizeHalf, sizeHalf), new Vector3d(0, 0, 0)));
		*/
		
		mesh.triangles.add(new Triangle(mesh, 0, 1, 3));
		mesh.triangles.add(new Triangle(mesh, 3, 1, 2));
		
		mesh.triangles.add(new Triangle(mesh, 7, 5, 4));
		mesh.triangles.add(new Triangle(mesh, 7, 6, 5));
		
		mesh.triangles.add(new Triangle(mesh, 0, 5, 1));
		mesh.triangles.add(new Triangle(mesh, 0, 4, 5));
		
		mesh.triangles.add(new Triangle(mesh, 2, 1, 5));
		mesh.triangles.add(new Triangle(mesh, 5, 6, 2));
		
		mesh.triangles.add(new Triangle(mesh, 3, 2, 6));
		mesh.triangles.add(new Triangle(mesh, 3, 6, 7));
		
		mesh.triangles.add(new Triangle(mesh, 3, 4, 0));
		mesh.triangles.add(new Triangle(mesh, 4, 3, 7));
		
		for(Vertex3d v : mesh.vertices)
		{
			v.material = material;
		}
	}
	
	
}
