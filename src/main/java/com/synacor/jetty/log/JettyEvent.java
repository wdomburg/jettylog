/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log;

import org.eclipse.jetty.server.Response;
import org.eclipse.jetty.server.Request;

/** An Event implementation that encapsulates a response / request pair */
public class JettyEvent extends Event
{
	/** Holds the Jetty request */
	public final Request request;
	/** Holds the Jetty response */
	public final Response response;

	/**
	 * Constructs an event object
	 */
	public JettyEvent(Request request, Response response)
	{
		super();
		this.request = request;
		this.response = response;
	}
}
