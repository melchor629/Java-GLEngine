package org.melchor629.engine;

import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;

/**
 * Basic class for every game
 * @author melchor9000
 */
public abstract class Game {
    protected short width, height, fpsCap;
    protected boolean fullscreen, resizable, vsync;
    protected String title;
    public static Renderer gl;

    protected void setDisplay() {
        gl = new LWJGLRenderer();
        gl.createDisplay(width, height, fullscreen, title);
        gl.setResizable(resizable);
        gl.setVsync(vsync);
    }
    
    /**
     * Default constructor for test games
     */
    protected Game() {
        width = 1280;
        height = 720;
        fullscreen = false;
        resizable = false;
        vsync = true;
        title = "";
        fpsCap = 60;
    }

    public static class Builder {
        
    }
}
