/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.util.txid;

public class Txid120Factory
	extends TxidFactory
{
	public native static void init();
	public native String getTxid();

	static
	{
		System.loadLibrary("txid-java");
		init();
	}
}
