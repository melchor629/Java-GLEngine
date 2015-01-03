package org.melchor629.engine.input;

import java.util.ArrayList;

/**
 * Base class for keyboard listener.<br>
 * Implementors should modify the {@link #keysPressed}
 * array and the booleans for the modifiers. For the
 * {@link #isKeyPressed(String)} method, implementors should
 * check it using the array before with the map <i>index -> Key</i>. TODO glfwSetCharCallback
 * @author melchor9000
 */
public abstract class Keyboard {
    /**
     * Array that maps between a key (<i>index</i>) and if is
     * pressed
     */
    protected final boolean[] keysPressed;
    
    /**
     * Control variables for modifier keys
     */
    protected boolean shiftPressed, controlPressed, altPressed, superPressed;
    
    /**
     * An array with listeners
     */
    protected final ArrayList<OnKeyboardEvent> listeners;
    
    public Keyboard() {
        keysPressed = new boolean[1024];
        shiftPressed = controlPressed = altPressed = superPressed = false;
        listeners = new ArrayList<>();
    }
    
    /**
     * Adds a listener for the event when is fired
     * @param l Listener
     */
    public final void addListener(OnKeyboardEvent l) {
        listeners.add(l);
    }
    
    /**
     * Call this method to fire all listeners. Is recommended to call
     * this method at the end/start of the game loop. It's needed to 
     * pass a delta value. This value is the inverse of the Frame Rate,
     * in other words, the time spent to draw a frame.
     * @param delta
     */
    public final void fireEvent(double delta) {
        for(OnKeyboardEvent e : listeners)
            e.invoke(this, delta);
    }
    
    /**
     * Call this method to know if a key is pressed.<br>
     * The string is a representation of the key, for example, the
     * key W, you will write "W", or Space, "SPACE". Depends on the
     * implementation, see Javadoc of the Implemented Class.
     * Implementers should return using the array {@link #keysPressed}.
     * @param key String representation of the Key
     * @return true if the key is pressed, or false otherwise
     */
    public abstract boolean isKeyPressed(String key);
    
    /**
     * {@code OnKeyboardEvent} interface. This Event is fired
     * every frame.
     * @author melchor9000
     */
    public static interface OnKeyboardEvent {
        public void invoke(Keyboard self, double delta);
    }
}
