package org.melchor629.engine.utils.math;

/**
 * Vector 2 Class
 * @author melchor9000
 */
public class vec2 {
    public float x, y;

    public vec2() {}
    
    public vec2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public String toString() {
        return String.format("(%.3f, %.3f)", x, y);
    }
}
