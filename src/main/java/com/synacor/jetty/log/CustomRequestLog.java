package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.appender.Appender;
import com.synacor.jetty.log.appender.TestAppender;
import com.synacor.jetty.log.converter.Converter;
import com.synacor.jetty.log.converter.JettyConverter;
import com.synacor.jetty.log.layout.Layout;
import com.synacor.jetty.log.layout.PatternLayout;

import java.io.FileNotFoundException;

public class CustomRequestLog extends AbstractLifeCycle implements RequestLog
{
	private Layout layout;
	private	Appender appender;

	public CustomRequestLog()
		throws FileNotFoundException
	{
		this(new PatternLayout(), new TestAppender("/var/tmp/test.log"));
	}

	public CustomRequestLog(Layout layout, Appender appender)
		throws FileNotFoundException
	{
		setLayout(layout);
		setAppender(appender);
	}

	public CustomRequestLog setLayout(Layout layout)
	{
		this.layout = layout;

		return this;
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
	public void log (Request request, Response response)
	{
		appender.write(layout.format(request, response));
	}

}
