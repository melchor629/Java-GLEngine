package org.melchor629.engine.utils.math;

/**
 * Vector 2 Class
 * @author melchor9000
 */
public class Vector2 {
    public float x, y;

    public Vector2() {}
    
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }
}
