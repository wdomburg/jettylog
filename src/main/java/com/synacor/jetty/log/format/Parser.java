package com.synacor.jetty.log.format;

import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;

public class Parser 
{
	public static enum State { LITERAL, FORMAT, DIRECTIVE, ARGUMENT};

	public static Format parse(String pattern)
	{
		return parse(new Format(), pattern);
	}

	public static Format parse(Format format, String pattern)
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
							// FIXME:  Throw exception here
							System.out.println("Got a bad argument");
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
							// FIXME: Throw exception here
							System.out.println("Unexpected {");
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
							// FIXME: Throw exception here
							System.out.println("Unexpected {");
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
							// FIXME:  Throw exception here
							System.out.println("Got a bad argument");
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
				// FIXME:  Throw exception here
				System.out.println("Unexpected end of pattern");
		}

		return format;
	}
}
