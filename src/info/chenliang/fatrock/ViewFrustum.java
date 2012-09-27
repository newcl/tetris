package info.chenliang.fatrock;

import info.chenliang.ds.Matrix4x4;
import info.chenliang.ds.Vector4d;

public class ViewFrustum {
	public Plane3d left;
	public Plane3d right;
	public Plane3d top;
	public Plane3d bottom;
	public Plane3d near;
	public Plane3d far;
	
	public ViewFrustum()
	{
		left = new Plane3d();
		right = new Plane3d();
		top = new Plane3d();
		bottom = new Plane3d();
		near = new Plane3d();
		far = new Plane3d();
	}
	
	public void update(Matrix4x4 m)
	{
		Vector4d v;
		float reciprocal;
		Vector4d row1, row2;
		
		//left plane set up
		row1 = m.getRow(0);
		row1.scale(-1);
		row2 = m.getRow(3);
		row2.scale(-1);
		v = row1.add(row2);
		
		left.n = v.degenerate();
		left.d = v.w;
		reciprocal = 1/left.n.length(); 
		left.n.scale(reciprocal);
		left.d *= reciprocal;
		
		//right plane set up
		row1 = m.getRow(0);
		row2 = m.getRow(3);
		row2.scale(-1);
		v = row1.add(row2);
		
		right.n = v.degenerate();
		right.d = v.w;
		reciprocal = 1/right.n.length(); 
		right.n.scale(reciprocal);
		right.d *= reciprocal;
		
		//top plane set up
		row1 = m.getRow(1);
		row2 = m.getRow(3);
		row2.scale(-1);
		v = row1.add(row2);
		
		top.n = v.degenerate();
		top.d = v.w;
		reciprocal = 1/top.n.length(); 
		top.n.scale(reciprocal);
		top.d *= reciprocal;
		
		//bottom plane set up
		row1 = m.getRow(1);
		row1.scale(-1);
		row2 = m.getRow(3);
		row2.scale(-1);
		v = row1.add(row2);
		
		bottom.n = v.degenerate();
		bottom.d = v.w;
		reciprocal = 1/bottom.n.length(); 
		bottom.n.scale(reciprocal);
		bottom.d *= reciprocal;
		
		//near plane set up
		row1 = m.getRow(2);
		row1.scale(-1);
		v = row1;
		near.n = v.degenerate();
		near.d = v.w;
		reciprocal = 1/near.n.length(); 
		near.n.scale(reciprocal);
		near.d *= reciprocal;
		
		//far plane set up
		row1 = m.getRow(2);
		row2 = m.getRow(3);
		row2.scale(-1);
		v = row1.add(row2);
		
		far.n = v.degenerate();
		far.d = v.w;
		reciprocal = 1/far.n.length(); 
		far.n.scale(reciprocal);
		far.d *= reciprocal;
	}
}
