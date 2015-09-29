package org.melchor629.engine.loaders.audio;

import org.reflections.Reflections;

import java.io.EOFException;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * Superclass for Audio Decoders
 */
public abstract class AudioDecoder {
    private static Map<String, Class<? extends AudioDecoder>> decoders = new HashMap<>();
    protected AudioContainer container;

    static {
        new Reflections("org.melchor629.engine.loaders.audio")
                .getSubTypesOf(AudioDecoder.class)
                .forEach(e -> {
                    try {
                        e.getConstructor().newInstance().addItselfToDecoders();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                        System.err.println("Cannot add " + e.getName() + " to decoders");
                        e1.printStackTrace();
                    }
                });
    }

    protected AudioDecoder() {
        container = new AudioContainer();
    }

    public abstract void readHeader() throws IOException;

    public abstract void decode() throws IOException;

    public AudioContainer getAudioContainer() {
        return container;
    }

    protected abstract void setFile(File file) throws FileNotFoundException;

    protected int throwIfEOF(int size) throws IOException {
        if(size == -1) throw new EOFException("Unexpected EOF");
        return size;
    }

    protected void setCleanUpFunction(AudioContainer.CleanUpFunction cb) {
        container.cleanUpFunction = cb;
    }

    protected abstract void addItselfToDecoders();

    static protected void addDecoder(String fmt, Class<? extends AudioDecoder> self) {
        decoders.put(fmt, self);
    }

    static private AudioDecoder createDecoderForFormat(String fmt) {
        try {
            return decoders.get(fmt).newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            return null;
        }
    }

    static public AudioDecoder createDecoderForFile(File file) throws FileNotFoundException {
        if(!file.exists()) throw new FileNotFoundException("File must exist to be decoded");
        if(!file.canRead()) throw new FileNotFoundException("File cannot be read");
        String path = file.getAbsolutePath();
        AudioDecoder d = createDecoderForFormat(path.substring(path.lastIndexOf(".") + 1));
        if(d != null)
            d.setFile(file);
        return d;
    }
}
