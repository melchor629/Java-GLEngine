package org.melchor629.engine.utils.math;

/**
 * Matrix 2x2 with operations on them
 */
public class Matrix2 extends Matrix<Float> {
    public Matrix2() {
        super(2, 2, ArithmeticOperations.floats);
    }

    @Override
    public Matrix<Float> setColumn(int col, Vector<Float> vec) {
        setValueAt(1, col, vec.getCoord(0));
        setValueAt(2, col, vec.getCoord(1));
        return this;
    }

    @Override
    public Matrix<Float> setRow(int row, Vector<Float> vec) {
        setvalueAt(1, row, vec.getCoord(0));
        setvalueAt(2, row, vec.getCoord(1));
        return this;
    }

    @Override
    public Vector<Float> getColumn(int col) {
        return new Vector2(valueAt(col, 1), valueAt(col, 2));
    }

    @Override
    public Vector<Float> getRow(int row) {
        return new Vector2(valueAt(1, row), valueAt(2, row));
    }
}
