package com.synacor.jetty.log;

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

	public static Directive directive(String directive)
	{
		return directive(directive, null);
	}

	public static Directive directive(String directive, String parameter)
	{
		return new Directive(directive, parameter);
	}

	private static class Literal extends Token
	{
		String literal; 

		public Literal(String literal)
		{
			this.literal = literal;
		}

		public String toString()
		{
			return literal.replaceAll("%", "%%");
		}
	}

	private static class Directive extends Token
	{
		String directive;
		String argument;

		public Directive(String directive)
		{
			this(directive,null);
		}

		public Directive(String directive, String argument)
		{
			this.directive = directive;
			this.argument = argument;
		}

		public boolean hasArgument()
		{
			return argument != null;
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
