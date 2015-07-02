package com.synacor.jetty.log;

public class Event
{
	public final String threadName;

	public Event()
	{
		this.threadName = Thread.currentThread().getName();
	}
}
