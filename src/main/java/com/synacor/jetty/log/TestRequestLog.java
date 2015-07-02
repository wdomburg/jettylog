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
		converter = new JettyConverter.RemoteAddress();

		converter
			.setChild(new JettyConverter.Literal(" - "))
			.setChild(new JettyConverter.Username())
			.setChild(new JettyConverter.Literal(" "))
			.setChild(new JettyConverter.TimeReceived())
			.setChild(new JettyConverter.Literal(" \""))
			.setChild(new JettyConverter.RequestString())
			.setChild(new JettyConverter.Literal("\" "))
			.setChild(new JettyConverter.Status())
			.setChild(new JettyConverter.Literal(" "))
			.setChild(new JettyConverter.BytesWritten())
			.setChild(new JettyConverter.Literal(" \""))
			.setChild(new JettyConverter.Header("Referer"))
			.setChild(new JettyConverter.Literal("\" \""))
			.setChild(new JettyConverter.Header("User-Agent"))
			.setChild(new JettyConverter.Literal("\" \""))
			.setChild(new JettyConverter.Header("Cookie"))
			.setChild(new JettyConverter.Literal("\" "))
			.setChild(new JettyConverter.Header("Syn-Txid"))
			.setChild(new JettyConverter.Literal(" "))
			.setChild(new JettyConverter.Literal("-")) //FIXME: should be "canonical server name"
			.setChild(new JettyConverter.Literal(" "))
			.setChild(new JettyConverter.Latency())
			.setChild(new JettyConverter.Literal(" \""))
			.setChild(new JettyConverter.Header("HOST"))
			.setChild(new JettyConverter.Literal("\" "))
			.setChild(new JettyConverter.ThreadName())
			.setChild(new JettyConverter.Literal(" -"));

	}

	@Override
	public void log(Request request, Response response)
	{
		System.out.println(converter.format(request, response));
	}
}
