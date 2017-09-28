/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.synacor.jetty.log.JettyEvent;
import com.synacor.jetty.log.layout.Layout;

/** An appender instance that simply writes out to a file */
public class FileAppender
	extends Appender
{
	/** The layout impleneted used to format events */
	protected Layout layout;
	/** The filename to write to */
	protected String filename;
	/** The writer used to output formatted events */
	protected PrintWriter writer;

	protected static final Logger LOG = Log.getLogger(FileAppender.class);

	/** An empty constructer intended for subclassing */
	protected FileAppender()
	{
	}

	/**
	  * Creates an appender with a fixed layout and filename
	  *
	  * @param layout The layout implementation to use
	  * @param filename The filename to output to
	  */
	public FileAppender(Layout layout, String filename)
	{
		this.layout = layout;
		this.filename = filename;
	}

	/**
	  * Opens a writer for the currently configured filename
	  */
	protected synchronized void open()
	{
		if (writer != null)
			writer.close();

		try
		{
			writer = new PrintWriter(new FileOutputStream(filename, true), true);
		}
		catch (FileNotFoundException e)
		{
			LOG.warn("Could not open log file: " + filename);
		}
	}

	/**
	  * Writes out the event
	  *
	  * @param event The jetty event
	  */
	public void append(JettyEvent event)
	{
		if (writer == null)
		{
			LOG.warn("Attempt to write to uninitialized FileAppender.");
		}
		else
		{
			writer.println(layout.format(event));
		}
	}

	/** Sets up the appender by opening the configured file */
	public synchronized void doStart()
	{
		open();
	}

	/** Shuts down the appender by closing the writer */
	public void doStop()
	{
		writer.close();
	}
}
