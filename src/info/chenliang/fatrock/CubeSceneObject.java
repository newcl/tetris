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
		material.diffuse = new Vector3d(1.0f, 1.0f, 1.0f);
		material.specular = new Vector3d(1.0f, 1.0f, 1.0f);
		material.emission = new Vector3d(1.0f, 1.0f, 1.0f);
		
		float sizeHalf = size*.5f;
		
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
		
		mesh.triangles.add(new Triangle(mesh, 0, 1, 2));
		mesh.triangles.add(new Triangle(mesh, 0, 2, 3));
		
		mesh.triangles.add(new Triangle(mesh, 4, 5, 6));
		mesh.triangles.add(new Triangle(mesh, 4, 7, 6));
		
		mesh.triangles.add(new Triangle(mesh, 0, 1, 5));
		mesh.triangles.add(new Triangle(mesh, 0, 4, 5));
		
		mesh.triangles.add(new Triangle(mesh, 1, 2, 6));
		mesh.triangles.add(new Triangle(mesh, 1, 5, 6));
		
		mesh.triangles.add(new Triangle(mesh, 2, 3, 6));
		mesh.triangles.add(new Triangle(mesh, 3, 7, 6));
		
		mesh.triangles.add(new Triangle(mesh, 0, 3, 4));
		mesh.triangles.add(new Triangle(mesh, 3, 4, 7));
		
		for(Vertex3d v : mesh.vertices)
		{
			v.material = material;
		}
	}
	
	
}
