package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;

import org.apache.log4j.Logger;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.layout.Layout;

public class Log4jAppender extends Appender
{
	private Layout layout;
	private Logger logger;

	public Log4jAppender(Layout layout)
		throws FileNotFoundException
	{   
		logger = Logger.getLogger("jetty.request");
		setLayout(layout);
	}

	public Log4jAppender setLayout(Layout layout)
	{
		this.layout = layout;

		return this;
	}

	public void append(Event event)
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
