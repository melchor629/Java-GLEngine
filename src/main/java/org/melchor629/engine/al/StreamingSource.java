package org.melchor629.engine.al;

import java.util.ArrayList;

/**
 * Source of a sound, with its position, gain, speed, and so...
 * <p>
 *     Source is an OpenAL Object that represents a Source of a sound. Its
 *     attributes defines how the source acts. The audio comes from one
 *     {@link Buffer} or a stream of {@link Buffer}s.
 * </p>
 * This version is for streaming audio
 * @author melchor9000
 */
public class StreamingSource extends Source {
    private ArrayList<Buffer> queuedBuffers = new ArrayList<>();

    StreamingSource(AL al) {
        super(al);
    }

    public int getBuffersQueued() {
        return al.getSourcei(source, AL.Source.BUFFERS_QUEUED);
    }

    public int getBuffersProcessed() {
        return al.getSourcei(source, AL.Source.BUFFERS_PROCESSED);
    }

    public void enqueueBuffer(Buffer buffer) {
        al.sourceQueueBuffers(source, buffer.buffer);
        queuedBuffers.add(buffer);
    }

    public void enqueueBuffers(Buffer... buffers) {
        int[] b = new int[buffers.length];
        for(int i = 0; i < buffers.length; i++) {
            b[i] = buffers[i].buffer;
            queuedBuffers.add(buffers[i]);
        }

        al.sourceQueueBuffers(source, b);
    }

    public void dequeueBuffers() {
        int n = getBuffersProcessed();
        int[] buffers = new int[n];

        al.sourceUnqueueBuffers(source, buffers);

        for(int i = 0; i < n; i++) {
            int pos = queuedBuffers.indexOf(new Buffer(buffers[i]));
            queuedBuffers.get(pos).delete();
            queuedBuffers.remove(pos);
        }
    }
}
