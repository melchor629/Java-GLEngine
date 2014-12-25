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
    public short fps = 0;
    protected short FPS = 0;
    protected double time = 0;

    protected double getTime() {
    	return GLFW.glfwGetTime();
        //return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
    }

    public Timing() {
        time = getTime() + 0.100; //Porque si xD
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
        frameTime = tt - frameTime;
    }
}
