#include <stdio.h>
#include "jni.h"
#include "ngx_http_txid120_logic.h"
#include "com_synacor_jetty_log_txid_Txid120Source.h"

// gcc -shared -fPIC -I/opt/zimbra/jdk-1.7.0_51/include/ -I/opt/zimbra/jdk-1.7.0_51/include/linux/ -o /var/tmp/libtxid-java.so com_synacor_jetty_log_txid_Txid120Source.c ngx_http_txid120_logic.c

static FILE* urandom = NULL;

void Java_com_synacor_jetty_log_txid_Txid120Source_init (JNIEnv *env, jclass this)
{
	urandom = fopen("/dev/urandom", "r");
}

jstring Java_com_synacor_jetty_log_txid_Txid120Source_getTxid(JNIEnv *env, jclass this)
{
	jstring value;
	u_char txid[21];

	ngx_http_txid120_logic(urandom, (u_char *) &txid);

	txid[20] = '\0';

	value = (*env)->NewStringUTF(env,txid);
	return value;
}
