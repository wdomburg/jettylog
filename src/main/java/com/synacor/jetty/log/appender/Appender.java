package com.synacor.jetty.log.appender;

import com.synacor.jetty.log.JettyEvent;

import java.io.FileNotFoundException;

/** Base class for appender implementations */
public abstract class Appender
{
	/** Write out an event */
	public abstract void append(JettyEvent event);
	/** Perform any necessary initialization tasks */
	public abstract void doStart() throws FileNotFoundException;
	/** Perform any necessary shutdown tasks */
	public abstract void doStop();
}
