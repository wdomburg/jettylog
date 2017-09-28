/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.util.txid;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

public abstract class TxidFactory
{
	public abstract String getTxid();

	public static TxidFactory defaultInstance = new UuidFactory();

	public static TxidFactory getInstance(String className)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		if (className == null || className.equals(""))
			throw new InstantiationException("No class specified.");

		return getInstance(Class.forName(className));
	}

	public static TxidFactory getInstance(Class source)
		throws IllegalAccessException, InstantiationException
	{
		return (TxidFactory) source.newInstance();
	}

	public static TxidFactory getDefaultInstance()
	{
		return defaultInstance;
	}
}
