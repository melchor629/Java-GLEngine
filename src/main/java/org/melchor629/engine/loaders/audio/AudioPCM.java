package org.melchor629.engine.loaders.audio;

import com.sun.jna.Native;
import org.melchor629.engine.utils.MemoryUtils;
import org.lwjgl.system.MemoryUtil;

import java.nio.*;

/**
 * Represents some audio in LPCM format.<br><br>
 * Must call {@link #close()} when is not needed anymore.
 * Can be used with a {@code try}-with-resources block like:<br>
 * <pre>
 * try(AudioPCM pcm = dec.decodeOne()) {
 *     ...
 * }
 * </pre>
 * Resources will be auto-freed when the block ends.
 */
public class AudioPCM implements AutoCloseable {
    private final AudioFormat format;
    private Buffer pcm;
    private long pcmAddress, pcmSize;
    CleanUpFunction cleanUpFunction;

    /**
     * Interface for CleanUpFunction
     * This function will be called when {@link #cleanUpNativeResources()}
     * is called and implementor should free native resources, close files
     * and other stuff.
     */
    public interface CleanUpFunction {
        void clean(AudioPCM self);
    }

    /**
     * Creates an empty object with some format
     * @param format format of the audio
     */
    public AudioPCM(AudioFormat format) {
        this.format = format;
    }

    /**
     * Creates an object with the format {@code format} and PCM sound
     * {@code buffer}
     * @param format format of the audio
     * @param buffer PCM sound
     */
    public AudioPCM(AudioFormat format, Buffer buffer) {
        this(format);
        setBuffer(buffer);
    }

    /**
     * @return Sound data as Short array
     */
    public ShortBuffer getDataAsShort() {
        return getDataAsByte().asShortBuffer();
    }

    /**
     * @return Sound data as Byte array (char*)
     */
    public ByteBuffer getDataAsByte() {
        return Native.getDirectByteBuffer(pcmAddress, pcmSize);
    }

    /**
     * @return Sound data as Integer array
     */
    public IntBuffer getDataAsInt() {
        return getDataAsByte().asIntBuffer();
    }

    /**
     * @return Sound data as Float array
     */
    public FloatBuffer getDataAsFloat() {
        return getDataAsByte().asFloatBuffer();
    }

    /**
     * Sets the buffer with sound data for the Audio
     * @param buffer pcm buffer
     */
    public void setBuffer(Buffer buffer) {
        if(!buffer.isDirect()) throw new RuntimeException("Buffer is not direct allocated");
        pcmAddress = MemoryUtil.memAddress0(buffer);
        pcmSize = buffer.capacity();
        pcm = buffer;

        if(buffer instanceof ShortBuffer)
            pcmSize *= 2;
        else if(buffer instanceof IntBuffer || buffer instanceof FloatBuffer)
            pcmSize *= 4;
    }

    /**
     * Returns the number of samples stored in this class. Differs from
     * {@link AudioFormat#getNumberOfSamples()} in that the last one tells
     * the total number of samples and this method only the ones stored in
     * this class.
     * @return samples stored in this class
     */
    public long getStoredSamples() {
        return pcmSize / format.getBitDepth().bitDepth * 8 / format.getChannels();
    }

    /**
     * @return return the number of seconds stored in this object
     */
    public double getStoredSeconds() {
        return (double) getStoredSamples() / (double) format.getSampleRate();
    }

    /**
     * @return the PCM audio format
     */
    public AudioFormat getFormat() {
        return format;
    }

    /**
     * Clean up all native resources for audio
     */
    public void cleanUpNativeResources() {
        if(cleanUpFunction != null) cleanUpFunction.clean(this);
        if(pcm != null) {
            MemoryUtils.free(pcm);
            pcm = null;
        }
    }

    /**
     * Clean up all native resources associated to this block of audio.
     * Can be used with a {@code try-with-resources} block.
     * @see AutoCloseable
     */
    @Override
    public void close() {
        cleanUpNativeResources();
    }
}
