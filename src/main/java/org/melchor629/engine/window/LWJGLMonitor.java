package org.melchor629.engine.window;

import org.lwjgl.PointerBuffer;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWVidMode;
import org.melchor629.engine.utils.math.Vector2;

/**
 * Implementation using LWJGL bindings to GLFW
 */
public class LWJGLMonitor implements Monitor {
    long monitor;

    public static LWJGLMonitor getPrimaryMonitor() {
        return new LWJGLMonitor(GLFW.glfwGetPrimaryMonitor());
    }

    public static LWJGLMonitor[] getMonitors() {
        PointerBuffer pb = GLFW.glfwGetMonitors();
        LWJGLMonitor[] monitors = new LWJGLMonitor[pb.capacity()];
        for(int i = 0; i < pb.capacity(); i++) {
            monitors[i] = new LWJGLMonitor(pb.get(i));
        }
        return monitors;
    }

    private LWJGLMonitor(long monitor) {
        if(monitor == 0) throw new NullPointerException("Monitor is null");
        this.monitor = monitor;
    }

    @Override
    public Vector2 getPhysicalSize() {
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetMonitorPhysicalSize(monitor, w, h);
        return new Vector2(w[0], h[0]);
    }

    @Override
    public Vector2 getVirtualPosition() {
        int[] w = new int[1], h = new int[1];
        GLFW.glfwGetMonitorPos(monitor, w, h);
        return new Vector2(w[0], h[0]);
    }

    @Override
    public String getName() {
        return GLFW.glfwGetMonitorName(monitor);
    }

    @Override
    public VideoMode getCurrentVideoMode() {
        GLFWVidMode mode = GLFW.glfwGetVideoMode(monitor);
        return new VideoMode(mode.width(), mode.height(), mode.refreshRate());
    }

    @Override
    public VideoMode[] getAvailableVideoModes() {
        GLFWVidMode.Buffer modes = GLFW.glfwGetVideoModes(monitor);
        VideoMode[] vmodes = new VideoMode[modes.capacity()];
        for(int i = 0; i < modes.capacity(); i++) {
            vmodes[i] = new VideoMode(modes.get(i).width(), modes.get(i).height(), modes.get(i).refreshRate());
        }
        return vmodes;
    }

    @Override
    public String toString() {
        return "GLFWMonitor(" + getName() + " " + getCurrentVideoMode() + ")";
    }
}
