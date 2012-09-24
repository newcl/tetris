package info.chenliang.fatrock;

import info.chenliang.debug.Assert;
import info.chenliang.ds.Matrix4x4;
import info.chenliang.ds.Plane3d;
import info.chenliang.ds.Precision;
import info.chenliang.ds.Vector3d;

public class Camera {
	private Vector3d position;
	private Vector3d lookAt;
	private Vector3d up;
	private float viewAngle;
	private float nearZ, farZ;
	private float aspectRatio;
	private Plane3d leftPlane, rightPlane, topPlane, bottomPlane;
	private Matrix4x4 worldToCameraTransform;
	private Matrix4x4 cameraToProjectionTransform;

	private Matrix4x4 projectionToScreenTransform;
	private int screenWidth, screenHeight;
	private int screenXOffset, screenYOffset;
	
	public Camera(Vector3d position, Vector3d lookAt, Vector3d up,
			float viewAngle, float nearZ, float farZ, 
			int screenWidth, int screenHeight, int screenXOffset, int screenYOffset) {
		super();
		this.position = position;
		this.lookAt = lookAt;
		this.up = up;
		this.viewAngle = viewAngle;
		this.nearZ = nearZ;
		this.farZ = farZ;
		this.screenWidth = screenWidth;
		this.screenHeight = screenHeight;
		this.screenXOffset = screenXOffset;
		this.screenYOffset = screenYOffset;
		this.aspectRatio = (float)(screenWidth*1.0/screenHeight);
		
		worldToCameraTransform = new Matrix4x4();
		cameraToProjectionTransform = new Matrix4x4();
		projectionToScreenTransform = new Matrix4x4();
		
		updateWorldToCameraTransform();
		updateCameraToProjectionTransform();
		updateProjectionToScreenTransform();
		
		Matrix4x4 m = worldToCameraTransform.multiply(cameraToProjectionTransform);
		m = m.multiply(projectionToScreenTransform);
		
	}
	
	private void updateProjectionToScreenTransform()
	{
		//x' = screenWidth/2 * x + screenWidth/2 + screenXOffset;
		//y' = -screenHeight/2 * y + screenHeight/2 + screenYOffset;
		
		float screenWidthHalf = screenWidth*1.0f/2;
		float screenHeightHalf = screenHeight*1.0f/2;
		
		projectionToScreenTransform.set(screenWidthHalf, 0, 0, screenWidthHalf + screenXOffset, 
										0, -screenHeightHalf, 0, screenHeightHalf + screenXOffset, 
										0, 0, 1, 0, 
										0, 0, 0, 1);
	}
	
	private void updateWorldToCameraTransform()
	{
		Vector3d zAxis = new Vector3d(position, lookAt);
		zAxis.normalize();
		
		Vector3d yAxis = up;
		yAxis.normalize();
		if(Precision.getInstance().equals(yAxis.dot(zAxis), 1))
		{
			yAxis.setX(yAxis.getX() + 0.5f);
			yAxis.normalize();
		}
		
		Vector3d xAxis = up.cross(zAxis);
		xAxis.normalize();
		
		float dx = position.dot(xAxis);
		float dy = position.dot(yAxis);
		float dz = position.dot(zAxis);
		
		worldToCameraTransform.set(xAxis.getX(), xAxis.getY(), xAxis.getZ(), -dx,
								   yAxis.getX(), yAxis.getY(), yAxis.getZ(), -dy,
								   zAxis.getX(), zAxis.getY(), zAxis.getZ(), -dz,
								   0		   , 0			 , 0		   ,  1);
	}
	
	private void updateCameraToProjectionTransform()
	{
		float viewAngleInRadian = (float)(Math.toRadians(viewAngle));
		Assert.judge(!Precision.getInstance().equals(viewAngleInRadian, 0.0f), "View angle should be greater than zero!");
		
		float d = (float)(1 / Math.tan(viewAngleInRadian));
		
		Assert.judge(!Precision.getInstance().equals(nearZ, farZ), "near and far z should not be the same!");
		//| d/AR  0 0 0|
		//| 0     d 0 0|
		//| 0     0 A B|
		//| 0     0 1 0|
		float A = (farZ + nearZ) / (farZ - nearZ);
		float B = -2 * farZ * nearZ / (farZ - nearZ);
		cameraToProjectionTransform.set(d/aspectRatio, 0, 0, 0,
										0, d, 0, 0, 
										0, 0, A, B, 
										0, 0, 1, 0);
	}
	
	public void setWorldToCameraTransform(Matrix4x4 worldToCameraTransform) {
		this.worldToCameraTransform = worldToCameraTransform;
	}

	public Matrix4x4 getCameraToProjectionTransform() {
		return cameraToProjectionTransform;
	}

	public void setCameraToProjectionTransform(Matrix4x4 cameraToProjectionTransform) {
		this.cameraToProjectionTransform = cameraToProjectionTransform;
	}
	

	public Matrix4x4 getProjectionToScreenTransform() {
		return projectionToScreenTransform;
	}

	public void setProjectionToScreenTransform(Matrix4x4 projectionToScreenTransform) {
		this.projectionToScreenTransform = projectionToScreenTransform;
	}
	
	public Matrix4x4 getWorldToCameraTransform() {
		return worldToCameraTransform;
	}
}
