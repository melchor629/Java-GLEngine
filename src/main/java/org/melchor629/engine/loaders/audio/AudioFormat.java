package org.melchor629.engine.loaders.audio;

/**
 * Superclass for containing lPCM audio. Ready to be encoded or
 * decoded from other codecs.
 */
public final class AudioFormat {
    private int samplerate = -1, channels = -1;
    private long samples = -1;
    private BitDepth bitDepth;

    /**
     * Enumeration enclosing supported PCM formats.
     * Bit Depth is size of every sample for every channel.
     */
    public enum BitDepth {
        UINT8(8, Byte.class),
        INT16(16, Short.class),
        INT24(24, Integer.class),
        INT24_NOPACKED(32, Integer.class),
        FLOAT32(32, Float.class);

        public final int bitDepth;
        final Class type;
        BitDepth(int i, Class t) { bitDepth = i; type = t; }
    }

    /**
     * Gets the sample rate for this Audio. Sample rate is
     * the number of samples will be emitted per second.
     * @return Sample rate in Hz (Hertzs)
     */
    public int getSampleRate() {
        return samplerate;
    }

    /**
     * Gets the number of channels of this Audio.
     * @return number of channels
     */
    public int getChannels() {
        return channels;
    }

    /**
     * Gets the total number of samples of this Audio.
     * @return total number of samples
     */
    public long getNumberOfSamples() {
        return samples;
    }

    /**
     * Gets the Bit Depth of the PCM data.
     * @return bit depth
     * @see AudioFormat.BitDepth
     */
    public BitDepth getBitDepth() {
        return bitDepth;
    }

    /**
     * Sets the sample rate of the sound. Sample rate is
     * the number of samples will be emitted per second.
     * @param sr Sample rate in Hz (Hertzs)
     */
    public void setSampleRate(int sr) {
        samplerate = sr;
    }

    /**
     * Sets the number of channels of the sound.
     * @param ch number of channels
     */
    public void setChannels(int ch) {
        channels = ch;
    }

    /**
     * Sets the total number of samples of the sound.
     * @param samples total number of samples
     */
    public void setSamples(long samples) {
        this.samples = samples;
    }

    /**
     * Sets the Bit Depth of the audio.
     * @param bd bit depth
     * @see #bitDepthFromNumber(int) Method for convert int number to Enum value
     */
    public void setBitDepth(BitDepth bd) {
        bitDepth = bd;
    }

    /**
     * Converts the int value to a valid {@link BitDepth} value
     * or returns null if is not valid.
     * @param dp number of depth bits
     * @return a {@link BitDepth} value or null
     */
    static public BitDepth bitDepthFromNumber(int dp) {
        switch(dp) {
            case 8: return BitDepth.UINT8;
            case 16: return BitDepth.INT16;
            case 24: return BitDepth.INT24;
            case 32: return BitDepth.FLOAT32;
            case 0x80000018: return BitDepth.INT24_NOPACKED;
            case 0x8018: return BitDepth.INT24_NOPACKED;
            default: return null;
        }
    }
}
