package org.melchor629.engine;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.LWJGLKeyboard;
import org.melchor629.engine.input.LWJGLMouse;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.utils.Timing;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Basic class for every game
 * @author melchor9000
 */
public abstract class Game {
    protected short width, height;
    protected boolean fullscreen, resizable, vsync;
    protected String title;
    protected Timing t;
    protected Queue<Runnable> events;
    public static Window window;
    public static GLContext gl;
    protected AL al;
    public static Keyboard keyboard;
    public static Mouse mouse;
    public static List<Erasable> erasableList;

    private final Object lock;
    private boolean destroyed = false;
    
    /**
     * Default constructor for test games
     */
    protected Game(Window window, AL audio) {
        width = 1280;
        height = 720;
        fullscreen = false;
        resizable = false;
        vsync = true;
        title = "";

        erasableList = new ArrayList<>();
        events = new ConcurrentLinkedQueue<>();
        lock = new Object();
        this.window = window;
        this.al = audio;
    }

    private void makeGame() {
        new Thread(this::wrapRenderLoop, "RenderLoop").start();

        while(!window.windowShouldClose()) {
            window.waitEvents();
        }

        synchronized(lock) {
            destroyed = true;
        }

        keyboard.release();
        mouse.release();
        window.destroyWindow();
    }

    private void wrapRenderLoop() {
        gl = window.createContext();
        keyboard = new LWJGLKeyboard();
        mouse = new LWJGLMouse();
        init();

        t = Timing.getGameTiming();
        while(!destroyed) {
            render();

            synchronized(lock) {
                if(!destroyed) {
                    window.syncGPU();
                    t.update();
                    keyboard.fireEvent(t.frameTime);
                    mouse.update(t.frameTime);
                    while(!events.isEmpty()) events.poll().run();
                }
            }
        }
        erasableList.forEach(Erasable::delete);
        closing();
        window.destroyWindow();
        if(al != null) {
            al.deleteContext();
        }
    }

    protected void startEngine() {
        if(fullscreen)
            window.createFullscreenWindow(width, height, title);
        else
            window.createWindow(width, height, title);

        window.addResizeEventListener((newWidth, newHeight) -> {
            this.width = (short) newWidth;
            this.height = (short) newHeight;
        });

        makeGame();
    }

    protected void postRunnable(Runnable r) {
        events.add(r);
    }

    public abstract void init();

    public abstract void render();

    public abstract void closing();
}
