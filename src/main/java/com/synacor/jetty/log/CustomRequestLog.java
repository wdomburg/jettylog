package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.appender.Appender;
import com.synacor.jetty.log.layout.Layout;
import com.synacor.jetty.log.layout.PatternLayout;

import java.io.FileNotFoundException;

public class CustomRequestLog extends AbstractLifeCycle implements RequestLog
{
	private	Appender appender;

	public CustomRequestLog(Appender appender)
		throws FileNotFoundException
	{
		setAppender(appender);
	}

	public CustomRequestLog setAppender(Appender appender)
		throws FileNotFoundException
	{
		this.appender = appender;
		appender.doStart();

		return this;
	}

	@Override
	protected void doStop()
		throws Exception
	{
		if (appender != null)
			appender.doStop();
	}

	@Override
	public void log(Request request, Response response)
	{
		appender.append(new Event(request, response));
	}
}
