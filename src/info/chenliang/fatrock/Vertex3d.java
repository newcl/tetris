package info.chenliang.fatrock;

import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;

public class Vertex3d {
	public Vector4d position;
	public Vector3d color;
	
	public Vertex3d()
	{
		this(new Vector4d(), new Vector3d());
	}
	
	public Vertex3d(Vector4d position, Vector3d color)
	{
		this.position = position;
		this.color = color;
	}
}
