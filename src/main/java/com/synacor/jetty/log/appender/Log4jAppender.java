package com.synacor.jetty.log.appender;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;

import org.apache.log4j.Logger;

public class Log4jAppender extends Appender
{
    private Logger logger;

	public Log4jAppender()
	{
		this("jetty.request");
	}

	public Log4jAppender(String name)
	{
		setLogger(name);
	}

	public void setLogger(String name)
	{
		logger = Logger.getLogger(name);
	}

	public void doStart()
	{

	}

	public void doStop()
	{

	}

	public void write(String entry)
	{   
		
	}

}
