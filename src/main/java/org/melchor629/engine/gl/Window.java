package org.melchor629.engine.gl;

import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.Mouse;

/**
 * OpenGL Context creator and Window manager. {@link Window} interface
 * encloses all basic and not at all basic functions to create a context
 * and manage the created Window. Implementors should implement all basic
 * methods, the rest, could be implemented if the backend supports it.
 */
public interface Window {

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
     * Interface for {@code OnWindowResizeEvent}. This event is
     * called when the window is resized. The parameters are
     * the new width and height.
     */
    interface OnWindowResizeEvent {
        void invoke(int newWidth, int newHeight);
    }

    /**
     * Interface for {@code OnFocusEvent}. This event is called
     * when the window is focused.
     */
    interface OnFocusEvent {
        void invoke();
    }

    /**
     * Interface for {@code OnBlurEvent}. This event is called
     * when the window loses focused, is blurred.
     */
    interface OnBlurEvent {
        void invoke();
    }

    /**
     * Represents size of something about the Screen. FrameBuffer's size,
     * Window size, Viewport size, Screen size...
     */
    class Size {
        public int width, height;

        protected Size(int w, int h) {
            width = w; height = h;
        }

        public String toString() {
            return String.format("(%d, %d)", width, height);
        }
    }

    /**
     * Specifies whether the window will be resizable or not by the user.
     * Ignored for fullscreen games
     * @param resizable enable or disable resizable window
     */
    void setResizable(boolean resizable);

    /**
     * Specifies the initial visibility state of the window. Ignored for
     * fullscreen games
     * @param visible set visible or not a window
     */
    default void setVisible(boolean visible) {
        throw new UnsupportedOperationException("Unimplemented setVisible(boolean)");
    }

    /**
     * Specifies whether to show borders and window buttons (close, minimize, etc)
     * or not. Undecorated windows are still windows. Ignored for fullscreen games.
     * @param decorated set decorated or undecorated window
     */
    default void setDecorated(boolean decorated) {
        throw new UnsupportedOperationException("Unimplemented setDecorated(boolean)");
    }

    /**
     * Specifies if the window will be focused or not when is created. Ignored
     * for fullscreen games and hidden windows.
     * @param focused initial focus status
     */
    default void setFocused(boolean focused) {
        throw new UnsupportedOperationException("Unimplemented setFocused(boolean)");
    }

    /**
     * Enable multisampling for the window.
     * @param multisample enable or disable multisampling
     * @param samples number of samples
     */
    default void setMultisample(boolean multisample, int samples) {
        throw new UnsupportedOperationException("Unimplemented setMultisample(boolean, int)");
    }

    /**
     * Specifies whether the main framebuffer should be Double Buffered.
     * @param doublebuffered true to activate it
     */
    default void setDoublebuffered(boolean doublebuffered) {
        throw new UnsupportedOperationException("Unimplemented setDoublebuffered(boolean)");
    }

    /**
     * Specifies the Client API (OpenGL or OpenGL ES), its profile and its major and minus
     * version for the context. Also on OS X, activates the {@code OPENGL_FORWAD_COMPAT}.
     * @param version version, profile and client API
     */
    void setContextProfileAndVersion(OpenGLContextVersion version);

    /**
     * Creates a Windowed display with the given width, height and title.
     * Also creates OpenGL context.
     * @param width width of the window
     * @param height height of the window
     * @param title title of the window
     */
    void createWindow(int width, int height, String title);

    /**
     * Creates a Fullscreen display with the given width, height and title.
     * Also creates OpenGL context.
     * @param width width of the window
     * @param height height of the window
     * @param title title of the window
     */
    void createFullscreenWindow(int width, int height, String title);

    /**
     * Creates a Fullscreen display with the given title. The width and
     * height will be the same as monitor's
     * Also creates OpenGL context.
     * @param title title of the window
     */
    void createFullscreenWindow(String title);

    /**
     * Determines if the window should close in the future or not
     * @return window should close?
     */
    boolean windowShouldClose();

    /**
     * Sets if the window should close or not
     * @param close close state
     */
    void setWindowShouldClose(boolean close);

    /**
     * Adds a listener for the event {@link OnWindowResizeEvent}.
     * Depending on the implementation, this event will be fired or not.
     * @param e event listener
     */
    default void addResizeEventListener(OnWindowResizeEvent e) {}

    /**
     * Shows a hidden window
     */
    default void showWindow() {
        throw new UnsupportedOperationException("Unimplemented showWindow()");
    }

    /**
     * Hides a visible window
     */
    default void hideWindow() {
        throw new UnsupportedOperationException("Unimplemented hideWindow()");
    }

    /**
     * @return true if the window is visible
     */
    default boolean isVisible() {
        return true;
    }

    /**
     * Sets if the context will run on Vertical Synchronization
     */
    void setVsync(boolean vsync);

    /**
     * Changes the title of the window to a new one
     * @param title new window title
     */
    void setTitle(String title);

    /**
     * Synchronices drawing. Have to be called after every frame is drawn.
     */
    void syncGPU();

    /**
     * Creates the OpenGL context and sets this context to this Window
     * @return the context created from the window
     */
    GLContext createContext();

    /**
     * If is applicable, poll events from window event loop
     */
    default void pollEvents() {}

    /**
     * If is applicable, blocks thread until new events have come in window event loop
     */
    default void waitEvents() {
        pollEvents();
    }

    /**
     * Posts a runnable to the main event loop. If runnable is null, sends
     * a wake up signal to the event loop
     * @param r runnable to post
     */
    void postEvent(Runnable r);

    /**
     * Adds a listener for the event {@link OnFocusEvent}.
     * Depending on the implementation, this event could not be
     * fired.
     * @param e the event listener
     */
    default void setOnFocusEventListener(OnFocusEvent e) {}

    /**
     * Adds a listener for the event {@link OnBlurEvent}.
     * Depending on the implementation, this event could not be
     * fired.
     * @param e the event listener
     */
    default void setOnBlurEventListener(OnBlurEvent e) {}

    /**
     * Calculates the Density Pixels per Inch of the screen.
     * Computers like Apple's Macbook Pro has a Retina Display
     * with a DPI different to the usual.
     * @return the DPI of the screen
     */
    default double getDPI() { return 76; }

    /**
     * Its default value is 1. But some screens have high-DPI,
     * on this ones, this value will be higher than 1. This value
     * is calculated using the real FrameBuffer width with the
     * window width.<br><br>
     *     {@code screenDensityMultiplier = fbo.width / window.width};
     * @return the above quocient value
     */
    double getPixelScaleFactor();

    /**
     * @return window's size
     */
    Size getWindowSize();

    /**
     * This framebuffer is not the current binded framebuffer, is
     * the window framebuffer, the main one.
     * @return framebuffer's size
     */
    Size getFramebufferSize();

    /**
     * Destroys the Window and the context.
     */
    void destroyWindow();

    /**
     * @return mouse controller associated to this window
     */
    Mouse getMouseController();

    /**
     * @return keyboard controller associated to this window
     */
    Keyboard getKeyboardController();
}
