#include <sound/flac.h>
#define READSIZE 1024

unsigned total_samples;
void EngineFlacEncoder::progress_callback(uint64_t bytes_written, uint64_t samples_written, unsigned frames_written, unsigned total_frames_estimate) {
    fprintf(stdout, "Wrote %llu bytes, %llu/%u samples, %u/%u frames\n", bytes_written,
        samples_written, total_samples, frames_written, total_frames_estimate);
}

bool _engine_flac_encoder(const char* file, PCMAttr* attr, ShortBuffer* buffer, OnErrorEventCallback e) {
    bool ok = true;
    EngineFlacEncoder encoder;
    FLAC__StreamEncoderInitStatus init_status;
    FLAC__StreamMetadata *metadata[2];
    FLAC__StreamMetadata_VorbisComment_Entry entry;
    FILE* f;
    total_samples = (attr->total_samples * attr->channels * attr->bps / 8);
    int32_t* pcm[attr->channels];
    pcm[0] = new int32_t[READSIZE];
    pcm[1] = new int32_t[READSIZE];

    if((f = fopen(file, "wb")) == NULL) {
        e(-1, "ERROR: no se ha podido abrir el archivo\n");
        ok = false;
    } else {
        ok &= encoder.set_verify(true);
        ok &= encoder.set_compression_level(5);
        ok &= encoder.set_channels(attr->channels);
        ok &= encoder.set_bits_per_sample(attr->bps);
        ok &= encoder.set_sample_rate(attr->sample_rate);
        ok &= encoder.set_total_samples_estimate(attr->total_samples);

        if(!ok) {
            e(-2, "ERROR: No se sae xq pero no se ha podido poner metadatos");
        }

        metadata[0] = FLAC__metadata_object_new(FLAC__METADATA_TYPE_VORBIS_COMMENT);
        metadata[1] = FLAC__metadata_object_new(FLAC__METADATA_TYPE_PADDING);
        FLAC__metadata_object_vorbiscomment_entry_from_name_value_pair(&entry, "ARTIST", "<>");
        FLAC__metadata_object_vorbiscomment_append_comment(metadata[0], entry, false);
        FLAC__metadata_object_vorbiscomment_entry_from_name_value_pair(&entry, "YEAR", "2015");
        FLAC__metadata_object_vorbiscomment_append_comment(metadata[0], entry, false);

        metadata[1]->length = 1234;
        ok = encoder.set_metadata(metadata, 2);

        if(ok) {
            init_status = encoder.init(file);
            if(init_status != FLAC__STREAM_ENCODER_INIT_STATUS_OK) {
                e(init_status, FLAC__StreamEncoderInitStatusString[init_status]);
                ok = false;
            }
        } else {
            e(-3, "ERROR: No se ha podido crear los metadatos");
        }

        if(ok) {
            size_t left = attr->total_samples;
            while(ok && left) {
                size_t need = (left > READSIZE ? (size_t) READSIZE : (size_t) left);
                for(size_t i = 0; i < need * attr->channels / 2; i++) {
                    //pcm[i] = (int)(((FLAC__int16)(FLAC__int8)buffer->data[2*i+1]<<8)|(FLAC__int16)buffer->data[2*i]);
                    //printf("%d\n", pcm[i]);
                    //pcm[0][i] = (FLAC__int32) buffer->get(2*i);
                    //pcm[1][i] = (FLAC__int32) buffer->get(2*i+1);
                    pcm[0][i] = (int)(((FLAC__int16)(FLAC__int8)buffer->data[4*i+1]<<8)|(FLAC__int16)buffer->data[4*i]);
                    pcm[1][i] = (int)(((FLAC__int16)(FLAC__int8)buffer->data[4*i+3]<<8)|(FLAC__int16)buffer->data[4*i+2]);
                }

                //ok = encoder.process_interleaved(pcm, need);
                ok = encoder.process(pcm, need);
                left -= need;
            }
        } else {
            e(-4, "ERROR: No se ha podido convertir a FLAC todo el sonido");
        }

        ok &= encoder.finish();

        FLAC__metadata_object_delete(metadata[0]);
        FLAC__metadata_object_delete(metadata[1]);
    }

    fclose(f);
    delete[] pcm[0];
    delete[] pcm[1];
    return ok;
}

extern "C" {
    bool engine_flac_encoder(const char* file, PCMAttr* attr, ShortBuffer* buffer, OnErrorEventCallback e) {
        return _engine_flac_encoder(file, attr, buffer, e);
    }
}