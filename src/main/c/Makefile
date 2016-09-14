SRC=$(wildcard *.c)
OBJ=$(SRC:.c=.o)

TARGET=../../../target

libtxid-java.so: ngx_http_txid120_logic.o com_synacor_txid_Txid120Factory.o
	gcc -shared -o $(TARGET)/$@ $^ 

com_synacor_txid_Txid120Factory.o: ngx_http_txid120_logic.o
	gcc -fpic -c $(@:.o=.c) -o $@ -I/usr/lib/jvm/java/include -I/usr/lib/jvm/java/include/linux

ngx_http_txid120_logic.o:
	gcc -fpic -c $(@:.o=.c) -o $@ 

.PHONY: clean
clean:
	rm -f $(OBJ) $(TARGET)/libtxid-java.so
