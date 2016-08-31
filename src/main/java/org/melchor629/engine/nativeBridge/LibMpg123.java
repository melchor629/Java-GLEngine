package org.melchor629.engine.nativeBridge;

import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Structure;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.List;

/**
 * Java binding to decoder mpg123
 */
public class LibMpg123 {
    static {
        Native.register("mpg123");
        mpg123_init();
        Runtime.getRuntime().addShutdownHook(new Thread(LibMpg123::mpg123_exit));
    }

    public static class mpg123_handle extends Structure implements Structure.ByReference {
        public NativeLong ptr;
        @Override
        protected List getFieldOrder() {
            return Collections.singletonList("ptr");
        }
    }

    /**
     * <a href="https://www.mpg123.de/api/group__mpg123__error.shtml#gac50432012aeaf7c23014de3198dfa5fd">mpg123_errors documentation</a>
     */
    public interface mpg123_errors {
        int MPG123_DONE = -12;
        int MPG123_NEW_FORMAT = -11;
        int MPG123_NEED_MORE = -10;
        int MPG123_ERR = -1;
        int MPG123_OK = 0;
        int MPG123_BAD_OUTFORMAT = 1;
        int MPG123_BAD_CHANNEL = 2;
        int MPG123_BAD_RATE = 3;
        int MPG123_ERR_16TO8TABLE = 4;
        int MPG123_BAD_PARAM = 5;
        int MPG123_BAD_BUFFER = 6;
        int MPG123_OUT_OF_MEM = 7;
        int MPG123_NOT_INITIALIZED = 8;
        int MPG123_BAD_DECODER = 9;
        int MPG123_BAD_HANDLE = 10;
        int MPG123_NO_BUFFERS = 11;
        int MPG123_BAD_RVA = 12;
        int MPG123_NO_GAPLESS = 13;
        int MPG123_NO_SPACE = 14;
        int MPG123_BAD_TYPES = 15;
        int MPG123_BAD_BAND = 16;
        int MPG123_ERR_NULL = 17;
        int MPG123_ERR_READER = 18;
        int MPG123_NO_SEEK_FROM_END = 19;
        int MPG123_BAD_WHENCE = 20;
        int MPG123_NO_TIMEOUT = 21;
        int MPG123_BAD_FILE = 22;
        int MPG123_NO_SEEK = 23;
        int MPG123_NO_READER = 24;
        int MPG123_BAD_PARS = 25;
        int MPG123_BAD_INDEX_PAR = 26;
        int MPG123_OUT_OF_SYNC = 27;
        int MPG123_RESYNC_FAIL = 28;
        int MPG123_NO_8BIT = 29;
        int MPG123_BAD_ALIGN = 30;
        int MPG123_NULL_BUFFER = 31;
        int MPG123_NO_RELSEEK = 32;
        int MPG123_NULL_POINTER = 33;
        int MPG123_BAD_KEY = 34;
        int MPG123_NO_INDEX = 35;
        int MPG123_INDEX_FAIL = 36;
        int MPG123_BAD_DECODER_SETUP = 37;
        int MPG123_MISSING_FEATURE = 38;
        int MPG123_BAD_VALUE = 39;
        int MPG123_LSEEK_FAILED = 40;
        int MPG123_BAD_CUSTOM_IO = 41;
        int MPG123_LFS_OVERFLOW = 42;
        int MPG123_INT_OVERFLOW = 43;
    }

    public interface mpg123_enc_enum {
        int MPG123_ENC_8      = 0x000f,
            MPG123_ENC_16     = 0x0040,
            MPG123_ENC_24     = 0x4000,
            MPG123_ENC_32     = 0x0100,
            MPG123_ENC_SIGNED = 0x0080,
            MPG123_ENC_FLOAT  = 0x0E00,

            MPG123_ENC_SIGNED_16   = MPG123_ENC_16 | MPG123_ENC_SIGNED | 0x10,
            MPG123_ENC_UNSIGNED_16 = MPG123_ENC_16 | 0x20,
            MPG123_ENC_UNSIGNED_8  = 0x01,
            MPG123_ENC_SIGNED_8    = MPG123_ENC_SIGNED|0x02,
            MPG123_ENC_ULAW_8      = 0x04,
            MPG123_ENC_ALAW_8      = 0x08,
            MPG123_ENC_SIGNED_32   = MPG123_ENC_32 | MPG123_ENC_SIGNED | 0x1000,
            MPG123_ENC_UNSIGNED_32 = MPG123_ENC_32 | 0x2000,
            MPG123_ENC_SIGNED_24   = MPG123_ENC_24 | MPG123_ENC_SIGNED | 0x1000,
            MPG123_ENC_UNSIGNED_24 = MPG123_ENC_24 | 0x2000,
            MPG123_ENC_FLOAT_32    = 0x200,
            MPG123_ENC_FLOAT_64    = 0x400,

            MPG123_ENC_ANY = MPG123_ENC_SIGNED_16  | MPG123_ENC_UNSIGNED_16
                    | MPG123_ENC_UNSIGNED_8 | MPG123_ENC_SIGNED_8
                    | MPG123_ENC_ULAW_8     | MPG123_ENC_ALAW_8
                    | MPG123_ENC_SIGNED_32  | MPG123_ENC_UNSIGNED_32
                    | MPG123_ENC_SIGNED_24  | MPG123_ENC_UNSIGNED_24
                    | MPG123_ENC_FLOAT_32   | MPG123_ENC_FLOAT_64;
    }

    public static native int mpg123_init();
    public static native void mpg123_exit();
    public static native mpg123_handle mpg123_new(final String decoder, IntByReference error);
    public static native void mpg123_delete(mpg123_handle handle);

    public static native int mpg123_open(mpg123_handle mh, String path);
    public static native int mpg123_close(mpg123_handle mh);
    public static native int mpg123_read(mpg123_handle mh, ByteBuffer outmemory, NativeLong outmemsize, NativeLongByReference done);

    public static native int mpg123_safe_buffer();
    public static native NativeLong mpg123_length(mpg123_handle mh);

    public static native int mpg123_getformat(mpg123_handle mh, NativeLongByReference rate, IntByReference channels, IntByReference encoding);

    public static native String mpg123_plain_strerror(int error);

    public static int mpg123_samplesize(int enc) {
        return (enc & mpg123_enc_enum.MPG123_ENC_8) != 0
        ?       1
        :       ( (enc & mpg123_enc_enum.MPG123_ENC_16) != 0
                ?       2
                :       ( (enc & mpg123_enc_enum.MPG123_ENC_24) != 0
                ?       3
                        :       ( (  (enc & mpg123_enc_enum.MPG123_ENC_32) != 0 || (enc == mpg123_enc_enum.MPG123_ENC_FLOAT_32)  )
                                ?       4
                                :       ( (enc) == mpg123_enc_enum.MPG123_ENC_FLOAT_64
                                        ?       8
                                        :       0
                                        )
                                )
                        )
                );
    }
}
