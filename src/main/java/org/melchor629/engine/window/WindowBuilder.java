package org.melchor629.engine.window;

/**
 * Helps you to create a {@code Window} by letting you change
 * all the options before create the window with this separated
 * class.
 */
public interface WindowBuilder<T extends Window> {
    /**
     * Determines a version and profile of a OpenGL Context
     */
    enum OpenGLContextVersion {
        GL_10(1, 0, OpenGLContext.OPENGL_COMPAT),
        GL_11(1, 1, OpenGLContext.OPENGL_COMPAT),
        GL_12(1, 2, OpenGLContext.OPENGL_COMPAT),
        GL_13(1, 3, OpenGLContext.OPENGL_COMPAT),
        GL_14(1, 4, OpenGLContext.OPENGL_COMPAT),
        GL_15(1, 5, OpenGLContext.OPENGL_COMPAT),
        GL_20(2, 0, OpenGLContext.OPENGL_COMPAT),
        GL_21(2, 1, OpenGLContext.OPENGL_COMPAT),
        GL_30_COMPAT(3, 0, OpenGLContext.OPENGL_COMPAT),
        GL_31_COMPAT(3, 1, OpenGLContext.OPENGL_COMPAT),
        GL_32_COMPAT(3, 2, OpenGLContext.OPENGL_COMPAT),

        GL_30(3, 0, OpenGLContext.OPENGL_CORE),
        GL_31(3, 1, OpenGLContext.OPENGL_CORE),
        GL_32(3, 2, OpenGLContext.OPENGL_CORE),
        GL_33(3, 3, OpenGLContext.OPENGL_CORE),
        GL_40(4, 0, OpenGLContext.OPENGL_CORE),
        GL_41(4, 1, OpenGLContext.OPENGL_CORE),
        GL_42(4, 2, OpenGLContext.OPENGL_CORE),
        GL_43(4, 3, OpenGLContext.OPENGL_CORE),
        GL_44(4, 4, OpenGLContext.OPENGL_CORE),
        GL_45(4, 5, OpenGLContext.OPENGL_CORE),

        GLES_10(1, 0, OpenGLContext.OPENGL_ES),
        GLES_11(1, 1, OpenGLContext.OPENGL_ES),
        GLES_20(2, 0, OpenGLContext.OPENGL_ES),
        GLES_30(3, 0, OpenGLContext.OPENGL_ES),
        GLES_31(3, 1, OpenGLContext.OPENGL_ES);

        int major, minor;
        OpenGLContext type;
        OpenGLContextVersion(int major, int minor, OpenGLContext type) {
            this.major = major;
            this.minor = minor;
            this.type = type;
        }
    }

    /**
     * Enum with posible context profiles
     */
    enum OpenGLContext {
        OPENGL_COMPAT,
        OPENGL_CORE,
        OPENGL_ES
    }

    /**
     * Sets whether the window should be resizable or not. By default is true.
     * @param resizable true to make the window resizable, false to not
     * @return this
     */
    WindowBuilder setResizable(boolean resizable);

    /**
     * Sets whether the window will be visible when created or
     * not. To make it visible when created, use {@link Window#showWindow()}.
     * By default is true.
     * @param visible true to show window, false to hide
     * @return this
     */
    WindowBuilder setVisible(boolean visible);

    /**
     * Sets whether the window will be decorated or not. This is,
     * with the borders and window top bar and buttons or without them.
     * By default is true.
     * @param decorated true to decorated, false otherwise.
     * @return this
     */
    WindowBuilder setDecorated(boolean decorated);

    /**
     * Makes this window focused or not when is created. By default is true.
     * @param focused true to focus windows, false to not
     * @return this
     */
    WindowBuilder setFocused(boolean focused);

    /**
     * If {@code samples} is not zero, enables multisampling with {@code samples},
     * if is zero, will disable that. Default is disabled.
     * @param samples samples for multisampling or 0 to disable
     * @return this
     */
    WindowBuilder setMultisample(int samples);

    /**
     * True to enable Double Buffering in the FrameBuffer. You should want enable
     * this. May fail the window creation.
     * @param doubleBuffered true to enable it, false otherwise
     * @return this
     */
    WindowBuilder setDoubleBuffered(boolean doubleBuffered);

    /**
     * Sets the OpenGL context profile and version.
     * @param version {@link OpenGLContextVersion}
     * @return this
     */
    WindowBuilder setOpenGLContextVersion(OpenGLContextVersion version);

    /**
     * Sets a title for the window
     * @param title window title
     * @return this
     */
    WindowBuilder setTitle(String title);

    /**
     * Changes the monitor where this window will be created. You can
     * pass {@code null} to set to the default monitor.
     * @param monitor the {@link Monitor} on which the window will be created
     * @return this
     */
    WindowBuilder setMonitor(Monitor monitor);

    /**
     * Creates a window in non-fullscreen mode with the size {@code width}
     * {@code height}.
     * @param width width of the window
     * @param height height of the window
     * @return the created {@link Window}
     */
    T create(int width, int height);

    /**
     * Creates a fullscreen window for the monitor set before.
     * @return the created {@link Window}
     */
    T createFullscreen();

    /**
     * Creates a fullscreen window for the monitor set before and
     * the screen resoultion desired.
     * @param videoMode the {@link Monitor.VideoMode} for the window
     * @return the created {@link Window}
     */
    T createFullscreen(Monitor.VideoMode videoMode);

    /**
     * Creates a windowed fullscreen window (if size is not supported by the
     * default monitor) or fullscreen window (otherwise).
     * @param width width of the screen
     * @param height height of the screen
     * @return the created {@link Window}
     */
    T createFullscreen(int width, int height);
}
