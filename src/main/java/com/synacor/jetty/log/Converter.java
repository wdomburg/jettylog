package com.synacor.jetty.log;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public abstract class Converter
{   
	protected Converter child = terminator;

	private static Converter terminator = new Converter()
	{
		protected Converter child = null;

		public String format(StringBuilder entry, Object ... args)
		{
			return entry.toString();
		}

	};

	public static ThreadName threadName()
	{
		return new ThreadName();
	}

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

	public static Converter literal(final String literal)
	{
		return new Converter()
		{
			public String format(StringBuilder entry, Object ... args)
			{
				entry.append(literal);
				return child.format(entry, args);
			}
		};
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
