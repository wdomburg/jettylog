package com.synacor.txid;

public class Txid120Factory extends TxidFactory
{
	public native static void init();
	public native String getTxid();

	static
	{
		System.loadLibrary("txid-java");
		init();
	}
}
