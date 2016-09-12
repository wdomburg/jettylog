package com.synacor.txid;

import java.lang.ClassNotFoundException;
import java.lang.IllegalAccessException;
import java.lang.InstantiationException;

public abstract class TxidFactory
{
	public abstract String getTxid();

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
}
