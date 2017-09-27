all: jettylog.jar libtxid-java.so 

jettylog.jar:
	buildr package

libtxid-java.so:
	$(MAKE) -C src/main/c
