#include <sound/flac.h>

void ShortBufferClearFunc(void* ptr) {
    ShortBuffer* buff = (ShortBuffer*) ptr;
    delete[] buff->data;
    delete buff;
}

static bool write_little_endian_uint16(ShortBuffer* f, FLAC__uint16 x) {
    return f->put((uint8_t) x) && f->put((uint8_t) (x >> 8));
}

static bool write_little_endian_int16(ShortBuffer* f, FLAC__int16 x) {
    return write_little_endian_uint16(f, (FLAC__uint16) x);
}

static bool write_little_endian_uint32(ShortBuffer* f, FLAC__uint32 x) {
    return f->put((uint8_t) x) && f->put((uint8_t) (x >> 8)) && f->put((uint8_t) (x >> 16))
        && f->put((uint8_t) (x >> 24));
}


::FLAC__StreamDecoderWriteStatus EngineFlacDecoder::write_callback(const::FLAC__Frame*frame,
        const int * const buffer[]) {
    
    size_t i;

    if(attr->total_samples == 0) {
        fprintf(stderr, "ERROR: Solo soporta FLAC con el total de muestas en STREAMINFO\n");
        return FLAC__STREAM_DECODER_WRITE_STATUS_ABORT;
    }
    if(attr->channels > 2 or attr->bps != 16) {
        fprintf(stderr, "ERROR: Solo soporta FLAC con 16 bit stereo/mono");
        return FLAC__STREAM_DECODER_WRITE_STATUS_ABORT;
    }

    if(attr->channels == 2) {
        for(i = 0; i < frame->header.blocksize; i++) {
            if(!write_little_endian_int16(f, (int16_t) buffer[0][i]) || 
                !write_little_endian_int16(f, (int16_t) buffer[1][i])) {
                fprintf(stderr, "ERROR: error al escribir en el buffer");
                return FLAC__STREAM_DECODER_WRITE_STATUS_ABORT;
            }
        }
    } else if(attr->channels == 1) {
        for(i = 0; i < frame->header.blocksize; i++) {
            if(!write_little_endian_int16(f, (int16_t) buffer[0][i])) {
                fprintf(stderr, "ERROR: error al escribir en el buffer");
                return FLAC__STREAM_DECODER_WRITE_STATUS_ABORT;
            }
        }
    }

    return FLAC__STREAM_DECODER_WRITE_STATUS_CONTINUE;
}

void EngineFlacDecoder::metadata_callback(const ::FLAC__StreamMetadata* metadata) {
    if(metadata->type == FLAC__METADATA_TYPE_STREAMINFO) {
        attr = new PCMAttr();
        attr->total_samples = metadata->data.stream_info.total_samples;
        attr->sample_rate = metadata->data.stream_info.sample_rate;
        attr->channels = metadata->data.stream_info.channels;
        attr->bps = metadata->data.stream_info.bits_per_sample;
        unsigned total_size = (attr->total_samples * attr->channels * attr->bps / 8);
        f = new ShortBuffer(total_size);

        if(met)
            met(attr);
    }
}

void EngineFlacDecoder::error_callback(::FLAC__StreamDecoderErrorStatus status) {
    if(err)
        err(status, FLAC__StreamDecoderErrorStatusString[status]);
}

bool _engine_flac_decoder(const char* file, OnMetadataEventCallback m, OnDataEventCallback d, OnErrorEventCallback e) {
    EngineFlacDecoder decoder;
    bool ret = true;

    if(!m || !d) {
        fprintf(stderr, "[engine_flac_decoder:%d] Function need a Metadata and Data event listeners\n", __LINE__);
        ret = false;
    }

    if(!decoder && e) {
        e(-1, "Cannot allocate memory for decoder");
        ret = false;
    } else {
        decoder.met = m;
        decoder.data = d;
        decoder.err = e;
        decoder.set_md5_checking(true);
        FLAC__StreamDecoderInitStatus status = decoder.init(file);
        if(status != FLAC__STREAM_DECODER_INIT_STATUS_OK && e) {
            e(status, FLAC__StreamDecoderInitStatusString[status]);
            ret = false;
        } else {
            ret = decoder.process_until_end_of_stream();
            if(ret) {
                decoder.data(decoder.f);
            } else {
                decoder.err(1, decoder.get_state().resolved_as_cstring(decoder));
            }
        }
    }

    return ret;
}

extern "C" {
bool engine_flac_decoder(const char* file, OnMetadataEventCallback m, OnDataEventCallback d, OnErrorEventCallback e) {
    return _engine_flac_decoder(file, m, d, e);
}
};
