package com.synacor.jetty.log.converter;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Converter
{   
	// Set default child of new converters to terminal singleton
	protected Converter child = terminator;

	// Singleton instance that terminates a converter chain
	private static final Converter terminator = new Converter()
	{
		private final Converter child = null;

		public String format(StringBuilder entry, Object ... args)
		{
			return entry.toString();
		}

	};

	public static class Builder
	{
		private Converter head = null;
		private Converter tail = null;

		public Builder add(Converter converter)
		{
			if (head == null)
			{
				head = tail = converter;
			}
			else
			{
				tail = tail.setChild(converter);
			}

			return this;
		}

		public Converter build()
		{
			return head;
		}
	}

	public static class DateNow extends Converter
	{
		private final DateFormat dateFormat;

		public DateNow(DateFormat dateFormat)
		{
			this.dateFormat = dateFormat;
		}

		public String format(StringBuilder entry, Object ... args)
		{
			entry.append(dateFormat.format(new Date()));
			return child.format(entry, args);
		}
	}

	public static class Literal extends Converter
	{
		private final String literal;

		public Literal(String literal)
		{
			this.literal = literal;
		}

		public String format(StringBuilder entry, Object ... args)
		{
			entry.append(literal);
			return child.format(entry, args);
		}
	}

	public static class ThreadName extends Converter
	{
		public String format(StringBuilder entry, Object ... args)
		{   
			entry.append(Thread.currentThread().getName());
			return child.format(entry, args);
		}
	}

	public String format(Object ... args)
	{
		StringBuilder sb = new StringBuilder();

		return this.format(sb, args);
	}

	public String format(StringBuilder entry, Object ... args)
	{
		return child.format(entry, args);
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
