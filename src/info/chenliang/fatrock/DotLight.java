package info.chenliang.fatrock;

import info.chenliang.ds.Vector3d;

public class DotLight extends Light {
	Vector3d position;
	float kc, kl, kq;
	
	public DotLight(Vector3d ambient, Vector3d diffuse, Vector3d specular,
			Vector3d position, float kc, float kl, float kq) {
		super(ambient, diffuse, specular);
		this.position = position;
		this.kc = kc;
		this.kl = kl;
		this.kq = kq;
	}
	
	
}
