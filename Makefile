all: jettylog.jar libtxid-java.so 

jettylog.jar:
	buildr package

libtxid-java.so:
	$(MAKE) -C src/main/c

clean:
	rm -f target/*.jar
	rm -rf target/classes/*
	$(MAKE) -C src/main/c clean
