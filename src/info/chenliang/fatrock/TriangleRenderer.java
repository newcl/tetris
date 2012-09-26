package info.chenliang.fatrock;

import info.chenliang.debug.Logger;
import info.chenliang.ds.Bounds;
import info.chenliang.ds.Line2d;
import info.chenliang.ds.Precision;
import info.chenliang.ds.Vector3d;
import info.chenliang.ds.Vector4d;
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
		if(zf == null || zf > z)
		{
			gameCanvas.fillRect(x, y, x+1, y+1, getColor(color), 0xff);			
			zBuffer.put(key, z);
		}
	}
	
	private Vector3d getRandomColor()
	{
		return new Vector3d((float)Math.random()*256, (float)Math.random()*256, (float)Math.random()*256);
	}
	
	public void fillTriangle(Vertex3d v1, Vertex3d v2, Vertex3d v3, Vector3d color)
	{
		Vector4d temp;
		Vector4d p1 = v1.position;
		Vector4d p2 = v2.position;
		Vector4d p3 = v3.position;
		
//		float dx31_ = p3.x - p1.x;
//		float dy31_ = p3.y - p1.y;
//		
//		float dx21_ = p2.x - p1.x;
//		float dy21_ = p2.y - p1.y;
//		
//		if(dx31_*dy21_ - dy31_*dx21_ > 0)
//		{
//			return;
//		}
		
		if(p1.y > p2.y)
		{
			temp = p1;
			p1 = p2;
			p2 = temp;
		}
		
		if(p1.y > p3.y)
		{
			temp = p1;
			p1 = p3;
			p3 = temp;
		}
		
		if(p2.y > p3.y)
		{
			temp = p2;
			p2 = p3;
			p3 = temp;
		}
		
		float dy31 = p3.y - p1.y;
		
		if(Precision.getInstance().equals(dy31, 0.0f))
		{
			return;
		}
		
		float dy21 = p2.y - p1.y;
		
		float dx31 = p3.x - p1.x;
		float dx21 = p2.x - p1.x;
		
		float cross = dx21*dy31 - dy21*dx31;
		if(cross == 0.0f)
		{
			//TODO this is not correct
			drawLine(p3.degenerate(), p1.degenerate());
			return;
		}
		
		//whether the center point p2 is on the right
		//of p1-->p3
		boolean right = cross > 0;		
		int fc = (int)(color.x) << 16 | (int)(color.y) << 8 | (int)(color.z);
		float dxLeft = 0.0f, dxRight = 0.0f, dzLeft=0.0f, dzRight=0.0f;
		float dz31 = p3.w - p1.w;
		float dz21 = p2.w - p1.w;
		if(dy21 > 0.0f)
		{
			int startY = (int)Math.ceil(p1.y);
			int endY = (int)Math.ceil(p2.y) - 1;
			
			int ySpan = endY - startY + 1;
			if(ySpan > 0)
			{
				dxLeft = right ? dx31/dy31 : dx21/dy21;
				dxRight = right ? dx21/dy21 : dx31/dy31;
				
				dzLeft = right ? dz31/dy31 : dx21/dy21;
				dzRight = right ? dz21/dy21 : dz31/dy31;

				float zLeft = p1.w;
				float zRight = p1.w;
				
				float xLeft = p1.x;
				float xRight = p1.x;
				
				for(int y=startY; y <= endY; y++)
				{
					int startX = (int)Math.ceil(xLeft);
					int endX = (int)Math.ceil(xRight) - 1;
					int xSpan = endX - startX + 1;
					
					if(xSpan > 0)
					{
						float dz = (zRight - zLeft) / xSpan;
						float z = zLeft;
						for(int x=startX; x <= endX; x++)
						{
							String key = x+""+y;
							Float zRecord = zBuffer.get(key);
							if(zRecord == null || zRecord > z)
							{
								gameCanvas.fillRect(x, y, x+1, y+1, fc, 0xff);
								zBuffer.put(key, z);
							}
							
							z += dz;
						}
					}
					
					xLeft += dxLeft;
					xRight += dxRight;
					
					zLeft += dzLeft;
					zRight += dzRight;
				}
			}
			
				
		}
		
		float dy32 = p3.y - p2.y;
		if(dy32 > 0.0f)
		{
			if(dy31 == 0.0f)
			{
				throw new RuntimeException("render bottom error");
			}
			
			int startY = (int)Math.ceil(p2.y);
			int endY = (int)Math.ceil(p3.y) - 1;
			int ySpan = endY - startY + 1;
			if(ySpan > 0)
			{
				float dx32 = p3.x - p2.x;
				
				float xLeft = right? p1.x+dy21*dxLeft : p2.x;
				float xRight = right ? p2.x : p1.x+dy21*dxRight;
				
				float zLeft = right ? p1.w+dy21*dzLeft : p2.w;
				float zRight = right ? p2.w: p1.w+dzRight*dy21;
				
				dxLeft = right ? dx31/dy31 : dx32/dy32;
				dxRight = right ? dx32/dy32 : dx31/dy31;
				
				float dz32 = p3.w - p2.w;
				
				dzLeft = right ? dz31/dy31 : dz32/dy32;
				dzRight = right ? dz32/dy32 : dz31/dy31;
				
				for(int y=startY; y <= endY; y++)
				{
					int startX = (int)Math.ceil(xLeft);
					int endX = (int)Math.ceil(xRight) - 1;
					int xSpan = endX - startX + 1;
					
					if(xSpan > 0)
					{
						float dz = (zRight - zLeft)/ xSpan;
						float z = zLeft;
						for(int x=startX; x <= endX; x++)
						{
							String key = x+""+y;
							Float zRecord = zBuffer.get(key);
							if(zRecord == null || zRecord > z)
							{
								gameCanvas.fillRect(x, y, x+1, y+1, fc, 0xff);
								zBuffer.put(key, z);
							}
							
							z += dz;
						}
					}
					
					xLeft += dxLeft;
					xRight += dxRight;
					
					zLeft += dzLeft;
					zRight += dzRight;
				}	
			}
		}
	}
	/*
	public void fillTriangle2(Vertex3d v1, Vertex3d v2, Vertex3d v3, Vector3d color)
	{
		Logger.startLog("fillTriangle");
		
		float x1 = v1.position.x;
		float y1 = v1.position.y;
		float z1 = v1.position.z;

		float x2 = v2.position.x;
		float y2 = v2.position.y;
		float z2 = v2.position.z;

		float x3 = v3.position.x;
		float y3 = v3.position.y;
		float z3 = v3.position.z;

		Vector3d c1 = color;//new Vector3d(0, 0, 0xff);//softwareDriverGetPixel(driver, v1);
		Vector3d c2 = color;//new Vector3d(0, 0, 0xff);//softwareDriverGetPixel(driver, v2);
		Vector3d c3 = color;//new Vector3d(0, 0, 0xff);//softwareDriverGetPixel(driver, v3);
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

						//driverSetPixel(x, y, new Vector3d(red, green, blue), z);
						String key = x+""+y;
						Float zf = zBuffer.get(key);
						if(zf == null || zf > z)
						{
							gameCanvas.fillRect(x, y, x+1, y+1, red<<16|green<<8|blue, 0xff);			
							zBuffer.put(key, z);
						}
					}
					
				}
			}
		}
		
		Logger.endLog();
	}
	*/
}
