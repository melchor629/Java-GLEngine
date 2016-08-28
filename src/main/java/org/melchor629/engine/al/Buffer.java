package org.melchor629.engine.al;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.loaders.audio.AudioContainer;

import java.nio.ShortBuffer;

/**
 * Sound buffer
 * <p>
 *     An OpenAL object that stores interleaved pcm sound in the device
 *     that can be used in any {@link Source} to be played
 * </p>
 * @author melchor9000
 */
public class Buffer implements Erasable {
	protected int buffer;
	private AL al;
	
	Buffer(AL al, AudioContainer data) {
        this.al = al;
		if(data == null)
			throw new IllegalArgumentException("Cannot pass a null or empty PCM sound");
		buffer = al.genBuffer();
        AL.Format fformat;
        if(data.getBitDepth() == AudioContainer.BitDepth.UINT8) {
            if (data.getChannels() == 2)
                fformat = AL.Format.STEREO8;
            else if (data.getChannels() == 1)
                fformat = AL.Format.MONO8;
            else
                throw new ALError("Unsupported number of channels (" + data.getChannels() + ")");
            //TODO comprobar si OpenAL soporta las extensiones con mas canales
        } else if(data.getBitDepth() == AudioContainer.BitDepth.INT16) {
            if (data.getChannels() == 2)
                fformat = AL.Format.STEREO16;
            else if (data.getChannels() == 1)
                fformat = AL.Format.MONO16;
            else
                throw new ALError("Unsupported number of channels (" + data.getChannels() + ")");
            //TODO comprobar si OpenAL soporta las extensiones con mas canales
        } else {
            throw new ALError("Unsupported bit depth (" + data.getBitDepth() + ")");
        }
		al.bufferData(buffer, fformat, data.getDataAsByte(), data.getSampleRate());
        al.addErasable(this);
    }

	Buffer(AL al, short[] data, AL.Format format, int freq) {
        this.al = al;
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
        al.addErasable(this);
	}

	Buffer(AL al, ShortBuffer data, AL.Format format, int freq) {
        this.al = al;
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
        al.addErasable(this);
	}

	Buffer(int buffer) {
	    this.buffer = buffer;
    }
	
	public int getBuffer() {
		return buffer;
	}
	
	public boolean isComplete() {
		return al.getBufferi(buffer, AL.Buffer.SIZE) != 0;
	}
	
	public void delete() {
	    if(buffer != 0)
		    al.deleteBuffer(buffer);
        buffer = 0;
	}

	@Override
    public int hashCode() {
		return 37 * buffer;
	}

	@Override
	public boolean equals(Object o) {
		return ((o instanceof Buffer) && ((Buffer) o).buffer == buffer);
	}
}
