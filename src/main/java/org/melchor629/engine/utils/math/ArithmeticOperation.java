package org.melchor629.engine.utils.math;

/**
 * Generic Arithmetic Operation
 */
public interface ArithmeticOperation<T extends Number> {
    /**
     * Applies the operation over two operands
     * @param a First operand
     * @param b Second operand
     * @return result of operation
     */
    T operate(T a, T b);
}
