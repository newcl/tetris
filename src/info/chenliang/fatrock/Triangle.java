package info.chenliang.fatrock;

import info.chenliang.ds.Vector3d;

public class Triangle {
	public Vertex3d v1, v2, v3;
	public Vector3d color;
	
	public Triangle(Vertex3d v1, Vertex3d v2, Vertex3d v3, Vector3d color) {
		super();
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
		this.color = color;
	}
	
}
