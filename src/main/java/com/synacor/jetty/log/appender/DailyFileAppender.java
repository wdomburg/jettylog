package com.synacor.jetty.log.appender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

public class DailyFileAppender extends FileAppender
{
	private String prefix;
	private long nextRotate; 

	private SimpleDateFormat dateFormat = new SimpleDateFormat(".yyyy-MM-dd");

	public DailyFileAppender(String prefix)
    {   
    	this.prefix = prefix;
		updateFilename();
		
 	}
	
	private synchronized void updateFilename()
	{
		Calendar cal = Calendar.getInstance();
		Date date = cal.getTime();

		cal.add(Calendar.DATE, 1);
		cal.set(Calendar.HOUR_OF_DAY, 0);
		cal.set(Calendar.MINUTE , 0);
		cal.set(Calendar.SECOND, 0);

		filename = prefix + dateFormat.format(date);
		nextRotate = cal.getTimeInMillis();
	}

	public void write(String entry)
	{
		if (System.currentTimeMillis() >= nextRotate)
		{
			try
			{
				updateFilename();
				open();
			}
			catch (Exception e)
			{
				LOG.warn("Could not rotate accesslog.");
			}
		}
		super.write(entry);
	}

}
