package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintWriter;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.synacor.jetty.log.Event;
import com.synacor.jetty.log.layout.Layout;

public class FileAppender extends Appender
{
	protected Layout layout;
	protected String filename;
	protected PrintWriter writer;

	protected static final Logger LOG = Log.getLogger(FileAppender.class);

	// Only here to allow subclassing; e.g. DailyFileAppender
	protected FileAppender()
	{
	}

	public FileAppender(Layout layout, String filename)
	{
		this.layout = layout;
		this.filename = filename;
	}

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

	public void append(Event event)
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

	public synchronized void doStart()
	{
		open();
	}

	public void doStop()
	{
		writer.close();
	}
}
