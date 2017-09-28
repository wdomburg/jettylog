/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.appender;

import java.io.PrintStream;

import org.apache.log4j.Logger;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.JettyEvent;
import com.synacor.jetty.log.layout.Layout;

/** Appender that formats an event and writes it to a PrintStream*/
public class PrintStreamAppender extends Appender
{
	/** Holds the underlying PrintStream */
	private PrintStream stream;
	/** Holds a layout class */
	private Layout layout;

	/**
	 * Creates an instance that writes to STDOUT
	 *
	 * @param layout A layout class for formating events
	 */

	public PrintStreamAppender(Layout layout)
	{
		this.layout = layout;
		this.stream = System.out;
	}
	
	/**
	 * Creates an instance
	 *
	 * @param layout A layout class for formating events
	 * @param stream A stream to write events to
	 */
	public PrintStreamAppender(Layout layout, PrintStream stream)
	{   
		this.layout = layout;
		this.stream = stream;
	}

	/**
	 * Sets a new layout class
	 *
	 * @param layout A layout class for formating events
	 * @return PrintStreamAppender The class instance being configured
	 */
	public PrintStreamAppender setLayout(Layout layout)
	{
		this.layout = layout;

		return this;
	}

	/**
	 * Writes out the event
	 *
	 * @param event An event to be logged
	 */
	public void append(JettyEvent event)
	{

		stream.println(layout.format(event));
	}
}
