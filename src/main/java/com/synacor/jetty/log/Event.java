package com.synacor.jetty.log;

/**
 * A base event class; mostly intended to be subclassed
 */
public class Event
{
	/**
	 * Stores the thread name so it isn't lost with async loggers
     */
	public final String threadName;

	/** Constructs a generic event object */
	public Event()
	{
		this.threadName = Thread.currentThread().getName();
	}
}
