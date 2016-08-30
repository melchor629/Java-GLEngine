package org.melchor629.engine.utils;

import java.nio.*;

import static org.lwjgl.system.MemoryUtil.*;

/**
 * A nicer wrapper for {@link org.lwjgl.system.MemoryUtil} with a few
 * functions. This allocator <b>don't free</b> for you the resources,
 * you must call {@link #free(Buffer)} to free the resources. This
 * allocator uses native heap memory to store all data, so is not
 * limited by any Java options.
 * <br><br>Requires LWJGL
 * @see org.lwjgl.system.MemoryUtil MemoryUtil
 */
public class MemoryUtils {

    /**
     * Creates a new buffer with {@code size} bytes. This memory
     * has undefined values, but is fast to create.
     * <br><br>You must call {@link #free(Buffer)} when you are done
     * with the buffer.
     * @param size number of bytes to store in the buffer
     * @return a new buffer
     */
    public static ByteBuffer createByteBuffer(int size) {
        return memAlloc(size);
    }

    /**
     * Creates a new buffer with {@code size*2} bytes. This memory
     * has undefined values, but is fast to create.
     * <br><br>You must call {@link #free(Buffer)} when you are done
     * with the buffer.
     * @param size number of shorts to store in the buffer
     * @return a new buffer
     */
    public static ShortBuffer createShortBuffer(int size) {
        return memAllocShort(size);
    }

    /**
     * Creates a new buffer with {@code size*4} bytes. This memory
     * has undefined values, but is fast to create.
     * <br><br>You must call {@link #free(Buffer)} when you are done
     * with the buffer.
     * @param size number of ints to store in the buffer
     * @return a new buffer
     */
    public static IntBuffer createIntBuffer(int size) {
        return memAllocInt(size);
    }

    /**
     * Creates a new buffer with {@code size*4} bytes. This memory
     * has undefined values, but is fast to create.
     * <br><br>You must call {@link #free(Buffer)} when you are done
     * with the buffer.
     * @param size number of floats to store in the buffer
     * @return a new buffer
     */
    public static FloatBuffer createFloatBuffer(int size) {
        return memAllocFloat(size);
    }

    /**
     * Creates a new buffer with {@code size*8} bytes. This memory
     * has undefined values, but is fast to create.
     * <br><br>You must call {@link #free(Buffer)} when you are done
     * with the buffer.
     * @param size number of doubles to store in the buffer
     * @return a new buffer
     */
    public static DoubleBuffer createDoubleBuffer(int size) {
        return memAllocDouble(size);
    }

    /**
     * Creates a new buffer with {@code size} bytes. This memory
     * has undefined values, but is fast to create.
     * <br><br>You must call {@link #free(Buffer)} when you are done
     * with the buffer.
     * @param size number of longs to store in the buffer
     * @return a new buffer
     */
    public static LongBuffer createLongBuffer(int size) {
        return memAllocLong(size);
    }

    /**
     * Converts a byte buffer to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static ByteBuffer toBuffer(byte[] data) {
        return createByteBuffer(data.length).put(data);
    }

    /**
     * Converts a short buffer to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static ShortBuffer toBuffer(short[] data) {
        return createShortBuffer(data.length).put(data);
    }

    /**
     * Converts a int buffer to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static IntBuffer toBuffer(int[] data) {
        return createIntBuffer(data.length).put(data);
    }

    /**
     * Converts a float buffer to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static FloatBuffer toBuffer(float[] data) {
        return createFloatBuffer(data.length).put(data);
    }

    /**
     * Converts a double buffer to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static DoubleBuffer toBuffer(double[] data) {
        return createDoubleBuffer(data.length).put(data);
    }

    /**
     * Converts a long buffer to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static LongBuffer toBuffer(long[] data) {
        return createLongBuffer(data.length).put(data);
    }

    /**
     * Converts a String to a native buffer
     * @param data data to convert to
     * @return a new buffer with the data
     */
    public static ByteBuffer toBuffer(String data) {
        return memASCII(data);
    }

    /**
     * Converts a C String to a Java UTF8 {@link String}
     * @param buffer string to convert to
     * @return the String representation
     */
    public static String fromCString(ByteBuffer buffer) {
        return memUTF8(buffer);
    }

    /**
     * Frees the {@code buffer} from the native heap. You must
     * call this method on every buffer created in this class to
     * avoid memory leaks.
     * @param buffer buffer to free
     */
    public static void free(Buffer buffer) {
        if(buffer == null) throw new NullPointerException("Cannot free null");
        memFree(buffer);
    }
}
