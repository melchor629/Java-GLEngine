package org.melchor629.engine.gui;

import org.melchor629.engine.window.Window;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.utils.math.Vector2;

/**
 * Stores information about the Mouse Event
 */
public class MouseEvent {
    private final Mouse mouse;
    private float x, y;

    /**
     * Creates the Mouse Event
     * @param m Mouse controller
     * @param _x relative position for the View
     * @param _y relative position for the View
     */
    MouseEvent(MouseEvent m, float _x, float _y) {
        mouse = m.mouse;
        x = _x;
        y = _y;
    }

    /**
     * Creates the Mouse Event
     * @param m Mouse controller
     * @param pos relative position for the View
     */
    MouseEvent(Mouse m, Vector2 pos) {
        mouse = m;
        x = pos.x();
        y = pos.y();
    }

    /**
     * @return the relative x position inside the {@link View}
     */
    public float getX() {
        return x;
    }

    void setX(float xx) {
        x = xx;
    }

    /**
     * @return the absolute x position for the {@link Window}
     */
    public float getAbsoluteX() {
        return mouse.getMousePosition().x();
    }

    /**
     * @return the relative y position inside the {@link View}
     */
    public float getY() {
        return y;
    }

    void setY(float yy) {
        y = yy;
    }

    /**
     * @return the absolute y position for the {@link Window}
     */
    public float getAbsoluteY() {
        return mouse.getMousePosition().y();
    }

    /**
     * @return the difference between the current position and the last position
     */
    public float getXMovement() {
        return mouse.getMouseSpeed().x();
    }

    /**
     * @return the difference between the current position and the last position
     */
    public float getYMovement() {
        return -mouse.getMouseSpeed().y();
    }

    /**
     * @return the movement of the (<i>vertical</i>) wheel
     */
    public float getWheelMovement() {
        return mouse.getWheelSpeed().y();
    }

    /**
     * Some mouses have some kind of horizontal wheel, like the
     * Apple's Magic Mouse, this function returns this movement
     * @return the movement of the <i>horizontal wheel</i>
     */
    public float getHorizontalWheelMovement() {
        return mouse.getWheelSpeed().x();
    }

    /**
     * @return true if the left button is pressed
     */
    public boolean isLeftButtonClicked() {
        return mouse.isKeyPressed("LEFT");
    }

    /**
     * @return true if the right button is pressed
     */
    public boolean isRightButtonClicked() {
        return mouse.isKeyPressed("RIGHT");
    }

    /**
     * @return true if the middle button is pressed
     */
    public boolean isMiddleButtonClicked() {
        return mouse.isKeyPressed("MIDDLE");
    }
}
