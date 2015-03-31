package org.melchor629.engine.utils;

import org.lwjgl.glfw.GLFW;

/**
 * Util class for save FPS info and other timing related stuff
 * @author melchor9000
 */
public class Timing {
    /** Total rendered frames during the session **/
    public long totalFrames = 0;
    /** Time wasted for render a frame (all stuff: Graphic & CPU) **/
    public double frameTime = 0;
    /** Last Frames per Second **/
    public short fps = 60;
    protected short FPS = 0;
    protected double time = 0;
    protected double startTime;
    private double tiempoAnterior;

    protected double getTime() {
    	return GLFW.glfwGetTime();
    }

    public Timing() {
        time = getTime() + 0.100; //Porque si xD
        startTime = tiempoAnterior = getTime();
    }

    /**
     * Use this at the end of every draw. Used for update FPS stats
     */
    public void update() {
        double tt = getTime();
        if (tt - time >= 1.) {
            fps = FPS;
            FPS = 0;
            time += 1.;
        }
        FPS++;
        totalFrames++;
        frameTime = tt - tiempoAnterior;
        tiempoAnterior = tt;
    }
    
    /**
     * Time elapsed since the timer started, in seconds
     * @return time
     */
    public double totalTime() {
        return GLFW.glfwGetTime() - startTime;
    }
    
    /**
     * Causes the current thread to "sleep", in other words, cease temporarly
     * its execution, for a determinated time in milliseconds. This method calls
     * {@code Thread.sleep()} wrapped with a try/catch.
     * @param time Time to sleep in milliseconds
     */
    public static void sleep(long time) {
        try {
            Thread.sleep(Math.abs(time));
        } catch(Exception e) {
            e.printStackTrace();
        }
    }
}
