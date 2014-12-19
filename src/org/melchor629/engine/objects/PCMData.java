package org.melchor629.engine.objects;

import java.nio.ByteBuffer;

import org.melchor629.engine.al.AL;

/**
 * Stores information about a raw sound
 * @author melchor9000
 */
public class PCMData {
	public int freq;
	public byte[] data;
	public ByteBuffer data2;
	public AL.Format format;
}
