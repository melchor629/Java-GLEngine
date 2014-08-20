package org.melchor629.engine.utils.math;

import java.nio.FloatBuffer;

/**
 * Vector 4 Class
 * @author melchor9000
 */
public class vec4 {
    public float x, y, z, w;

    public vec4() { }
    
    public vec4(float a, float b, float c, float d) {
        x = a;
        y = b;
        z = c;
        w = d;
    }
    
    public vec4(vec4 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
        w = vec.w;
    }

    public vec4(vec3 vec, float d) {
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
    public vec4 normalize() {
        float modulo = length();
        x = x / modulo;
        y = y / modulo;
        z = z / modulo;
        w = w / modulo;
        return this;
    }

    public vec4 add(float v) {
        x += v;
        y += v;
        z += v;
        w += v;
        return this;
    }

    public vec4 add(vec4 v) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += v.w;
        return this;
    }

    public vec4 add(vec3 v, float d) {
        x += v.x;
        y += v.y;
        z += v.z;
        w += d;
        return this;
    }

    public vec4 substract(float v) {
        x -= v;
        y -= v;
        z -= v;
        w -= v;
        return this;
    }

    public vec4 substract(vec4 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= v.w;
        return this;
    }

    public vec4 substract(vec3 v, float d) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
        w -= d;
        return this;
    }

    public vec4 product(float v) {
        x *= v;
        y *= v;
        z *= v;
        w *= v;
        return this;
    }

    public vec4 product(mat4 mat) {
        x = GLM.dot(mat.getRow(0), this);
        y = GLM.dot(mat.getRow(1), this);
        z = GLM.dot(mat.getRow(2), this);
        w = GLM.dot(mat.getRow(3), this);
        return this;
    }

    public vec4 divide(float v) {
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
     * Clones this vector. Shortcut of {@code new vec4(this); }
     */
    @Override
    public Object clone() {
        return new vec4(this);
    }

    /**
     * Determine if this vector is equal to other. First try to see
     * if the object is a instance of vec4, after that, try to determine
     * if all values are equal.
     */
    @Override
    public boolean equals(Object vec) {
        if(!(vec instanceof vec4))
            return false;
        vec4 v = (vec4) vec;
        return this.x == v.x && this.y == v.y && this.z == v.z && this.w == v.w;
    }
}
