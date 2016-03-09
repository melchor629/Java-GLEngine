package org.melchor629.engine.utils.math;

import java.util.function.Function;

/**
 * Generic Arithmetic Operations
 */
public class ArithmeticOperations<T extends Number> {
    private ArithmeticOperation<T> add, sub, mul, div;
    private Function<Number, T> f;

    public static final ArithmeticOperations<Float> floats;
    public static final ArithmeticOperations<Double> doubles;

    static {
        floats = new ArithmeticOperations<Float>()
            .setAdd((a, b) -> a + b)
            .setSubstract((a, b) -> a - b)
            .setMultiply((a, b) -> a * b)
            .setDivide((a, b) -> a / b)
            .setConvert(Number::floatValue);
        doubles = new ArithmeticOperations<Double>()
            .setAdd((a, b) -> a + b)
            .setSubstract((a, b) -> a - b)
            .setMultiply((a, b) -> a * b)
            .setDivide((a, b) -> a / b)
            .setConvert(Number::doubleValue);
    }

    /**
     * Function that calculates add operation over two operands
     * @param add add function
     * @return this
     */
    public ArithmeticOperations<T> setAdd(ArithmeticOperation<T> add) {
        this.add = add;
        return this;
    }

    /**
     * Function that calculates substract operation over two operands
     * @param sub substraction function
     * @return this
     */
    public ArithmeticOperations<T> setSubstract(ArithmeticOperation<T> sub) {
        this.sub = sub;
        return this;
    }

    /**
     * Function that calculates multiply operation over two operands
     * @param mul multiply function
     * @return this
     */
    public ArithmeticOperations<T> setMultiply(ArithmeticOperation<T> mul) {
        this.mul = mul;
        return this;
    }

    /**
     * Function that calculates division operation over two operands
     * @param div division function
     * @return this
     */
    public ArithmeticOperations<T> setDivide(ArithmeticOperation<T> div) {
        this.div = div;
        return this;
    }

    /**
     * Function that convert some number into T type
     * @param f conversion function
     * @return this
     */
    public ArithmeticOperations<T> setConvert(Function<Number, T> f) {
        this.f = f;
        return this;
    }

    /**
     * Operates addition over two operands
     * @param a First operand
     * @param b Second operand
     * @return result of the addition
     */
    public T add(T a, T b) {
        return add.operate(a, b);
    }

    /**
     * Operates substraction over two operands
     * @param a First operand
     * @param b Second operand
     * @return result of the substraction
     */
    public T sub(T a, T b) {
        return sub.operate(a, b);
    }

    /**
     * Operates multiply over two operands
     * @param a First operand
     * @param b Second operand
     * @return result of the multiply
     */
    public T mul(T a, T b) {
        return mul.operate(a, b);
    }

    /**
     * Operates division over two operands
     * @param a First operand
     * @param b Second operand
     * @return result of the division
     */
    public T div(T a, T b) {
        return div.operate(a, b);
    }

    /**
     * Convert a number into T type
     * @param number number to convert
     * @return number in T type
     */
    public T convert(Number number) {
        return f.apply(number);
    }
}
