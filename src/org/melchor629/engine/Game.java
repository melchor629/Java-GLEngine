package org.melchor629.engine;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.Mouse;

import java.util.List;

/**
 * Basic class for every game
 * @author melchor9000
 */
public abstract class Game {
    protected short width, height, fpsCap;
    protected boolean fullscreen, resizable, vsync;
    protected String title;
    public static Renderer gl;
    public static AL al;
    public static Keyboard keyboard;
    public static Mouse mouse;
    public static List<Erasable> erasableList;

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
