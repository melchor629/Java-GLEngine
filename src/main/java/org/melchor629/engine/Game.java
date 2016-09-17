package org.melchor629.engine;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.window.Window;
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

    protected int width, height;
    protected Timing t;
    protected Queue<Runnable> events;
    protected Window window;
    protected GLContext gl;
    protected AL al;
    protected TextureManager textureManager;
    protected ShaderManager shaderManager;
    protected GUI gui;

    private final Lock lock = new ReentrantLock(true);
    private final Condition waitClosing, waitFocus;
    private volatile boolean destroyed = false, focused = true;
    private boolean enableGui = false;
    
    /**
     * Full constructor for test games
     * @param window The Window created with your chosen implementation
     * @param audio The Audio context created with your chosen implementation
     * @param enableGui Enables/Disables the GUI
     */
    protected Game(Window window, AL audio, boolean enableGui) {
        events = new ConcurrentLinkedQueue<>();
        waitClosing = lock.newCondition();
        waitFocus = lock.newCondition();
        this.window = window;
        this.al = audio;
        width = window.getWindowSize().width;
        height = window.getWindowSize().height;
        this.enableGui = enableGui;
    }

    /**
     * Constructor for test games without OpenAL
     * @param window The Window created with your chosen implementation
     * @param enableGui Enables/Disables the GUI
     */
    protected Game(Window window, boolean enableGui) {
        this(window, null, enableGui);
    }

    /**
     * Constructor for test games without GUI
     * @param window The Window created with your chosen implementation
     * @param audio The Audio context created with your chosen implementation
     */
    protected Game(Window window, AL audio) {
        this(window, audio, false);
    }

    /**
     * Constructor for test games without OpenAL and GUI
     * @param window The Window created with your chosen implementation
     */
    protected Game(Window window) {
        this(window, null, false);
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
        gl = window.makeContextOnCurrentThread();
        if(al != null) {
            al.createContext();
        }

        textureManager = new TextureManager(gl);
        shaderManager = new ShaderManager(gl);

        if(enableGui) {
            gui = new GUI(this, gl, window);
        }

        init();

        window.getKeyboardController().addListener((Keyboard.OnPressKeyEvent) (self, key) -> {
            if(self.isKeyPressed("ESCAPE"))
                this.window.setWindowShouldClose(true);
        });

        window.showWindow();
        t = Timing.getGameTiming();
        while(!destroyed) {
            while(!events.isEmpty()) events.poll().run();
            if(focused) {
                if (enableGui) {
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
                }
                lock.unlock();
            } else {
                lock.lock();
                try { waitFocus.await(); } catch(Exception ignore) {}
                lock.unlock();
                t.update();
            }
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
        window.addResizeEventListener((newWidth, newHeight) -> {
            this.width = newWidth;
            this.height = newHeight;
            post(() -> gl.viewport(0, 0, window.getFramebufferSize().width, window.getFramebufferSize().height));
        });

        window.setOnFocusEventListener(() -> {
            focused = true;
            lock.lock();
            waitFocus.signal();
            lock.unlock();
        });

        window.setOnBlurEventListener(() -> {
            focused = false;
        });

        makeGame();
    }

    public final void post(Runnable r) {
        events.add(r);
        lock.lock();
        waitFocus.signal();
        lock.unlock();
    }

    public final void postInBackground(Runnable r) {
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
