package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.JettyEvent;

/** Appender that passes an unformated log Event to log4j for processing */
public class Log4jAppender extends Appender
{
	/** Holds the underlying log4j logger */
	private Logger logger;

	/** Creates an an instance and the underlying log4j logger */
	public Log4jAppender()
		throws FileNotFoundException
	{   
		logger = Logger.getLogger("jetty.request");
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
			logger.error(event);
		}
		else if (status >= 400)
		{   
			logger.warn(event);
		}
		else
		{   
			logger.info(event);
		}
	}
}
