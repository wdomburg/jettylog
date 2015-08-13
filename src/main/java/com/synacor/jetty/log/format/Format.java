package com.synacor.jetty.log.format;

import java.lang.Character;
import java.lang.CharSequence;
import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;
import java.text.ParseException;

/** Class that represents a logging format */
public class Format
{
	ArrayList<Token> tokens = new ArrayList<Token>();

	/** Construct an blank format for programmatic configuration */
	public Format()
	{
	}

	/**
	 * Construct a format object from a pattern string
	 *
	 * @param pattern A pattern string to use for constructing the format
	 */
	public Format(String pattern)
		throws ParseException
	{
		Parser.parse(this, pattern);
	}

	/**	
	 * Adds a sequence of characters to the format
	 *
	 * @param literal A sequence of characters
	 * @return Format the format instance
	 */
	public Format addLiteral(CharSequence literal)
	{
		return addLiteral(literal.toString());
	}

	/**
	 * Adds a string of characters to the format
	 *
	 * @param literal A string of characters
	 * @return Format The format instance
	 */
	public Format addLiteral(String literal)
	{
		tokens.add(Token.literal(literal));
		return this;
	}

	/**	
	 * Adds a format directive with no argument
	 *
	 * @param directive A character representing the directive
	 * @return Format The format instance
	 */
	public Format addDirective(char directive)
	{
		tokens.add(Token.directive(directive));
		return this;
	}

	/**	
	 * Adds a format directive with an argument
	 *
	 * @param directive A character representing the directive
	 * @param argument An argument altering the behavior of the directive
	 * @return Format The format instance
	 */
	public Format addDirective(CharSequence directive, String argument)
	{
		return addDirective(directive.toString().charAt(0), argument);
	}

	/**	
	 * Adds a format directive with an argument
	 *
	 * @param directive A character representing the directive
	 * @param argument An argument altering the behavior of the directive
	 * @return Format The format instance
	 */
	public Format addDirective(CharSequence directive, CharSequence argument)
	{
		return addDirective(directive.toString().charAt(0), argument.toString());
	}

	/**	
	 * Adds a format directive with an argument
	 *
	 * @param directive A character representing the directive
	 * @param argument An argument altering the behavior of the directive
	 * @return Format The format instance
	 */
	public Format addDirective(char directive, String argument)
	{
		tokens.add(Token.directive(directive, argument));
		return this;
	}

	/**
	 * Returns an array of the tokens comprising the format
	 *
	 * @return ArrayList A list of tokens
	 */
	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

	/**
	 * Returns a string representation of the format
	 *
	 * @return String A string representation of the format
	 */
	public String toString()
	{
		StringBuilder sb = new StringBuilder();

		for (Object token: tokens)
		{
			sb.append(token.toString());
		}

		return sb.toString();
	}
}
