package com.synacor.jetty.log.layout;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.converter.Converter;
import com.synacor.jetty.log.converter.ConverterBuilder;
import com.synacor.jetty.log.converter.JettyConverter;
import com.synacor.jetty.log.format.Format;
import com.synacor.jetty.log.format.Token;

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

	// do not like; revisit
	public static String getPattern()
	{
		String pattern = System.getProperty("com.synacor.jetty.log.format");

		return (pattern == null) ? COMMON : pattern;
	}

	public void setPattern(String pattern)
	{
		this.pattern = pattern;

		Format format = new Format(pattern);

		ConverterBuilder builder = new ConverterBuilder();

		for (Token token: format.getTokens())
		{
			addToken(builder, token);
		}

		converter = builder.build();
	}

	private void addToken(ConverterBuilder builder, Token token)
	{   
		char directive = token.getDirective();

		switch(directive)
		{   
			case 'b':
				builder.add(new JettyConverter.BytesWritten(false));
				break;
			case 'B':
				builder.add(new JettyConverter.BytesWritten(true));
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
				// "The contents of note Foobar from another module." // Doing request attribute instead.
				builder.add(new JettyConverter.Attribute(token.getArgument()));
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

	public String format(Event event)
	{
		return converter.format(event);
	}
}
