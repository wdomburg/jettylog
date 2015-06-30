package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.synacor.jetty.log.layout.Layout;
import com.synacor.jetty.log.layout.PatternLayout;

import java.io.FileNotFoundException;

public class Log4jRequestLog extends AbstractLifeCycle implements RequestLog
{
	private Layout layout;
	private Logger logger;

	public Log4jRequestLog()
		throws FileNotFoundException
	{
		this(new PatternLayout());
	}

	public Log4jRequestLog(Layout layout)
		throws FileNotFoundException
	{
		logger = Logger.getLogger("jetty.request");
		setLayout(layout);
	}

	public Log4jRequestLog setLayout(Layout layout)
	{
		this.layout = layout;

		return this;
	}

	@Override
	public void log(Request request, Response response)
	{
		int status = response.getStatus();

		if (status >= 500)
		{
			logger.error(layout.format(request, response));
		}
		else if (status >= 400)
		{
			logger.warn(layout.format(request, response));
		}
		else
		{
			logger.info(layout.format(request, response));
		}
	}

}
