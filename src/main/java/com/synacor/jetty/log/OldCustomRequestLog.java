package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.Format;
import com.synacor.jetty.log.Converter;
import com.synacor.jetty.log.JettyConverter;

import com.synacor.jetty.log.appender.Appender;
import com.synacor.jetty.log.appender.TestAppender;

public class OldCustomRequestLog extends AbstractLifeCycle implements RequestLog
{

	//FIXME: handle %>s
	//public static final COMMON = "%h %l %u %t \"%r\" %>s %b";
	public static final String COMMON = "%h %l %u %t \"%r\" %s %b";

	private Converter converter;
	private	Appender appender;

	public static String getPattern()
	{
		String pattern = System.getProperty("com.synacor.jetty.log.format");

		if (pattern == null)
			pattern = COMMON;

		return pattern;
	}

	public OldCustomRequestLog()
	{
		this(getPattern());
	}

	public OldCustomRequestLog(String pattern)
	{

		Format format = new Format(pattern);

		Converter.Builder builder = new Converter.Builder();

		for (Token token: format.getTokens())
		{
			addToken(builder, token);
		}

		converter = builder.build();

		appender = new TestAppender("/var/tmp/testing.out");
	}

	private void addToken(Converter.Builder builder, Token token)
	{
		char directive = token.getDirective();

		switch(directive)
		{
			case 'b':
				builder.add(JettyConverter.bytesWritten());
				break;
			case 'D':
				builder.add(JettyConverter.latency());
				break;
			case 'h':
				builder.add(JettyConverter.remoteAddress());
				break;
			case 'i':
				builder.add(JettyConverter.header(token.getArgument()));
				break;
			case 'l':
				builder.add(JettyConverter.literal("-"));
				break;
			case 'm':
				builder.add(JettyConverter.method());
			case 'n':
				// "The contents of note Foobar from another module." // Maybe reuse as debug context?
				builder.add(JettyConverter.literal("-"));
				break;
			case 'r':
				builder.add(JettyConverter.requestString());
				break;
			case 'P':
				// "The process ID of the child that serviced the request." // Doing TID instead.
				builder.add(JettyConverter.threadName());
				break;
			case 's':
				builder.add(JettyConverter.status());
				break;
			case 't':
				builder.add(JettyConverter.timeReceived());
				break;
			case 'T':
				builder.add(JettyConverter.latencySeconds());
				break;
			case 'u':
				builder.add(JettyConverter.username());
				break;
			case 'U':
				builder.add(JettyConverter.requestUrl());
				break;
			case 'v':
				// Should be canonical server name
				builder.add(JettyConverter.literal("-"));
				break;
			case 0:
				builder.add(JettyConverter.literal(token.getArgument()));
				break;
			default:
				break;
		}
	}

	@Override
	protected synchronized void doStart()
		throws Exception
	{
		appender.doStart();
		super.doStart();
	}

	@Override
	protected void doStop()
		throws Exception
	{
		appender.doStop();
	}

	@Override
	public void log (Request request, Response response)
	{
		appender.write(converter.format(request, response));
		//System.out.println(converter.format(request, response));
		//System.out.println(converter.format(request, response));
		//System.out.println(format.toString());
	}

}
