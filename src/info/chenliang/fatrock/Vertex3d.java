package info.chenliang.fatrock;

import info.chenliang.ds.Vector3d;

public class Vertex3d {
	public Vector3d position;
	public Vector3d color;
	
	public Vertex3d()
	{
		this(new Vector3d(), new Vector3d());
	}
	
	public Vertex3d(Vector3d position, Vector3d color)
	{
		this.position = position;
		this.color = color;
	}
}
