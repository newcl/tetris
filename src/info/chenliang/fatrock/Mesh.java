package info.chenliang.fatrock;

import java.util.ArrayList;
import java.util.List;

public class Mesh {
	public List<Vertex3d> vertices;
	public List<Triangle> triangles;
	
	public Mesh()
	{
		vertices = new ArrayList<Vertex3d>();
		triangles = new ArrayList<Triangle>();
	}
	
	public void updateNormals()
	{
		for(int i=0;i < triangles.size();i ++)
		{
			Triangle triangle = triangles.get(i);
			triangle.updateNormal();
		}
		
		for(int i=0;i < vertices.size();i ++)
		{
			Vertex3d v = vertices.get(i);
			v.normal.set(0, 0, 0);
			
			for(int j=0;j < triangles.size();j ++)
			{
				Triangle triangle = triangles.get(j);
				if(triangle.v1 == i || triangle.v2 == i || triangle.v3 == i)
				{
					v.normal = v.normal.add(triangle.normal);
				}
			}
			
			v.normal.normalize();
		}
	}
}
