package com.synacor.jetty.log.layout;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

import com.synacor.jetty.log.Event;

/** Base class for layout implementations */
public abstract class Layout
{
	/**
	 * Formats an event for writing
	 *
	 * @param event A log event
	 * @return String A formated log entry
	 */
	abstract public String format(Event event);
}
