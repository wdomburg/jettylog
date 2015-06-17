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

	public static DateNow dateNow(String dateFormatPattern) { return new DateNow(new SimpleDateFormat(dateFormatPattern)); }
	public static DateNow dateNow(SimpleDateFormat dateFormat) { return new DateNow(dateFormat); }
	public static Literal literal(String literal) { return new Literal(literal); }
	public static ThreadName threadName() { return new ThreadName(); }

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

	/*
	public static Converter date(String dateFormatPattern)
	{
		return date(new SimpleDateFormat(dateFormatPattern));
	}

	public static Converter date(final DateFormat dateFormat)
	{
		return new Converter()
		{
			public String format(StringBuilder entry, Object ... args)
			{
				entry.append(dateFormat.format(new Date()));
				return child.format(entry, args);
			}
		};
	}
	*/

	private static class DateNow extends Converter
	{
		private final DateFormat dateFormat;

		private DateNow(DateFormat dateFormat)
		{
			this.dateFormat = dateFormat;
		}

		public String format(StringBuilder entry, Object ... args)
		{
			entry.append(dateFormat.format(new Date()));
			return child.format(entry, args);
		}
	}

	private static class Literal extends Converter
	{
		private final String literal;

		private Literal(String literal)
		{
			this.literal = literal;
		}

		public String format(StringBuilder entry, Object ... args)
		{
			entry.append(literal);
			return child.format(entry, args);
		}
	}

	private static class ThreadName extends Converter
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
