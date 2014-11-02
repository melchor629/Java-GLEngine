package org.melchor629.engine.objects;

import org.melchor629.engine.al.AL;

/**
 * Stores information about a raw sound
 * @author melchor9000
 *
 */
public class PCMData {
	public int freq;
	public byte[] data;
	public AL.Format format;
}
