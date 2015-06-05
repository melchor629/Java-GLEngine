package org.melchor629.engine.gl.types;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.GLError;
import org.melchor629.engine.gl.Renderer;

/**
 * Class for manage all types of *BO (<i>Buffer Objects</i>) like VBO
 * (<i>Vertex Buffer Object</i>) or EBO (<i>Element Buffer Object</i>)
 * Incomplete, but 100% usable TODO (Buffers and glBufferSubData)
 * @author melchor9000
 */
public class BufferObject implements Erasable {
    protected int bo;
    protected Renderer.BufferTarget target;
    protected Renderer.BufferUsage usage;

    /**
     * Create and bind a Buffer object of type {@code target}. Binding the
     * BO, it fixes its type to the {@code target}.
     * @param target Type of the Buffer Object
     * @param usage Specifies the expected usage pattern of the data store, can be null (default STATIC_DRAW)
     */
    public BufferObject(Renderer.BufferTarget target, Renderer.BufferUsage usage) {
        this.target = target;
        this.usage = usage != null ? usage : Renderer.BufferUsage.STATIC_DRAW;
        bo = Game.gl.genBuffer();
        Game.gl.bindBuffer(target, bo);

        Game.erasableList.add(this);
    }

    /**
     * Binds this BO
     * @throws GLError If you try to bind when BO was deleted
     */
    public void bind() {
        if(bo != -1)
            Game.gl.bindBuffer(target, bo);
        else
            throw new GLError("glBindBuffer", "Cannot bind a deleted buffer");
    }

    /**
     * Unbinds this BO. Shortcut of {@code glBindBuffer(target, 0) }
     */
    public void unbind() {
        Game.gl.bindBuffer(target, 0);
    }

    /**
     * Deletes this BO. If you deleted before, this method
     * won't complain (safe call).
     */
    public void delete() {
        if(bo == -1) return;
        Game.gl.deleteBuffer(bo);
        bo = -1;
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as bytes (1 byte)
     */
    public void fillBuffer(byte[] buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as shorts (2 bytes)
     */
    public void fillBuffer(short[] buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as ints (3 bytes)
     */
    public void fillBuffer(int[] buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as ints (3 bytes)
     */
    public void fillBuffer(IntBuffer buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as floats (32 bit floating number)
     */
    public void fillBuffer(float[] buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as floats (32 bit floating number)
     */
    public void fillBuffer(FloatBuffer buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }

    /**
     * Fill the buffer object with data from {@code buff}. And also binds
     * this BO for more intuitive coding ;)
     * @param buff Data to be copied as double (64 bit floating number)
     */
    public void fillBuffer(double[] buff) {
        bind();
        Game.gl.bufferData(target, buff, usage);
    }
    
    public void initPartialFillBuffer(int count) {
        bind();
        Game.gl.bufferData(target, count, usage);
    }
    
    /**
     * Partially fills the buffer object with data from {@code buff}, starting
     * from the {@code offset}.
     * @param offset Position where start copying the data
     * @param buff Data to be copied as int (32 bit number)
     */
    public void partiallyFillBuffer(long offset, IntBuffer buff) {
        bind();
        Game.gl.bufferSubData(target, offset, buff);
    }  
    
    /**
     * Partially fills the buffer object with data from {@code buff}, starting
     * from the {@code offset}.
     * @param offset Position where start copying the data
     * @param buff Data to be copied as float (32 bit floating number)
     */
    public void partiallyFillBuffer(long offset, FloatBuffer buff) {
        bind();
        Game.gl.bufferSubData(target, offset, buff);
    }

    /**
     * Ask GPU to tell the size of this buffer object
     * @return the size of the buffer object, measured in bytes
     */
    public long getBufferSize() {
        return Game.gl.getBufferParameteri64(target, Renderer.GLGetBuffer.BUFFER_SIZE);
    }

    /**
     * @return Usage pattern of the data store of this buffer object.
     */
    public Renderer.BufferUsage getBufferUsage() {
        return usage;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        delete();
    }
}
