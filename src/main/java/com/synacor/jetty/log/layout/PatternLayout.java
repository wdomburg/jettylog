package com.synacor.jetty.log.layout;

import com.synacor.jetty.log.Token;
import com.synacor.jetty.log.Format;
import com.synacor.jetty.log.converter.Converter;
import com.synacor.jetty.log.converter.JettyConverter;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

public class PatternLayout extends Layout
{
	public static final String COMMON = "%h %l %u %t \"%r\" %s %b";
	public static final String COMBINED = "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"";
    //FIXME: handle %>s vs %s
	//public static final String COMMON = "%h %l %u %t \"%r\" %>s %b";
	//public static final String COMBINED = "%h %l %u %t \"%r\" %>s %b \"%{Referer}i\" \"%{User-agent}i\"";

	private String pattern;
	private Converter converter;

	public PatternLayout()
	{
		this(getPattern());
	}


	public PatternLayout(String pattern)
	{
		setPattern(pattern);
	}

	// no not like; revisit
	public static String getPattern()
	{
		String pattern = System.getProperty("com.synacor.jetty.log.format");

		return (pattern == null) ? COMMON : pattern;
	}

	public void setPattern(String pattern)
	{
		this.pattern = pattern;

		Format format = new Format(pattern);

		Converter.Builder builder = new Converter.Builder();

		for (Token token: format.getTokens())
		{
			addToken(builder, token);
		}

		converter = builder.build();
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

	public String format(Request request, Response response)
	{
		return converter.format(request, response);
	}
}
