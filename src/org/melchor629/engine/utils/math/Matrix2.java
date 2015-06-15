package org.melchor629.engine.utils.math;

/**
 * Matrix 2x2 with operations on them
 */
public class Matrix2 {
    private float matrix[];

    public Matrix2() {
        matrix = new float[4];
    }

    public Matrix2(float a11, float a21, float a12, float a22) {
        this();
        setMatrix(a11, a21, a12, a22);
    }

    public Matrix2(Matrix2 other) {
        this();
        setMatrix(other.getValue(1, 1), other.getValue(2, 1),
                  other.getValue(1, 2), other.getValue(2, 2));
    }

    public Matrix2(Vector2 col1, Vector2 col2) {
        this();
        setColumns(col1, col2);
    }

    public Matrix2 setIdentity() {
        setMatrix(1, 0, 0, 1);
        return this;
    }

    public float getValue(int columna, int fila) {
        return matrix[index(columna, fila)];
    }

    public void setValue(int columna, int fila, float valor) {
        matrix[index(columna, fila)] = valor;
    }

    public void setColumns(Vector2 col1, Vector2 col2) {
        setValue(1, 1, col1.x); setValue(2, 1, col2.x);
        setValue(1, 2, col1.y); setValue(2, 2, col2.y);
    }

    public Vector2 getColumn(int col) {
        return new Vector2(getValue(col, 1), getValue(col, 2));
    }

    public void setMatrix(float a11, float a21, float a12, float a22) {
        matrix[index(1, 1)] = a11;
        matrix[index(2, 1)] = a21;
        matrix[index(1, 2)] = a12;
        matrix[index(2, 2)] = a22;
    }

    public Matrix2 multiply(Matrix2 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                float valor = 0.f;
                for(int r = 1; r <= 2; r++) {
                    valor += matrix[index(r, i)] * other.getValue(j, r);
                }
                matrix[index(j, i)] = valor;
            }
        }
        return this;
    }

    public Matrix2 multiply(float value) {
        for(int i = 0; i < 4; i++)
            matrix[i] *= value;
        return this;
    }

    public Matrix2 sum(Matrix2 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                matrix[index(i, j)] = matrix[index(i, j)] + other.getValue(i, j);
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
    }
}
