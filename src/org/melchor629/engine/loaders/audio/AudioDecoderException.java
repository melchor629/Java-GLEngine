package org.melchor629.engine.loaders.audio;

/**
 * Exception thrown when an error ocurred when decoding.
 * This exception has no be thrown if an IO exception ocurrs.
 */
public class AudioDecoderException extends RuntimeException {
    public AudioDecoderException(String msg) {
        super(msg);
    }
}
