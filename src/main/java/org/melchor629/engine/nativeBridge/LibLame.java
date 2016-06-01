package org.melchor629.engine.nativeBridge;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;

/**
 * Java Binding to lame encoder and decoder
 */
public interface LibLame extends Library {
    LibLame INSTANCE = (LibLame) Native.loadLibrary("mp3lame", LibLame.class);

    class mp3data_struct extends Structure {
        /**
         * 1 if header was parsed and following data was<br>
         * computed
         */
        public int header_parsed;
        /** number of channels */
        public int stereo;
        /** sample rate */
        public int samplerate;
        /** bitrate */
        public int bitrate;
        /** mp3 frame type */
        public int mode;
        /** mp3 frame type */
        public int mode_ext;
        /** number of samples per mp3 frame */
        public int framesize;
        /**
         * this data is only computed if mpglib detects a Xing VBR header<br>
         * number of samples in mp3 file.
         */
        public NativeLong nsamp;
        /** total number of frames in mp3 file */
        public int totalframes;
        /**
         * this data is not currently computed by the mpglib routines<br>
         * frames decoded counter
         */
        public int framenum;
        public mp3data_struct() {
            super();
        }
        protected List<? > getFieldOrder() {
            return Arrays.asList("header_parsed", "stereo", "samplerate", "bitrate", "mode", "mode_ext", "framesize", "nsamp", "totalframes", "framenum");
        }
    }

    PointerByReference hip_decode_init();
    int hip_decode_exit(PointerByReference tag);
    int hip_decode1_headersB(PointerByReference ctx, ByteBuffer mp3_buf, int bufferSize, short[] left_buf, short[] right_buf, mp3data_struct mp3data, IntByReference enc_delay, IntByReference enc_padding);
    int hip_decode1_headers(PointerByReference ctx, ByteBuffer mp3_buf, int bufferSize, short[] left_buf, short[] right_buf, mp3data_struct mp3data);
}
