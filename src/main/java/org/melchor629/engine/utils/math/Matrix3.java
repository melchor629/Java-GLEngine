package org.melchor629.engine.utils.math;

/**
 * Matrix 3x3 with operations on them
 */
public class Matrix3 extends Matrix<Float> {
    public Matrix3() {
        super(3, 3, ArithmeticOperations.floats);
    }

    @Override
    public Matrix<Float> setColumn(int col, Vector<Float> vec) {
        setValueAt(1, col, vec.getCoord(0));
        setValueAt(2, col, vec.getCoord(1));
        setValueAt(3, col, vec.getCoord(2));
        return this;
    }

    @Override
    public Matrix<Float> setRow(int row, Vector<Float> vec) {
        setvalueAt(1, row, vec.getCoord(0));
        setvalueAt(2, row, vec.getCoord(1));
        setvalueAt(3, row, vec.getCoord(2));
        return this;
    }

    @Override
    public Vector<Float> getColumn(int col) {
        return new Vector3(valueAt(col, 1), valueAt(col, 2), valueAt(col, 3));
    }

    @Override
    public Vector<Float> getRow(int row) {
        return new Vector3(valueAt(1, row), valueAt(2, row), valueAt(3, row));
    }
}
