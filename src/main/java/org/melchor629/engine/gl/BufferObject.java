package org.melchor629.engine.gl;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.melchor629.engine.Erasable;

/**
 * Class for manage all types of *BO (<i>Buffer Objects</i>) like VBO
 * (<i>Vertex Buffer Object</i>) or EBO (<i>Element Buffer Object</i>)
 * <p>
 *     Buffer Object is an OpenGL Object that stores unformatted data in
 *     GPU's memory. This object can be a very frequently mutable object,
 *     or one time modified object. Can represent vertex, colors, pixels,
 *     and other kind of data.
 * </p>
 * @author melchor9000
 */
//TODO (Buffers and glBufferSubData)
public class BufferObject implements Erasable {
    private int bo;
    private GLContext.BufferTarget target;
    private GLContext.BufferUsage usage;
    private final GLContext gl;

    /**
     * Create and bind a Buffer object of type {@code target}. Binding the
     * BO, it fixes its type to the {@code target}.
     * @param gl a OpenGL Context
     * @param target Type of the Buffer Object
     * @param usage Specifies the expected usage pattern of the data store, can be null (default STATIC_DRAW)
     */
    BufferObject(GLContext gl, GLContext.BufferTarget target, GLContext.BufferUsage usage) {
        this.gl = gl;
        this.target = target;
        this.usage = usage != null ? usage : GLContext.BufferUsage.STATIC_DRAW;
        bo = gl.genBuffer();
        gl.bindBuffer(target, bo);

        gl.addErasable(this);
    }

    /**
     * Binds this BO
     * @throws GLError If you try to bind when BO was deleted
     */
    public void bind() {
        if(bo != -1)
            gl.bindBuffer(target, bo);
        else
            throw new GLError("glBindBuffer", "Cannot bind a deleted buffer");
    }

    /**
     * Unbinds this BO. Shortcut of {@code glBindBuffer(target, 0) }
     */
    public void unbind() {
        gl.bindBuffer(target, 0);
    }

    /**
     * Deletes this BO. If you deleted before, this method
     * won't complain (safe call).
     */
    public void delete() {
        if(bo == -1) return;
        gl.deleteBuffer(bo);
        bo = -1;
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as bytes (1 byte)
     */
    public void fillBuffer(byte[] buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as shorts (2 bytes)
     */
    public void fillBuffer(short[] buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as ints (3 bytes)
     */
    public void fillBuffer(int[] buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as ints (3 bytes)
     */
    public void fillBuffer(IntBuffer buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as floats (32 bit floating number)
     */
    public void fillBuffer(float[] buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as floats (32 bit floating number)
     */
    public void fillBuffer(FloatBuffer buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as double (64 bit floating number)
     */
    public void fillBuffer(double[] buff) {
        bind();
        gl.bufferData(target, buff, usage);
    }
    
    public void initPartialFillBuffer(int count) {
        bind();
        gl.bufferData(target, count, usage);
    }
    
    /**
     * Partially fills the buffer object with data from {@code buff}, starting
     * from the {@code offset}.
     * @param offset Position where start copying the data
     * @param buff Data to be copied as int (32 bit number)
     */
    public void partiallyFillBuffer(long offset, IntBuffer buff) {
        bind();
        gl.bufferSubData(target, offset, buff);
    }  
    
    /**
     * Partially fills the buffer object with data from {@code buff}, starting
     * from the {@code offset}.
     * @param offset Position where start copying the data
     * @param buff Data to be copied as float (32 bit floating number)
     */
    public void partiallyFillBuffer(long offset, FloatBuffer buff) {
        bind();
        gl.bufferSubData(target, offset, buff);
    }

    //TODO http://onrendering.blogspot.com.es/2011/10/buffer-object-streaming-in-opengl.html
    public void clearData() {}

    /**
     * Ask GPU to tell the size of this buffer object
     * @return the size of the buffer object, measured in bytes
     */
    public long getBufferSize() {
        return gl.getBufferParameteri64(target, GLContext.GLGetBuffer.BUFFER_SIZE);
    }

    /**
     * @return Usage pattern of the data store of this buffer object.
     */
    public GLContext.BufferUsage getBufferUsage() {
        return usage;
    }
}
