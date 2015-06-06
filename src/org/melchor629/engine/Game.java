package org.melchor629.engine;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.Window;
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
    public static Window window;
    public static GLContext gl;
    public static AL al;
    public static Keyboard keyboard;
    public static Mouse mouse;
    public static List<Erasable> erasableList;
    
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
