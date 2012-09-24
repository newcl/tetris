package info.chenliang.fatrock;

import info.chenliang.ds.Bounds;
import info.chenliang.ds.Line2d;
import info.chenliang.ds.Vector3d;
import info.chenliang.tetris.GameCanvas;

import java.util.HashMap;
import java.util.Map;

public class TriangleRenderer {
	public GameCanvas gameCanvas;
	public Map<String, Float> zBuffer;
	
	public TriangleRenderer(GameCanvas gameCanvas)
	{
		this.gameCanvas = gameCanvas;
		zBuffer = new HashMap<String, Float>();
	}
	
	public boolean isPointOnLine(Vector3d p1, Vector3d p2, Vector3d p3)
	{
		Line2d l = new Line2d(p1.x, p1.y, p2.x, p2.y);
		
		return l.line2dPointOnLine(p3.x, p3.y);
	}
	
	public void drawLine(Vector3d p1, Vector3d p2)
	{
		gameCanvas.drawLine(p1.x, p1.y, p2.x, p2.y, 0xffffffff);
	}
	
	private int getColor(Vector3d v)
	{
		return (int)v.x << 16 | (int)v.y << 8 | (int)v.z; 
	}
	
	public void driverSetPixel(float x, float y, Vector3d color, float z)
	{
		String key = x+""+y;
		Float zf = zBuffer.get(key);
		if(zf == null || zf < z)
		{
			gameCanvas.fillRect(x, y, x+1, y+1, getColor(color), 0xff);			
			zBuffer.put(key, z);
		}
	}
	
	private Vector3d getRandomColor()
	{
		return new Vector3d((float)Math.random()*256, (float)Math.random()*256, (float)Math.random()*256);
	}
	
	
	public void fillTriangle(Vertex3d v1, Vertex3d v2, Vertex3d v3)
	{
		float x1 = v1.position.x;
		float y1 = v1.position.y;
		float z1 = v1.position.z;

		float x2 = v2.position.x;
		float y2 = v2.position.y;
		float z2 = v2.position.z;

		float x3 = v3.position.x;
		float y3 = v3.position.y;
		float z3 = v3.position.z;

		Vector3d c1 = getRandomColor();//new Vector3d(0, 0, 0xff);//softwareDriverGetPixel(driver, v1);
		Vector3d c2 = getRandomColor();//new Vector3d(0, 0, 0xff);//softwareDriverGetPixel(driver, v2);
		Vector3d c3 = getRandomColor();//new Vector3d(0, 0, 0xff);//softwareDriverGetPixel(driver, v3);

		if(isPointOnLine(v1.position, v2.position, v3.position))
		{
			drawLine(v1.position, v2.position);
			drawLine(v2.position, v3.position);
			drawLine(v3.position, v1.position);
		}
		else
		{
			//v1->v2 = v2 - v1
			//v1->v3 = v3 - v1
			//f(x,y) = y * (x2 - x1) - x * (y2 - y1) + x1 * y2 - x2 * y1
			
			Bounds bound = new Bounds();
			
			bound.join(x1, y1);
			bound.join(x2, y2);
			bound.join(x3, y3);

			Line2d lv1v2 = new Line2d(x1, y1, x2, y2);
			Line2d lv2v3 = new Line2d(x3, y3, x2, y2);
			Line2d lv1v3 = new Line2d(x3, y3, x1, y1);

			for(int x = (int)bound.left(); x <= bound.right(); x ++)
			{
				for(int y = (int)bound.top(); y <= bound.bottom(); y ++)
				{
					float a = lv1v2.line2dFxy(x, y) / lv1v2.line2dFxy(x3, y3);
					float b = lv2v3.line2dFxy(x, y) / lv2v3.line2dFxy(x1, y1);
					float c = lv1v3.line2dFxy(x, y) / lv1v3.line2dFxy(x2, y2);
					
					if(a > 0 && a < 1 && b > 0 && b < 1 && c > 0 && c < 1)
					{
						float z = a * z3 + b * z1 + c * z2;
						int red = (int)(a * c3.x + b * c1.x + c * c2.x);
						int green = (int)(a * c3.y + b * c1.y + c * c2.y);
						int blue = (int)(a * c3.z + b * c1.z + c * c2.z);

						driverSetPixel(x, y, new Vector3d(red, green, blue), z);	
					}
					
				}
			}
		}
	}
}
