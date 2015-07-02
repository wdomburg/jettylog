package com.synacor.jetty.log.layout;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

import com.synacor.jetty.log.Event;

public abstract class Layout
{
	abstract public String format(Event event);
}
