package org.melchor629.engine.loaders.audio;

import org.melchor629.engine.utils.logger.Logger;
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
    private static Logger LOG = Logger.getLogger(AudioDecoder.class);
    protected AudioFormat format;

    static {
        //Curious method for loading decoders in runtime instead of hardcoded
        new Reflections("org.melchor629.engine.loaders.audio")
                .getSubTypesOf(AudioDecoder.class)
                .forEach(e -> {
                    try {
                        e.getConstructor().newInstance().addItselfToDecoders();
                    } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e1) {
                        LOG.throwable("Cannot add " + e.getName() + " to decoders", e1);
                    }
                });
    }

    protected AudioDecoder() {
        format = new AudioFormat();
    }

    /**
     * Opens the file and reads basic information from the file. When is called, {@link #format} is already created
     * and should be filled with this bit depth, samplerate, number of channels and number of samples.
     * @throws IOException if the file cannot be read
     * @throws AudioDecoderException if there's some other troubles
     */
    public abstract AudioFormat readHeader() throws IOException;

    /**
     * Starts decoding the stream and returns an {@link AudioPCM} with
     * everything decoded. It also deletes native resources.
     * @return decoded audio
     * @throws IOException if suddenly cannot read the stream
     * @throws AudioDecoderException if decoder has an internal error
     */
    public abstract AudioPCM decodeAll() throws IOException;

    /**
     * Decodes one frame and returns an {@link AudioPCM} with the
     * decoded in one frame. Deletes native resources when <i>end
     * of stream</i> is reached.
     * @return decoded audio
     * @throws IOException if suddenly cannot read the stream
     * @throws AudioDecoderException if decoder has an internal error
     * @see #delete() Only if you not plan to reach EOF
     */
    public abstract AudioPCM decodeOne() throws IOException;

    /**
     * Deletes all native resources only if EOF is not reached.
     * Otherwise will do nothing
     */
    public abstract void delete();

    /**
     * Returns the information and data about the pcm sound.
     * @return information or {@code null} if is not filled yet
     */
    public AudioFormat getAudioFormat() {
        return format;
    }

    /**
     * Before decoding, this method could be called passing the file to be decoded
     * @param file File to be decoded
     * @throws FileNotFoundException if the file is not found
     */
    protected abstract void setFile(File file) throws FileNotFoundException;

    protected int throwIfEOF(int size) throws IOException {
        if(size == -1) throw new EOFException("Unexpected EOF");
        return size;
    }

    /**
     * Implementations should add itself to decoders list when this method is called. To add itself just call
     * {@link #addDecoder(String, Class)}.
     * @see #addDecoder(String, Class)
     */
    protected abstract void addItselfToDecoders();

    /**
     * Static method that adds a class to the decoders list
     * @param fmt file extension supported by this decoder
     * @param self class of the decoder
     */
    static protected void addDecoder(String fmt, Class<? extends AudioDecoder> self) {
        decoders.put(fmt, self);
    }

    /**
     * Tries to instantiate a decoder from the list.
     * @param fmt extension of the file to be decoded
     * @return a new decoder instance for the file or null
     */
    static private AudioDecoder createDecoderForFormat(String fmt) {
        try {
            return decoders.get(fmt).newInstance();
        } catch (InstantiationException | IllegalAccessException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Create a new instance of a AudioDecoder to decodeAll the desired file. File must exist and have to be readable.
     * @param file file to be decoded
     * @return a instace of AudioDecoder or null
     * @throws FileNotFoundException If file is not readable or not exists
     */
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
