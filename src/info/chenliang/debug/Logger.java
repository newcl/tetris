package info.chenliang.debug;

public class Logger {
	private static long startTime;
	private static String stage;
	private static boolean logging;
	public static void startLog(String stage)
	{
		startTime = System.currentTimeMillis();
		Logger.stage = stage;
		Assert.judge(!logging, "Call end log before calling start log again!");
		logging = true;
	}
	
	public static void endLog()
	{
		long endTime = System.currentTimeMillis();
		System.out.println("["+stage+"] takes ["+(endTime-startTime)+"] ms.");
		logging = false;
	}
}
