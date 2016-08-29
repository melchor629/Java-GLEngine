package org.melchor629.engine.nativeBridge;

import com.sun.jna.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * Bindings to libflac
 */
public class LibFlac {
    static {
        Native.register("flac");
    }

    public static native Pointer FLAC__stream_decoder_new();
    public static native void FLAC__stream_decoder_delete(Pointer decoder);
    public static native boolean FLAC__stream_decoder_set_md5_checking(Pointer decoder, boolean value);
    public static native int FLAC__stream_decoder_init_file(Pointer decoder,
                                                            String filename,
                                                            FlacStreamDecoderWriteStatusCallback write_callback,
                                                            FlacStreamDecoderMetadataCallback metadata_callback,
                                                            FlacStreamDecoderErrorCallback error_callback,
                                                            Pointer client_data);
    public static native boolean FLAC__stream_decoder_process_until_end_of_stream(Pointer decoder);
    public static native boolean FLAC__stream_decoder_process_until_end_of_metadata(Pointer decoder);
    public static native boolean FLAC__stream_decoder_process_single(Pointer decoder);
    public static native int FLAC__stream_decoder_get_state(Pointer decoder);


    public interface FlacStreamDecoderWriteStatusCallback extends Callback {
        int invoke(Pointer decoder, FlacFrame frame, Pointer buffer, Pointer clientData);
    }

    public interface FlacStreamDecoderMetadataCallback extends Callback {
        void invoke(Pointer decoder, FlacStreamMetadata metadata, Pointer clientData);
    }

    public interface FlacStreamDecoderErrorStatus {
        int LOST_SYNC = 0, BAD_HEADER = 1, FRAME_CRC_MISMATCH = 2, UNPARSEABLE_STREAM = 3;
    }

    public interface FlacStreamDecoderErrorCallback extends Callback {
        void invoke(Pointer decoder, int status, Pointer clientData);
    }

    public interface FlacMetadataType {
        int STREAMINFO = 0, PADDING = 1, APPLICATION = 2, SEEKTABLE = 3,
            VORBIS_COMMENT = 4, CUESHEET = 5, PICTURE = 6, UNDEFINED = 7;
    }

    public static class FlacStreamMetadataData extends Union {
        public static class StreamInfo extends Structure {
            public int minBlockSize, maxBlockSize, minFrameSize, maxFrameSize;
            public int sampleRate, channels, bitsPerSample;
            public NativeLong totalSamples;
            public byte[] md5sum = new byte[16];

            @Override
            protected List getFieldOrder() {
                return Arrays.asList("minBlockSize", "maxBlockSize", "minFrameSize",
                    "maxFrameSize", "sampleRate", "channels",
                    "bitsPerSample", "totalSamples", "md5sum");
            }
        }

        //public StreamInfo streamInfo; Tocaria estar usando Union, pero no va
    }

    public static class FlacStreamMetadata extends Structure {
        public int type;
        public boolean isLast;
        public int length;
        public FlacStreamMetadataData.StreamInfo data;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("type", "isLast", "length", "data");
        }
    }

    public interface FlacStreamDecoderWriteStatus {
        int CONTINUE = 0, ABORT = 1;
    }

    public static class FlacFrame extends Structure {
        public FlacFrameHeader header;
        //Está incompleto, solo se necesita la cabecera

        @Override
        protected List getFieldOrder() {
            return Collections.singletonList("header");
        }
    }

    public static class FlacFrameHeader extends Structure {
        public int blockSize, sampleRate, channels, channelAssignment, bps;
        public int numberType;
        public NativeLong sampleNumber;
        public byte crc;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("blockSize", "sampleRate", "channels",
                "channelAssignment", "bps", "numberType", "sampleNumber", "crc");
        }
    }
}
