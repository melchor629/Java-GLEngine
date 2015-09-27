#ifndef _COMMON_H_
#define _COMMON_H_

#include <stdio.h>
#include <stdlib.h>

/**
 * Struct with some basic info about a sound
 **/
struct PCMAttr {
    FLAC__uint64 total_samples;
    unsigned sample_rate, channels, bps;
};

void ShortBufferClearFunc(void* buff);
/**
 * Basic short (uint16_t) buffer, with put and get functions.
 **/
struct ShortBuffer {
    uint8_t* data;
    unsigned pos, size;

    void (*clear)(ShortBuffer*);

    ShortBuffer(unsigned cap) {
        data = new uint8_t[cap * 2];
        size = cap * 2;
        pos = 0;
        clear = (void(*)(ShortBuffer*)) ShortBufferClearFunc;
    }

    bool put(uint8_t x) {
        if(pos < size)
            data[pos++] = x;
        return pos <= size;
    }

    bool put(uint16_t x) {
        return put((uint8_t) x) && put((uint8_t) (x >> 8));
    }

    uint16_t get() {
        uint16_t v = ((uint16_t*) data)[pos];
        pos += 2;
        return v;
    }

    void reset() {
        pos = 0;
    }

    const uint16_t get(unsigned pos) const {
        return ((uint16_t*) data)[pos];
    }
};

typedef void (*OnErrorEventCallback)(int, const char*);
typedef void (*OnMetadataEventCallback)(PCMAttr*);
typedef void (*OnDataEventCallback)(ShortBuffer*);

#endif