BIN_DIR=../../build/resources/main

OS_DIR=darwin
PREFIX=lib
EXT=dylib

LD_FLAGS=-lFLAC++ -lFLAC
CPPFLAGS=-O3 -I./include -fPIC
SOURCES=sound/flac_decoder.cpp sound/flac_encoder.cpp
OBJECTS=$(SOURCES:.cpp=.o)

UNAME_S := $(shell uname -s)
ifeq ($(UNAME_S),Darwin)
	OS_DIR=darwin
	EXT=dylib
else
ifeq ($(UNAME_S),Linux)
	OS_DIR=linux-amd64
	EXT=so
endif
endif

library: $(OBJECTS)
	g++ $(OBJECTS) $(LD_FLAGS) -fPIC -shared -o $(BIN_DIR)/$(OS_DIR)/$(PREFIX)Engine.$(EXT)

all: $(SOURCES) library

clean:
	rm $(OBJECTS)