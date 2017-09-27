/*
 * Copyright (c) 2017. Synacor, Inc.
 */

#include <stdio.h>
#include "jni.h"
#include "ngx_http_txid120_logic.h"
#include "com_synacor_txid_Txid120Factory.h"

static FILE* urandom = NULL;

void Java_com_synacor_txid_Txid120Factory_init (JNIEnv *env, jclass this)
{
	urandom = fopen("/dev/urandom", "r");
}

jstring Java_com_synacor_txid_Txid120Factory_getTxid(JNIEnv *env, jclass this)
{
	jstring value;
	u_char txid[21];

	ngx_http_txid120_logic(urandom, (u_char *) &txid);

	txid[20] = '\0';

	value = (*env)->NewStringUTF(env,txid);
	return value;
}
