package info.chenliang.fatrock;

import info.chenliang.ds.Vector3d;


public class Triangle {
	public int v1, v2, v3;
	public Mesh mesh;
	public Vector3d normal;
	
	public Triangle(Mesh mesh, int v1, int v2, int v3) {
		super();
		this.mesh = mesh;
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}
	
	public void updateNormal()
	{
		Vertex3d vertex1 = mesh.vertices.get(v1);
		Vertex3d vertex2 = mesh.vertices.get(v2);
		Vertex3d vertex3 = mesh.vertices.get(v3);
		
		Vector3d v21 = vertex2.transformedPosition.degenerate().minus(vertex1.transformedPosition.degenerate());
		Vector3d v31 = vertex3.transformedPosition.degenerate().minus(vertex1.transformedPosition.degenerate());
		
		normal = v21.cross(v31);
		normal.normalize();
	}
}
