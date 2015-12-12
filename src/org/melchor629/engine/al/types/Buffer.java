package org.melchor629.engine.al.types;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.ALError;
import org.melchor629.engine.loaders.audio.AudioContainer;

import java.nio.ShortBuffer;

import static org.melchor629.engine.Game.al;

/**
 * Sound buffer
 * @author melchor9000
 */
public class Buffer implements Erasable {
	protected int buffer;
	
	public Buffer(AudioContainer data) {
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
        Game.erasableList.add(this);
    }

	public Buffer(short[] data, AL.Format format, int freq) {
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
        Game.erasableList.add(this);
	}

	public Buffer(ShortBuffer data, AL.Format format, int freq) {
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
        Game.erasableList.add(this);
	}

	public Buffer(byte[] data, AL.Format format, int freq) {
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
        Game.erasableList.add(this);
	}
	
	public int getBuffer() {
		return buffer;
	}
	
	public boolean isComplete() {
		return al.getBufferi(buffer, AL.Buffer.SIZE) != 0;
	}
	
	public void delete() {
		al.deleteBuffer(buffer);
	}
}
