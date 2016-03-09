package org.melchor629.engine.utils.math;

/**
 * Matrix 4x4 Class
 * @author melchor9000
 */
public class Matrix4 extends Matrix<Float> {
    public Matrix4() {
        super(4, 4, ArithmeticOperations.floats);
    }

    @Override
    public Matrix<Float> setColumn(int col, Vector<Float> vec) {
        setValueAt(1, col, vec.getCoord(1));
        setValueAt(2, col, vec.getCoord(2));
        setValueAt(3, col, vec.getCoord(3));
        setValueAt(4, col, vec.getCoord(4));
        return this;
    }

    @Override
    public Matrix<Float> setRow(int row, Vector<Float> vec) {
        setvalueAt(1, row, vec.getCoord(1));
        setvalueAt(2, row, vec.getCoord(2));
        setvalueAt(3, row, vec.getCoord(3));
        setvalueAt(4, row, vec.getCoord(4));
        return this;
    }

    @Override
    public Vector<Float> getColumn(int col) {
        return new Vector4(valueAt(col, 1), valueAt(col, 2), valueAt(col, 3), valueAt(col, 4));
    }

    @Override
    public Vector<Float> getRow(int row) {
        return new Vector4(valueAt(1, row), valueAt(2, row), valueAt(3, row), valueAt(4, row));
    }
}
