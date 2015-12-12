package org.melchor629.engine.clib;

import com.sun.jna.*;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * (part) Native bindings for libvorbisfile
 */
public class LibVorbisFile implements Library {
    public static final String JNA_LIBRARY_NAME = "vorbisfile";
    public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(LibVorbisFile.JNA_LIBRARY_NAME);
    static {
        Native.register(LibVorbisFile.class, LibVorbisFile.JNA_NATIVE_LIB);
    }

    public static class vorbis_info extends Structure {
        public int version;
        public int channels;
        public NativeLong rate;

        public NativeLong bitrate_upper;
        public NativeLong bitrate_nominal;
        public NativeLong bitrate_lower;
        public NativeLong bitrate_window;

        public Pointer codec_setup;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("version", "channels", "rate", "bitrate_upper", "bitrate_nominal", "bitrate_lower",
                "bitrate_window", "codec_setup");
        }

        public static class byReference extends vorbis_info implements Structure.ByReference {}
    }

    public static Pointer createOggVorbis_File() {
        return new Memory(944);
    }

    public static native int ov_clear(Pointer vf);
    public static native int ov_fopen(String path, Pointer vf);
    public static native vorbis_info.byReference ov_info(Pointer vf, int link);
    public static native NativeLong ov_pcm_total(Pointer vf, int link);
    public static native NativeLong ov_read(Pointer vf, byte[] buffer, int length,
                                            int bigendianp, int word, int sgned, IntByReference bitstream);
}
