package com.synacor.jetty.log.appender;

import com.synacor.jetty.log.JettyEvent;

import java.io.FileNotFoundException;

public abstract class Appender
{
	public abstract void append(JettyEvent event);
	public abstract void doStart() throws FileNotFoundException;
	public abstract void doStop();
}
