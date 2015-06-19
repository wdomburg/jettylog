package com.synacor.jetty.log.appender;

import java.io.FileOutputStream;
import java.io.PrintWriter;

import java.io.FileNotFoundException;

public class TestAppender extends Appender
{

	private String filename;
	private boolean append;
	private int retainDays;

	private PrintWriter writer;

	public TestAppender(String filename)
	{
		this.filename = filename;
	}

	public void write(String entry)
	{
		if (writer == null)
		{
			System.out.println("No writer. :(");
			return;
		}

		System.out.println("Logging: " + entry);
		writer.println(entry);
	}

	public synchronized void doStart()
		throws FileNotFoundException
	{
		if (filename != null)
		{
			FileOutputStream output = new FileOutputStream(filename);
			writer = new PrintWriter(output, true);
		}

	}

	public void doStop()
	{
		writer.close();
	}

}
