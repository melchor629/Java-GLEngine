package org.melchor629.engine.window;

import org.melchor629.engine.gl.GLContext;
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
     * @param vsync true to activate, false otherwise
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

    /**
     * @return the OpenGL context associated to this window
     */
    GLContext getGLContext();

    /**
     * Creates the OpenGL context on the current thread. Can be
     * called in any thread, but that thread will have the OpenGL
     * context and no other thread could have it.
     * @return the {@code GLContext} for this Window
     */
    GLContext makeContextOnCurrentThread();

    /**
     * Limits the window size to this limits, or deletes limits. The
     * minimum size of the window is {@code min}. The maximum size of
     * the window is {@code max}. If one (or both) values are {@code null},
     * then no limit is applied. Only works for non fullscreen windows and
     * resizable ones.
     * @param min minimum size limit or null to disable limit
     * @param max maximum size limit or null to disable limit
     */
    default void setWindowSizeLimit(Size min, Size max) {
        throw new UnsupportedOperationException("Cannot limit window size in this implementation");
    }

    /**
     * Limits the resize to fulfill the aspect ratio defined. Only works
     * for non fullscreen windows and resizable ones. To disable, pass
     * -1 on both arguments.
     * @param a number
     * @param b denom
     */
    default void setWindowSizeAspectRatio(int a, int b) {
        throw new UnsupportedOperationException("Cannot limit the resize to an aspect ratio in this implementation");
    }
}
