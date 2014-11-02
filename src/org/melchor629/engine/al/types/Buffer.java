package org.melchor629.engine.al.types;

import static org.melchor629.engine.Game.al;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.objects.PCMData;

/**
 * Sound buffer
 * @author melchor9000
 */
public class Buffer {
	protected int buffer;
	
	public Buffer(PCMData data) {
		if(data == null || data.data == null || data.data.length == 0)
			throw new IllegalArgumentException("Cannot pass a null or empty PCM sound");
		buffer = al.genBuffer();
		al.bufferData(buffer, data.format, data.data, data.freq);
	}
	
	public int getBuffer() {
		return buffer;
	}
	
	public boolean isComplete() {
		int[] ret = new int[1];
		al.buffer(buffer, AL.Buffer.SIZE, ret);
		return ret[0] != 0;
	}
}
