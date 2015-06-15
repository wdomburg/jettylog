package com.synacor.jetty.log;

import java.lang.StringBuilder;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.Format;
import com.synacor.jetty.log.JettyConverter;

public class CustomRequestLog extends AbstractLifeCycle implements RequestLog
{

	private Converter converter;
	{
	}

	@Override
	public void log (Request request, Response response)
	{
		//System.out.println(converter.format(request, response));
		//System.out.println(format.toString());
	}

}
