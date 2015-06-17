package com.synacor.jetty.log.format;

import java.lang.Character;
import java.lang.CharSequence;
import java.lang.RuntimeException;
import java.lang.StringBuilder;
import java.util.List;
import java.util.ArrayList;
import java.text.StringCharacterIterator;

public class Format
{
	ArrayList tokens = new ArrayList();

	public Format()
	{
	}

	public Format(String pattern)
	{
		Parser.parse(this, pattern);
	}

	public Format addLiteral(String literal)
	{
		tokens.add(Token.literal(literal));
		return this;
	}

	public Format addLiteral(CharSequence literal)
	{
		tokens.add(Token.literal(literal.toString()));
		return this;
	}

	public Format addDirective(char directive)
	{
		tokens.add(Token.directive(directive));
		return this;
	}

	public Format addDirective(char directive, String argument)
	{
		tokens.add(Token.directive(directive, argument));
		return this;
	}

	public Format addDirective(CharSequence directive, String argument)
	{
		tokens.add(Token.directive(directive.toString().charAt(0), argument));
		return this;
	}

	public Format addDirective(CharSequence directive, CharSequence argument)
	{
		tokens.add(Token.directive(directive.toString().charAt(0), argument.toString()));
		return this;
	}

	public ArrayList<Token> getTokens()
	{
		return tokens;
	}

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
