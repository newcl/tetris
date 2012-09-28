package info.chenliang.tetris;

import info.chenliang.ds.Vector3d;

import java.util.ArrayList;
import java.util.List;

public class GameObject3dPath {
	public List<GameObject3dPathElement> elements;
	
	
	public GameObject3dPath()
	{
		elements = new ArrayList<GameObject3dPathElement>();
	}
	
	public void addPosition(Vector3d position)
	{
		elements.add(new GameObject3dPathElement(position));
	}
}
