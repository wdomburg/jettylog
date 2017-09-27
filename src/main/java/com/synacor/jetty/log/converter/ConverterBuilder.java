/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.jetty.log.converter;

import java.lang.StringBuilder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ConverterBuilder
{
	private Converter head = null;
	private Converter tail = null;

	public ConverterBuilder add(Converter converter)
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
