/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.appender;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;

import com.synacor.jetty.log.JettyEvent;
import com.synacor.jetty.log.layout.Layout;

/** FileAppender class that supports daily rotation */
public class DailyFileAppender extends FileAppender
{
	/** The path prefix used for filename generation */
	private String prefix;
	/** The time in millisections of the next rotation */
	private long nextRotate; 

	/** The extension format used for filename generation */
	private SimpleDateFormat dateFormat = new SimpleDateFormat(".yyyy-MM-dd");

	/**
	  * Create an instance with a specified layout and target
	  *
	  * @param layout The layout implementation to use
	  * @param prefix The file prefix to use; will append the data in ISO-8601 format
	  */
	public DailyFileAppender(Layout layout, String prefix)
    {   
		this.layout = layout;
    	this.prefix = prefix;
		updateFilename();
 	}

	/** Generates a new filename according to the current date and sets the next rotate time */
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

	/**
	  * Checks if filename needs to be rotate then writes out the event
	  *
	  * @param event The jetty event
	  */
	public void append(JettyEvent event)
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
		super.append(event);
	}
}
