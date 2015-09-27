#include <FLAC++/decoder.h>
#include <FLAC++/encoder.h>
#include <FLAC++/metadata.h>
#include <sound/common.h>

class EngineFlacDecoder : public FLAC::Decoder::File {
public:
    EngineFlacDecoder(bool fm) : FLAC::Decoder::File(), _forceMono(fm) {}
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
