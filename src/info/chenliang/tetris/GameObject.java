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
	
	private int lifeTime = 500;
	private int color;
	
	public GameObject(float x, float y, int color) {
		super();
		
		this.x = x;
		this.y = y;
		this.color = color;
	}

	public void tick(int timeElapsed) {
		float deltaX = speedX + accelerationX/2;
		float deltaY = speedY + accelerationY/2;
		
		x += deltaX;
		y += deltaY;
		
		speedX += accelerationX;
		speedY += accelerationY;
		
		angle += angleSpeed;
		
		lifeTime -= timeElapsed;
		
		if(isFinished())
		{
			cleanUp();
		}
	}
	
	private void cleanUp()
	{
		
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

	public void setSpeed(float speedX, float speedY)
	{
		setSpeedX(speedX);
		setSpeedY(speedY);
	}
	
	public void setAcceleration(float accelerationX, float accelerationY)
	{
		setAccelerationX(accelerationX);
		setAccelerationY(accelerationY);
	}
	
	public void setSpeedParams(float speedX, float speedY, float accelerationX, float accelerationY, float angleSpeed)
	{
		setSpeed(speedX, speedY);
		setAcceleration(accelerationX, accelerationY);
		setAngleSpeed(angleSpeed);
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
		paint.setStyle(Style.FILL);
		paint.setColor(color);
		
		canvas.save();
		canvas.translate(x, y);
		canvas.rotate(angle);
		shape.draw(canvas, paint, 0, 0);
		canvas.restore();
	}
	
}
