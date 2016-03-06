package org.melchor629.engine.gl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.Platform;
import org.melchor629.engine.utils.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Window and context creator for LWJGL library, that uses GLFW.
 */
public class LWJGLWindow implements Window {
    public long window;
    public GLContext context;

    private ArrayList<OnWindowResizeEvent> resizeListeners;
    private ArrayList<OnFocusEvent> focusListeners;
    private ArrayList<OnBlurEvent> blurListeners;
    private GLFWWindowSizeCallback sizeCallback;
    private GLFWWindowFocusCallback focusCallback;
    private GLFWErrorCallback errorCallback;
    private boolean core;
    private ConcurrentLinkedQueue<Runnable> events;

    public LWJGLWindow() {
        if(glfwInit() == 0)
            throw new GLError("Could not initiate GLFW");
        glfwDefaultWindowHints();

        resizeListeners = new ArrayList<>();
        focusListeners = new ArrayList<>();
        blurListeners = new ArrayList<>();
        events = new ConcurrentLinkedQueue<>();
    }

    @Override
    public void setResizable(boolean resizable) {
        glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);
    }

    @Override
    public void setVisible(boolean visible) {
        glfwWindowHint(GLFW_VISIBLE, visible ? 1 : 0);
    }

    @Override
    public void setDecorated(boolean decorated) {
        glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
    }

    @Override
    public void setFocused(boolean focused) {
        glfwWindowHint(GLFW_FOCUSED, focused ? 1 : 0);
    }

    @Override
    public void setMultisample(boolean multisample, int samples) {
        glfwWindowHint(GLFW_SAMPLES, multisample ? samples : 0);
    }

    @Override
    public void setDoublebuffered(boolean doublebuffered) {
        glfwWindowHint(GLFW_DOUBLE_BUFFER, doublebuffered ? 1 : 0);
    }

    @Override
    public void setContextProfileAndVersion(OpenGLContextVersion version) {
        if(version.type == OpenGLContext.OPENGL_ES) {
            glfwWindowHint(GLFW_CLIENT_API, GLFW_OPENGL_ES_API);
        } else if(version.type == OpenGLContext.OPENGL_COMPAT) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_COMPAT_PROFILE);
        } else if(version.type == OpenGLContext.OPENGL_CORE) {
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            if(Platform.get() == Platform.MACOSX)
                glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, 1);
        }

        core = version.type == OpenGLContext.OPENGL_CORE;

        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, version.major);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, version.minor);
    }

    @Override
    public void createWindow(int width, int height, String title) {
        if((window = glfwCreateWindow(width, height, title, 0L, 0L)) == 0L)
            throw new GLError("Context cannot be created");
        setListenerCallbacks();
    }

    @Override
    public void createFullscreenWindow(int width, int height, String title) {
        if((window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), 0L)) == 0L)
            throw new GLError("Context cannot be created");
        setListenerCallbacks();
    }

    @Override
    public void createFullscreenWindow(String title) {
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        if((window = glfwCreateWindow(mode.width(), mode.height(), title, glfwGetPrimaryMonitor(), 0L)) == 0)
            throw new GLError("Context cannot be created");
        setListenerCallbacks();
    }

    @Override
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(window) == 1;
    }

    @Override
    public void setWindowShouldClose(boolean close) {
        glfwSetWindowShouldClose(window, close ? 1 : 0);
        glfwPostEmptyEvent();
    }

    @Override
    public void addResizeEventListener(OnWindowResizeEvent e) {
        resizeListeners.add(e);
    }

    @Override
    public void showWindow() {
        glfwShowWindow(window);
    }

    @Override
    public void hideWindow() {
        glfwHideWindow(window);
    }

    @Override
    public boolean isVisible() {
        return glfwGetWindowAttrib(window, GLFW_VISIBLE) == 1;
    }

    @Override
    public void setVsync(boolean vsync) {
        glfwSwapInterval(vsync ? 1 : 0);
    }

    @Override
    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    @Override
    public void syncGPU() {
        glfwSwapBuffers(window);
    }

    @Override
    public GLContext createContext() {
        glfwMakeContextCurrent(window);
        return context = new LWJGLGLContext(core);
    }

    @Override
    public void pollEvents() {
        glfwPollEvents();
        while(!events.isEmpty()) events.poll().run();
    }

    @Override
    public void waitEvents() {
        glfwWaitEvents();
        while(!events.isEmpty()) events.poll().run();
    }

    @Override
    public void postEvent(Runnable r) {
        events.add(r);
        glfwPostEmptyEvent();
    }

    @Override
    public void setOnFocusEventListener(OnFocusEvent e) {
        focusListeners.add(e);
    }

    @Override
    public void setOnBlurEventListener(OnBlurEvent e) {
        blurListeners.add(e);
    }

    @Override
    public double getDPI() {
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        glfwGetMonitorPhysicalSize(glfwGetPrimaryMonitor(), width, height);
        GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        return mode.width() / (width.get(0) / 25.4);
    }

    @Override
    public double getPixelScaleFactor() {
        return getFramebufferSize().width / getWindowSize().width;
    }

    @Override
    public Size getWindowSize() {
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        glfwGetWindowSize(window, width, height);
        return new Size(width.get(), height.get());
    }

    @Override
    public Size getFramebufferSize() {
        IntBuffer width = BufferUtils.createIntBuffer(1), height = BufferUtils.createIntBuffer(1);
        glfwGetFramebufferSize(window, width, height);
        return new Size(width.get(), height.get());
    }

    @Override
    public void destroyWindow() {
        context.destroyContext();
        glfwDestroyWindow(window);
        window = -1;
        resizeListeners.clear();
        focusListeners.clear();
        blurListeners.clear();
        sizeCallback.release();
        focusCallback.release();
        errorCallback.release();
        glfwTerminate();
    }

    private void setListenerCallbacks() {
        glfwSetWindowSizeCallback(window, sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int width, int height) {
                resizeListeners.forEach((l) -> l.invoke(width, height));
            }
        });

        glfwSetWindowFocusCallback(window, focusCallback = new GLFWWindowFocusCallback() {
            public void invoke(long window, int focused) {
                if(focused == 0)
                    blurListeners.forEach(OnBlurEvent::invoke);
                else
                    focusListeners.forEach(OnFocusEvent::invoke);
            }
        });

        glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                ByteBuffer native_str = org.lwjgl.system.MemoryUtil.memByteBufferNT1(description);
                String java_str = org.lwjgl.system.MemoryUtil.memDecodeUTF8(native_str);
                System.out.printf("GLFW [ERROR %d]: %s\n", error, java_str);
            }
        });
    }
}
