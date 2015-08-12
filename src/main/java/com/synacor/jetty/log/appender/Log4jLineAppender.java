package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.JettyEvent;
import com.synacor.jetty.log.layout.Layout;

/** Appender that formats an event and passes it to log4j */
public class Log4jLineAppender extends Appender
{
	/** Holds the underlying log4j logger */
	private Layout layout;
	/** Holds a layout class */
	private Logger logger;

	/**
	 * Creates an an instance and the underlying log4j logger
	 *
	 * @param layout A layout class for formating events
	 */
	public Log4jLineAppender(Layout layout)
		throws FileNotFoundException
	{   
		logger = Logger.getLogger("jetty.request");
		setLayout(layout);
	}

	/**
	 * Sets a new layout class
	 *
	 * @param layout A layout class for formating events
	 * @return Log4jLineAppender The class instance being configured
	 */
	public Log4jLineAppender setLayout(Layout layout)
	{
		this.layout = layout;

		return this;
	}

	/**
	 * Writes out the event
	 *
	 * Server errors are written as errors
	 * Client errors are written as warnings
	 * All other requests are logged as informational
	 *
	 * @param event An event to be logged
	 */
	public void append(JettyEvent event)
	{
		int status = event.response.getStatus();

		if (status >= 500)
		{   
			logger.error(layout.format(event));
		}
		else if (status >= 400)
		{   
			logger.warn(layout.format(event));
		}
		else
		{   
			logger.info(layout.format(event));
		}
	}
}
