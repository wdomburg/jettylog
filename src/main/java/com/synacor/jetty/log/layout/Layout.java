package com.synacor.jetty.log.layout;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

public abstract class Layout
{
	abstract public String format(Request request, Response response);
}
