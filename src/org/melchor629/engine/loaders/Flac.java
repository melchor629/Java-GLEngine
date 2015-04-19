package org.melchor629.engine.loaders;

import org.melchor629.engine.clib.FlacLibraryNative;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * FLAC decoding/encoding class
 */
public class Flac {
    private String file;
    private boolean forceMono;
    private FlacLibraryNative.PCMAttr attributes;
    private FlacLibraryNative.ShortBuffer data;

    /**
     * Creates an instance of the FLAC encoder/decoder with a file
     * @param file File to read/write
     * @throws FileNotFoundException if the file doesn't exist
     */
    public Flac(String file) throws FileNotFoundException {
        this(file, false);
    }

    /**
     * Creates an instance of the FLAC encoder/decoder with a file. Force
     * Mono param is, as its name indicates, to force a stereo sound to be mono.
     * @param file File to read/write
     * @param forceMono Force mono
     * @throws FileNotFoundException if the fuke doesn't exist
     */
    public Flac(String file, boolean forceMono) throws FileNotFoundException {
        this.forceMono = forceMono;
        File f = new File(file);
        if(f.exists()) {
            if(f.isFile()) {
                this.file = f.getAbsolutePath();
            } else {
                throw new FileNotFoundException("Is not a file:" + f.getAbsolutePath());
            }
        } else {
            throw new FileNotFoundException("File doesn't exists: " + f.getAbsolutePath());
        }
    }

    /**
     * Decodes FLAC file
     * @return true if could decode correctly the file
     */
    public boolean decode() {
        return FlacLibraryNative.instance.engine_flac_decoder(file,
                (FlacLibraryNative.PCMAttr attr) -> attributes = attr,
                (FlacLibraryNative.ShortBuffer buff) -> data = buff,
                (int errCode, String msg) -> System.err.printf("[Flac] (%d) %s\n", errCode, msg), forceMono);
    }

    /**
     * Gets the sample rate of the sound
     * @return the sample rate
     */
    public int getSampleRate() {
        return attributes.sample_rate;
    }

    /**
     * Gets the number of the channels of the sound
     * @return channels
     */
    public int getChannels() {
        return attributes.channels;
    }

    /**
     * Gets the number of bits per sample
     * @return bps
     */
    public int getBitsPerSample() {
        return attributes.bps;
    }

    /**
     * Gets the total number of samples
     * @return total samples
     */
    public long getTotalSamples() {
        return attributes.total_samples.longValue();
    }

    public java.nio.ShortBuffer getSampleData() {
        java.nio.ShortBuffer buff = null;
        if(data != null) {
            buff = data.data.getByteBuffer(0, data.size).asShortBuffer();
        }
        return buff;
    }

    /**
     * Clear all memory stuff
     */
    public void clear() {
        if(data != null) {
            data.clear.invoke(data);
            data.clear();
            data = null;
        }

        if(attributes != null) {
            attributes.clear();
            attributes = null;
        }
    }
}
