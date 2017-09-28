/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.converter.Converter;
import com.synacor.jetty.log.converter.ConverterBuilder;
import com.synacor.jetty.log.converter.JettyConverter;

/** Example of building a converter programatically */
public class TestRequestLog
	extends AbstractLifeCycle
	implements RequestLog
{
	/** Holds the final converter */
	private Converter converter;

	/** Constructs a request log using Apache Common Log format */
	public TestRequestLog()
	{
		ConverterBuilder builder = new ConverterBuilder();

		builder	
			.add(new JettyConverter.RemoteAddress())
			.add(new Converter.Literal(" - "))
			.add(new JettyConverter.Username())
			.add(new Converter.Literal(" "))
			.add(new JettyConverter.TimeReceived())
			.add(new Converter.Literal(" \""))
			.add(new JettyConverter.RequestString())
			.add(new Converter.Literal("\" "))
			.add(new JettyConverter.Status())
			.add(new Converter.Literal(" "))
			.add(new JettyConverter.BytesWritten())
			.add(new Converter.Literal(" \""))
			.add(new JettyConverter.Header("Referer"))
			.add(new Converter.Literal("\" \""))
			.add(new JettyConverter.Header("User-Agent"))
			.add(new Converter.Literal("\" \""))
			.add(new JettyConverter.Header("Cookie"))
			.add(new Converter.Literal("\" "))
			.add(new JettyConverter.Header("X-Transaction-ID"))
			.add(new Converter.Literal(" "))
			.add(new Converter.Literal("-")) //FIXME: should be "canonical server name"
			.add(new Converter.Literal(" "))
			.add(new JettyConverter.Latency())
			.add(new Converter.Literal(" \""))
			.add(new JettyConverter.Header("HOST"))
			.add(new Converter.Literal("\" "))
			.add(new JettyConverter.ThreadName())
			.add(new Converter.Literal(" -"));

		converter = builder.build();
	}

	/** Log the formated log event to stdout */
	@Override
	public void log(Request request, Response response)
	{
		System.out.println(converter.format(new JettyEvent(request, response)));
	}
}
