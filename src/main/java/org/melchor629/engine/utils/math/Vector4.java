package org.melchor629.engine.utils.math;

import java.nio.FloatBuffer;

/**
 * Vector 4 Class
 * @author melchor9000
 */
public class Vector4 implements Cloneable {
    public float x, y, z, w;

    public Vector4() { }
    
    public Vector4(float a, float b, float c, float d) {
        x = a;
        y = b;
        z = c;
        w = d;
    }
    
    public Vector4(Vector4 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        w = vec.w;
    }

    public Vector4(Vector3 vec, float d) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        w = d;
    }

    public float length() {
        return GLM.length(this);
    }

    /**
     * Normalizes this vector
     */
    public Vector4 normalize() {
        float modulo = length();
        x = x / modulo;
        y = y / modulo;
        z = z / modulo;
        w = w / modulo;
        return this;
    }

    public Vector4 add(float v) {
        x += v;
        y += v;
        z += v;
        w += v;
        return this;
    }

    public Vector4 add(Vector4 v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
        return this;
    }

    public Vector4 add(Vector3 v, float d) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += d;
        return this;
    }

    public Vector4 substract(float v) {
        x -= v;
        y -= v;
        z -= v;
        w -= v;
        return this;
    }

    public Vector4 substract(Vector4 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
        return this;
    }

    public Vector4 substract(Vector3 v, float d) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= d;
        return this;
    }

    public Vector4 product(float v) {
        x *= v;
        y *= v;
        z *= v;
        w *= v;
        return this;
    }

    public Vector4 product(Matrix4 mat) {
        x = GLM.dot(mat.getRow(0), this);
        y = GLM.dot(mat.getRow(1), this);
        z = GLM.dot(mat.getRow(2), this);
        w = GLM.dot(mat.getRow(3), this);
        return this;
    }

    public Vector4 divide(float v) {
        x /= v;
        y /= v;
        z /= v;
        w /= v;
        return this;
    }

    public float[] toFloats() {
        return new float[] { x, y, z, w };
    }

    public FloatBuffer fillBuffer(FloatBuffer buffer) {
        if(buffer.capacity() < 4)
            throw new IllegalArgumentException("Buffer should have atleast 4 floats of capacity, this one has " + buffer.capacity());
        buffer.position(0);
        return buffer.put(toFloats()).compact();
    }

    /**
     * Returns a representation of this vector
     */
    @Override
    public String toString() {
        return String.format("(%+.3f, %+.3f, %+.3f, %+.3f)", x, y, z, w);
    }

    /**
     * Clones this vector. Shortcut of {@code new Vector4(this); }
     */
    @Override
    public Vector4 clone() {
        try {
            return (Vector4) super.clone();
        } catch(Exception e) {
            RuntimeException r = new RuntimeException();
            r.initCause(e);
            throw r;
        }
    }

    /**
     * Determine if this vector is equal to other. First try to see
     * if the object is a instance of Vector4, after that, try to determine
     * if all values are equal.
     */
    @Override
    public boolean equals(Object vec) {
        if(!(vec instanceof Vector4))
            return false;
        Vector4 v = (Vector4) vec;
        return this.x == v.x && this.y == v.y && this.z == v.z && this.w == v.w;
    }

    @Override
    public int hashCode() {
        return (int) Math.floor(this.x * 29 * 29 * 29 + this.y * 29 * 29 + this.z * 29 + this.w);
    }
}
