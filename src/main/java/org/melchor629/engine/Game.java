package org.melchor629.engine;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.gui.GUI;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.utils.ShaderManager;
import org.melchor629.engine.utils.TextureManager;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.logger.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Basic class for every game
 * @author melchor9000
 */
public abstract class Game {
    private static final Logger LOG = Logger.getLogger(Game.class);

    protected short width, height;
    protected boolean fullscreen, resizable, vsync, enableGui = false;
    protected String title;
    protected Timing t;
    protected Queue<Runnable> events;
    protected Window window;
    protected GLContext gl;
    protected AL al;
    protected TextureManager textureManager;
    protected ShaderManager shaderManager;
    protected GUI gui;

    private final Lock lock = new ReentrantLock(true);
    private final Condition waitClosing;
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

        events = new ConcurrentLinkedQueue<>();
        waitClosing = lock.newCondition();
        this.window = window;
        this.al = audio;
    }

    private void makeGame() {
        Thread renderLoop = new Thread(this::wrapRenderLoop, "RenderLoop");
        renderLoop.setName("Render Loop");
        renderLoop.setPriority(Thread.MAX_PRIORITY);
        renderLoop.start();

        Thread.currentThread().setPriority(Thread.MAX_PRIORITY);
        while(!window.windowShouldClose()) {
            window.waitEvents();
        }

        try {
            lock.lock();
            destroyed = true;
            waitClosing.await();
        } catch(InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }

        try {
            renderLoop.join();
        } catch(InterruptedException ignore) {}
        window.destroyWindow();
    }

    private void wrapRenderLoop() {
        gl = window.createContext();
        if(al != null) {
            al.createContext();
        }

        textureManager = new TextureManager(gl);
        shaderManager = new ShaderManager(gl);

        if(enableGui) {
            gui = new GUI(gl, window);
        }

        init();

        window.getKeyboardController().addListener((Keyboard.OnPressKeyEvent) (self, key) -> {
            if(self.isKeyPressed("ESCAPE"))
                this.window.setWindowShouldClose(true);
        });

        t = Timing.getGameTiming();
        while(!destroyed) {
            if(enableGui) {
                gui.render(this::render);
                t.split("render");
                gui.gui(this::gui);
                t.split("gui");
            } else {
                render();
            }

            lock.lock();
            if(!destroyed) {
                window.syncGPU();
                t.update();
                window.getKeyboardController().fireEvent(t.frameTime);
                window.getMouseController().update(t.frameTime);
                while(!events.isEmpty()) events.poll().run();
            }
            lock.unlock();
        }

        lock.lock();
        window.hideWindow();
        closing();
        if(enableGui) gui.delete();
        gl.destroyContext();
        waitClosing.signal();
        lock.unlock();

        if(al != null) {
            al.destroyContext();
        }
    }

    protected final void startEngine() {
        if(fullscreen) {
            window.createFullscreenWindow(width, height, title);
            LOG.debug("Created fullscreen window %dx%d with title %s", width, height, title);
        } else {
            window.createWindow(width, height, title);
            LOG.debug("Created window %dx%d with title %s", width, height, title);
        }

        window.addResizeEventListener((newWidth, newHeight) -> {
            this.width = (short) newWidth;
            this.height = (short) newHeight;
            post(() -> gl.viewport(0, 0, window.getFramebufferSize().width, window.getFramebufferSize().height));
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

    public void gui(long nvCtx) {}

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

    public final ShaderManager getShaderManager() {
        return shaderManager;
    }
}
