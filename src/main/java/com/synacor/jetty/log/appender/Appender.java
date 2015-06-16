package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;


public abstract class Appender
{
	public abstract void write(String line);
	public abstract void doStart() throws FileNotFoundException;
	public abstract void doStop();
}
