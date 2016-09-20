package org.melchor629.engine.utils.math;

import java.util.Collection;
import java.util.function.Function;

/**
 * Generic abstract vector
 */
public class Vector<T extends Number> extends Point<T> implements Cloneable {
    private T mod = null;

    /**
     * Creates a new Vector with a determined number of coordinates and using
     * the given operations over numbers
     * @param coords number of coordinates
     * @param ops operations over numbers
     */
    public Vector(int coords, ArithmeticOperations<T> ops) {
        super(coords, ops);
    }

    /**
     * Sets the n coordinate value to the one given. Coordinate x, or the first
     * one, is n = 1.<br>V<sub>n</sub> = {@code value}
     * @param n number of the coordinate
     * @param value value to be set
     * @return itself
     * @throws IndexOutOfBoundsException if the coordinate is invalid
     */
    public Vector<T> setCoord(int n, T value) {
        mod = null;
        return (Vector<T>) super.setCoord(n, value);
    }

    /**
     * Fills the vector's coordinates with the given in the {@link Collection}.
     * The {@link Collection} must have at least {@link #getNumCoords()} values.
     * @param values values to be set in coordinates of the point
     * @return itself
     * @throws IllegalArgumentException if collection has less values than needed
     */
    public Vector<T> fillWithValues(Collection<T> values) {
        mod = null;
        return (Vector<T>) super.fillWithValues(values);
    }

    /**
     * Fills the vector's coordinates with the given in the {@code values}.
     * The array must have at least {@link #getNumCoords()} values.
     * @param values values to be set in coordinates of the point
     * @return itself
     * @throws IllegalArgumentException if collection has less values than needed
     */
    public Vector<T> fillWithValues(T... values) {
        mod = null;
        return (Vector<T>) super.fillWithValues(values);
    }

    /**
     * Adds the value of this vector to {@code v}, and saves the value into this
     * vector. Is like P = P + V.
     * @param v the other vector
     * @return itself
     * @throws ArithmeticException if the number of coordinates mismatch
     */
    public Vector<T> add(Point<T> v) {
        mod = null;
        return (Vector<T>) super.add(v);
    }

    /**
     * Substracts the value of this vector to {@code v}, and saves the new value
     * into this vector. Is like P = P - V.
     * @param v the other vector
     * @return itself
     * @throws ArithmeticException if the number of coordinates mismatch
     */
    public Vector<T> substract(Point<T> v) {
        mod = null;
        return (Vector<T>) super.substract(v);
    }

    /**
     * Multiplies the value of this vector to the scalar value, and saves the new
     * value into this vector. Is like V = x·V.
     * @param scalar scalar to multiply
     * @return itself
     */
    public Vector<T> product(T scalar) {
        mod = null;
        return (Vector<T>) super.product(scalar);
    }

    /**
     * Makes the dot product between this and the other vector and returns its
     * result
     * @param v the other vector
     * @return result of the dot product
     */
    public T dot(Vector<T> v) {
        checkSizes(v);

        T dot = ops.convert(0);
        for(int coord = 1; coord <= coords; coord++) {
            dot = ops.add(dot, ops.mul(getCoord(coord), v.getCoord(coord)));
        }

        mod = null;
        return dot;
    }

    /**
     * Computes the module (<i>length</i>) of this vector
     * @return vector module
     */
    public T module() {
        if(mod == null) {
            mod = ops.convert(Math.sqrt(dot(this).doubleValue()));
        }
        return mod;
    }

    /**
     * Calculates the distance between thos vector and the other one
     * @param vec the other vector
     * @return distance of between the vectors
     * @throws ArithmeticException if the other vector has not the same
     * coordinates number
     */
    public T distance(Vector<T> vec) {
        checkSizes(vec);

        double dist = 0;
        for(int i = 1; i <= coords; i++) {
            T diff = ops.sub(getCoord(i), vec.getCoord(i));
            dist += Math.pow(diff.doubleValue(), 2);
        }

        return ops.convert(Math.sqrt(dist));
    }

    /**
     * Normalizes this vector
     * @return itself
     */
    public Vector<T> normalize() {
        T module = module();
        return apply((n) -> ops.div(n, module));
    }

    /**
     * Negates the vector
     * @return itself
     */
    public Vector<T> negate() {
        return apply((n) -> ops.mul(ops.convert(-1), n));
    }

    /**
     * Calculates the angle in radians formed between this vector and the
     * given one
     * @param vector the other vector
     * @return angle in radians
     * @throws ArithmeticException if the number of coordinates does not match
     */
    public double angle(Vector<T> vector) {
        T dot = dot(vector);
        T uv = ops.mul(module(), vector.module());
        double cos = dot.doubleValue() / uv.doubleValue();
        return Math.acos(cos);
    }

    /**
     * Applies a function to every value in the Vector
     * @param func function to apply
     * @return itself
     */
    public Vector<T> apply(Function<T, T> func) {
        mod = null;
        return (Vector<T>) super.apply(func);
    }

    /**
     * Clones the point
     * @return the new point
     */
    @Override
    @SuppressWarnings("unchecked")
    public Vector<T> clone() {
        try {
            Vector<T> newVector = (Vector<T>) super.clone();
            newVector.mod = null;
            return newVector;
        } catch(Exception e) {
            RuntimeException r = new RuntimeException();
            r.initCause(e);
            throw r;
        }
    }

    /**
     * Compares equality of an object and this one.
     * <p>
     * A Vector is floatEquals to another if and only if is an instance of a Vector
     * or another subclass, the type of the numbers are floatEquals and the contents
     * of the both points are equal.
     * @param o another object
     * @return true if are the same vector
     */
    @Override
    public boolean equals(Object o) {
        return o instanceof Vector && super.equals(o);
    }

    /**
     * Computes the hash code value for this vector.
     * @return vector' hash code
     */
    @Override
    public int hashCode() {
        return 43 * super.hashCode();
    }
}
