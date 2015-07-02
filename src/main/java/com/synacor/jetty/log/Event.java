package com.synacor.jetty.log;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

public class Event
{
	public final String threadName;
	public final Request request;
	public final Response response;

	public Event(Request request, Response response)
	{
		this.threadName = Thread.currentThread().getName();
		this.request = request;
		this.response = response;
	}
}
