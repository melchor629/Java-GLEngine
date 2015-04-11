#include <stdio.h>
#include <stdlib.h>
#include <FLAC++/decoder.h>
#include <FLAC++/encoder.h>
#include <FLAC++/metadata.h>

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


class EngineFlacDecoder : public FLAC::Decoder::File {
public:
    EngineFlacDecoder() : FLAC::Decoder::File() {}
    OnErrorEventCallback err;
    OnMetadataEventCallback met;
    OnDataEventCallback data;
    ShortBuffer *f;

protected:
    virtual ::FLAC__StreamDecoderWriteStatus write_callback(const ::FLAC__Frame * frame,
        const int * const buffer[]);
    virtual void metadata_callback(const ::FLAC__StreamMetadata *metadata);
    virtual void error_callback(::FLAC__StreamDecoderErrorStatus status);
    void forceMono(bool fm) { _forceMono = fm; }

private:
    PCMAttr* attr;
    bool _forceMono;

    EngineFlacDecoder(const EngineFlacDecoder&);
    EngineFlacDecoder& operator=(const EngineFlacDecoder&);
};

class EngineFlacEncoder : public FLAC::Encoder::File {
public:
    EngineFlacEncoder(): FLAC::Encoder::File() {}
protected:
    virtual void progress_callback(uint64_t bytes_written, uint64_t samples_written, unsigned frames_written, unsigned total_frames_estimate);
};

extern "C" {
    /**
     * Decodes a FLAC file to PCM sound format. Only supported 16 bit stereo or mono.
     *
     * @param file Path to the file be read
     * @param m Callback called when basic metadata is read
     * @param d Callback called when the sound data is read
     * @param e Callback called when an error occurrs
     * @param forceMono Forces the audio to be mono
     **/
    bool engine_flac_decoder(const char* file, OnMetadataEventCallback m, OnDataEventCallback d, OnErrorEventCallback e,
                             bool forceMono);

    /**
     * Encodes a PCM sound to FLAC. Only supported 16 bit stereo or mono.
     *
     * @param file Path to the file to be writter
     * @param attr PCM attributes
     * @param buffer PCM buffer data
     **/
    bool engine_flac_encoder(const char* file, PCMAttr* attr, ShortBuffer* buffer, OnErrorEventCallback e);
};
