package info.chenliang.tetris;


public class RectangleShape extends Shape{
	private float width, height;
	
	public RectangleShape(float width, float height)
	{
		this.width = width;
		this.height = height;
	}

	@Override
	public void draw(GameCanvas canvas, float x, float y) {
		canvas.fillRect(x-width/2, y-height/2,x+width/2,y+height/2, 0xffff0000);
		
		canvas.drawRect(x-width/2, y-height/2,x+width/2,y+height/2, 0);
	}

}
