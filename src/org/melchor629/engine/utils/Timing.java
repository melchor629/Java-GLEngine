package org.melchor629.engine.utils;

import org.lwjgl.Sys;

/**
 * Util class for save FPS info and other timing related stuff
 * @author melchor9000
 */
public class Timing {
    /** Total rendered frames during the session **/
    public long totalFrames = 0;
    /** Time wasted for render a frame (all stuff: Graphic & CPU) **/
    public long frameTime = 0;
    /** Last Frames per Second **/
    public short fps = 0;
    protected short FPS = 0;
    protected long time = 0;

    protected long getTime() {
    	return System.nanoTime(); //TODO
        //return (Sys.getTime() * 1000L) / Sys.getTimerResolution();
    }

    public Timing() {
        time = getTime() + 100; //Porque si xD
    }

    /**
     * Use this at the end of every draw. Used for update FPS stats
     */
    public void update() {
        long tt = getTime();
        if (tt - time >= 1000) {
            fps = FPS;
            FPS = 0;
            time += 1000;
        }
        FPS++;
        totalFrames++;
        frameTime = tt - frameTime;
    }
}
