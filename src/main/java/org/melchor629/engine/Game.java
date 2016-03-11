package org.melchor629.engine;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.utils.TextureManager;
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
    protected Window window;
    public static GLContext gl;
    protected AL al;
    protected TextureManager textureManager;
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
            window.destroyWindow();
        }
    }

    private void wrapRenderLoop() {
        gl = window.createContext();
        if(al != null) {
            al.createContext();
        }

        textureManager = new TextureManager(gl);

        init();

        window.getKeyboardController().addListener((Keyboard.OnPressKeyEvent) (self, key) -> {
            if(self.isKeyPressed("ESCAPE"))
                this.window.setWindowShouldClose(true);
        });

        t = Timing.getGameTiming();
        while(!destroyed) {
            render();

            synchronized(lock) {
                if(!destroyed) {
                    window.syncGPU();
                    t.update();
                    window.getKeyboardController().fireEvent(t.frameTime);
                    window.getMouseController().update(t.frameTime);
                    while(!events.isEmpty()) events.poll().run();
                }
            }
        }

        erasableList.forEach(Erasable::delete);
        closing();
        if(al != null) {
            al.deleteContext();
        }
    }

    protected final void startEngine() {
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

    protected final void post(Runnable r) {
        events.add(r);
    }

    protected final void postInBackground(Runnable r) {
        window.postEvent(r);
    }

    public abstract void init();

    public abstract void render();

    public abstract void closing();

    public final Mouse getMouse() {
        return window.getMouseController();
    }

    public final Keyboard getKeyboard() {
        return window.getKeyboardController();
    }

    public final Window getWindow() {
        return window;
    }

    public final GLContext getOpenGLContext() {
        return gl;
    }

    public final TextureManager getTextureManager() {
        return textureManager;
    }
}
