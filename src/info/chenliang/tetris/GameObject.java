package info.chenliang.tetris;

import org.jbox2d.collision.shapes.PolygonShape;
import org.jbox2d.common.Vec2;
import org.jbox2d.dynamics.Body;
import org.jbox2d.dynamics.Fixture;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;

public class GameObject implements Tickable, Drawable {
	/*
	private float x, y;
	
	private Shape shape;
	private float speedX, speedY;
	private float accelerationX, accelerationY;
	
	private float angleSpeed;
	private float angle;
	*/
	private static int _id;
	
	private int id;
	
	private int lifeTime = 5000;
	private int color;

	private Body body;
	
	public GameObject(int color) {
		super();
		this.color = color;
		/*
		this.x = x;
		this.y = y;
		this.color = color;
		
		accelerationY = 1;
		*/
		id = _id ++;
	}



	public void tick(int timeElapsed) {
		/*
		float deltaX = speedX*timeElapsed + accelerationX*timeElapsed*timeElapsed/2;
		float deltaY = speedY*timeElapsed + accelerationY*timeElapsed*timeElapsed/2;
		
		deltaX = 1;
		deltaY = 1;
		
		x += deltaX;
		y += deltaY;
		
		speedX += accelerationX;
		speedY += accelerationY;
		
		angle += angleSpeed*timeElapsed;
		*/
		
		lifeTime -= timeElapsed;
	}

	public boolean isFinished() {
		return lifeTime <= 0;
	}

	/*
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
	 */
	public int getLifeTime() {
		return lifeTime;
	}

	public void setLifeTime(int lifeTime) {
		this.lifeTime = lifeTime;
	}

/*

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

*/

	public int getColor() {
		return color;
	}



	public void setColor(int color) {
		this.color = color;
	}

	private float scaleFactor = 1.0f;
	
	private float unitScale(float f)
	{
		return f*scaleFactor;
	}
	
	private void vectorScale(Vec2 v)
	{
		v.x *= scaleFactor;
		v.y *= scaleFactor;
	}
	
	public void draw(Canvas canvas, Paint paint) {
		if(body != null)
		{
			paint.setStyle(Style.FILL);
			paint.setColor(color);
			paint.setAntiAlias(true);
			Vec2 position = body.getPosition();
			vectorScale(position);
			
			float angleInRandian = body.getAngle();
			float angleInDegrees = (float)Math.toDegrees(angleInRandian);
			
			Fixture fixture = body.getFixtureList();
			PolygonShape shape = (PolygonShape)fixture.getShape();

			canvas.save();
			System.out.println("id=" + id + " Angle " + angleInDegrees + " x=" + position.x + " y=" + position.y);
			canvas.translate(position.x, position.y);
			canvas.rotate(angleInDegrees);
			canvas.drawRect(-13/2 , -13/2, 13/2, 13/2, paint);
			canvas.restore();
			/*
			canvas.save();
			canvas.translate(position.x, position.y);
			canvas.rotate(angleInDegrees);
			
			RectF rect = new RectF();
			rect.left = position.;
			
			canvas.drawRect(rect, paint);
			*/
			//shape.draw(canvas, paint);
			//canvas.restore();
		}
	}



	public Body getBody() {
		return body;
	}



	public void setBody(Body body) {
		this.body = body;
	}
	
	
}
