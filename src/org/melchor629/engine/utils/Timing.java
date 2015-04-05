package org.melchor629.engine.utils;

import org.lwjgl.glfw.GLFW;

import java.util.HashMap;
import java.util.Map;

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
    private Map<String, Double> particiones;
    private double tiempoAnterior;
    private String name;
    private static Timing instance;

    protected double getTime() {
    	return GLFW.glfwGetTime();
    }

    /**
     * Returns the global instance of {@link Timing} for the game loop.
     * If this instance doesn't exists, a new one is created.
     * @return the instance
     */
    public static Timing getGameTiming() {
        if(instance == null)
            instance = new Timing("global");
        return instance;
    }

    public Timing(String name) {
        time = getTime() + 0.100; //Porque si xD
        startTime = tiempoAnterior = getTime();
        this.name = name.toLowerCase();
        particiones = new HashMap<>();
    }

    /**
     * Splits the time between updates with a tag.
     * @param name Tag name
     */
    public void split(String name) {
        particiones.put(name, getTime() - tiempoAnterior);
    }

    /**
     * Gets the time spent since the last update to the split call
     * @param name Tag name
     * @return time spent
     */
    public double getSplitTime(String name) {
        return particiones.get(name);
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

    public String toString() {
        return String.format("%c%s timing (%.1f)", name.charAt(0) - 'a' + 'A', name, totalTime());
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
