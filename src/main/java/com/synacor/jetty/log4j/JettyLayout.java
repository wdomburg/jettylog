/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log4j;

import java.text.ParseException;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorHandler;

import org.apache.log4j.helpers.LogLog;

import com.synacor.jetty.log.JettyEvent;
import com.synacor.jetty.log.layout.PatternLayout;

/* A log4j layout implementation that calls our PatternLayout class to format logging events */
public class JettyLayout
	extends org.apache.log4j.Layout
{
	/* The jetty layout object */
	private PatternLayout layout;

	/* Creates a noop Layout object */
	public JettyLayout()
	{
		layout = new PatternLayout();
	}

	/* Builds a new Layout object from a pattern string */
	public JettyLayout(String pattern)
	{
		layout = new PatternLayout(pattern);
	}

	/* Set or reset the underlying PatternLayout with a new pattern string */
	public void setPattern(String pattern)
	{
		try
		{
			layout.setPattern(pattern);
		}
		catch (ParseException e)
		{
			LogLog.error("Could not parse pattern: " + e.getMessage());
		}
	}

	/* Format a logging event according to the specified layout */
	public String format(LoggingEvent event)
	{
		JettyEvent jettyEvent = (JettyEvent) event.getMessage();

		return layout.format(jettyEvent) + "\n";
	}

	/* N/A - This may be removeable if there is a stub implementation in the parent class */
	public void activateOptions()
	{
	}

	/* We don't handle throwables here */
	public boolean ignoresThrowable()
	{
		return true;
	}
}
