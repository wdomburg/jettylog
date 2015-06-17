package com.synacor.jetty.log;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.RequestLog;

//import org.eclipse.jetty.util.annotation.ManagedAttribute;
//import org.eclipse.jetty.util.annotation.ManagedObject;
import org.eclipse.jetty.util.component.AbstractLifeCycle;;

import com.synacor.jetty.log.converter.Converter;
import com.synacor.jetty.log.converter.JettyConverter;

//@ManagedObject("Test request log")
public class TestRequestLog extends AbstractLifeCycle implements RequestLog
{
	private Converter converter;

	public TestRequestLog()
	{

		converter = JettyConverter.remoteAddress();

		converter
			.setChild(JettyConverter.literal(" - "))
			.setChild(JettyConverter.username())
			.setChild(JettyConverter.literal(" "))
			.setChild(JettyConverter.timeReceived())
			.setChild(JettyConverter.literal(" \""))
			.setChild(JettyConverter.requestString())
			.setChild(JettyConverter.literal("\" "))
			.setChild(JettyConverter.status())
			.setChild(JettyConverter.literal(" "))
			.setChild(JettyConverter.bytesWritten())
			.setChild(JettyConverter.literal(" \""))
			.setChild(JettyConverter.header("Referer"))
			.setChild(JettyConverter.literal("\" \""))
			.setChild(JettyConverter.header("User-Agent"))
			.setChild(JettyConverter.literal("\" \""))
			.setChild(JettyConverter.header("Cookie"))
			.setChild(JettyConverter.literal("\" "))
			.setChild(JettyConverter.header("Syn-Txid"))
			.setChild(JettyConverter.literal(" "))
			.setChild(JettyConverter.literal("-")) //FIXME: should be "canonical server name"
			.setChild(JettyConverter.literal(" "))
			.setChild(JettyConverter.latency())
			.setChild(JettyConverter.literal(" \""))
			.setChild(JettyConverter.header("HOST"))
			.setChild(JettyConverter.literal("\" "))
			.setChild(JettyConverter.threadName())
			.setChild(JettyConverter.literal(" -"));

	}

	@Override
	public void log(Request request, Response response)
	{
		System.out.println(converter.format(request, response));
	}
}
