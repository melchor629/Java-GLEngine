package org.melchor629.engine.input;

import java.util.ArrayList;

import org.melchor629.engine.Game;
import org.melchor629.engine.utils.math.vec2;

/**
 * Manages the Mouse input. This abstract class has to be implemented
 * fully depending on the library used. See the Javadoc for all the
 * subclasses that implements this one.
 * @author melchor9000
 */
public abstract class Mouse {
    /**
     * Array that maps between a button (<i>index</i>) and if it's
     * pressed
     */
    protected final boolean[] mousePressed;
    
    /**
     * Value that represents the sensibility of the mouse
     */
    protected float sensibility;
    
    /**
     * Position of the mouse
     */
    protected vec2 pos;
    
    /**
     * Derivative of the position with respect to time, 
     * in other words, the difference between the last position
     * and the current.
     */
    protected vec2 dPos;
    
    /**
     * The speed of the wheel. Some mouses has Y wheel, an example,
     * Apple Magic Mouse.
     */
    protected vec2 wheel;

    /**
     * An array with listeners
     */
    private final ArrayList<OnMouseClickEvent> listeners;
    
    /**
     * An array with listeners, again
     */
    private final ArrayList<OnMouseMoveEvent> listeners2;

    /**
     * Another array with more listeners
     */
    private final ArrayList<OnButtonPressedEvent> listeners3;
    

    public Mouse() {
        mousePressed = new boolean[20];
        listeners = new ArrayList<>();
        listeners2 = new ArrayList<>();
        listeners3 = new ArrayList<>();
        sensibility = 1.f;
        pos = new vec2();
        dPos = new vec2();
        wheel = new vec2();
    }
    
    /**
     * Adds a listener for the event {@link Mouse.OnMouseClickEvent}
     * @param e Event listener
     */
    public void addListener(OnMouseClickEvent e) {
        listeners.add(e);
    }
    
    /**
     * Adds a listener for the event {@link Mouse.OnMouseMoveEvent}
     * @param e Event listener
     */
    public void addListener(OnMouseMoveEvent e) {
        listeners2.add(e);
    }

    /**
     * Adds a listener for the event {@link Mouse.OnButtonPressedEvent}
     * @param e Event listener
     */
    public void addListener(OnButtonPressedEvent e) {
        listeners3.add(e);
    }
    
    /**
     * Fire all listeners for the event {@link Mouse.OnMouseClickEvent}.
     */
    protected void fireMouseClick() {
        for(OnMouseClickEvent e : listeners)
            e.invoke(this);
    }
    
    /**
     * Fire all listeners for the event {@link Mouse.OnMouseMoveEvent}.
     * Delta value is the inverse of the Frame Rate, in other words,
     * the time spent to draw a frame. This value can be useful for
     * animation calculations.
     * @param delta Delta value
     */
    protected void fireMouseMove(double delta) {
        if((wheel.x != 0 && wheel.y != 0) || (dPos.x != 0 && dPos.y != 0)) {
            for(OnMouseMoveEvent e : listeners2)
                e.invoke(this, delta);
            wheel.x = wheel.y = 0.f;
            dPos.x = dPos.y = 0.f;
        }
    }

    /**
     * Fire all listeners for the event {@link Mouse.OnButtonPressedEvent}.
     * Delta value is the inverse of the Frame Rate.
     * @param delta Delva value
     */
    protected void fireButtonPressed(double delta) {
        boolean hasSomethingTrue = false;
        int i = 0;
        while(!hasSomethingTrue && i < mousePressed.length)
            hasSomethingTrue = mousePressed[i++];

        if(hasSomethingTrue) {
            for(OnButtonPressedEvent e : listeners3)
                e.invoke(this, delta);
        }
    }

    /**
     * Method that updates all stuff related to mouse input. Call this
     * at the end of every frame.
     * @param delta Inverse of the FrameRate
     */
    public void update(double delta) {
        fireMouseMove(delta);
        fireButtonPressed(delta);
    }
    
    /**
     * @return the mouse position
     */
    public final vec2 getMousePosition() {
        return pos;
    }
    
    /**
     * Speed is calculed by derivative of the position with
     * respect to time, in other words, the diference between
     * the last position and the new between frames
     * @return the mouse speed
     */
    public final vec2 getMouseSpeed() {
        return dPos;
    }
    
    /**
     * Returns a two-dim vector because there're some mouses
     * that have two axis wheels. One example is Apple Magic Mouse.
     * @return the wheel offset or speed
     */
    public final vec2 getWheelSpeed() {
        return wheel;
    }
    
    public final boolean isShiftPressed() {
        return Game.keyboard.shiftPressed;
    }
    
    public final boolean isCtrlPressed() {
        return Game.keyboard.controlPressed;
    }
    
    public final boolean isAltPressed() {
        return Game.keyboard.altPressed;
    }
    
    public final boolean isSuperPressed() {
        return Game.keyboard.superPressed;
    }
    
    /**
     * Call this method to know if a mouse button is pressed.<br>
     * The string is a representation of the button, for example, the
     * right button will be "RIGHT", or 5th, "5". Depends on the
     * implementation, see Javadoc of the Implemented Class.
     * Implementers should return using the array {@link Keyboard#keysPressed}.
     * @param key String representation of the Key
     * @return true if the key is pressed, or false otherwise
     */
    public abstract boolean isKeyPressed(String key);
    
    /**
     * Mouse is captured if the mouse is not visible and its position
     * is always the same.
     * @return true if it's captured, false otherwise
     */
    public abstract boolean isCaptured();
    
    /**
     * Set whether capture the mouse or not
     * @param c true to capture it, false otherwise
     */
    public abstract void setCaptured(boolean c);
    
    /**
     * Sets the position of the mouse. Depending of the implementation,
     * this method might not be effective.
     * @param x X position relative to the window
     * @param y Y position relative to the window
     */
    public abstract void setCursorPosition(float x, float y);

    /**
     * Sets the position of the mouse. Depending of the implementation,
     * this method may not be effective.
     * @param position Position relative to the window
     */
    public abstract void setCursorPosition(vec2 position);
    
    /**
     * Use this method when you will not use anymore the mouse
     * input.<br>
     * Implementors should call methods to release native data
     * and related stuff.
     */
    public abstract void release();


    /**
     * {@code OnMouseClickEvent} interface. This Event is everytime
     * a mouse button is clicked or released.
     * @author melchor9000
     */
    public static interface OnMouseClickEvent {
        void invoke(Mouse self);
    }
    
    /**
     * {@code OnMouseMoveEvent} interface. This Event is called on every
     * frame with a mouse movement.
     * @author melchor9000
     */
    public static interface OnMouseMoveEvent {
        void invoke(Mouse self, double delta);
    }

    /**
     * {@code OnButtonPressedEvent} interface. This Event is called
     * on every frame with a button pressed. This event differs from
     * {@link org.melchor629.engine.input.Mouse.OnMouseClickEvent}
     * because this event is called all time the button is pressed, whilst
     * the other is called only when the button is pressed and released.
     */
    public static interface OnButtonPressedEvent {
        void invoke(Mouse self, double delta);
    }
}
