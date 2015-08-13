package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.appender.Appender;
import com.synacor.jetty.log.layout.Layout;
import com.synacor.jetty.log.layout.PatternLayout;

import java.io.IOException;

/** A Jetty RequestLog implementation that supports pluggable appender implementations. */
public class CustomRequestLog extends AbstractLifeCycle implements RequestLog
{
	private	Appender appender;

	/**
	 * Creates an instance with the given appender.
	 *
	 * @param appender An appender instance.
	 */
	public CustomRequestLog(Appender appender)
		throws IOException
	{
		setAppender(appender);
	}

	/**
	 * Sets an appender for the instance
	 *
	 * @param appender An appender instance.
	 * @return CustomerRequestLog The object being configured.
	 * @throws IOException when an appender cannot open configured output.
	 */
	public CustomRequestLog setAppender(Appender appender)
		throws IOException
	{
		this.appender = appender;
		appender.doStart();

		return this;
	}

	/**
	 *  Shuts down the appender.
	 */
	@Override
	protected void doStop()
		throws Exception
	{
		if (appender != null)
			appender.doStop();
	}

	/**
	 *  Logs an event.
	 *
	 * @param request The servlet request.
	 * @param response The servlet response.
	 */
	@Override
	public void log(Request request, Response response)
	{
		appender.append(new JettyEvent(request, response));
	}
}
