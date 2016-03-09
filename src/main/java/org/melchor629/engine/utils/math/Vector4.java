package org.melchor629.engine.utils.math;

import java.util.Arrays;

/**
 * Vector 4 Class
 * @author melchor9000
 */
public class Vector4 extends Vector<Float> {
    public Vector4() {
        super(4, ArithmeticOperations.floats);
    }
    
    public Vector4(float a, float b, float c, float d) {
        this();
        fillWithValues(Arrays.asList(a, b, c, d));
    }

    public Vector4(Vector3 vec, float d) {
        this();
        fillWithValues(Arrays.asList(vec.x(), vec.y(), vec.z(), d));
    }

    public float x() {
        return getCoord(1);
    }

    public float y() {
        return getCoord(2);
    }

    public float z() {
        return getCoord(3);
    }

    public float w() {
        return getCoord(4);
    }

    public Vector4 x(float v) {
        return (Vector4) setCoord(1, v);
    }

    public Vector4 y(float v) {
        return (Vector4) setCoord(2, v);
    }

    public Vector4 z(float v) {
        return (Vector4) setCoord(3, v);
    }

    public Vector4 w(float v) {
        return (Vector4) setCoord(4, v);
    }
}
