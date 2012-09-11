package info.chenliang.tetris;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class GameObject implements Tickable, Drawable {
	private float x, y;
	
	private Shape shape;
	private float speedX, speedY;
	private float accelerationX, accelerationY;
	
	private float angleSpeed;
	private float angle;
	
	private int lifeTime = 5000;
	private int color;

	public GameObject(float x, float y, int color) {
		super();
		this.x = x;
		this.y = y;
		this.color = color;
		
		accelerationY = 1;
	}



	public void tick(int timeElapsed) {
		float deltaX = speedX*timeElapsed + accelerationX*timeElapsed*timeElapsed/2;
		float deltaY = speedY*timeElapsed + accelerationY*timeElapsed*timeElapsed/2;
		
		x += deltaX;
		y += deltaY;
		
		speedX += accelerationX;
		speedY += accelerationY;
		
		angle += angleSpeed*timeElapsed;
		
		lifeTime -= timeElapsed;
	}

	public boolean isFinished() {
		return lifeTime <= 0;
	}

	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getAngle() {
		return angle;
	}

	public void setAngle(float angle) {
		this.angle = angle;
	}

	public float getSpeedX() {
		return speedX;
	}

	public void setSpeedX(float speedX) {
		this.speedX = speedX;
	}

	public float getSpeedY() {
		return speedY;
	}

	public void setSpeedY(float speedY) {
		this.speedY = speedY;
	}

	public float getAccelerationX() {
		return accelerationX;
	}

	public void setAccelerationX(float accelerationX) {
		this.accelerationX = accelerationX;
	}

	public float getAccelerationY() {
		return accelerationY;
	}

	public void setAccelerationY(float accelerationY) {
		this.accelerationY = accelerationY;
	}

	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}



	public Shape getShape() {
		return shape;
	}



	public void setShape(Shape shape) {
		this.shape = shape;
	}



	public float getAngleSpeed() {
		return angleSpeed;
	}



	public void setAngleSpeed(float angleSpeed) {
		this.angleSpeed = angleSpeed;
	}



	public int getColor() {
		return color;
	}



	public void setColor(int color) {
		this.color = color;
	}



	public void draw(Canvas canvas, Paint paint) {
		// TODO Auto-generated method stub
		if(shape != null)
		{
			paint.setStyle(Style.FILL);
			paint.setColor(color);
			
			canvas.save();
			canvas.translate(x, y);
			canvas.rotate(angle);
			shape.draw(canvas, paint);
			canvas.restore();
		}
	}
	
	
}
