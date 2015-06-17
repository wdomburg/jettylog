package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.Format;
import com.synacor.jetty.log.appender.Appender;
import com.synacor.jetty.log.appender.TestAppender;
import com.synacor.jetty.log.converter.Converter;
import com.synacor.jetty.log.converter.JettyConverter;

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
				builder.add(new JettyConverter.BytesWritten());
				break;
			case 'D':
				builder.add(new JettyConverter.Latency());
				break;
			case 'h':
				builder.add(new JettyConverter.RemoteAddress());
				break;
			case 'i':
				builder.add(new JettyConverter.Header(token.getArgument()));
				break;
			case 'l':
				builder.add(new JettyConverter.Literal("-"));
				break;
			case 'm':
				builder.add(new JettyConverter.Method());
			case 'n':
				// "The contents of note Foobar from another module." // Maybe reuse as debug context?
				builder.add(new JettyConverter.Literal("-"));
				break;
			case 'r':
				builder.add(new JettyConverter.RequestString());
				break;
			case 'P':
				// "The process ID of the child that serviced the request." // Doing TID instead.
				builder.add(new JettyConverter.ThreadName());
				break;
			case 's':
				builder.add(new JettyConverter.Status());
				break;
			case 't':
				builder.add(new JettyConverter.TimeReceived());
				break;
			case 'T':
				builder.add(new JettyConverter.LatencySeconds());
				break;
			case 'u':
				builder.add(new JettyConverter.Username());
				break;
			case 'U':
				builder.add(new JettyConverter.RequestUrl());
				break;
			case 'v':
				// Should be canonical server name
				builder.add(new JettyConverter.Literal("-"));
				break;
			case 0:
				builder.add(new JettyConverter.Literal(token.getArgument()));
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
