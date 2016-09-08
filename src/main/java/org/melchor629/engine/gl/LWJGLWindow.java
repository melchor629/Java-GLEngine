package org.melchor629.engine.gl;

import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.glfw.GLFWWindowFocusCallback;
import org.lwjgl.glfw.GLFWWindowSizeCallback;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.Platform;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.LWJGLKeyboard;
import org.melchor629.engine.input.LWJGLMouse;
import org.melchor629.engine.input.Mouse;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedQueue;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Window and context creator for LWJGL library, that uses GLFW.
 */
public class LWJGLWindow implements Window {
    private long window;
    private GLContext context;
    private static int createdWindows = 0;

    private ArrayList<OnWindowResizeEvent> resizeListeners;
    private ArrayList<OnFocusEvent> focusListeners;
    private ArrayList<OnBlurEvent> blurListeners;
    private GLFWWindowSizeCallback sizeCallback;
    private GLFWWindowFocusCallback focusCallback;
    private GLFWErrorCallback errorCallback;
    private boolean core;
    private ConcurrentLinkedQueue<Runnable> events;
    private LWJGLMouse mouse;
    private LWJGLKeyboard keyboard;

    private LWJGLWindow(long window, boolean core) {
        resizeListeners = new ArrayList<>();
        focusListeners = new ArrayList<>();
        blurListeners = new ArrayList<>();
        events = new ConcurrentLinkedQueue<>();
        this.window = window;
        this.core = core;
        setListenerCallbacks();
        createdWindows++;
    }

