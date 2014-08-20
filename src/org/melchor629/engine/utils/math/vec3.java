package org.melchor629.engine.utils.math;

import java.nio.FloatBuffer;

/**
 * Vector 3 Class
 * @author melchor9000
 */
public class vec3 {
    /** component of the vector **/
    public float x, y, z;

    /**
     * Create an empty vector (0, 0, 0)
     */
    public vec3() { }

    /**
     * Create a vector with values (a, b, c)
     * @param a Value for x
     * @param b Value for y
     * @param c Value for z
     */
    public vec3(float a, float b, float c) {
        x = a;
        y = b;
        z = c;
    }

    /**
     * Create a copy of the vector
     * @param vec Vector to be copied
     */
    public vec3(vec3 vec) {
        x = vec.x;
        y = vec.y;
        z = vec.z;
    }

    /**
     * Create a copy of the vector (except w component)
     * @param obj Vector to be copied
     */
    public vec3(vec4 obj) {
        x = obj.x;
        y = obj.y;
        z = obj.z;
    }

    /**
     * @return Computes the length value of the vector
     */
    public float length() {
        return GLM.length(this);
    }

    /**
     * Normalizes this vector
     */
    public void normalize() {
        float modulo = length();
        x = x / modulo;
        y = y / modulo;
        z = z / modulo;
    }

    public void add(float v) {
        x += v;
        y += v;
        z += v;
    }
    
    public void add(vec3 v) {
        x += v.x;
        y += v.y;
        z += v.z;
    }

    public void substract(float v) {
        add(- v);
    }

    public void substract(vec3 v) {
        x -= v.x;
        y -= v.y;
        z -= v.z;
    }

    public void product(float v) {
        x *= v;
        y *= v;
        z *= v;
    }

    public void divide(float v) {
        x /= v;
        y /= v;
        z /= v;
    }

    public void cross(vec3 v) {
        x = y * v.z - v.y * z;
        y = v.x * z - x * v.z;
        z = x * v.y - y * v.x;
    }

    public float[] toFloats() {
        return new float[] { x, y, z };
    }

    public FloatBuffer fillBuffer(FloatBuffer buffer) {
        if(buffer.capacity() < 3)
            throw new IllegalArgumentException("Buffer should have atleast 3 floats of capacity, this one has " + buffer.capacity());
        buffer.position(0);
        return buffer.put(toFloats()).compact();
    }

    /**
     * Clones this vector. Shortcut of {@code new vec3(this); }
     */
    @Override
    public vec3 clone() {
        return new vec3(this);
    }

    /**
     * Returns a representation of this vector
     */
    @Override
    public String toString() {
        return String.format("(%+.3f, %+.3f, %+.3f)", x, y, z);
    }

    /**
     * Determine if this vector is equal to other. First try to see
     * if the object is a instance of vec3, after that, try to determine
     * if all values are equal.
     */
    @Override
    public boolean equals(Object vec) {
        if(!(vec instanceof vec3))
            return false;
        vec3 v = (vec3) vec;
        return this.x == v.x && this.y == v.y && this.z == v.z;
    }

}
