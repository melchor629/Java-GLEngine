package org.melchor629.engine.native_bridge;

import com.sun.jna.*;
import com.sun.jna.ptr.IntByReference;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;

/**
 * (part) Native bindings to libopusfile
 */
public class LibOpusFile implements Library {
    public static final String JNA_LIBRARY_NAME = "opusfile";
    public static final NativeLibrary JNA_NATIVE_LIB = NativeLibrary.getInstance(LibOpusFile.JNA_LIBRARY_NAME);
    static {
        Native.register(LibOpusFile.class, LibOpusFile.JNA_NATIVE_LIB);
    }

    public static class OggOpusFile extends Structure implements Structure.ByReference {
        @Override
        protected List getFieldOrder() {
            return Arrays.asList("");
        }
    }

    public static final int OP_FALSE = -1;
    public static final int OP_EOF = (-2);
    public static final int OP_HOLE = -3;
    public static final int OP_EREAD = -128;
    public static final int OP_EFAULT = -129;
    public static final int OP_EIMPL = -130;
    public static final int OP_EINVAL = -131;
    public static final int OP_ENOTFORMAT = -132;
    public static final int OP_EBADHEADER = -133;
    public static final int OP_EVERSION = -134;
    public static final int OP_ENOTAUDIO = -135;
    public static final int OP_EBADLINK = -137;
    public static final int OP_ENOSEEK = -138;
    public static final int OP_EBADTIMESTAMP = -139;

    /**
     * Open a stream from the given file path.
     * @param _path The path to the file to open.
     * @param _error <b>[OUT]</b>
     *               Returns 0 on success, or a failure code on error. You may pass in NULL if you don't want the
     *               failure code. The failure code will be OP_EFAULT if the file could not be opened, or one of the
     *               following failure codes:
     *               <ul>
     *               <li>{@link #OP_EREAD} An underlying read, seek, or tell operation failed when it should have
     *                  succeeded, or we failed to find data in the stream we had seen before.</li>
     *               <li>{@link #OP_EFAULT} There was a memory allocation failure, or an internal library error.</li>
     *               <li>{@link #OP_EIMPL} The stream used a feature that is not implemented, such as an unsupported
     *                  channel family.</li>
     *               <li>{@link #OP_EINVAL} seek() was implemented and succeeded on this source, but tell() did not, or
     *                  the starting position indicator was not equal to <i>_initial_bytes</i>.</li>
     *               <li>{@link #OP_ENOTFORMAT} The stream contained a link that did not have any logical Opus streams
     *                  in it.</li>
     *               <li>{@link #OP_EBADHEADER} A required header packet was not properly formatted, contained illegal
     *                  values, or was missing altogether.</li>
     *               <li>{@link #OP_EVERSION} An ID header contained an unrecognized version number.</li>
     *               <li>{@link #OP_EBADLINK} We failed to find data we had seen before after seeking.</li>
     *               <li>{@link #OP_EBADTIMESTAMP} The first or last timestamp in a link failed basic validity checks.</li>
     *               </ul>
     * @return A freshly opened {@code OggOpusFile}, or {@code NULL }on error.
     */
    public static native OggOpusFile op_open_file(String _path, IntByReference _error);

    /**
     * Open a stream from a memory buffer.
     * @param _data The memory buffer to open.
     * @param _size The number of bytes in the buffer.
     * @param _error <b>[OUT]</b> Returns 0 on success, or a failure code on error. You may pass in NULL if you don't
     *               want the failure code. See {@link #op_open_file(String, IntByReference)} for a full list of failure
     *               codes.
     * @return A freshly opened {@code OggOpusFile}, or {@code NULL }on error.
     */
    public static native OggOpusFile op_open_memory(final ByteBuffer _data, NativeLong _size, IntByReference _error);

    /**
     * Release all memory used by an OggOpusFile.
     * @param _of The OggOpusFile to free.
     */
    public static native void op_free(OggOpusFile _of);


    /**
     * Get the channel count of the given link in a (possibly-chained) Ogg Opus stream. …
     * @param _of The {@code OggOpusFile} from which to retrieve the channel count.
     * @param _li The index of the link whose channel count should be retrieved. Use a negative number to get the
     *            channel count of the current link.
     * @return The channel count of the given link. If {@code _li} is greater than the total number of links, this
     *         returns thechannel count of the last link. If the source is not seekable, this always returns the channel
     *         count of the current link.
     */
    public static native int op_channel_count(final OggOpusFile _of, int _li);

    /**
     * Get the total PCM length (number of samples at 48 kHz) of the stream, or of an individual link in a
     * (possibly-chained) Ogg Opus stream.
     * @param _of The {@code OggOpusFile} from which to retrieve the PCM offset.
     * @param _li The index of the link whose PCM length should be computed. Use a negative number to get the PCM length
     *            of the entire stream.
     * @return The PCM length of the entire stream if {@code _li} is negative, the PCM length of link {@code _li} if it
     *         is non-negative, or a negative value on error.
     */
    public static native NativeLong op_pcm_total(final OggOpusFile _of, int _li);

    /**
     * Reads more samples from the stream.
     * @param _of The {@code OggOpusFile} from which to read.
     * @param _pcm A buffer in which to store the output PCM samples, as signed native-endian 16-bit values at 48 kHz
     *             with a nominal range of [-32768,32767). Multiple channels are interleaved using the Vorbis channel
     *             ordering. This must have room for at least {@code _buf_size} values.
     * @param _buf_size The number of values that can be stored in {@code _pcm}. It is recommended that this be large
     *                  enough for at least 120 ms of data at 48 kHz per channel (5760 values per channel). Smaller
     *                  buffers will simply return less data, possibly consuming more memory to buffer the data
     *                  internally. <i>libopusfile</i> may return less data than requested. If so, there is no guarantee
     *                  that the remaining data in {@code _pcm} will be unmodified.
     * @param _li The index of the link this data was decoded from. You may pass NULL if you do not need this
     *            information. If this function fails (returning a negative value), this parameter is left unset.
     * @return The number of samples read per channel on success, or a negative value on failure. The channel count can
     *         be retrieved on success by calling op_head(_of,*_li). The number of samples returned may be 0 if the
     *         buffer was too small to store even a single sample for all channels, or if end-of-file was reached. The
     *         list of possible failure codes follows. Most of them can only be returned by unseekable, chained streams
     *         that encounter a new link.
     */
    public static native int op_read(OggOpusFile _of, short[] _pcm, int _buf_size, IntByReference _li);
}
