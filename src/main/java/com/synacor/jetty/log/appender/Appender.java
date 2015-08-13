package com.synacor.jetty.log.appender;

import com.synacor.jetty.log.JettyEvent;

import java.io.IOException;

/** Base class for appender implementations */
public abstract class Appender
{
	/**
	 * Write out an event
	 *
	 * @param event The log event to process
	 */
	public abstract void append(JettyEvent event);

	/** Override for any necessary initialization tasks */
	public void doStart()
		throws IOException
	{
	}

	/** Override for any necessary shutdown tasks */
	public void doStop()
	{
	}
}
