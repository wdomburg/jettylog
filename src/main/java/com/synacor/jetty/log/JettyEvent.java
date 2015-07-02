package com.synacor.jetty.log;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

public class JettyEvent extends Event
{
	public final Request request;
	public final Response response;

	public JettyEvent(Request request, Response response)
	{
		super();
		this.request = request;
		this.response = response;
	}
}
