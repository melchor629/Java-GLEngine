package org.melchor629.engine.utils.math;

/**
 * Matrix 3x3 with operations on them
 */
public class Matrix3 {
    private float matrix[];


    public Matrix3() {
        matrix = new float[9];
    }

    public Matrix3(float a11, float a21, float a31, float a12, float a22, float a32, float a13, float a23, float a33) {
        this();
        setMatrix(a11, a21, a31, a12, a22, a32, a13, a23, a33);
    }

    public Matrix3(Matrix2 other) {
        this();
        setMatrix(other.getValue(1, 1), other.getValue(2, 1), other.getValue(3, 1),
                  other.getValue(1, 2), other.getValue(2, 2), other.getValue(3, 2),
                  other.getValue(1, 3), other.getValue(2, 3), other.getValue(3, 3));
    }

    public Matrix3(Vector2 col1, Vector2 col2) {
        this();
        setColumns(col1, col2);
    }

    public Matrix3 setIdentity() {
        setMatrix(1, 0, 0, 0, 1, 0, 0, 0, 1);
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

    public Vector3 getColumn(int col) {
        return new Vector3(getValue(col, 1), getValue(col, 2), getValue(col, 3));
    }

    public void setMatrix(float a11, float a21, float a31, float a12, float a22, float a32, float a13, float a23, float a33) {
        matrix[index(1, 1)] = a11;
        matrix[index(2, 1)] = a21;
        matrix[index(3, 1)] = a31;
        matrix[index(1, 2)] = a12;
        matrix[index(2, 2)] = a22;
        matrix[index(3, 2)] = a32;
        matrix[index(1, 3)] = a13;
        matrix[index(2, 3)] = a23;
        matrix[index(3, 3)] = a33;
    }

    public Matrix3 multiply(Matrix3 other) {
        for(int i = 1; i <= 3; i++) {
            for(int j = 1; j <= 3; j++) {
                float valor = 0.f;
                for(int r = 1; r <= 3; r++) {
                    valor += matrix[index(r, i)] * other.getValue(j, r);
                }
                matrix[index(j, i)] = valor;
            }
        }
        return this;
    }

    public Matrix3 multiply(float value) {
        for(int i = 0; i < 4; i++)
            matrix[i] *= value;
        return this;
    }

    public Matrix3 sum(Matrix3 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                matrix[index(i, j)] = matrix[index(i, j)] + other.getValue(i, j);
            }
        }
        return this;
    }

    public Matrix3 substract(Matrix3 other) {
        for(int i = 1; i <= 2; i++) {
            for(int j = 1; j <= 2; j++) {
                matrix[index(i, j)] = matrix[index(i, j)] - other.getValue(i, j);
            }
        }
        return this;
    }

    public Matrix3 transpose() {
        for(int i = 1; i <= 3; i++) {
            for(int j = i; j <= 3; j++) {
                float a = matrix[index(i, j)], b = matrix[index(j, i)];
                matrix[index(i, j)] = b;
                matrix[index(j, i)] = a;
            }
        }
        return this;
    }

    public float determinant() {
        float a = getValue(1, 1), b = getValue(2, 1), c = getValue(3, 1), d = getValue(1, 2), e = getValue(2, 2),
                f = getValue(3, 2), g = getValue(1, 3), h = getValue(2, 3), i = getValue(3, 3);
        return a*e*i + b*g*f + c*d*h - c*e*g - b*d*i - a*h*f;
    }

    public Matrix3 invert() {
        return this; //TODO
    }

    public String toString() {
        return String.format("⎛%+.3f, %+.3f, %+.3f⎞\n"
                           + "⎥%+.3f, %+.3f, %+.3f⎥\n"
                           + "⎝%+.3f, %+.3f, %+.3f⎠",
                getValue(1, 1), getValue(2, 1), getValue(3, 1),
                getValue(1, 2), getValue(2, 2), getValue(3, 2),
                getValue(1, 3), getValue(2, 3), getValue(3, 3));
    }

    private int index(int columna, int fila) {
        return (columna - 1) * 3 + fila - 1;
    }
}
