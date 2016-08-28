package org.melchor629.engine.al;

import org.melchor629.engine.loaders.audio.AudioContainer;

/**
 * Source of a sound, with its position, gain, speed, and so...
 * <p>
 *     Source is an OpenAL Object that represents a Source of a sound. Its
 *     attributes defines how the source acts. The audio comes from one
 *     {@link Buffer} or a stream of {@link Buffer}s.
 * </p>
 * This version is for static audio
 * @author melchor9000
 */
public class StaticSource extends Source {
    private Buffer buffer;
    private int source;

    StaticSource(AL al, Buffer buffer0) {
        super(al);
        if(buffer0 == null || !buffer0.isComplete())
            throw new IllegalArgumentException("Cannot pass a null or incomplete buffer");
        buffer = buffer0;
        al.sourcei(source, AL.Source.BUFFER, buffer.getBuffer());
    }

    StaticSource(AL al, AudioContainer data) {
        this(al, new Buffer(al, data));
    }

    @Override
    public void delete() {
        super.delete();
        if(buffer != null)
            buffer.delete();
        buffer = null;
    }
}
