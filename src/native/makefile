BIN_DIR=../../bin

OS_DIR=darwin
PREFIX=lib
EXT=dylib

LD_FLAGS=-lFLAC++ -lFLAC
CPPFLAGS=-O3 -I./include
SOURCES=sound/flac_decoder.cpp sound/flac_encoder.cpp
OBJECTS=$(SOURCES:.cpp=.o)

ifeq ($(OS),Darwin)
	OS_DIR=darwin
else
ifeq ($(OS),Linux)
	OS_DIR=linux-amd64
endif
endif

library: $(OBJECTS)
	g++ $(OBJECTS) $(LD_FLAGS) -fPIC -shared -o $(BIN_DIR)/$(OS_DIR)/$(PREFIX)Engine.$(EXT)

all: $(SOURCES) library

clean:
	rm $(OBJECTS)