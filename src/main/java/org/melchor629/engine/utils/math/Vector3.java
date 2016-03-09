package org.melchor629.engine.utils.math;

import java.util.Arrays;

/**
 * Vector 3 Class
 * @author melchor9000
 */
public class Vector3 extends Vector<Float> {
    /**
     * Create an empty vector (0, 0, 0)
     */
    public Vector3() {
        super(3, ArithmeticOperations.floats);
    }

    /**
     * Create a vector with values (a, b, c)
     * @param a Value for x
     * @param b Value for y
     * @param c Value for z
     */
    public Vector3(float a, float b, float c) {
        this();
        fillWithValues(Arrays.asList(a, b, c));
    }

    public Vector3 cross(Vector<Float> v) {
        float vx = v.getCoord(1), vy = v.getCoord(2), vz = v.getCoord(3);
        return new Vector3(
            y() * vz - vy * z(),
            z() * vx - vz * x(),
            x() * vy - vx * y());
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

    public Vector3 x(float v) {
        return (Vector3) setCoord(1, v);
    }

    public Vector3 y(float v) {
        return (Vector3) setCoord(2, v);
    }

    public Vector3 z(float v) {
        return (Vector3) setCoord(3, v);
    }
}
