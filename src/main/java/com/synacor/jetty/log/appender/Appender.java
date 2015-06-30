package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;

public abstract class Appender
{
	public abstract void write(String entry);
	public abstract void doStart() throws FileNotFoundException;
	public abstract void doStop();
}
