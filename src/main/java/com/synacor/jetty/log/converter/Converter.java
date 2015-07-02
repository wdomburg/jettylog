package com.synacor.jetty.log.converter;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.synacor.jetty.log.Event;

public abstract class Converter
{   
	// Set default child of new converters to terminal singleton
	protected Converter child = terminator;

	// Singleton instance that terminates a converter chain
	private static final Converter terminator = new Converter()
	{
		private final Converter child = null;

		public String format(StringBuilder entry, Event event)
		{
			return entry.toString();
		}

	};

	public static class DateNow extends Converter
	{
		private final DateFormat dateFormat;

		public DateNow(DateFormat dateFormat)
		{
			this.dateFormat = dateFormat;
		}

		public String format(StringBuilder entry, Event event)
		{
			entry.append(dateFormat.format(new Date()));
			return child.format(entry, event);
		}
	}

	public static class Literal extends Converter
	{
		private final String literal;

		public Literal(String literal)
		{
			this.literal = literal;
		}

		public String format(StringBuilder entry, Event event)
		{
			entry.append(literal);
			return child.format(entry, event);
		}
	}

	public static class ThreadName extends Converter
	{
		public String format(StringBuilder entry, Event event)
		{   
			entry.append(event.threadName);
			return child.format(entry, event);
		}
	}

	public String format(Event event)
	{
		StringBuilder sb = new StringBuilder();

		return this.format(sb, event);
	}

	public String format(StringBuilder entry, Event event)
	{
		return child.format(entry, event);
	}

	public Converter setChild(Converter child)
	{
		return this.child = child;
	}

	public Converter addChild(Converter child)
	{   
		lastChild().child = child;
		return this;
	}

	public Converter getChild()
	{
		return this.child;
	}

	public Converter lastChild()
	{
		return (child == terminator) ? this : child.lastChild();
	}
		
}
