package com.synacor.jetty.log.format;

import java.lang.Character;
import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;

/** A discrete component of a format */
public abstract class Token
{
	/**
	 * Returns a string representation of the token
	 *
	 * @return String A string representation of the token
	 */
	public abstract String toString();

	/**
	 * Constructs a new literal token
	 *
	 * @param literal A string of characters 
	 * @return Literal A literal token
	 *
	 */
	public static Literal literal(String literal)
	{
		return new Literal(literal);
	}

	/**
	 * Constructs a new directive token
	 *
	 * @param directive A format directive
	 * @return Directive A directive token
	 */
	public static Directive directive(char directive)
	{
		return directive(directive, null);
	}

	/**
	 * Constructs a new directive token
	 *
	 * @param directive A format directive
	 * @param parameter A parameter for a format directive
	 * @return Directive A directive token
	 */
	public static Directive directive(char directive, String parameter)
	{
		return new Directive(directive, parameter);
	}

	/**
	 * Returns the directive argument
	 *
	 * Default implementation returns null
	 *
	 * @return String A null; subclasses should override as appropriate
	 */
	public String getArgument()
	{
		return null;
	}

	/**
	 * Returns the directive
	 *
	 * Default implementation returns null character
	 *
	 * @return char A null character; subclasses should override as appropriate
	 */
	public char getDirective()
	{
		return (char) 0;
	}

	/** A token representing a string of literal characters */
	protected static class Literal extends Token
	{
		String literal; 

		/**
		 * Construct a literal token
		 *
		 * @param literal A string of characters
		 */
		public Literal(String literal)
		{
			this.literal = literal;
		}

		/**
		 * Returns to contents of the string
		 *
		 * @return String The string of literal characters
		 */
		public String getArgument()
		{
			return literal;
		}

		/**
		 * Returns a string representation of the token
		 *
		 * @return String An escaped version of the string
		 */
		public String toString()
		{
			return literal.replaceAll("%", "%%");
		}
	}

	/** A token representing a format directive */
	protected static class Directive extends Token
	{
		/** The charcter specifying the format directive */
		char directive;
		/** The argument for the format directive */
		String argument;

		/**
		 * Construct a dirctive token with no argument
		 *
		 * @param directive The directive specifier
		 */
		public Directive(char directive)
		{
			this(directive,null);
		}

		/**
		 * Construct a dirctive token with no argument
		 *
		 * @param directive The directive specifier
		 * @param argument The directive argument
		 */
		public Directive(char directive, String argument)
		{
			this.directive = directive;
			this.argument = argument;
		}

		/**
		 * Return whether the format directive has an argument
		 *
		 * @return boolean whether the format directive has an argument
		 */
		public boolean hasArgument()
		{
			return argument != null;
		}

		/**
		 * Return the directive specifier
		 *
		 * @return char The directive speciefier
		 */
		public char getDirective()
		{
			return directive;
		}

		/**
		 * Return the directive argument
		 *
		 * @return String The directive argument
		 */
		public String getArgument()
		{
			return argument;
		}

		/**
		 * Returns a string representation of the token
		 *
		 * @return String An stringified version of the directive and argument
		 */
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
