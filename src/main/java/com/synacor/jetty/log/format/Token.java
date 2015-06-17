package com.synacor.jetty.log.format;

import java.lang.Character;
import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;

public abstract class Token
{
	public abstract String toString();

	public static Literal literal(String literal)
	{
		return new Literal(literal);
	}

	public static Directive directive(char directive)
	{
		return directive(directive, null);
	}

	public static Directive directive(char directive, String parameter)
	{
		return new Directive(directive, parameter);
	}

	public String getArgument()
	{
		return null;
	}

	public char getDirective()
	{
		return (char) 0;
	}

	protected static class Literal extends Token
	{
		String literal; 

		public Literal(String literal)
		{
			this.literal = literal;
		}

		public String getArgument()
		{
			return literal;
		}

		public String toString()
		{
			return literal.replaceAll("%", "%%");
		}
	}

	protected static class Directive extends Token
	{
		char directive;
		String argument;

		public Directive(char directive)
		{
			this(directive,null);
		}

		public Directive(char directive, String argument)
		{
			this.directive = directive;
			this.argument = argument;
		}

		public boolean hasArgument()
		{
			return argument != null;
		}

		public char getDirective()
		{
			return directive;
		}

		public String getArgument()
		{
			return argument;
		}

		public String toString()
		{
			if (hasArgument())
			{
				return "%{" + argument + "}" + directive;
			}
			else
			{
				return "%" + directive;
			}
		}
	}
}
