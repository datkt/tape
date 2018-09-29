CC = clang

CFLAGS += -L ../../build/lib
CFLAGS += -L ../../deps/*

CFLAGS += -I ../../build/include
CFLAGS += -I ../../deps

CFLAGS += -l ktape

export LD_LIBRARY_PATH += ":../../build/lib"

STATIC_LIBRARY ?= $(wildcard ../../build/lib/*.so)

test: test.c
	$(CC) $(CFLAGS) -o $@ $^ $(STATIC_LIBRARY)
	@#LD_LIBRARY_PATH=../../build/lib ./$@
	LD_LIBRARY_PATH+=":../../build/lib" ./$@

clean:
	$(RM) -f test
