package org.melchor629.engine.utils;

import org.lwjgl.system.MemoryUtil;

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

    /**
     * If {@code buffer} is {@code NULL}:
     * <p>
     *     Behaves like {@link #createByteBuffer(int)}
     * </p>
     * If {@code buffer} is not {@code NULL}:
     * <p>
     *     Changes the size of the memory block pointed by {@code buffer}
     *     maintaining the old data the same, and if is larger than before,
     *     the new part of memory contains undefined values. May move the
     *     memory into another part, so don't use the old {@code buffer}
     *     and use the returned one.
     * </p>
     * @param buffer memory block to resize
     * @param newSize new size of the memory block
     * @return a new buffer
     */
    public static ByteBuffer realloc(ByteBuffer buffer, int newSize) {
        return MemoryUtil.memRealloc(buffer, newSize);
    }
    /**
     * If {@code buffer} is {@code NULL}:
     * <p>
     *     Behaves like {@link #createShortBuffer(int)}
     * </p>
     * If {@code buffer} is not {@code NULL}:
     * <p>
     *     Changes the size of the memory block pointed by {@code buffer}
     *     maintaining the old data the same, and if is larger than before,
     *     the new part of memory contains undefined values. May move the
     *     memory into another part, so don't use the old {@code buffer}
     *     and use the returned one.
     * </p>
     * @param buffer memory block to resize
     * @param newSize new size of the memory block
     * @return a new buffer
     */
    public static ShortBuffer realloc(ShortBuffer buffer, int newSize) {
        return MemoryUtil.memRealloc(buffer, newSize);
    }
    /**
     * If {@code buffer} is {@code NULL}:
     * <p>
     *     Behaves like {@link #createIntBuffer(int)}
     * </p>
     * If {@code buffer} is not {@code NULL}:
     * <p>
     *     Changes the size of the memory block pointed by {@code buffer}
     *     maintaining the old data the same, and if is larger than before,
     *     the new part of memory contains undefined values. May move the
     *     memory into another part, so don't use the old {@code buffer}
     *     and use the returned one.
     * </p>
     * @param buffer memory block to resize
     * @param newSize new size of the memory block
     * @return a new buffer
     */
    public static IntBuffer realloc(IntBuffer buffer, int newSize) {
        return MemoryUtil.memRealloc(buffer, newSize);
    }
    /**
     * If {@code buffer} is {@code NULL}:
     * <p>
     *     Behaves like {@link #createLongBuffer(int)}
     * </p>
     * If {@code buffer} is not {@code NULL}:
     * <p>
     *     Changes the size of the memory block pointed by {@code buffer}
     *     maintaining the old data the same, and if is larger than before,
     *     the new part of memory contains undefined values. May move the
     *     memory into another part, so don't use the old {@code buffer}
     *     and use the returned one.
     * </p>
     * @param buffer memory block to resize
     * @param newSize new size of the memory block
     * @return a new buffer
     */
    public static LongBuffer realloc(LongBuffer buffer, int newSize) {
        return MemoryUtil.memRealloc(buffer, newSize);
    }
    /**
     * If {@code buffer} is {@code NULL}:
     * <p>
     *     Behaves like {@link #createFloatBuffer(int)}
     * </p>
     * If {@code buffer} is not {@code NULL}:
     * <p>
     *     Changes the size of the memory block pointed by {@code buffer}
     *     maintaining the old data the same, and if is larger than before,
     *     the new part of memory contains undefined values. May move the
     *     memory into another part, so don't use the old {@code buffer}
     *     and use the returned one.
     * </p>
     * @param buffer memory block to resize
     * @param newSize new size of the memory block
     * @return a new buffer
     */
    public static FloatBuffer realloc(FloatBuffer buffer, int newSize) {
        return MemoryUtil.memRealloc(buffer, newSize);
    }
    /**
     * If {@code buffer} is {@code NULL}:
     * <p>
     *     Behaves like {@link #createDoubleBuffer(int)}
     * </p>
     * If {@code buffer} is not {@code NULL}:
     * <p>
     *     Changes the size of the memory block pointed by {@code buffer}
     *     maintaining the old data the same, and if is larger than before,
     *     the new part of memory contains undefined values. May move the
     *     memory into another part, so don't use the old {@code buffer}
     *     and use the returned one.
     * </p>
     * @param buffer memory block to resize
     * @param newSize new size of the memory block
     * @return a new buffer
     */
    public static DoubleBuffer realloc(DoubleBuffer buffer, int newSize) {
        return MemoryUtil.memRealloc(buffer, newSize);
    }

    /**
     * Copies {@code bytes} bytes from {@code in} buffer starting at its current
     * position into {@code out} buffer starting at its current position.
     * @param in buffer from the data will come from
     * @param out buffer where the data will be copied to
     * @param bytes number of bytes to copy
     */
    public static void copy(ByteBuffer in, ByteBuffer out, int bytes) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(in), MemoryUtil.memAddress(out), bytes);
    }

    /**
     * Copies {@code n} elements from {@code in} buffer starting at its current
     * position into {@code out} buffer starting at its current position.
     * @param in buffer from the data will come from
     * @param out buffer where the data will be copied to
     * @param n number of elements to copy
     */
    public static void copy(ShortBuffer in, ShortBuffer out, int n) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(in), MemoryUtil.memAddress(out), n << 1);
    }

    /**
     * Copies {@code n} elements from {@code in} buffer starting at its current
     * position into {@code out} buffer starting at its current position.
     * @param in buffer from the data will come from
     * @param out buffer where the data will be copied to
     * @param n number of elements to copy
     */
    public static void copy(IntBuffer in, IntBuffer out, int n) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(in), MemoryUtil.memAddress(out), n << 2);
    }

    /**
     * Copies {@code n} elements from {@code in} buffer starting at its current
     * position into {@code out} buffer starting at its current position.
     * @param in buffer from the data will come from
     * @param out buffer where the data will be copied to
     * @param n number of elements to copy
     */
    public static void copy(FloatBuffer in, FloatBuffer out, int n) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(in), MemoryUtil.memAddress(out), n << 2);
    }

    /**
     * Copies {@code n} elements from {@code in} buffer starting at its current
     * position into {@code out} buffer starting at its current position.
     * @param in buffer from the data will come from
     * @param out buffer where the data will be copied to
     * @param n number of elements to copy
     */
    public static void copy(LongBuffer in, LongBuffer out, int n) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(in), MemoryUtil.memAddress(out), n << 3);
    }

    /**
     * Copies {@code n} elements from {@code in} buffer starting at its current
     * position into {@code out} buffer starting at its current position.
     * @param in buffer from the data will come from
     * @param out buffer where the data will be copied to
     * @param n number of elements to copy
     */
    public static void copy(DoubleBuffer in, DoubleBuffer out, int n) {
        MemoryUtil.memCopy(MemoryUtil.memAddress(in), MemoryUtil.memAddress(out), n << 3);
    }
}
