package org.melchor629.engine.utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.nio.LongBuffer;
import java.nio.ShortBuffer;

/**
 * Some Buffer Utilities
 */
@Deprecated
public class BufferUtils {

    /**
     * Creates a ByteBuffer ready to use on native functions
     * @param size Size of the buffer in bytes
     * @return the buffer
     */
	public static ByteBuffer createByteBuffer(int size) {
		return ByteBuffer.allocateDirect(size)
				.order(ByteOrder.nativeOrder());
	}

    /**
     * Creates a ShortBuffer with the given capacity prepared
     * to be used on native functions
     * @param size Capacity of short elements
     * @return the buffer
     */
	public static ShortBuffer createShortBuffer(int size) {
		return createByteBuffer(size << 1).asShortBuffer();
	}

    /**
     * Creates an IntBuffer with the given capacity prepared
     * to be used on native functions
     * @param size Capacity of int elements
     * @return the buffer
     */
	public static IntBuffer createIntBuffer(int size) {
		return createByteBuffer(size << 2).asIntBuffer();
	}

    /**
     * Creates an LongBuffer with the given capacity prepared
     * to be used on native functions. Long size on Java sometimes
     * differs from native Long size.
     * @param size Capacity of long elements
     * @return the buffer
     */
	public static LongBuffer createLongBuffer(int size) {
		return createByteBuffer(size << 3).asLongBuffer();
	}

    /**
     * Creates a FloatBuffer with the given capacity prepared
     * to be used on native functions
     * @param size Capacity of float elements
     * @return the buffer
     */
	public static FloatBuffer createFloatBuffer(int size) {
		return createByteBuffer(size << 2).asFloatBuffer();
	}

    /**
     * Creates a DoubleBuffer with the given capacity prepared
     * to be used on native functions.
     * @param size Capacity of double elements
     * @return the buffer
     */
	public static DoubleBuffer createDoubleBuffer(int size) {
		return createByteBuffer(size << 3).asDoubleBuffer();
	}

    /**
     * Converts an array to a direct ByteBuffer
     * @param arr Array to be converted
     * @return the array as buffer
     */
	public static ByteBuffer toBuffer(byte[] arr) {
		ByteBuffer buff = createByteBuffer(arr.length);
		buff.put(arr);
		buff.compact();
		return buff;
	}

    /**
     * Converts an array to a direct ShortBuffer
     * @param arr Array to be converted
     * @return the array as buffer
     */
	public static ShortBuffer toBuffer(short[] arr) {
		return createShortBuffer(arr.length)
				.put(arr)
				.compact();
	}

    /**
     * Converts an array to a direct IntBuffer
     * @param arr Array to be converted
     * @return the array as buffer
     */
	public static IntBuffer toBuffer(int[] arr) {
		return createIntBuffer(arr.length)
				.put(arr)
				.compact();
	}

    /**
     * Converts an array to a direct LongBuffer
     * @param arr Array to be converted
     * @return the array as buffer
     */
	public static LongBuffer toBuffer(long[] arr) {
		return createLongBuffer(arr.length)
				.put(arr)
				.compact();
	}

    /**
     * Converts an array to a direct FloatBuffer
     * @param arr Array to be converted
     * @return the array as buffer
     */
	public static FloatBuffer toBuffer(float[] arr) {
		return createFloatBuffer(arr.length)
				.put(arr)
				.compact();
	}

    /**
     * Converts an array to a direct DoubleBuffer
     * @param arr Array to be converted
     * @return the array as buffer
     */
	public static DoubleBuffer toBuffer(double[] arr) {
		return createDoubleBuffer(arr.length)
				.put(arr)
				.compact();
	}

    /**
     * Fills an array with the values of the {@link ByteBuffer}. All values distinct
     * of 0 are considered as true.
     * @param buffer ByteBuffer with data
     * @param array Array to be filled
     */
	public static void fillArray(ByteBuffer buffer, boolean[] array) {
        for(int i = 0; i < buffer.capacity(); i++)
            array[i] = buffer.get(i) != 0;
    }
}
