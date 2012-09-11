package info.chenliang.tetris;

public class Precision {
	
	public static boolean isCloseEnough(float f1, float f2)
	{
		return Math.abs(f1 - f2) < 0.000001;
	}
}
