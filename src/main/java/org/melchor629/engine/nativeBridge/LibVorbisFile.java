package org.melchor629.engine.nativeBridge;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

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

    /**
     * The vorbis_info structure contains basic information about the audio in a vorbis bitstream.
     */
    public static class vorbis_info extends Structure {
        /**
         * Vorbis encoder version used to create this bitstream.
         */
        public int version;
        /**
         * Int signifying number of channels in bitstream.
         */
        public int channels;
        /**
         * Sampling rate of the bitstream.
         */
        public NativeLong rate;

        /**
         * Specifies the upper limit in a VBR bitstream. If the value matches the {@code bitrate_nominal} and
         * {@code bitrate_lower} parameters, the stream is fixed bitrate. May be unset if no limit exists.
         */
        public NativeLong bitrate_upper;
        /**
         * Specifies the average bitrate for a VBR bitstream. May be unset. If the {@code bitrate_upper} and
         * {@code bitrate_lower} parameters match, the stream is fixed bitrate.
         */
        public NativeLong bitrate_nominal;
        /**
         * Specifies the lower limit in a VBR bitstream. If the value matches the {@code bitrate_nominal} and
         * {@code bitrate_upper} parameters, the stream is fixed bitrate. May be unset if no limit exists.
         */
        public NativeLong bitrate_lower;
        /**
         * Currently unset
         */
        public NativeLong bitrate_window;

        public Pointer codec_setup;

        @Override
        protected List getFieldOrder() {
            return Arrays.asList("version", "channels", "rate", "bitrate_upper", "bitrate_nominal", "bitrate_lower",
                "bitrate_window", "codec_setup");
        }

        public static class byReference extends vorbis_info implements Structure.ByReference {}
    }

    public static class OggVorbis_File extends Memory {
        public OggVorbis_File() {
            super(944);
        }
    }

    /**
     * Allocates memory for OggVorbis_File structure
     * @return OggVorbis_File pointer
     */
    public static Pointer createOggVorbis_File() {
        return new Memory(944);
    }

    /**
     * After a bitstream has been opened using {@link #ov_fopen(String, OggVorbis_File)} and decoding is complete,
     * the application must call {@code ov_clear()} to clear the decoder's buffers.
     * @param vf A pointer to the OggVorbis_File structure
     * @return 0 for success
     */
    public static native int ov_clear(OggVorbis_File vf);

    /**
     * This is the simplest function used to open and initialize an OggVorbis_File structure. It sets up all the related
     * decoding structure. (…) It is often useful to call ov_fopen() simply to determine whether a given file is a
     * Vorbis bitstream. If the {@code ov_fopen()} call fails, then the file is either inaccessable ({@code errno} is
     * set) or not recognizable as Vorbis ({@code errno} unchanged). If the call succeeds but the initialized {@code vf}
     * structure will not be used, the application is responsible for calling {@link #ov_clear(OggVorbis_File)} to clear
     * the decoder's buffers and close the file.
     * @param path Null terminated string containing a file path suitable for passing to {@code fopen()}.
     * @param vf A pointer to the OggVorbis_File structure
     * @return 0 for success, negative values for errors
     */
    public static native int ov_fopen(String path, OggVorbis_File vf);

    /**
     * Returns the {@link vorbis_info} struct for the specified bitstream. For nonseekable files, always returns the
     * current {@link vorbis_info} struct.
     * @param vf A pointer to the OggVorbis_File structure
     * @param link Link to the desired logical bitstream. For nonseekable files, this argument is ignored. To retrieve
     *             the {@link vorbis_info} struct for the current bitstream, this parameter should be set to -1.
     * @return Returns the {@link vorbis_info} struct for the specified bitstream. Returns {@link vorbis_info} for
     *         current bitstream if the file is nonseekable or i=-1. <b>NULL</b> if the specified bitstream does not
     *         exist or the file has been initialized improperly.
     */
    public static native vorbis_info.byReference ov_info(OggVorbis_File vf, int link);

    /**
     * Returns the total pcm samples of the physical bitstream or a specified logical bitstream.
     * @param vf A pointer to the OggVorbis_File structure
     * @param link Link to the desired logical bitstream. To retrieve the total pcm samples for the entire physical
     *             bitstream, this parameter should be set to -1.
     * @return total length in pcm samples of content if i=-1, OR length in pcm samples of logical bitstream if i=0 to n
     */
    public static native NativeLong ov_pcm_total(OggVorbis_File vf, int link);

    /**
     * This is the main function used to decodeAll a Vorbis file within a loop. It returns up to the specified number of
     * bytes of decoded PCM audio in the requested endianness, signedness, and word size. If the audio is multichannel,
     * the channels are interleaved in the output buffer. If the passed in buffer is large, {@code ov_read()} will not
     * fill it; the passed in buffer size is treated as a limit and not a request.
     * @param vf A pointer to the OggVorbis_File structure
     * @param buffer A pointer to an output buffer. The decoded output is inserted into this buffer.
     * @param length Number of bytes to be read into the buffer. Should be the same size as the buffer. A typical value
     *               is 4096.
     * @param bigendianp Specifies big or little endian byte packing. 0 for little endian, 1 for b ig endian. Typical
     *                   value is 0.
     * @param word Specifies word size. Possible arguments are 1 for 8-bit samples, or 2 or 16-bit samples. Typical
     *             value is 2.
     * @param sgned Signed or unsigned data. 0 for unsigned, 1 for signed. Typically 1.
     * @param bitstream A pointer to the number of the current logical bitstream.
     * @return <ul>
     *     <li>0 indicates EOF</li>
     *     <li>Otherwise, indicates the number of bytes read, and could be less than {@code length}</li>
     * </ul>
     */
    public static native NativeLong ov_read(OggVorbis_File vf, byte[] buffer, int length,
                                            int bigendianp, int word, int sgned, IntByReference bitstream);
}