    @Override
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(window);
    }

    @Override
    public void setWindowShouldClose(boolean close) {
        glfwSetWindowShouldClose(window, close);
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
        if(r != null) {
            events.add(r);
        }
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
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.ints(0), height = stack.ints(0);
            glfwGetMonitorPhysicalSize(glfwGetPrimaryMonitor(), width, height);
            GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            return mode.width() / (width.get(0) / 25.4);
        }
    }

    @Override
    public double getPixelScaleFactor() {
        return getFramebufferSize().width / getWindowSize().width;
    }

    @Override
    public Size getWindowSize() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.ints(0), height = stack.ints(0);
            glfwGetWindowSize(window, width, height);
            return new Size(width.get(), height.get());
        }
    }

    @Override
    public Size getFramebufferSize() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer width = stack.ints(0), height = stack.ints(0);
            glfwGetFramebufferSize(window, width, height);
            return new Size(width.get(), height.get());
        }
    }

    @Override
    public void destroyWindow() {
        glfwDestroyWindow(window);
        window = -1;
        resizeListeners.clear();
        focusListeners.clear();
        blurListeners.clear();
        sizeCallback.free();
        focusCallback.free();
        errorCallback.free();
        createdWindows--;
        if(createdWindows == 0) glfwTerminate();
    }

    @Override
    public Mouse getMouseController() {
        return mouse;
    }

    @Override
    public Keyboard getKeyboardController() {
        return keyboard;
    }

    @Override
    public GLContext getGLContext() {
        return context;
    }

    @Override
    public GLContext makeContextOnCurrentThread() {
        glfwMakeContextCurrent(window);
        return context = new LWJGLGLContext(core);
    }

    @Override
    public void setWindowSizeLimit(Size min, Size max) {
        glfwSetWindowSizeLimits(window,
                min != null ? min.width : GLFW_DONT_CARE,
                min != null ? min.height : GLFW_DONT_CARE,
                max != null ? max.width : GLFW_DONT_CARE,
                max != null ? max.height : GLFW_DONT_CARE);
    }

    @Override
    public void setWindowSizeAspectRatio(int a, int b) {
        glfwSetWindowAspectRatio(window, a < 0 ? GLFW_DONT_CARE : a, b < 0 ? GLFW_DONT_CARE : b);
    }

    private void setListenerCallbacks() {
        mouse = new LWJGLMouse(window);
        keyboard = new LWJGLKeyboard(window);

        glfwSetWindowSizeCallback(window, sizeCallback = new GLFWWindowSizeCallback() {
            public void invoke(long window, int width, int height) {
                resizeListeners.forEach((l) -> l.invoke(width, height));
            }
        });

        glfwSetWindowFocusCallback(window, focusCallback = new GLFWWindowFocusCallback() {
            public void invoke(long window, boolean focused) {
                if(!focused)
                    blurListeners.forEach(OnBlurEvent::invoke);
                else
                    focusListeners.forEach(OnFocusEvent::invoke);
            }
        });

        glfwSetErrorCallback(errorCallback = new GLFWErrorCallback() {
            @Override
            public void invoke(int error, long description) {
                ByteBuffer native_str = org.lwjgl.system.MemoryUtil.memByteBufferNT1(description);
                String java_str = org.lwjgl.system.MemoryUtil.memUTF8(native_str);
                System.out.printf("GLFW [ERROR %d]: %s\n", error, java_str);
            }
        });
    }

    /**
     * Retrieves the valid default monitor sizes
     * @return valid monitor sizes
     */
    //TODO clase para manejar monitores varios
    public static Size[] monitorSizes() {
        if(!Builder.glfwInitialized) {
            if(!glfwInit())
                throw new RuntimeException("Could not initiate GLFW");
            Builder.glfwInitialized = true;
        }

        GLFWVidMode.Buffer modes = glfwGetVideoModes(glfwGetPrimaryMonitor());
        Size[] sizes = new Size[modes.capacity()];
        for(int i = 0; i < modes.capacity(); i++) {
            sizes[i] = new Size(modes.get(i).width(), modes.get(i).height());
        }
        return sizes;
    }

    public static class Builder implements WindowBuilder<LWJGLWindow> {
        private String title;
        private boolean core;
        private static boolean glfwInitialized;

        public Builder() {
            if(!glfwInitialized) {
                if(!glfwInit())
                    throw new RuntimeException("Could not initiate GLFW");
                glfwInitialized = true;
            }

            glfwDefaultWindowHints();
            glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, 1);
        }

        @Override
        public WindowBuilder setResizable(boolean resizable) {
            glfwWindowHint(GLFW_RESIZABLE, resizable ? 1 : 0);
            return this;
        }

        @Override
        public WindowBuilder setVisible(boolean visible) {
            glfwWindowHint(GLFW_VISIBLE, visible ? 1 : 0);
            return this;
        }

        @Override
        public WindowBuilder setDecorated(boolean decorated) {
            glfwWindowHint(GLFW_DECORATED, decorated ? 1 : 0);
            return this;
        }

        @Override
        public WindowBuilder setFocused(boolean focused) {
            glfwWindowHint(GLFW_FOCUSED, focused ? 1 : 0);
            return this;
        }

        @Override
        public WindowBuilder setMultisample(int samples) {
            glfwWindowHint(GLFW_SAMPLES, samples);
            return this;
        }

        @Override
        public WindowBuilder setDoubleBuffered(boolean doubleBuffered) {
            glfwWindowHint(GLFW_DOUBLEBUFFER, doubleBuffered ? 1 : 0);
            return this;
        }

        @Override
        public WindowBuilder setOpenGLContextVersion(OpenGLContextVersion version) {
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
            return this;
        }

        @Override
        public WindowBuilder setTitle(String title) {
            this.title = title;
            return this;
        }

        @Override
        public LWJGLWindow create(int width, int height) {
            long window = glfwCreateWindow(width, height, title, 0L, 0L);
            if(window == 0L) throw new GLError("Context cannot be created");
            return new LWJGLWindow(window, core);
        }

        @Override
        public LWJGLWindow createFullscreen() {
            GLFWVidMode mode = glfwGetVideoMode(glfwGetPrimaryMonitor());
            long window = glfwCreateWindow(mode.width(), mode.height(), title, glfwGetPrimaryMonitor(), 0L);
            if(window == 0L) throw new GLError("Context cannot be created");
            return new LWJGLWindow(window, core);
        }

        @Override
        public LWJGLWindow createFullscreen(int width, int height) {
            long window = glfwCreateWindow(width, height, title, glfwGetPrimaryMonitor(), 0L);
            if(window == 0L) throw new GLError("Context cannot be created");
            return new LWJGLWindow(window, core);
        }
    }
}
