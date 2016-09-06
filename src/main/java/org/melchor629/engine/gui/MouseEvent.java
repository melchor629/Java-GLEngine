package org.melchor629.engine.gui;

import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.utils.math.Vector2;

/**
 * Mouse event
 */
public class MouseEvent {
    private final Mouse mouse;
    private float x, y;

    MouseEvent(MouseEvent m, float _x, float _y) {
        mouse = m.mouse;
        x = _x;
        y = _y;
    }

    MouseEvent(Mouse m, Vector2 pos) {
        mouse = m;
        x = pos.x();
        y = pos.y();
    }

    public float getX() {
        return x;
    }

    void setX(float xx) {
        x = xx;
    }

    public float getAbsoluteX() {
        return mouse.getMousePosition().x();
    }

    public float getY() {
        return y;
    }

    void setY(float yy) {
        y = yy;
    }

    public float getAbsoluteY() {
        return mouse.getMousePosition().y();
    }

    public float getXMovement() {
        return mouse.getMouseSpeed().x();
    }

    public float getYMovement() {
        return -mouse.getMouseSpeed().y();
    }

    public float getWheelMovement() {
        return mouse.getWheelSpeed().y();
    }

    public float getHorizontalWheelMovement() {
        return mouse.getWheelSpeed().x();
    }

    public boolean isLeftButtonClicked() {
        return mouse.isKeyPressed("LEFT");
    }

    public boolean isRightButtonClicked() {
        return mouse.isKeyPressed("RIGHT");
    }

    public boolean isMiddleButtonClicked() {
        return mouse.isKeyPressed("MIDDLE");
    }
}
