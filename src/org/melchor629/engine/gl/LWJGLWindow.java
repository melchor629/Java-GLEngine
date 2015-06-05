package org.melchor629.engine.gl;

import org.lwjgl.opengl.GLContext;
import static org.lwjgl.glfw.GLFW.*;

/**
 * Window and context creator for LWJGL library, that uses GLFW.
 */
public class LWJGLWindow implements Window {
    public long window;
    public GLContext context;

    public LWJGLWindow() {
        if(glfwInit() != 0)
            throw new GLError("Could not initiate GLFW");
        glfwDefaultWindowHints();
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

    }

    @Override
    public void setFocused(boolean focused) {

    }

    @Override
    public void setMultisample(boolean multisample, int samples) {

    }

    @Override
    public void setDoublebuffered(boolean doublebuffered) {

    }

    @Override
    public void setContextProfileAndVersion(OpenGLContextVersion version) {

    }

    @Override
    public void createWindow(int width, int height, String title) {

    }

    @Override
    public void createFullscreenWindow(int width, int height, String title) {

    }

    @Override
    public void createFullScreenWindow(String title) {

    }

    @Override
    public boolean windowShouldClose() {
        return false;
    }

    @Override
    public void setWindowShouldClose(boolean close) {

    }

    @Override
    public void addResizeEventListener(OnWindowResizeEvent e) {

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
    public void setTitle(String title) {
        glfwSetWindowTitle(window, title);
    }

    @Override
    public void setOnFocusEventListener(OnFocusEvent e) {

    }

    @Override
    public void setOnBlurEventListener(OnBlurEvent e) {

    }

    @Override
    public double getDPI() {
        return 0;
    }

    @Override
    public void destroyWindow() {
        glfwDestroyWindow(window);
    }
}
