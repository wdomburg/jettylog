package com.synacor.jetty.log.txid;

import java.util.UUID;

public class UuidSource extends TxidSource
{
	public String getTxid()
	{
		return String.valueOf(UUID.randomUUID());
	}
}
