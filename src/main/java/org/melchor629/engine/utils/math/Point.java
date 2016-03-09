package org.melchor629.engine.utils.math;

import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.function.Function;

/**
 * Abstract generic point class
 */
public class Point<T extends Number> implements Cloneable {
    protected T[] storage;
    protected int coords;
    protected ArithmeticOperations<T> ops;

    /**
     * Creates a new Point with a number of coordinates using the given
     * operation over numbers.
     * @param coords number of coordinates
     * @param ops operations over numbers
     */
    @SuppressWarnings("unchecked")
    public Point(int coords, ArithmeticOperations<T> ops) {
        this.coords = coords;
        this.ops = ops;
        this.storage = (T[]) new Number[coords];

        for(int i = 0; i < coords; i++) {
            storage[i] = ops.convert(0);
        }
    }

    /**
     * @return the number of coordinates the point has
     */
    public int getNumCoords() {
        return coords;
    }

    /**
     * Obtains the n coordinate value. Coordinate x, or the first, is n = 1.
     * @param n number of the coordinate
     * @return value of P<sub>n</sub>
     * @throws IndexOutOfBoundsException if the coordinate is invalid
     */
    public T getCoord(int n) {
        if(n < 1 || n > coords)
            throw new IndexOutOfBoundsException("Invalid coordinate number");
        return storage[n - 1];
    }

    /**
     * Sets the n coordinate value to the one given. Coordinate x, or the first
     * one, is n = 1.<br>P<sub>n</sub> = {@code value}
     * @param n number of the coordinate
     * @param value value to be set
     * @return itself
     * @throws IndexOutOfBoundsException if the coordinate is invalid
     */
    public Point<T> setCoord(int n, T value) {
        if(n < 1 || n > coords)
            throw new IndexOutOfBoundsException("Invalid coordinate number");
        storage[n - 1] = value;
        return this;
    }

    /**
     * Fills the point's coordinates with the given in the {@link Collection}.
     * The {@link Collection} must have at least {@link #getNumCoords()} values.
     * @param values values to be set in coordinates of the point
     * @return itself
     * @throws IllegalArgumentException if collection has less values than needed
     */
    public Point<T> fillWithValues(Collection<T> values) {
        if(values.size() < storage.length)
            throw new IllegalArgumentException("Collection has less values than needed");

        Iterator<T> iterator = values.iterator();
        for(int coord = 1; coord <= coords; coord++) {
            setCoord(coord, iterator.next());
        }

        return this;
    }

    /**
     * Fills the point's coordinates with the given in the {@code values} array.
     * The array must have at least {@link #getNumCoords()} values.
     * @param values values to be set in coordinates of the point
     * @return itself
     * @throws IllegalArgumentException if collection has less values than needed
     */
    public Point<T> fillWithValues(T... values) {
        if(values.length < storage.length)
            throw new IllegalArgumentException("Collection has less values than needed");

        for(int coord = 1; coord <= coords; coord++) {
            setCoord(coord, values[coord - 1]);
        }

        return this;
    }

    /**
     * Adds the value of this point to {@code v}, and saves the value into this
     * point. Is like P = P + V.
     * @param v the other point
     * @return itself
     * @throws ArithmeticException if the number of coordinates mismatch
     */
    public Point<T> add(Point<T> v) {
        checkSizes(v);

        for(int coord = 1; coord <= coords; coord++) {
            setCoord(coord, ops.add(getCoord(coord), v.getCoord(coord)));
        }

        return this;
    }

    /**
     * Substracts the value of this point to {@code v}, and saves the new value
     * into this point. Is like P = P - V.
     * @param v the other point
     * @return itself
     * @throws ArithmeticException if the number of coordinates mismatch
     */
    public Point<T> substract(Point<T> v) {
        checkSizes(v);

        for(int coord = 1; coord <= coords; coord++) {
            setCoord(coord, ops.sub(getCoord(coord), v.getCoord(coord)));
        }

        return this;
    }

    /**
     * Multiplies the value of this point to the scalar value, and saves the new
     * value into this point. Is like P = x·P.
     * @param scalar scalar to multiply
     * @return itself
     */
    public Point<T> product(T scalar) {
        return apply((n) -> ops.mul(n, scalar));
    }

    /**
     * Makes a vector from this point to the other one {@code b}. Ū = B - P.
     * @param b the other point
     * @return a generic vector
     */
    @SuppressWarnings("unchecked")
    public Vector<T> makeVector(Point<T> b) {
        return new Vector<>(coords, ops).fillWithValues(
            ops.sub(b.getCoord(1), getCoord(1)),
            ops.sub(b.getCoord(2), getCoord(2)),
            ops.sub(b.getCoord(3), getCoord(3))
        );
    }

    /**
     * Applies a function to every value in the Point
     * @param func function to apply
     * @return itself
     */
    public Point<T> apply(Function<T, T> func) {
        for(int i = 1; i <= coords; i++) {
            setCoord(i, func.apply(getCoord(i)));
        }
        return this;
    }

    /**
     * Clones the point
     * @return the new point
     */
    @Override
    @SuppressWarnings("unchecked")
    public Point<T> clone() {
        try {
            Point<T> newPoint = (Point<T>) super.clone();
            newPoint.storage = Arrays.copyOf(storage, storage.length);
            return newPoint;
        } catch(Exception e) {
            RuntimeException r = new RuntimeException();
            r.initCause(e);
            throw r;
        }
    }

    /**
     * Compares equality of an object and this one.
     * <p>
     * A point is floatEquals to another if and only if is an instance of a Point
     * or another subclass, the type of the numbers are floatEquals and the contents
     * of the both points are equal.
     * @param o another object
     * @return true if are the same point
     */
    @Override
    public boolean equals(Object o) {
        if(o instanceof Point) {
            Point v = (Point) o;
            if(Matrix.getNumberClass(storage[0]).equals(Matrix.getNumberClass(v.storage[0])) && coords == v.coords) {
                for(int i = 1; i <= coords; i++) {
                    if(!getCoord(i).equals(v.getCoord(i)))
                        return false;
                }
                return true;
            }
        }
        return false;
    }

    /**
     * Computes the hash code value for this point.
     * @return hash code
     */
    @Override
    public int hashCode() {
        int hash = coords;
        for(int i = 0; i < coords; i++) {
            hash = (storage[i].hashCode() + hash) * 29;
        }
        return hash;
    }

    /**
     * Returns a String representation of the point.
     * @return point as string
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("(");
        for(int i = 0; i < storage.length - 1; i++) {
            sb.append(storage[i]).append(", ");
        }
        sb.append(storage[storage.length-1]).append(")");
        return sb.toString();
    }

    /**
     * Checks whether this point and {@code v} have the same coordinates
     * @param v another point
     * @throws ArithmeticException if the number of coordinates mismatch
     */
    protected void checkSizes(Point<T> v) {
        if(coords != v.coords)
            throw new ArithmeticException("Different number of coordinates");
    }
}
