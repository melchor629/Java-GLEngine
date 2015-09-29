package org.melchor629.engine.loaders.audio.flac;

import org.melchor629.engine.clib.FlacLibraryNative;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;

import java.io.File;

/**
 * FLAC decoding class
 */
public class FlacDecoder extends AudioDecoder {
    private String file;
    private boolean forceMono = false;
    private FlacLibraryNative.PCMAttr attributes;
    private FlacLibraryNative.ShortBuffer data;

    /**
     * Uses internal engine force mono to force the sound data
     * to be mono, or not.
     * @param forceMono1 Value for force mono
     */
    public void setForceMono(boolean forceMono1) {
        forceMono = forceMono1;
    }

    /**
     * Decodes FLAC file
     */
    public void decode() {
        FlacLibraryNative.instance.engine_flac_decoder(file,
                (FlacLibraryNative.PCMAttr attr) -> attributes = attr,
                (FlacLibraryNative.ShortBuffer buff) -> data = buff,
                (int errCode, String msg) -> System.err.printf("[FlacDecoder] (%d) %s\n", errCode, msg), forceMono);
        container.setSamples(attributes.total_samples.longValue());
        container.setChannels(attributes.channels);
        container.setSampleRate(attributes.sample_rate);
        container.setBitDepth(AudioContainer.bitDepthFromNumber(attributes.bps));
        container.setBuffer(data.data.getByteBuffer(0, data.size));
        setCleanUpFunction((self) -> clear());
    }

    /**
     * Clear all memory stuff
     */
    private void clear() {
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

    @Deprecated
    public void readHeader() { }

    @Override
    protected void setFile(File file) {
        this.file = file.getAbsolutePath();
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("flac", FlacDecoder.class);
    }
}