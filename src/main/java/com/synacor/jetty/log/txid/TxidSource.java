package com.synacor.jetty.log.txid;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

public abstract class TxidSource
{
	public abstract String getTxid();

	public static TxidSource getInstance(String className)
		throws ClassNotFoundException, IllegalAccessException, InstantiationException
	{
		if (className == null || className.equals(""))
			throw new InstantiationException("No class specified.");

		return getInstance(Class.forName(className));
	}

	public static TxidSource getInstance(Class source)
		throws IllegalAccessException, InstantiationException
	{
		return (TxidSource) source.newInstance();
	}
}
