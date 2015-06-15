package com.synacor.jetty.log;

public class Txid
{

	public native static void init();
	public native static String getTxid();

	static
	{
		System.loadLibrary("txid-java");
		init();
	}

}
