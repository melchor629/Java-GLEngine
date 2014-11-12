package org.melchor629.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

public class BufferUtils {
	
	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size)
				.order(ByteOrder.nativeOrder());
	}
	
	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << 1).asShortBuffer();
	}
	
	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}
	
	public static LongBuffer createLongBuffer(int size) {
		return createByteBuffer(size << 3).asLongBuffer();
	}
	
	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}
	
	public static DoubleBuffer createDoubleBuffer(int size) {
		return createByteBuffer(size << 3).asDoubleBuffer();
	}

	public static ByteBuffer toBuffer(byte[] arr) {
		ByteBuffer buff = createByteBuffer(arr.length);
		buff.put(arr);
		buff.compact();
		return buff;
	}
	
	public static ShortBuffer toBuffer(short[] arr) {
		return createShortBuffer(arr.length)
				.put(arr)
				.compact();
	}
	
	public static IntBuffer toBuffer(int[] arr) {
		return createIntBuffer(arr.length)
				.put(arr)
				.compact();
	}
	
	public static LongBuffer toBuffer(long[] arr) {
		return createLongBuffer(arr.length)
				.put(arr)
				.compact();
	}
	
	public static FloatBuffer toBuffer(float[] arr) {
		return createFloatBuffer(arr.length)
				.put(arr)
				.compact();
	}
	
	public static DoubleBuffer toBuffer(double[] arr) {
		return createDoubleBuffer(arr.length)
				.put(arr)
				.compact();
	}
}
