package com.synacor.jetty.log.appender;

import com.synacor.jetty.log.Event;

import java.io.FileNotFoundException;

public abstract class Appender
{
	public abstract void append(Event event);
	public abstract void doStart() throws FileNotFoundException;
	public abstract void doStop();
}
