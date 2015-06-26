package com.synacor.jetty.log.appender;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.io.FileNotFoundException;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class FileAppender extends Appender
{
	protected String filename;
	protected PrintWriter writer;

	protected static final Logger LOG = Log.getLogger(FileAppender.class);

	public FileAppender()
	{
	}

	public FileAppender(String filename)
	{
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

	public void write(String entry)
	{
		if (writer == null)
		{
			LOG.warn("Attempt to write to uninitialized FileAppender.");
		}
		else
		{
			writer.println(entry);
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
