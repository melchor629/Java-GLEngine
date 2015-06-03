package org.melchor629.engine.gl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLContext;
import org.melchor629.engine.utils.BufferUtils;

import java.nio.*;

import static org.lwjgl.glfw.GLFW.*;
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
public class LWJGLRenderer implements Renderer {
	public long window;
    public GLContext context;
	boolean vsync = false;

    private GLFWErrorCallback errorCallback;
	
	public LWJGLRenderer() {
		Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            if(window != 0)
                destroyDisplay();
            glfwTerminate();
        }));

        glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                ByteBuffer native_str = org.lwjgl.system.MemoryUtil.memByteBufferNT1(description);
                String java_str = org.lwjgl.system.MemoryUtil.memDecodeUTF8(native_str);
                System.out.printf("GLFW [ERROR %d]: %s\n", error, java_str);
            }
        });

        if(glfwInit() == 0)
            throw new IllegalStateException("Could not initialize GLFW");
        glfwDefaultWindowHints();
	}

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#createDisplay(short, short, boolean, java.lang.String)
     */
    @Override
    public boolean createDisplay(int width, int height, boolean fullscreen, String title) {
        //glfwInit();
        long fourth = fullscreen ? glfwGetPrimaryMonitor() : 0l;
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 3);
        if(org.lwjgl.LWJGLUtil.getPlatform().getName().equalsIgnoreCase("macosx"))
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        window = glfwCreateWindow(width, height, title, fourth, 0l);
        if(window != 0)
        	glfwMakeContextCurrent(window);
        //else
        	//glfwTerminate();
        context = GLContext.createFromCurrent();
        context.setupDebugMessageCallback();
        return window != 0;
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#createDisplay(short, short, boolean, java.lang.String, org.melchor629.engine.gl.Renderer.GLVersion)
     */
    @Override
    public boolean createDisplay(int width, int height, boolean fullscreen, String title,
            GLVersion version) {
    	//glfwInit();
        long fourth = fullscreen ? glfwGetPrimaryMonitor() : 0l;
        if(version.a >= 3) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1);
        }
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, 1);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, version.a);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, version.b);
        window = glfwCreateWindow(width, height, title, fourth, 0l);
        if(window != 0)
        	glfwMakeContextCurrent(window);
        //else
        	//glfwTerminate();
        context = GLContext.createFromCurrent();
        context.setupDebugMessageCallback();
        return window != 0;
    }

    
    public void setVsync(boolean vsync) {
    	this.vsync = vsync;
        glfwSwapInterval(vsync ? 1 : 0);
    }

    public void setResizable(boolean resizable) {
        glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteDisplay()
     */
    @Override
    public void destroyDisplay() {
    	glfwDestroyWindow(window);
    	glfwMakeContextCurrent(0l);
        glfwTerminate();
        window = 0l;
    }

    public void _game_loop_sync(int fps) {
        glfwSwapBuffers(window);
        glfwPollEvents();
        //TODO Sync with FPS
    }

    public boolean windowIsClosing() {
        return glfwWindowShouldClose(window) == 1;
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#enable(org.melchor629.engine.gl.Renderer.GLEnable)
     */
    @Override
    public void enable(GLEnable enable) {
        glEnable(enable.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#disable(org.melchor629.engine.gl.Renderer.GLEnable)
     */
    @Override
    public void disable(GLEnable disable) {
        glDisable(disable.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#isEnabled(org.melchor629.engine.gl.Renderer.GLEnable)
     */
    @Override
    public boolean isEnabled(GLEnable enabled) {
        return glIsEnabled(enabled.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genVertexArray()
     */
    @Override
    public int genVertexArray() {
        return glGenVertexArrays();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genVertexArrays(int[])
     */
    @Override
    public void genVertexArrays(int[] buff) {
        for(short i = 0; i < buff.length; i++)
            buff[i] = genVertexArray();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteVertexArray(int)
     */
    @Override
    public void deleteVertexArray(int vao) {
        glDeleteVertexArrays(vao);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteVertexArrays(int[])
     */
    @Override
    public void deleteVertexArrays(int[] buff) {
        for (int aBuff : buff) deleteVertexArray(aBuff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bindVertexArray(int)
     */
    @Override
    public void bindVertexArray(int vao) {
        glBindVertexArray(vao);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genBuffer()
     */
    @Override
    public int genBuffer() {
        return glGenBuffers();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genBuffers(int[])
     */
    @Override
    public void genBuffers(int[] buff) {
        IntBuffer b = BufferUtils.createIntBuffer(buff.length);
        b.compact();
        glGenBuffers(b);
        b.get(buff).clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteBuffer(int)
     */
    @Override
    public void deleteBuffer(int vbo) {
        glDeleteBuffers(vbo);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteBuffers(int[])
     */
    @Override
    public void deleteBuffers(int[] ebo) {
        IntBuffer b = BufferUtils.createIntBuffer(ebo.length);
        b.put(ebo).compact();
        glDeleteBuffers(b);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bindBuffer(org.melchor629.engine.gl.Renderer.BufferTarget, int)
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
     * @see org.melchor629.engine.gl.Renderer#drawArrays(org.melchor629.engine.gl.Renderer.DrawMode, int, int)
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
     * @see org.melchor629.engine.gl.Renderer#drawElements(org.melchor629.engine.gl.Renderer.DrawMode, int, org.melchor629.engine.gl.Renderer.type, long)
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
     * @see org.melchor629.engine.gl.Renderer#createShader(org.melchor629.engine.gl.Renderer.ShaderType)
     */
    @Override
    public int createShader(ShaderType type) {
        return glCreateShader(shToInt(type));
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#createProgram()
     */
    @Override
    public int createProgram() {
        return glCreateProgram();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteShader(int)
     */
    @Override
    public void deleteShader(int shader) {
        glDeleteShader(shader);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteProgram(int)
     */
    @Override
    public void deleteProgram(int program) {
        glDeleteProgram(program);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#shaderSource(int, java.lang.String)
     */
    @Override
    public void shaderSource(int shader, String src) {
        glShaderSource(shader, src);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#compileShader(int)
     */
    @Override
    public void compileShader(int shader) {
        glCompileShader(shader);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#attachShader(int, int)
     */
    @Override
    public void attachShader(int program, int shader) {
        glAttachShader(program, shader);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#linkProgram(int)
     */
    @Override
    public void linkProgram(int program) {
        glLinkProgram(program);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#useProgram(int)
     */
    @Override
    public void useProgram(int program) {
        glUseProgram(program);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getShader(int, org.melchor629.engine.gl.Renderer.GLGetShader)
     */
    @Override
    public int getShader(int shader, Renderer.GLGetShader pName) {
        return glGetShaderi(shader, pName.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getShaderInfoLog(int)
     */
    @Override
    public String getShaderInfoLog(int shader) {
        int length = getShader(shader, Renderer.GLGetShader.INFO_LOG_LENGTH);
        return glGetShaderInfoLog(shader, length);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getProgram(int, org.melchor629.engine.gl.Renderer.GLGetShader)
     */
    @Override
    public int getProgram(int program, Renderer.GLGetProgram pName) {
        return glGetProgrami(program, pName.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getProgramInfoLog(int)
     */
    @Override
    public String getProgramInfoLog(int program) {
        int length = getProgram(program, GLGetProgram.INFO_LOG_LENGTH);
        return glGetProgramInfoLog(program, length);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getAttribLocation(int, java.lang.String)
     */
    @Override
    public int getAttribLocation(int program, String name) {
        return glGetAttribLocation(program, name);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#enableVertexAttribArray(int)
     */
    @Override
    public void enableVertexAttribArray(int loc) {
        glEnableVertexAttribArray(loc);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#vertexAttribPointer(int, int, org.melchor629.engine.gl.Renderer.type, boolean, int, long)
     */
    @Override
    public void vertexAttribPointer(int loc, int size, type type, boolean norm, int stride, long off) {
        glVertexAttribPointer(loc, size, type.e, norm, stride, off);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bindFragDataLocation(int, int, java.lang.String)
     */
    @Override
    public void bindFragDataLocation(int program, int colorNumber, String name) {
        glBindFragDataLocation(program, colorNumber, name);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getActiveAttrib(int, int, int)
     */
    @Override
    public String getActiveAttrib(int program, int pos, int strlen) {
        IntBuffer size, type;
        size = BufferUtils.createIntBuffer(1);
        type = BufferUtils.createIntBuffer(1);
        return glGetActiveAttrib(program, pos, strlen, size, type);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getActiveAttribSize(int, int, int)
     */
    @Override
    public int getActiveAttribSize(int program, int pos) {
        return -1;//glGetActiveAttribSize(program, pos); TODO
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getActiveAttribType(int, int, int)
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
     * @see org.melchor629.engine.gl.Renderer#getUniformLocation(int, java.lang.String)
     */
    @Override
    public int getUniformLocation(int program, String name) {
        return glGetUniformLocation(program, name);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getActiveUniform(int, int, int)
     */
    @Override
    public String getActiveUniform(int program, int pos, int strlen) {
        IntBuffer size, type;
        size = BufferUtils.createIntBuffer(1);
        type = BufferUtils.createIntBuffer(1);
        return glGetActiveUniform(program, pos, strlen, size, type);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform1f(int, float)
     */
    @Override
    public void uniform1f(int loc, float value) {
        glUniform1f(loc, value);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform1i(int, int)
     */
    @Override
    public void uniform1i(int loc, int value) {
        glUniform1i(loc, value);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#setActiveTexture(int)
     */
    @Override
    public void setActiveTexture(int num) {
        glActiveTexture(GL_TEXTURE0 + num);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getMaxTextureUnits()
     */
    @Override
    public short getMaxTextureUnits() {
        return (short) glGetInteger(GL_MAX_TEXTURE_IMAGE_UNITS);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genTexture()
     */
    @Override
    public int genTexture() {
        return glGenTextures();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genTextures(int[])
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
     * @see org.melchor629.engine.gl.Renderer#deleteTexture(int)
     */
    @Override
    public void deleteTexture(int tex) {
        glDeleteTextures(tex);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteTextures(int[])
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
     * @see org.melchor629.engine.gl.Renderer#bindTexture(org.melchor629.engine.gl.Renderer.TextureTarget, int)
     */
    @Override
    public void bindTexture(TextureTarget target, int tex) {
        glBindTexture(target.e, tex);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texParameteri(org.melchor629.engine.gl.Renderer.TextureTarget, int, int)
     */
    @Override
    public void texParameteri(TextureTarget target, Renderer.TextureParameter pName, int param) {
        glTexParameteri(target.e, pName.e, param);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texParameteri(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureWrap)
     */
    @Override
    public void texParameteri(TextureTarget target, Renderer.TextureParameter pName, TextureWrap p) {
        glTexParameteri(target.e, pName.e, p.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texParameteri(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFilter)
     */
    @Override
    public void texParameteri(TextureTarget target, Renderer.TextureParameter pName, TextureFilter p) {
        glTexParameteri(target.e, pName.e, p.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage1D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type)
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t) {
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, (ByteBuffer) null);
    }
            
    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage1D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, byte[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, byte[] b) {
        ByteBuffer buff = BufferUtils.createByteBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage1D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, short[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, short[] b) {
        ShortBuffer buff = BufferUtils.createShortBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage1D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, int[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, int[] b) {
        IntBuffer buff = BufferUtils.createIntBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage1D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, float[])
     */
    @Override
    public void texImage1D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int border, TextureExternalFormat efmt, type t, float[] b) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(b.length).put(b).compact();
        glTexImage1D(target.e, level, ifmt.e, width, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage1D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, double[])
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
     * @see org.melchor629.engine.gl.Renderer#texImage2D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type)
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t) {
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, (ByteBuffer) null);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage2D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, byte[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, byte[] b) {
        ByteBuffer buff = BufferUtils.createByteBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage2D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, short[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, short[] b) {
        ShortBuffer buff = BufferUtils.createShortBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage2D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, int[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, int[] b) {
        IntBuffer buff = BufferUtils.createIntBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage2D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, float[])
     */
    @Override
    public void texImage2D(TextureTarget target, int level, TextureFormat ifmt, int width,
            int height, int border, TextureExternalFormat efmt, type t, float[] b) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(b.length).put(b).compact();
        glTexImage2D(target.e, level, ifmt.e, width, height, border, efmt.e, t.e, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#texImage2D(org.melchor629.engine.gl.Renderer.TextureTarget, int, org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int, org.melchor629.engine.gl.Renderer.TextureExternalFormat, org.melchor629.engine.gl.Renderer.type, double[])
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

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genFramebuffer()
     */
    @Override
    public int genFramebuffer() {
        return glGenFramebuffers();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genFramebuffers(int[])
     */
    @Override
    public void genFramebuffers(int[] fbs) {
        IntBuffer b = BufferUtils.createIntBuffer(fbs.length).compact();
        glGenFramebuffers(b);
        b.get(fbs).clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteFramebuffer(int)
     */
    @Override
    public void deleteFramebuffer(int fb) {
        glDeleteFramebuffers(fb);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteFramebuffers(int[])
     */
    @Override
    public void deleteFramebuffers(int[] fbs) {
        IntBuffer b = BufferUtils.createIntBuffer(fbs.length).put(fbs).compact();
        glDeleteFramebuffers(b);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genRenderbuffer()
     */
    @Override
    public int genRenderbuffer() {
        return glGenRenderbuffers();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#genRenderbuffers(int[])
     */
    @Override
    public void genRenderbuffers(int[] fbs) {
        IntBuffer b = BufferUtils.createIntBuffer(fbs.length).compact();
        glGenRenderbuffers(b);
        b.get(fbs).clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteRenderbuffer(int)
     */
    @Override
    public void deleteRenderbuffer(int rb) {
        glDeleteRenderbuffers(rb);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#deleteRendebuffers(int[])
     */
    @Override
    public void deleteRendebuffers(int[] rbs) {
        IntBuffer b = BufferUtils.createIntBuffer(rbs.length).put(rbs).compact();
        glGenRenderbuffers(b);
        b.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bindFramebuffer(int)
     */
    @Override
    public void bindFramebuffer(int framebuffer) {
        glBindFramebuffer(GL_FRAMEBUFFER, framebuffer);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bindRenderbuffer(int)
     */
    @Override
    public void bindRenderbuffer(int framebuffer) {
        glBindRenderbuffer(GL_RENDERBUFFER, framebuffer);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#checkFramebufferStatus()
     */
    @Override
    public Renderer.FramebufferStatus checkFramebufferStatus() {
        int status = glCheckFramebufferStatus(GL_FRAMEBUFFER);
        switch(status) {
            case GL_FRAMEBUFFER_COMPLETE: return Renderer.FramebufferStatus.COMPLETE;
            case GL_FRAMEBUFFER_UNDEFINED: return Renderer.FramebufferStatus.UNDEFINED;
            case GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT: return Renderer.FramebufferStatus.INCOMPLETE_ATTACHMENT;
            case GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT:
                return Renderer.FramebufferStatus.INCOMPLETE_MISSING_ATTACHMENT;
            case GL_FRAMEBUFFER_INCOMPLETE_DRAW_BUFFER: return Renderer.FramebufferStatus.INCOMPLETE_DRAW_BUFFER;
            case GL_FRAMEBUFFER_INCOMPLETE_READ_BUFFER: return Renderer.FramebufferStatus.INCOMPLETE_READ_BUFFER;
            case GL_FRAMEBUFFER_UNSUPPORTED: return Renderer.FramebufferStatus.UNSUPPORTED;
            case GL_FRAMEBUFFER_INCOMPLETE_MULTISAMPLE: return Renderer.FramebufferStatus.INCOMPLETE_MULTISAMPLE;
            case GL_FRAMEBUFFER_INCOMPLETE_LAYER_TARGETS:
                return Renderer.FramebufferStatus.INCOMPLETE_LAYER_TARGETS;
            default: return Renderer.FramebufferStatus.UNDEFINED;
        }
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#framebufferTexture1D(org.melchor629.engine.gl.Renderer.FramebufferAttachment, org.melchor629.engine.gl.Renderer.TextureTarget, int, int)
     */
    @Override
    public void framebufferTexture1D(FramebufferAttachment at, TextureTarget tg, int tex, int level) {
        glFramebufferTexture1D(GL_FRAMEBUFFER, at.e, tg.e, tex, level);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#framebufferTexture2D(org.melchor629.engine.gl.Renderer.FramebufferAttachment, org.melchor629.engine.gl.Renderer.TextureTarget, int, int)
     */
    @Override
    public void framebufferTexture2D(FramebufferAttachment at, TextureTarget tg, int tex, int level) {
        glFramebufferTexture2D(GL_FRAMEBUFFER, at.e, tg.e, tex, level);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#framebufferTexture3D(org.melchor629.engine.gl.Renderer.FramebufferAttachment, org.melchor629.engine.gl.Renderer.TextureTarget, int, int, int)
     */
    @Override
    public void framebufferTexture3D(FramebufferAttachment at, TextureTarget tg, int tex,
            int level, int z) {
        glFramebufferTexture3D(GL_FRAMEBUFFER, at.e, tg.e, tex, level, z);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#framebufferRenderbuffer(org.melchor629.engine.gl.Renderer.FramebufferAttachment, int)
     */
    @Override
    public void framebufferRenderbuffer(FramebufferAttachment at, int rbo) {
        glFramebufferRenderbuffer(GL_FRAMEBUFFER, at.e, GL_RENDERBUFFER, rbo);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#renderbufferStorage(org.melchor629.engine.gl.Renderer.TextureFormat, int, int)
     */
    @Override
    public void renderbufferStorage(TextureFormat fmt, int width, int height) {
        glRenderbufferStorage(GL_RENDERBUFFER, fmt.e, width, height);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#renderbufferStorageMultisample(org.melchor629.engine.gl.Renderer.TextureFormat, int, int, int)
     */
    @Override
    public void renderbufferStorageMultisample(TextureFormat fmt, int samples, int w, int h) {
        glRenderbufferStorageMultisample(GL_RENDERBUFFER, samples, fmt.e, w, h);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#stencilFunc(org.melchor629.engine.gl.Renderer.StencilFunc, int, int)
     */
    @Override
    public void stencilFunc(StencilFunc func, int ref, int mask) {
        glStencilFunc(func.e, ref, mask);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#stencilOp(org.melchor629.engine.gl.Renderer.StencilOp, org.melchor629.engine.gl.Renderer.StencilOp, org.melchor629.engine.gl.Renderer.StencilOp)
     */
    @Override
    public void stencilOp(StencilOp sfail, StencilOp dfail, StencilOp pass) {
        glStencilOp(sfail.e, dfail.e, pass.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#stencilMask(int)
     */
    @Override
    public void stencilMask(int mask) {
        glStencilMask(mask);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#depthMask(boolean)
     */
    @Override
    public void depthMask(boolean a) {
        glDepthMask(a);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#clear(int)
     */
    @Override
    public void clear(int mask) {
        glClear(mask);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#clearColor(float, float, float, float)
     */
    @Override
    public void clearColor(float r, float g, float b, float a) {
        glClearColor(r, g, b, a);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#viewport(int, int, int, int)
     */
    @Override
    public void viewport(int x, int y, int width, int height) {
        glViewport(x, y, width, height);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#colorMask(boolean, boolean, boolean, boolean)
     */
    @Override
    public void colorMask(boolean r, boolean g, boolean b, boolean a) {
        glColorMask(r, g, b, a);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#cullFace(org.melchor629.engine.gl.Renderer.CullFaceMode)
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
     * @see org.melchor629.engine.gl.Renderer#getBoolean(org.melchor629.engine.gl.Renderer.GLGet)
     */
    @Override
    public boolean getBoolean(GLGet get) {
        return glGetBoolean(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getInt(org.melchor629.engine.gl.Renderer.GLGet)
     */
    @Override
    public int getInt(GLGet get) {
        return glGetInteger(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getLong(org.melchor629.engine.gl.Renderer.GLGet)
     */
    @Override
    public long getLong(GLGet get) {
        return glGetInteger64(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getFloat(org.melchor629.engine.gl.Renderer.GLGet)
     */
    @Override
    public float getFloat(GLGet get) {
        return glGetFloat(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getDouble(org.melchor629.engine.gl.Renderer.GLGet)
     */
    @Override
    public double getDouble(GLGet get) {
        return glGetDouble(get.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform2f(int, float, float)
     */
    @Override
    public void uniform2f(int loc, float v1, float v2) {
        glUniform2f(loc, v1, v2);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform2i(int, int, int)
     */
    @Override
    public void uniform2i(int loc, int v1, int v2) {
        glUniform2i(loc, v1, v2);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform3f(int, float, float, float)
     */
    @Override
    public void uniform3f(int loc, float v1, float v2, float v3) {
        glUniform3f(loc, v1, v2, v3);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform3i(int, int, int, int)
     */
    @Override
    public void uniform3i(int loc, int v1, int v2, int v3) {
        glUniform3i(loc, v1, v2, v3);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform4f(int, float, float, float, float)
     */
    @Override
    public void uniform4f(int loc, float v1, float v2, float v3, float v4) {
        glUniform4f(loc, v1, v2, v3, v4);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniform4i(int, int, int, int, int)
     */
    @Override
    public void uniform4i(int loc, int v1, int v2, int v3, int v4) {
        glUniform4i(loc, v1, v2, v3, v4);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniformMatrix2(int, boolean, float[])
     */
    @Override
    public void uniformMatrix2(int loc, boolean trans, float[] matrix) throws BufferUnderflowException {
        if(matrix.length != 2 * 2) throw new BufferUnderflowException();
        FloatBuffer buff = BufferUtils.createFloatBuffer(2 * 2).put(matrix).compact();
        glUniformMatrix2fv(loc, trans, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniformMatrix3(int, boolean, float[])
     */
    @Override
    public void uniformMatrix3(int loc, boolean trans, float[] matrix) throws BufferUnderflowException {
        if(matrix.length != 3 * 3) throw new BufferUnderflowException();
        FloatBuffer buff = BufferUtils.createFloatBuffer(3 * 3).put(matrix).compact();
        glUniformMatrix3fv(loc, trans, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#uniformMatrix4(int, boolean, float[])
     */
    @Override
    public void uniformMatrix4(int loc, boolean trans, float[] matrix) throws BufferUnderflowException {
        if(matrix.length != 4 * 4) throw new BufferUnderflowException();
        FloatBuffer buff = BufferUtils.createFloatBuffer(4 * 4).put(matrix);buff.flip();
        glUniformMatrix4fv(loc, trans, buff);
        buff.clear();
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getBufferParameteri(org.melchor629.engine.gl.Renderer.BufferTarget, org.melchor629.engine.gl.Renderer.GLGetBuffer)
     */
    @Override
    public int getBufferParameteri(BufferTarget target, GLGetBuffer param) {
        return glGetBufferParameteri(target.e, param.e);
    }
    

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getBufferParameteri64(org.melchor629.engine.gl.Renderer.BufferTarget, org.melchor629.engine.gl.Renderer.GLGetBuffer)
     */
    @Override
    public long getBufferParameteri64(BufferTarget target, GLGetBuffer param) {
        return glGetBufferParameteri64(target.e, param.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferData(org.melchor629.engine.gl.Renderer.BufferTarget, java.nio.ByteBuffer, org.melchor629.engine.gl.Renderer.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, ByteBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferData(org.melchor629.engine.gl.Renderer.BufferTarget, java.nio.ShortBuffer, org.melchor629.engine.gl.Renderer.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, ShortBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferData(org.melchor629.engine.gl.Renderer.BufferTarget, java.nio.IntBuffer, org.melchor629.engine.gl.Renderer.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, IntBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferData(org.melchor629.engine.gl.Renderer.BufferTarget, java.nio.FloatBuffer, org.melchor629.engine.gl.Renderer.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, FloatBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferData(org.melchor629.engine.gl.Renderer.BufferTarget, java.nio.DoubleBuffer, org.melchor629.engine.gl.Renderer.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, DoubleBuffer buff, BufferUsage usage) {
        glBufferData(target.e, buff, usage.e);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, byte[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, byte[] buff) {
        bufferSubData(target, offset, BufferUtils.createByteBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, short[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, short[] buff) {
        bufferSubData(target, offset, BufferUtils.createShortBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, int[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, int[] buff) {
        bufferSubData(target, offset, BufferUtils.createIntBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, float[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, float[] buff) {
        bufferSubData(target, offset, BufferUtils.createFloatBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, double[])
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, double[] buff) {
        bufferSubData(target, offset, BufferUtils.createDoubleBuffer(buff.length).put(buff).compact());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, java.nio.ByteBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, ByteBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, java.nio.ShortBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, ShortBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, java.nio.IntBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, IntBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, java.nio.FloatBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, FloatBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferSubData(org.melchor629.engine.gl.Renderer.BufferTarget, long, java.nio.DoubleBuffer)
     */
    @Override
    public void bufferSubData(BufferTarget target, long offset, DoubleBuffer buff) {
        glBufferSubData(target.e, offset, buff);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#getError()
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

    /* (non-Javadoc)
     * @see org.melchor629.engine.gl.Renderer#bufferData(org.melchor629.engine.gl.Renderer.BufferTarget, int, org.melchor629.engine.gl.Renderer.BufferUsage)
     */
    @Override
    public void bufferData(BufferTarget target, int count, BufferUsage usage) {
        glBufferData(target.e, count, usage.e);
    }

}
