package org.melchor629.engine.al.types;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.objects.PCMData;

import java.nio.ShortBuffer;

import static org.melchor629.engine.Game.al;

/**
 * Sound buffer
 * @author melchor9000
 */
public class Buffer {
	protected int buffer;
	
	public Buffer(PCMData data) {
		if(data == null || (data.data == null && data.data2 == null))
			throw new IllegalArgumentException("Cannot pass a null or empty PCM sound");
		buffer = al.genBuffer();
		if(data.data != null)
			al.bufferData(buffer, data.format, data.data, data.freq);
		else
			al.bufferData(buffer, data.format, data.data2, data.freq);
	}

	public Buffer(short[] data, AL.Format format, int freq) {
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
	}

	public Buffer(ShortBuffer data, AL.Format format, int freq) {
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
	}

	public Buffer(byte[] data, AL.Format format, int freq) {
		buffer = al.genBuffer();
		al.bufferData(buffer, format, data, freq);
	}
	
	public int getBuffer() {
		return buffer;
	}
	
	public boolean isComplete() {
		/*int[] ret = new int[1];
		al.buffer(buffer, AL.Buffer.SIZE, ret);
		return ret[0] != 0;*/
		return al.getBufferi(buffer, AL.Buffer.SIZE) != 0;
	}
	
	public void destroy() {
		al.deleteBuffer(buffer);
	}
}
