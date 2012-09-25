package info.chenliang.debug;

import java.util.ArrayList;
import java.util.List;

public class Logger {
	private static class LoggerItem
	{
		long startTime;
		String stage;
	}
	
	private static List<LoggerItem> loggerItems = new ArrayList<LoggerItem>();
	public static void startLog(String stage)
	{
		long startTime = System.currentTimeMillis();
		
		LoggerItem loggerItem = new LoggerItem();
		loggerItem.stage = stage;
		loggerItem.startTime = startTime;
		
		loggerItems.add(loggerItem);
	}
	
	public static void endLog()
	{
		long endTime = System.currentTimeMillis();
		LoggerItem loggerItem = loggerItems.remove(loggerItems.size()-1);
		System.out.println("["+loggerItem.stage+"] takes ["+(endTime-loggerItem.startTime)+"] ms.");
	}
}
