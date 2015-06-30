package com.synacor.jetty.log.txid;

public class Txid120Source extends TxidSource
{
	public native static void init();
	public native String getTxid();

	static
	{
		System.loadLibrary("txid-java");
		init();
	}
}
