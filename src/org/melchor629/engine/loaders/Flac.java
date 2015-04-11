package org.melchor629.engine.loaders;

import org.melchor629.engine.clib.FlacLibraryNative;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Created by melchor9000 on 4/4/15.
 */
public class Flac {
    private String file;
    private boolean forceMono;
    private FlacLibraryNative.PCMAttr attributes;
    private FlacLibraryNative.ShortBuffer data;

    public Flac(String file) throws FileNotFoundException {
        this(file, false);
    }

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

    public boolean decode() {
        return FlacLibraryNative.instance.engine_flac_decoder(file,
                (FlacLibraryNative.PCMAttr attr) -> attributes = attr,
                (FlacLibraryNative.ShortBuffer buff) -> data = buff,
                (int errCode, String msg) -> System.err.printf("[Flac] (%d) %s\n", errCode, msg), forceMono);
    }

    public int getSampleRate() {
        return attributes.sample_rate;
    }

    public int getChannels() {
        return attributes.channels;
    }

    public int getBitsPerSample() {
        return attributes.bps;
    }

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
