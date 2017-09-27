/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.appender;

import java.io.IOException;
import java.util.concurrent.BlockingQueue;

import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

import com.synacor.jetty.log.JettyEvent;

/** Appender wrapper that utilizing a blocking queue to allow async operation */
public class AsyncAppender extends Appender
{
	/** The underlying appender being wrapped */
	private Appender appender;
	/** The queue of unwritten events */
	private BlockingQueue<JettyEvent> queue;
	/** The tread responsible for writing to the underlying appender */
	private WriterThread thread;

	/** The Jetty logger for logging initialization and faults */
	private static final Logger LOG = Log.getLogger(AsyncAppender.class);

	/**
	 * Creates an instance with the given appender and the default queue
	 *
	 * @param appender An appender instance.
	 */
	public AsyncAppender(Appender appender)
	{
		this(appender, getDefaultQueue());
	}

	/**
	 * Creates an instance with the given appender and queue
	 *
	 * @param appender An appender instance.
	 * @param queue A queue instance.
	 */
	public AsyncAppender(Appender appender, BlockingQueue<JettyEvent> queue)
	{
		this.appender = appender;
		this.queue = queue;
	}

	/**
	 * Returns a reasonable default queue
	 *
	 * We borrow the implementation used by Jetty in the AsyncNCSARequestLog
	 *
	 * @return BlockingQueue The default queue implementation
	 */
	private static BlockingQueue<JettyEvent> getDefaultQueue()
	{
		return new BlockingArrayQueue<JettyEvent>(1024);
	}

	/** A thread object responsible for writing out events asynchronously */
	private class WriterThread extends Thread
	{
		/** Construct the writer thread */
		WriterThread()
		{
			setName("AsyncAppender");
		}

		/** Run the event loop that writes to the wrapped appender until interuppted */
		public void run()
		{
			while(!isInterrupted())
			{
				try
				{
					appender.append(queue.take());
				}
				catch (InterruptedException e)
				{
					LOG.ignore(e);
				}
				catch (Exception e)
				{
					LOG.warn(e);
				}
			}
		}
	}

	/** Sets up the appender by initializing the wrapped appender and starting the writer */
	public synchronized void doStart()
		throws IOException
	{
		appender.doStart();
		thread = new WriterThread();
		thread.start();
	}

	/** Shuts down the appender by closing the writer thread and stopping the underlying appender */
	public synchronized void doStop()
	{
		try
		{
			thread.interrupt();
			thread.join();
			thread = null;
		}
		catch (InterruptedException e)
		{
			LOG.warn(e);
			thread = null;
		}

		appender.doStop();
	}

	/**
	  * Attempts to queue the event for writing; otherwise logs an overflow
	  *
	  * @param event The jetty event
	  */
	public void append(JettyEvent event)
	{
		if(!queue.offer(event))
		{
			LOG.warn("Log Queue overflow");
		}
	}
}
