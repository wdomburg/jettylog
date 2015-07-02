package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.JettyEvent;

public class Log4jAppender extends Appender
{
	private Logger logger;

	public Log4jAppender()
		throws FileNotFoundException
	{   
		logger = Logger.getLogger("jetty.request");
	}

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

	public void doStart()
	{
	}

	public void doStop()
	{
	}
}
