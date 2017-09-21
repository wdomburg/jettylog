all: jettlog.jar libtxid-java.so 

jettylog.jar:
	buildr package

libtxid-java.so:
	$(MAKE) -C src/main/c
	#cd src/main/c && $(MAKE)
