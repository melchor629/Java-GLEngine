package org.melchor629.engine.gl;

import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Configuration;
import org.lwjgl.system.Platform;
import org.lwjgl.system.libffi.Closure;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.Matrix2;
import org.melchor629.engine.utils.math.Matrix3;
import org.melchor629.engine.utils.math.Matrix4;

import java.nio.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.glTexImage3D;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL32.*;
import static org.lwjgl.opengl.GL33.*;

//import static org.lwjgl.opengl.GL21.*;

/**
 * Class for Render with LWJGL
 * @author melchor9000
 */
public class LWJGLGLContext implements GLContext {
    private org.lwjgl.opengl.GLCapabilities context;
    private Closure debugClosure;

    LWJGLGLContext(boolean core) {
        context = org.lwjgl.opengl.GL.createCapabilities(core && Platform.get() == Platform.MACOSX);
        debugClosure = GLUtil.setupDebugMessageCallback();
        Configuration.DEBUG.set(true);
    }

    @Override
    public void destroyContext() {
        if(debugClosure != null)
            debugClosure.free();
    }

    @Override
    public boolean hasCapability(String name) {
        boolean ret = false;
        try {
            ret = context.getClass().getField(name).getBoolean(context);
        } catch(NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#enable(org.melchor629.engine.gl.GLContext.GLEnable)
     */
    @Override
    public void enable(GLEnable enable) {
        glEnable(enable.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#disable(org.melchor629.engine.gl.GLContext.GLEnable)
     */
    @Override
    public void disable(GLEnable disable) {
        glDisable(disable.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#isEnabled(org.melchor629.engine.gl.GLContext.GLEnable)
     */
    @Override
    public boolean isEnabled(GLEnable enabled) {
        return glIsEnabled(enabled.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genVertexArray()
     */
    @Override
    public int genVertexArray() {
        return glGenVertexArrays();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genVertexArrays(int[])
     */
    @Override
    public void genVertexArrays(int[] buff) {
        for(short i = 0; i < buff.length; i++)
            buff[i] = genVertexArray();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteVertexArray(int)
     */
    @Override
    public void deleteVertexArray(int vao) {
        glDeleteVertexArrays(vao);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteVertexArrays(int[])
     */
    @Override
    public void deleteVertexArrays(int[] buff) {
        for (int aBuff : buff) deleteVertexArray(aBuff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bindVertexArray(int)
     */
    @Override
    public void bindVertexArray(int vao) {
        glBindVertexArray(vao);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genBuffer()
     */
    @Override
    public int genBuffer() {
        return glGenBuffers();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genBuffers(int[])
     */
    @Override
    public void genBuffers(int[] buff) {
        IntBuffer b = BufferUtils.createIntBuffer(buff.length);
        b.compact();
        glGenBuffers(b);
        b.get(buff).clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteBuffer(int)
     */
    @Override
    public void deleteBuffer(int vbo) {
        glDeleteBuffers(vbo);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteBuffers(int[])
     */
    @Override
    public void deleteBuffers(int[] ebo) {
        IntBuffer b = BufferUtils.createIntBuffer(ebo.length);
        b.put(ebo).compact();
        glDeleteBuffers(b);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bindBuffer(org.melchor629.engine.gl.GLContext.BufferTarget, int)
     */
    @Override
    public void bindBuffer(BufferTarget target, int bo) {
        glBindBuffer(target.e, bo);
    }

    @Override
    public void bufferData(BufferTarget target, byte[] buff, BufferUsage usage) {
        ByteBuffer data = BufferUtils.createByteBuffer(buff.length);
        data.put(buff).compact();
        glBufferData(target.e, data, usage.e);
        data.clear();
    }

    @Override
    public void bufferData(BufferTarget target, short[] buff, BufferUsage usage) {
        ShortBuffer data = BufferUtils.createShortBuffer(buff.length);
        data.put(buff).compact();
        glBufferData(target.e, data, usage.e);
        data.clear();
    }

    @Override
    public void bufferData(BufferTarget target, int[] buff, BufferUsage usage) {
        IntBuffer data = BufferUtils.createIntBuffer(buff.length);
        data.put(buff).compact();
        glBufferData(target.e, data, usage.e);
        data.clear();
    }

    @Override
    public void bufferData(BufferTarget target, float[] buff, BufferUsage usage) {
        FloatBuffer data = BufferUtils.createFloatBuffer(buff.length);
        data.put(buff).compact();
        glBufferData(target.e, data, usage.e);
        data.clear();
    }

    @Override
    public void bufferData(BufferTarget target, double[] buff, BufferUsage usage) {
        DoubleBuffer data = BufferUtils.createDoubleBuffer(buff.length);
        data.put(buff).compact();
        glBufferData(target.e, data, usage.e);
        data.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#drawArrays(org.melchor629.engine.gl.GLContext.DrawMode, int, int)
     */
    @Override
    public void drawArrays(DrawMode mode, int first, int count) {
        glDrawArrays(mode.e, first, count);
    }

    @Override
    public void drawArraysInstanced(DrawMode mode, int first, int count, int times) {
        glDrawArraysInstanced(mode.e, first, count, times);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#drawElements(org.melchor629.engine.gl.GLContext.DrawMode, int, org.melchor629.engine.gl.GLContext.type, long)
     */
    @Override
    public void drawElements(DrawMode mode, int length, type type, long offset) {
        glDrawElements(mode.e, length, type.e, offset);
    }

    @Override
    public void drawElementsInstanced(DrawMode mode, int length, type type, long offset, int times) {
        glDrawElementsInstanced(mode.e, length, type.e, offset, times);
    }

    protected int shToInt(ShaderType type) {
        switch(type) {
            case VERTEX:
                return GL_VERTEX_SHADER;
            case FRAGMENT:
                return GL_FRAGMENT_SHADER;
            case GEOMETRY:
                return GL_GEOMETRY_SHADER;
            default:
                return -1;
        }
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#createShader(org.melchor629.engine.gl.GLContext.ShaderType)
     */
    @Override
    public int createShader(ShaderType type) {
        return glCreateShader(shToInt(type));
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#createProgram()
     */
    @Override
    public int createProgram() {
        return glCreateProgram();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteShader(int)
     */
    @Override
    public void deleteShader(int shader) {
        glDeleteShader(shader);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteProgram(int)
     */
    @Override
    public void deleteProgram(int program) {
        glDeleteProgram(program);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#shaderSource(int, java.lang.String)
     */
    @Override
    public void shaderSource(int shader, String src) {
        glShaderSource(shader, src);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#compileShader(int)
     */
    @Override
    public void compileShader(int shader) {
        glCompileShader(shader);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#attachShader(int, int)
     */
    @Override
    public void attachShader(int program, int shader) {
        glAttachShader(program, shader);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#linkProgram(int)
     */
    @Override
    public void linkProgram(int program) {
        glLinkProgram(program);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#useProgram(int)
     */
    @Override
    public void useProgram(int program) {
        glUseProgram(program);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getShader(int, org.melchor629.engine.gl.GLContext.GLGetShader)
     */
    @Override
    public int getShader(int shader, GLContext.GLGetShader pName) {
        return glGetShaderi(shader, pName.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getShaderInfoLog(int)
     */
    @Override
    public String getShaderInfoLog(int shader) {
        int length = getShader(shader, GLContext.GLGetShader.INFO_LOG_LENGTH);
        return glGetShaderInfoLog(shader, length);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getProgram(int, org.melchor629.engine.gl.GLContext.GLGetShader)
     */
    @Override
    public int getProgram(int program, GLContext.GLGetProgram pName) {
        return glGetProgrami(program, pName.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getProgramInfoLog(int)
     */
    @Override
    public String getProgramInfoLog(int program) {
        int length = getProgram(program, GLGetProgram.INFO_LOG_LENGTH);
        return glGetProgramInfoLog(program, length);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getAttribLocation(int, java.lang.String)
     */
    @Override
    public int getAttribLocation(int program, String name) {
        return glGetAttribLocation(program, name);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#enableVertexAttribArray(int)
     */
    @Override
    public void enableVertexAttribArray(int loc) {
        glEnableVertexAttribArray(loc);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#vertexAttribPointer(int, int, org.melchor629.engine.gl.GLContext.type, boolean, int, long)
     */
    @Override
    public void vertexAttribPointer(int loc, int size, type type, boolean norm, int stride, long off) {
        glVertexAttribPointer(loc, size, type.e, norm, stride, off);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bindFragDataLocation(int, int, java.lang.String)
     */
    @Override
    public void bindFragDataLocation(int program, int colorNumber, String name) {
        glBindFragDataLocation(program, colorNumber, name);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getActiveAttrib(int, int, int)
     */
    @Override
    public String getActiveAttrib(int program, int pos, int strlen) {
        IntBuffer size, type;
        size = BufferUtils.createIntBuffer(1);
        type = BufferUtils.createIntBuffer(1);
        return glGetActiveAttrib(program, pos, strlen, size, type);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getActiveAttribSize(int, int, int)
     */
    @Override
    public int getActiveAttribSize(int program, int pos) {
        return -1;//glGetActiveAttribSize(program, pos); TODO
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getActiveAttribType(int, int, int)
     */
    @Override
    public int getActiveAttribType(int program, int pos) {
        return -1;//glGetActiveAttribType(program, pos); TODO
    }

    @Override
    public void vertexAttribDivisor(int loc, int divisor) {
        glVertexAttribDivisor(loc, divisor);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getUniformLocation(int, java.lang.String)
     */
    @Override
    public int getUniformLocation(int program, String name) {
        return glGetUniformLocation(program, name);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getActiveUniform(int, int, int)
     */
    @Override
    public String getActiveUniform(int program, int pos, int strlen) {
        IntBuffer size, type;
        size = BufferUtils.createIntBuffer(1);
        type = BufferUtils.createIntBuffer(1);
        return glGetActiveUniform(program, pos, strlen, size, type);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform1f(int, float)
     */
    @Override
    public void uniform1f(int loc, float value) {
        glUniform1f(loc, value);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform1i(int, int)
     */
    @Override
    public void uniform1i(int loc, int value) {
        glUniform1i(loc, value);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#setActiveTexture(int)
     */
    @Override
    public void setActiveTexture(int num) {
        glActiveTexture(GL_TEXTURE0 + num);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getMaxTextureUnits()
     */
    @Override
    public short getMaxTextureUnits() {
        return (short) glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genTexture()
     */
    @Override
    public int genTexture() {
        return glGenTextures();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genTextures(int[])
     */
    @Override
    public void genTextures(int[] texs) {
        IntBuffer b = BufferUtils.createIntBuffer(texs.length);
        b.compact();
        glGenTextures(b);
        b.get(texs);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteTexture(int)
     */
    @Override
    public void deleteTexture(int tex) {
        glDeleteTextures(tex);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteTextures(int[])
     */
    @Override
    public void deleteTextures(int[] texs) {
        IntBuffer b = BufferUtils.createIntBuffer(texs.length);
        b.put(texs).compact();
        glDeleteTextures(b);
        b.clear();
    }

    @Override
    public void generateMipmap(TextureTarget t) {
        glGenerateMipmap(t.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bindTexture(org.melchor629.engine.gl.GLContext.TextureTarget, int)
     */
    @Override
    public void bindTexture(TextureTarget target, int tex) {
        glBindTexture(target.e, tex);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texParameteri(org.melchor629.engine.gl.GLContext.TextureTarget, int, int)
     */
    @Override
    public void texParameteri(TextureTarget target, GLContext.TextureParameter pName, int param) {
        glTexParameteri(target.e, pName.e, param);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texParameteri(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureWrap)
     */
    @Override
    public void texParameteri(TextureTarget target, GLContext.TextureParameter pName, TextureWrap p) {
        glTexParameteri(target.e, pName.e, p.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texParameteri(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFilter)
     */
    @Override
    public void texParameteri(TextureTarget target, GLContext.TextureParameter pName, TextureFilter p) {
        glTexParameteri(target.e, pName.e, p.e);
    }

    @Override
    public void copyTexImage1D(TextureTarget t, int level, TextureFormat ifmt, int x, int y, int width) {
        glCopyTexImage1D(t.e, level, ifmt.e, x, y, width, 0);
    }

    @Override
    public void copyTexImage2D(TextureTarget t, int level, TextureFormat ifmt, int x, int y, int width, int height) {
        glCopyTexImage2D(t.e, level, ifmt.e, x, y, width, height, 0);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage1D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type)
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, (ByteBuffer) null);
    }
            
    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage1D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, byte[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, byte[] b) {
        ByteBuffer buff = BufferUtils.createByteBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage1D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, short[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, short[] b) {
        ShortBuffer buff = BufferUtils.createShortBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage1D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, int[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, int[] b) {
        IntBuffer buff = BufferUtils.createIntBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage1D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, float[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, float[] b) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage1D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, double[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, double[] b) {
        DoubleBuffer buff = BufferUtils.createDoubleBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, ByteBuffer b) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, ShortBuffer b) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, IntBuffer b) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, FloatBuffer b) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width, int border, TextureExternalFormat efmt, type t, DoubleBuffer b) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, b);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage2D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type)
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, (ByteBuffer) null);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage2D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, byte[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, byte[] b) {
        ByteBuffer buff = BufferUtils.createByteBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage2D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, short[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, short[] b) {
        ShortBuffer buff = BufferUtils.createShortBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage2D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, int[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, int[] b) {
        IntBuffer buff = BufferUtils.createIntBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage2D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, float[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, float[] b) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#texImage2D(org.melchor629.engine.gl.GLContext.TextureTarget, int, org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int, org.melchor629.engine.gl.GLContext.TextureExternalFormat, org.melchor629.engine.gl.GLContext.type, double[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, double[] b) {
        DoubleBuffer buff = BufferUtils.createDoubleBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, ByteBuffer b) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, ShortBuffer b) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, IntBuffer b) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, FloatBuffer b) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int border, TextureExternalFormat efmt, type t, DoubleBuffer b) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int depth, int border, TextureExternalFormat efmt, type t) {
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, (ByteBuffer) null);
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int depth, int border, TextureExternalFormat efmt, type t, byte[] b) {
        ByteBuffer buff = BufferUtils.createByteBuffer(b.length).put(b).compact();
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int depth, int border, TextureExternalFormat efmt, type t, short[] b) {
        ShortBuffer buff = BufferUtils.createShortBuffer(b.length).put(b).compact();
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int depth, int border, TextureExternalFormat efmt, type t, int[] b) {
        IntBuffer buff = BufferUtils.createIntBuffer(b.length).put(b).compact();
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int depth, int border, TextureExternalFormat efmt, type t, float[] b) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(b.length).put(b).compact();
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int depth, int border, TextureExternalFormat efmt, type t, double[] b) {
        DoubleBuffer buff = BufferUtils.createDoubleBuffer(b.length).put(b).compact();
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, buff);
        buff.clear();
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, ByteBuffer b) {
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, ShortBuffer b) {
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, IntBuffer b) {
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, FloatBuffer b) {
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, b);
    }

    @Override
    public void texImage3D(TextureTarget target, int level, TextureFormat ifmt, int width, int height, int depth, int border, TextureExternalFormat efmt, type t, DoubleBuffer b) {
        glTexImage3D(target.e, level, ifmt.e, width, height, depth, border, efmt.e, t.e, b);
    }

    @Override
    public void texSubImage2D(TextureTarget target, int level, int xoffset, int yoffset, int width, int height, TextureExternalFormat efmt, type t, ByteBuffer b) {
        glTexSubImage2D(target.e, level, xoffset, yoffset, width, height, efmt.e, t.e, b);
    }

    @Override
    public void texSubImage2D(TextureTarget target, int level, int xoffset, int yoffset, int width, int height, TextureExternalFormat efmt, type t, ShortBuffer b) {
        glTexSubImage2D(target.e, level, xoffset, yoffset, width, height, efmt.e, t.e, b);
    }

    @Override
    public void texSubImage2D(TextureTarget target, int level, int xoffset, int yoffset, int width, int height, TextureExternalFormat efmt, type t, IntBuffer b) {
        glTexSubImage2D(target.e, level, xoffset, yoffset, width, height, efmt.e, t.e, b);
    }

    @Override
    public void texSubImage2D(TextureTarget target, int level, int xoffset, int yoffset, int width, int height, TextureExternalFormat efmt, type t, FloatBuffer b) {
        glTexSubImage2D(target.e, level, xoffset, yoffset, width, height, efmt.e, t.e, b);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genFramebuffer()
     */
    @Override
    public int genFramebuffer() {
        return glGenFramebuffers();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genFramebuffers(int[])
     */
    @Override
    public void genFramebuffers(int[] fbs) {
        IntBuffer b = BufferUtils.createIntBuffer(fbs.length).compact();
        glGenFramebuffers(b);
        b.get(fbs).clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteFramebuffer(int)
     */
    @Override
    public void deleteFramebuffer(int fb) {
        glDeleteFramebuffers(fb);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteFramebuffers(int[])
     */
    @Override
    public void deleteFramebuffers(int[] fbs) {
        IntBuffer b = BufferUtils.createIntBuffer(fbs.length).put(fbs).compact();
        glDeleteFramebuffers(b);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genRenderbuffer()
     */
    @Override
    public int genRenderbuffer() {
        return glGenRenderbuffers();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#genRenderbuffers(int[])
     */
    @Override
    public void genRenderbuffers(int[] fbs) {
        IntBuffer b = BufferUtils.createIntBuffer(fbs.length).compact();
        glGenRenderbuffers(b);
        b.get(fbs).clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteRenderbuffer(int)
     */
    @Override
    public void deleteRenderbuffer(int rb) {
        glDeleteRenderbuffers(rb);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#deleteRendebuffers(int[])
     */
    @Override
    public void deleteRendebuffers(int[] rbs) {
        IntBuffer b = BufferUtils.createIntBuffer(rbs.length).put(rbs).compact();
        glGenRenderbuffers(b);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bindFramebuffer(int)
     */
    @Override
    public void bindFramebuffer(int framebuffer) {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bindRenderbuffer(int)
     */
    @Override
    public void bindRenderbuffer(int framebuffer) {
        glBindRenderbuffer(GL_RENDERBUFFER, framebuffer);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#checkFramebufferStatus()
     */
    @Override
    public GLContext.FramebufferStatus checkFramebufferStatus() {
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        switch(status) {
            case GL_FRAMEBUFFER_COMPLETE: return GLContext.FramebufferStatus.COMPLETE;
            case GL_FRAMEBUFFER_UNDEFINED: return GLContext.FramebufferStatus.UNDEFINED;
            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: return GLContext.FramebufferStatus.INCOMPLETE_ATTACHMENT;
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                return GLContext.FramebufferStatus.INCOMPLETE_MISSING_ATTACHMENT;
            case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER: return GLContext.FramebufferStatus.INCOMPLETE_DRAW_BUFFER;
            case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER: return GLContext.FramebufferStatus.INCOMPLETE_READ_BUFFER;
            case GL_FRAMEBUFFER_UNSUPPORTED: return GLContext.FramebufferStatus.UNSUPPORTED;
            case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE: return GLContext.FramebufferStatus.INCOMPLETE_MULTISAMPLE;
            case GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS:
                return GLContext.FramebufferStatus.INCOMPLETE_LAYER_TARGETS;
            default: return GLContext.FramebufferStatus.UNDEFINED;
        }
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#framebufferTexture1D(org.melchor629.engine.gl.GLContext.FramebufferAttachment, org.melchor629.engine.gl.GLContext.TextureTarget, int, int)
     */
    @Override
    public void framebufferTexture1D(FramebufferAttachment at, TextureTarget tg, int tex, int level) {
        glFramebufferTexture1D(GL_FRAMEBUFFER, at.e, tg.e, tex, level);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#framebufferTexture2D(org.melchor629.engine.gl.GLContext.FramebufferAttachment, org.melchor629.engine.gl.GLContext.TextureTarget, int, int)
     */
    @Override
    public void framebufferTexture2D(FramebufferAttachment at, TextureTarget tg, int tex, int level) {
        glFramebufferTexture2D(GL_FRAMEBUFFER, at.e, tg.e, tex, level);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#framebufferTexture3D(org.melchor629.engine.gl.GLContext.FramebufferAttachment, org.melchor629.engine.gl.GLContext.TextureTarget, int, int, int)
     */
    @Override
    public void framebufferTexture3D(FramebufferAttachment at, TextureTarget tg, int tex,
            int level, int z) {
        glFramebufferTexture3D(GL_FRAMEBUFFER, at.e, tg.e, tex, level, z);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#framebufferRenderbuffer(org.melchor629.engine.gl.GLContext.FramebufferAttachment, int)
     */
    @Override
    public void framebufferRenderbuffer(FramebufferAttachment at, int rbo) {
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, at.e, GL_RENDERBUFFER, rbo);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#renderbufferStorage(org.melchor629.engine.gl.GLContext.TextureFormat, int, int)
     */
    @Override
    public void renderbufferStorage(TextureFormat fmt, int width, int height) {
        glRenderbufferStorage(GL_RENDERBUFFER, fmt.e, width, height);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#renderbufferStorageMultisample(org.melchor629.engine.gl.GLContext.TextureFormat, int, int, int)
     */
    @Override
    public void renderbufferStorageMultisample(TextureFormat fmt, int samples, int w, int h) {
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, fmt.e, w, h);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#stencilFunc(org.melchor629.engine.gl.GLContext.StencilFunc, int, int)
     */
    @Override
    public void stencilFunc(StencilFunc func, int ref, int mask) {
        glStencilFunc(func.e, ref, mask);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#stencilOp(org.melchor629.engine.gl.GLContext.StencilOp, org.melchor629.engine.gl.GLContext.StencilOp, org.melchor629.engine.gl.GLContext.StencilOp)
     */
    @Override
    public void stencilOp(StencilOp sfail, StencilOp dfail, StencilOp pass) {
        glStencilOp(sfail.e, dfail.e, pass.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#stencilMask(int)
     */
    @Override
    public void stencilMask(int mask) {
        glStencilMask(mask);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#depthMask(boolean)
     */
    @Override
    public void depthMask(boolean a) {
        glDepthMask(a);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#clear(int)
     */
    @Override
    public void clear(int mask) {
        glClear(mask);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#clearColor(float, float, float, float)
     */
    @Override
    public void clearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#viewport(int, int, int, int)
     */
    @Override
    public void viewport(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#colorMask(boolean, boolean, boolean, boolean)
     */
    @Override
    public void colorMask(boolean r, boolean g, boolean b, boolean a) {
        glColorMask(r, g, b, a);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#cullFace(org.melchor629.engine.gl.GLContext.CullFaceMode)
     */
    @Override
    public void cullFace(CullFaceMode mode) {
        int m;
        switch(mode) {
            case FRONT: m = GL_FRONT; break;
            case BACK: m = GL_BACK; break;
            case FRONT_AND_BACK: m = GL_FRONT_AND_BACK; break;
            default: m = -1;
        }
        glCullFace(m);
    }

    @Override
    public void blendFunc(BlendOption sourceFactor, BlendOption destinationFactor) {
        glBlendFunc(sourceFactor.e, destinationFactor.e);
    }

    @Override
    public void blendFuncSeparate(BlendOption rgbSrcFactor, BlendOption rgbDestFactor, BlendOption aSrcFactor, BlendOption aDestFactor) {
        glBlendFuncSeparate(rgbSrcFactor.e, rgbDestFactor.e, aSrcFactor.e, aDestFactor.e);
    }

    @Override
    public void blendEquation(BlendEquation eq) {
        glBlendEquation(eq.e);
    }

    @Override
    public void blendEquationSeparate(BlendEquation colorEq, BlendEquation alphaEq) {
        glBlendEquationSeparate(colorEq.e, alphaEq.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getBoolean(org.melchor629.engine.gl.GLContext.GLGet)
     */
    @Override
    public boolean getBoolean(GLGet get) {
        return glGetBoolean(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getInt(org.melchor629.engine.gl.GLContext.GLGet)
     */
    @Override
    public int getInt(GLGet get) {
        return glGetInteger(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getLong(org.melchor629.engine.gl.GLContext.GLGet)
     */
    @Override
    public long getLong(GLGet get) {
        return glGetInteger64(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getFloat(org.melchor629.engine.gl.GLContext.GLGet)
     */
    @Override
    public float getFloat(GLGet get) {
        return glGetFloat(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getDouble(org.melchor629.engine.gl.GLContext.GLGet)
     */
    @Override
    public double getDouble(GLGet get) {
        return glGetDouble(get.e);
    }

    @Override
    public void getBoolean(GLGet get, boolean[] v) {
        ByteBuffer b = BufferUtils.createByteBuffer(v.length);
        glGetBooleanv(get.e, b);
        BufferUtils.fillArray(b, v);
    }

    @Override
    public void getInt(GLGet get, int[] v) {
        IntBuffer b = BufferUtils.createIntBuffer(v.length);
        glGetIntegerv(get.e, b);
        b.get(v);
    }

    @Override
    public void getLong(GLGet get, long[] v) {
        LongBuffer b = BufferUtils.createLongBuffer(v.length);
        glGetInteger64v(get.e, b);
        b.get(v);
    }

    @Override
    public void getFloat(GLGet get, float[] v) {
        FloatBuffer b = BufferUtils.createFloatBuffer(v.length);
        glGetFloatv(get.e, b);
        b.get(v);
    }

    @Override
    public void getDouble(GLGet get, double[] v) {
        DoubleBuffer b = BufferUtils.createDoubleBuffer(v.length);
        glGetDoublev(get.e, b);
        b.get(v);
    }

    @Override
    public void readBuffer(CullFaceMode mode) {
        glReadBuffer(mode == CullFaceMode.FRONT ? GL_FRONT : GL_BACK);
    }

    @Override
    public void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, ByteBuffer data) {
        glReadPixels(x, y, width, height, fmt.e, type.e, data);
    }

    @Override
    public void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, ShortBuffer data) {
        glReadPixels(x, y, width, height, fmt.e, type.e, data);
    }

    @Override
    public void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, IntBuffer data) {
        glReadPixels(x, y, width, height, fmt.e, type.e, data);
    }

    @Override
    public void readPixels(int x, int y, int width, int height, TextureFormat fmt, type type, FloatBuffer data) {
        glReadPixels(x, y, width, height, fmt.e, type.e, data);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform2f(int, float, float)
     */
    @Override
    public void uniform2f(int loc, float v1, float v2) {
        glUniform2f(loc, v1, v2);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform2i(int, int, int)
     */
    @Override
    public void uniform2i(int loc, int v1, int v2) {
        glUniform2i(loc, v1, v2);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform3f(int, float, float, float)
     */
    @Override
    public void uniform3f(int loc, float v1, float v2, float v3) {
        glUniform3f(loc, v1, v2, v3);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform3i(int, int, int, int)
     */
    @Override
    public void uniform3i(int loc, int v1, int v2, int v3) {
        glUniform3i(loc, v1, v2, v3);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform4f(int, float, float, float, float)
     */
    @Override
    public void uniform4f(int loc, float v1, float v2, float v3, float v4) {
        glUniform4f(loc, v1, v2, v3, v4);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniform4i(int, int, int, int, int)
     */
    @Override
    public void uniform4i(int loc, int v1, int v2, int v3, int v4) {
        glUniform4i(loc, v1, v2, v3, v4);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniformMatrix2(int, boolean, float[])
     */
    @Override
    public void uniformMatrix2(int loc, boolean trans, float[] matrix) throws BufferUnderflowException {
        if(matrix.length != 2 * 2) throw new BufferUnderflowException();
        FloatBuffer buff = BufferUtils.createFloatBuffer(2 * 2).put(matrix).compact();
        glUniformMatrix2fv(loc, trans, buff);
        buff.clear();
    }

    @Override
    public void uniformMatrix2(int loc, Matrix2 matrix) {
        uniformMatrix2(loc, false, GLM.matrixAsArray(matrix));
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniformMatrix3(int, boolean, float[])
     */
    @Override
    public void uniformMatrix3(int loc, boolean trans, float[] matrix) throws BufferUnderflowException {
        if(matrix.length != 3 * 3) throw new BufferUnderflowException();
        FloatBuffer buff = BufferUtils.createFloatBuffer(3 * 3).put(matrix).compact();
        glUniformMatrix3fv(loc, trans, buff);
        buff.clear();
    }

    @Override
    public void uniformMatrix3(int loc, Matrix3 matrix) {
        uniformMatrix3(loc, false, GLM.matrixAsArray(matrix));
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#uniformMatrix4(int, boolean, float[])
     */
    @Override
    public void uniformMatrix4(int loc, boolean trans, float[] matrix) throws BufferUnderflowException {
        if(matrix.length != 4 * 4) throw new BufferUnderflowException();
        FloatBuffer buff = BufferUtils.createFloatBuffer(4 * 4).put(matrix);buff.flip();
        glUniformMatrix4fv(loc, trans, buff);
        buff.clear();
    }

    @Override
    public void uniformMatrix4(int loc, Matrix4 matrix) {
        uniformMatrix4(loc, false, GLM.matrixAsArray(matrix));
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getBufferParameteri(org.melchor629.engine.gl.GLContext.BufferTarget, org.melchor629.engine.gl.GLContext.GLGetBuffer)
     */
    @Override
    public int getBufferParameteri(BufferTarget target, GLGetBuffer param) {
        return glGetBufferParameteri(target.e, param.e);
    }
    

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getBufferParameteri64(org.melchor629.engine.gl.GLContext.BufferTarget, org.melchor629.engine.gl.GLContext.GLGetBuffer)
     */
    @Override
    public long getBufferParameteri64(BufferTarget target, GLGetBuffer param) {
        return glGetBufferParameteri64(target.e, param.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferData(org.melchor629.engine.gl.GLContext.BufferTarget, java.nio.ByteBuffer, org.melchor629.engine.gl.GLContext.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, ByteBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferData(org.melchor629.engine.gl.GLContext.BufferTarget, java.nio.ShortBuffer, org.melchor629.engine.gl.GLContext.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, ShortBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferData(org.melchor629.engine.gl.GLContext.BufferTarget, java.nio.IntBuffer, org.melchor629.engine.gl.GLContext.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, IntBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferData(org.melchor629.engine.gl.GLContext.BufferTarget, java.nio.FloatBuffer, org.melchor629.engine.gl.GLContext.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, FloatBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferData(org.melchor629.engine.gl.GLContext.BufferTarget, java.nio.DoubleBuffer, org.melchor629.engine.gl.GLContext.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, DoubleBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, byte[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, byte[] buff) {
        bufferSubData(target, offset, BufferUtils.createByteBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, short[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, short[] buff) {
        bufferSubData(target, offset, BufferUtils.createShortBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, int[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, int[] buff) {
        bufferSubData(target, offset, BufferUtils.createIntBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, float[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, float[] buff) {
        bufferSubData(target, offset, BufferUtils.createFloatBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, double[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, double[] buff) {
        bufferSubData(target, offset, BufferUtils.createDoubleBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, java.nio.ByteBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, ByteBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, java.nio.ShortBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, ShortBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, java.nio.IntBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, IntBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, java.nio.FloatBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, FloatBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferSubData(org.melchor629.engine.gl.GLContext.BufferTarget, long, java.nio.DoubleBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, DoubleBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#getError()
     */
    @Override
    public Error getError() {
        int errno = glGetError();
        Error error;
        
        switch(errno) {
            case GL_INVALID_ENUM:
                error = Error.INVALID_ENUM;
                break;
            case GL_INVALID_VALUE:
                error = Error.INVALID_VALUE;
                break;
            case GL_INVALID_OPERATION:
                error = Error.INVALID_OPERATION;
                break;
            case GL_INVALID_FRAMEBUFFER_OPERATION:
                error = Error.INVALID_FRAMEBUFFER_OPERATION;
                break;
            case GL_OUT_OF_MEMORY:
                error = Error.OUT_OF_MEMORY;
                break;
            case GL_STACK_OVERFLOW:
                error = Error.STACK_OVERFLOW;
                break;
            case GL_STACK_UNDERFLOW:
                error = Error.STACK_UNDERFLOW;
                break;
            default:
                error = Error.NO_ERROR;
        }
        
        return error;
    }

    @Override
    public BufferObject createBufferObject(BufferTarget target, BufferUsage usage) {
        return new BufferObject(this, target, usage);
    }

    @Override
    public FrameBuffer createFrameBuffer() {
        return new FrameBuffer(this);
    }

    @Override
    public RenderBuffer createRenderBuffer(TextureFormat fmt, int width, int height) {
        return new RenderBuffer(this, fmt, width, height);
    }

    @Override
    public VertexArrayObject createVertexArrayObject() {
        return new VertexArrayObject(this);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.GLContext#bufferData(org.melchor629.engine.gl.GLContext.BufferTarget, int, org.melchor629.engine.gl.GLContext.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, int count, BufferUsage usage) {
        glBufferData(target.e, count, usage.e);
    }

}
