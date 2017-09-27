/*
 * Copyright (c) 2017. Synacor, Inc.
 */

package com.synacor.txid;

import java.util.UUID;

public class UuidFactory extends TxidFactory
{
	public String getTxid()
	{
		return String.valueOf(UUID.randomUUID());
	}
}
