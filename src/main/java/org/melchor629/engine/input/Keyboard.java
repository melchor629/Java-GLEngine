package org.melchor629.engine.input;

import java.util.ArrayList;

/**
 * Base class for keyboard listener.<br>
 * Implementors should modify the {@link #keysPressed}
 * array and the booleans for the modifiers. For the
 * {@link #isKeyPressed(String)} method, implementors should
 * check it using the array before with the map <i>index -&gt; Key</i>.
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
    protected final ArrayList<OnPressKeyEvent> listeners2;
    protected final ArrayList<OnReleaseKeyEvent> listeners3;
    protected final ArrayList<OnCharacterEvent> listeners4;
    
    public Keyboard() {
        keysPressed = new boolean[1024];
        shiftPressed = controlPressed = altPressed = superPressed = false;
        listeners = new ArrayList<>();
        listeners2 = new ArrayList<>();
        listeners3 = new ArrayList<>();
        listeners4 = new ArrayList<>();
    }
    
    /**
     * Adds a listener for the event when is fired
     * @param l Listener
     */
    public final void addListener(OnKeyboardEvent l) {
        listeners.add(l);
    }

    /**
     * Adds a listener for the {@code OnPressKeyEvent} event.
     * This event is fired every time a key is pressed.
     * @param l Listener
     */
    public final void addListener(OnPressKeyEvent l) {
        listeners2.add(l);
    }

    /**
     * Adds a listener for the {@code OnReleaseKeyEvent}.
     * This event is fired every time a key is released.
     * @param l Listener
     */
    public final void addListener(OnReleaseKeyEvent l) {
        listeners3.add(l);
    }

    /**
     * Ads a listener for the {@code OnCharacterEvent}.
     * This event is fired every time a key with character
     * representation is pressed.
     * @param l Listener
     */
    public final void addListener(OnCharacterEvent l) {
        listeners4.add(l);
    }
    
    /**
     * Call this method to fire all listeners. Is recommended to call
     * this method at the end/start of the game loop. It's needed to 
     * pass a delta value. This value is the inverse of the Frame Rate,
     * in other words, the time spent to draw a frame.
     * @param delta Time spent between frames
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
     * Converts the keycode to its String representation. For example,
     * of you pass the keycode 32, this method could return "SPACE" or
     * "SPACEBAR". This method is useful when it is wanted to know
     * what key is pressed or released on both events.<br>
     * Some implementations could return {@code null} if it is not
     * fully implemented. All invalid keycodes have to return {@code null}.
     * @param keycode Keycode to be converted
     * @return its String representation or null if cannot be found
     */
    public abstract String getStringRepresentation(int keycode);
    
    /**
     * Use this method when you will not use anymore th keyboard
     * input.<br>
     * Implementors should call methods to release native data
     * and related stuff.
     */
    public abstract void release();

    /**
     * Implementors should call this method every time a key
     * is pressed to propagate the event to all listeners
     * @param key the key pressed
     */
    protected final void firePressEvent(int key) {
        listeners2.forEach((l) -> l.invoke(this, key));
    }

    /**
     * Implementors should call this method every time a key
     * is released to propagate the event to all listeners
     * @param key the key released
     */
    protected final void fireReleaseEvent(int key) {
        listeners3.forEach((l) -> l.invoke(this, key));
    }

    protected final void fireCharacterEvent(String rep) {
        listeners4.forEach(l -> l.invoke(this, rep));
    }
    
    /**
     * {@code OnKeyboardEvent} interface. This Event is fired
     * every frame.
     * @author melchor9000
     */
    public interface OnKeyboardEvent {
        void invoke(Keyboard self, double delta);
    }

    /**
     * {@code OnPressKeyEvent} interface. Event fired
     * every time a key is pressed.
     */
    public interface OnPressKeyEvent {
        void invoke(Keyboard self, int key);
    }

    /**
     * {@code OnReleaseKeyEvent} interface. Event fired
     * every time a key is released.
     */
    public interface OnReleaseKeyEvent {
        void invoke(Keyboard self, int key);
    }

    /**
     * {@code OnCharacterEvent} interface. Event fired
     * every time a key with a character representation
     * (<i>like O or 1</i>) is pressed. Useful for input
     * text.
     */
    public interface OnCharacterEvent {
        void invoke(Keyboard self, String character);
    }
}
