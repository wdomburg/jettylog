package com.synacor.jetty.log4j;

import java.text.ParseException;

import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ErrorHandler;

import org.apache.log4j.helpers.LogLog;

import com.synacor.jetty.log.JettyEvent;
import com.synacor.jetty.log.layout.PatternLayout;

public class JettyLayout extends org.apache.log4j.Layout
{
	private PatternLayout layout;

	public JettyLayout()
	{
		layout = new PatternLayout();
	}

	public JettyLayout(String pattern)
	{
		layout = new PatternLayout(pattern);
	}

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

	public String format(LoggingEvent event)
	{
		JettyEvent jettyEvent = (JettyEvent) event.getMessage();

		return layout.format(jettyEvent) + "\n";
	}

	public void activateOptions()
	{
	}

	public boolean ignoresThrowable()
	{
		return true;
	}
}
