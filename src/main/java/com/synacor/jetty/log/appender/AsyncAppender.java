package com.synacor.jetty.log.appender;

import java.io.FileNotFoundException;
import java.util.concurrent.BlockingQueue;

import org.eclipse.jetty.util.BlockingArrayQueue;
import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;

public class AsyncAppender extends Appender
{
	private Appender appender;
	private BlockingQueue<String> queue;
	private WriterThread thread;

	private static final Logger LOG = Log.getLogger(AsyncAppender.class);

	public AsyncAppender()
	{
		this(getDefaultAppender(), getDefaultQueue());
	}

	public AsyncAppender(Appender appender)
	{
		this(appender, getDefaultQueue());
	}

	public AsyncAppender(Appender appender, BlockingQueue<String> queue)
	{
		this.appender = appender;
		this.queue = queue;
	}

	private static Appender getDefaultAppender()
	{
		return new TestAppender("/var/tmp/jetty.log");
	}

	private static BlockingQueue<String> getDefaultQueue()
	{
		return new BlockingArrayQueue<String>(1024);
	}

	private class WriterThread extends Thread
	{
		WriterThread()
		{
			setName("AsyncAppender");
		}

		public void run()
		{
			while(!isInterrupted())
			{
				try
				{
					appender.write(queue.take());
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

	public synchronized void doStart()
		throws FileNotFoundException
	{
		appender.doStart();
		thread = new WriterThread();
		thread.start();
	}

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
	}

	public void write(String entry)
	{
		if(!queue.offer(entry))
		{
			LOG.warn("Log Queue overflow");
		}
	}
}
