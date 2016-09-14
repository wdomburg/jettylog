all: jettlog.jar libtxid-java.so 

jettlog.jar:
	buildr package

libtxid-java.so:
	$(MAKE) -C src/main/c
	#cd src/main/c && $(MAKE)
