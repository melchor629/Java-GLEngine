package org.melchor629.engine.utils.math;

/**
 * Matrix 2x2 with operations on them
 */
public class Matrix2 {
/*
    public Matrix2() {
        //super(2, 2, (a, b) -> a+b, (a,b)->a-b, (a,b)->a*b);
    }

    public Matrix2(float a11, float a21, float a12, float a22) {
        this();
        setMatrix(a11, a21, a12, a22);
    }

    public Matrix2(Matrix2 other) {
        this();
        setMatrix(other.valueAt(1, 1), other.valueAt(2, 1),
                  other.valueAt(1, 2), other.valueAt(2, 2));
    }

    public Matrix2(Vector2 col1, Vector2 col2) {
        this();
        setColumns(col1, col2);
    }

    public Matrix<Float> setIdentity() {
        setMatrix(1, 0, 0, 1);
        return this;
    }

    public void setColumns(Vector2 col1, Vector2 col2) {
        setvalueAt(1, 1, col1.x);
        setvalueAt(2, 1, col2.x);
        setvalueAt(1, 2, col1.y);
        setvalueAt(2, 2, col2.y);
    }

    public Vector2 getColumn(int col) {
        return new Vector2(valueAt(col, 1), valueAt(col, 2));
    }

    public void setMatrix(float a11, float a21, float a12, float a22) {
        setvalueAt(1, 1, a11);
        setvalueAt(2, 1, a21);
        setvalueAt(1, 2, a12);
        setvalueAt(2, 2, a22);
    }

    public Matrix2 multiply(Matrix2 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                float valor = 0.f;
                for(int r = 1; r <= 2; r++) {
                    valor += valueAt(r, i) * other.valueAt(j, r);
                }
                setvalueAt(j, i, valor);
            }
        }
        return this;
    }

    public Matrix2 multiply(float value) {
        for(int i = 0; i < 4; i++)
            storage[i] *= value;
        return this;
    }

    public Matrix2 sum(Matrix2 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                matrix[index(i, j)] = matrix[index(i, j)] + other.valueAt(i, j);
            }
        }
        return this;
    }

    public Matrix2 substract(Matrix2 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                matrix[index(i, j)] = matrix[index(i, j)] - other.getValue(i, j);
            }
        }
        return this;
    }

    public Matrix2 transpose() {
        for(int i = 1; i <= 2; i++) {
            for(int j = i; j <= 2; j++) {
                float a = matrix[index(i, j)], b = matrix[index(j, i)];
                matrix[index(i, j)] = b;
                matrix[index(j, i)] = a;
            }
        }
        return this;
    }

    @Override
    public Matrix<Float> clone() {
        return null;
    }

    public Matrix2 invert() {
        float a11 = getValue(1,1), a21 = getValue(2,1), a12 = getValue(1,2), a22 = getValue(2,2);
        float det = 1f/determinant();
        if(det == 0) throw new ArithmeticException("This matrix has no Inverse");
        setValue(1, 1, a22);
        setValue(2, 1, -a12);
        setValue(1, 2, -a21);
        setValue(2, 2, a11);
        return transpose().multiply(det);
    }

    public float determinant() {
        return getValue(1, 1) * getValue(2, 2) - getValue(2, 1) * getValue(1, 2);
    }

    public String toString() {
        return String.format("⎛%+.3f, %+.3f⎞\n"
                           + "⎝%+.3f, %+.3f⎠",
                getValue(1, 1), getValue(2, 1), getValue(1, 2), getValue(2, 2));
    }

    private int index(int columna, int fila) {
        return (columna - 1) * 2 + fila - 1;
    }*/
}
