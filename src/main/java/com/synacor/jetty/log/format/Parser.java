/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.format;

import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;
import java.text.ParseException;

/**
  * Parses a string into a format object using a simple state machine.
  *
  * The format is patterened after the LogFormat strings used in
  * the Apache httpd project.
  *
  * Non-literal directives are specified with a '%' character and
  * can be prefixed with an optional argument.
  */
public class Parser 
{
	/** Represents the state of the parser */
	public static enum State { LITERAL, FORMAT, DIRECTIVE, ARGUMENT};

	/**
	 * Constructs a new Format object from the pattern string
	 *
	 * @param pattern The pattern string
	 * @return Format The constructed format
	 */
	public static Format parse(String pattern)
		throws ParseException
	{
		return parse(new Format(), pattern);
	}

	/**
	 * Parses a pattern string into an existing Format object
	 *
	 * @param format The incomplete format
	 * @param pattern The pattern string
	 * @return Format The completed format
	 */
	public static Format parse(Format format, String pattern)
		throws ParseException
	{
		StringBuilder buffer = new StringBuilder();
		String param = null;

		State state = State.LITERAL;

		StringCharacterIterator iter = new StringCharacterIterator(pattern);
		char c = iter.current();

		while (c != StringCharacterIterator.DONE)
		{
			switch(c)
			{
				case '%':
					if (buffer.length() > 0)
					{
						format.addLiteral(buffer.toString());
						buffer.setLength(0);
					}
					switch(state)
					{
						case LITERAL:
							state = State.FORMAT;
							break;
						case FORMAT:
							buffer.append(c);
							state = State.LITERAL;
							break;
						default:
							throw new ParseException("Found bad argument: " + pattern, iter.getIndex());
					}

					break;
				case '{':
					switch(state)
					{
						case FORMAT:
							state = State.ARGUMENT;
							break;
						case LITERAL:
							buffer.append(c);
							break;
						default:
							throw new ParseException("Found unxpected {: " + pattern, iter.getIndex());
					}

					break;
				case '}':
					switch(state)
					{
						case LITERAL:
							buffer.append(c);
							break;
						case DIRECTIVE:
							format.addDirective(buffer.toString(), param);
							param = null;
							buffer.setLength(0);
							buffer.append(c);
							state = State.LITERAL;
							break;
						case ARGUMENT:
							param = buffer.toString();
							buffer.setLength(0);
							state = State.DIRECTIVE;
							break;
						default:
							throw new ParseException("Found unxpected }: " + pattern, iter.getIndex());
					}

					break;
				case ' ':
				case '"':
					switch(state)
					{
						case LITERAL:
							buffer.append(c);
							break;
						case DIRECTIVE:
							format.addDirective(buffer.toString(), param);
							param = null;
							buffer.setLength(0);
							buffer.append(c);
							state = State.LITERAL;
							break;
						case FORMAT:
						case ARGUMENT:
							throw new ParseException("Found bad argument: " + pattern, iter.getIndex());
					}

					break;
				/* Why was I breaking this out again?
				case '"':
					switch(state)
					{
						case LITERAL:
							break;
						case FORMAT:
							break;
						case DIRECTIVE:
							break;
						case ARGUMENT:
							break;
					}

					break;
				*/
				default:
					buffer.append(c);
					
					// Doesn't actually need to be a switch!
					switch(state)
					{
						case FORMAT:
							state = State.DIRECTIVE;
					}
			}

			c = iter.next();
		}

		switch(state)
		{
			case LITERAL:
				format.addLiteral(buffer);
				break;
			case DIRECTIVE:
				format.addDirective(buffer, param);
				break;
			default:
				throw new ParseException("Unexpected end of pattern: " + pattern, iter.getIndex());
		}

		return format;
	}
}
