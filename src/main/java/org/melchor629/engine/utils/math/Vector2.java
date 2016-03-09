package org.melchor629.engine.utils.math;

import java.util.Arrays;

/**
 * Vector 2 Class
 * @author melchor9000
 */
public class Vector2 extends Vector<Float> {
    public Vector2() {
        super(2, ArithmeticOperations.floats);
    }
    
    public Vector2(float x, float y) {
        this();
        fillWithValues(Arrays.asList(x, y));
    }

    public float x() {
        return getCoord(1);
    }

    public float y() {
        return getCoord(2);
    }

    public Vector2 x(float v) {
        return (Vector2) setCoord(1, v);
    }

    public Vector2 y(float v) {
        return (Vector2) setCoord(2, v);
    }
}
