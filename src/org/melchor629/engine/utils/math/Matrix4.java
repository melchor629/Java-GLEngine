package org.melchor629.engine.utils.math;

import java.nio.FloatBuffer;

/**
 * Matrix 4x4 Class
 * @author melchor9000
 */
public class Matrix4 {
    /** Matrix as array of floats **/
    public float[][] matrix = new float[4][4];
    
    /**
     * Create a Identity Matrix 4x4
     */
    public Matrix4() {
        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[3][3] = 1;
    }
    
    /**
     * Create a copy of the Matrix given
     * @param m Matrix to be copied
     */
    public Matrix4(Matrix4 m) {
        for(int i = 0; i < 4; i++)
            this.setColumn(i, m.getColumn(i));
    }

    /**
     * Create a matrix with the values given
     */
    public Matrix4(float v00, float v01, float v02, float v03, float v10, float v11, float v12, float v13, float v20, float v21, float v22, float v23, float v30, float v31, float v32, float v33) {
        matrix[0][0] = v00;
        matrix[0][1] = v01;
        matrix[0][2] = v02;
        matrix[0][3] = v03;
        matrix[1][0] = v10;
        matrix[1][1] = v11;
        matrix[1][2] = v12;
        matrix[1][3] = v13;
        matrix[2][0] = v20;
        matrix[2][1] = v21;
        matrix[2][2] = v22;
        matrix[2][3] = v23;
        matrix[3][0] = v30;
        matrix[3][1] = v31;
        matrix[3][2] = v32;
        matrix[3][3] = v33;
    }

    /**
     * Create a matrix filled all with the value v
     * @param v Value
     */
    public Matrix4(float v) {
        matrix[0][0] = v;
        matrix[0][1] = v;
        matrix[0][2] = v;
        matrix[0][3] = v;
        matrix[1][0] = v;
        matrix[1][1] = v;
        matrix[1][2] = v;
        matrix[1][3] = v;
        matrix[2][0] = v;
        matrix[2][1] = v;
        matrix[2][2] = v;
        matrix[2][3] = v;
        matrix[3][0] = v;
        matrix[3][1] = v;
        matrix[3][2] = v;
        matrix[3][3] = v;
    }

    /**
     * Get the row number {@code row} as {@link Vector4}
     * @param row Row
     * @return the row as vector
     */
    public Vector4 getRow(int row) {
        Vector4 v = new Vector4();
        v.x = matrix[row][0];
        v.y = matrix[row][1];
        v.z = matrix[row][2];
        v.w = matrix[row][3];
        return v;
    }

    /**
     * Set the row with values in {@code v}: <i>(v.x, v.y, v.z, v.w)</i>
     * @param row Row to be changed
     * @param v The new row
     */
    public void setRow(int row, Vector4 v) {
        matrix[row][0] = v.x;
        matrix[row][1] = v.y;
        matrix[row][2] = v.z;
        matrix[row][3] = v.w;
    }

    /**
     * Set the row with the float values: <i>(a, b, c, d)</i>
     * @param row Row to be changed
     * @param a Value 1
     * @param b Value 2
     * @param c Value 3
     * @param d Value 4
     */
    public void setRow(int row, float a, float b, float c, float d) {
        matrix[row][0] = a;
        matrix[row][1] = b;
        matrix[row][2] = c;
        matrix[row][3] = d;
    }

    /**
     * Set the row with values from the vector and the float:
     * <i>(v.x, v.y, v.z, d)</i>
     * @param row Row to be changed
     * @param v Part of the new row
     * @param d Last value to be changed
     */
    public void setRow(int row, Vector3 v, float d) {
        matrix[row][0] = v.x;
        matrix[row][1] = v.y;
        matrix[row][2] = v.z;
        matrix[row][3] = d;
    }

    /**
     * Obtains the column as a {@link Vector4}
     * @param col Column
     * @return The column as vector
     */
    public Vector4 getColumn(int col) {
        Vector4 vec = new Vector4();
        vec.x = matrix[0][col];
        vec.y = matrix[1][col];
        vec.z = matrix[2][col];
        vec.w = matrix[3][col];
        return vec;
    }

    /**
     * Set the column with values in {@code v}: <i>(v.x, v.y, v.z, v.w)</i>
     * @param col Column to be changed
     * @param vec The new column
     */
    public void setColumn(int col, Vector4 vec) {
        matrix[0][col] = vec.x;
        matrix[1][col] = vec.y;
        matrix[2][col] = vec.z;
        matrix[3][col] = vec.w;
    }

    /**
     * Set the column with the float values: <i>(a, b, c, d)</i>
     * @param col Column to be changed
     * @param a Value 1
     * @param b Value 2
     * @param c Value 3
     * @param d Value 4
     */
    public void setColumn(int col, float a, float b, float c, float d) {
        matrix[0][col] = a;
        matrix[1][col] = b;
        matrix[2][col] = c;
        matrix[3][col] = d;
    }

    /**
     * Set the column with values from the vector and the float:
     * <i>(v.x, v.y, v.z, d)</i>
     * @param col Column to be changed
     * @param vec Part of the new column
     * @param d Last value to be changed
     */
    public void setColumn(int col, Vector3 vec, float d) {
        matrix[0][col] = vec.x;
        matrix[1][col] = vec.y;
        matrix[2][col] = vec.z;
        matrix[3][col] = d;
    }

    /**
     * Get the value of the row and column
     * @param row Row
     * @param col Column
     * @return The value at matrix[row][column]
     */
    public float get(int row, int col) {
        return matrix[row][col];
    }

    /**
     * Set the value of the row and column
     * @param row Row
     * @param col Column
     * @param value Value to be set
     */
    public void set(int row, int col, float value) {
        matrix[row][col] = value;
    }

    /**
     * Transposes the matrix
     */
    public void transpose() {
        Vector4 c0 = getColumn(0), c1 = getColumn(1), c2 = getColumn(2), c3 = getColumn(3);
        setRow(0, c0);
        setRow(1, c1);
        setRow(2, c2);
        setRow(3, c3);
    }

    /**
     * Computes the determinant of the matrix (shortcut of {@link GLM#determinant(Matrix4)})
     * @return the matrix determinant
     */
    public float determinant() {
        return GLM.determinant(this);
    }

    /**
     * Computes the inverse of the matrix and sets that matrix to it (the inverse)
     */
    public void inverse() {
        float SubFactor00 = matrix[2][2] * matrix[3][3] - matrix[3][2] * matrix[2][3];
        float SubFactor01 = matrix[2][1] * matrix[3][3] - matrix[3][1] * matrix[2][3];
        float SubFactor02 = matrix[2][1] * matrix[3][2] - matrix[3][1] * matrix[2][2];
        float SubFactor03 = matrix[2][0] * matrix[3][3] - matrix[3][0] * matrix[2][3];
        float SubFactor04 = matrix[2][0] * matrix[3][2] - matrix[3][0] * matrix[2][2];
        float SubFactor05 = matrix[2][0] * matrix[3][1] - matrix[3][0] * matrix[2][1];
        float SubFactor06 = matrix[1][2] * matrix[3][3] - matrix[3][2] * matrix[1][3];
        float SubFactor07 = matrix[1][1] * matrix[3][3] - matrix[3][1] * matrix[1][3];
        float SubFactor08 = matrix[1][1] * matrix[3][2] - matrix[3][1] * matrix[1][2];
        float SubFactor09 = matrix[1][0] * matrix[3][3] - matrix[3][0] * matrix[1][3];
        float SubFactor10 = matrix[1][0] * matrix[3][2] - matrix[3][0] * matrix[1][2];
        float SubFactor11 = matrix[1][1] * matrix[3][3] - matrix[3][1] * matrix[1][3];
        float SubFactor12 = matrix[1][0] * matrix[3][1] - matrix[3][0] * matrix[1][1];
        float SubFactor13 = matrix[1][2] * matrix[2][3] - matrix[2][2] * matrix[1][3];
        float SubFactor14 = matrix[1][1] * matrix[2][3] - matrix[2][1] * matrix[1][3];
        float SubFactor15 = matrix[1][1] * matrix[2][2] - matrix[2][1] * matrix[1][2];
        float SubFactor16 = matrix[1][0] * matrix[2][3] - matrix[2][0] * matrix[1][3];
        float SubFactor17 = matrix[1][0] * matrix[2][2] - matrix[2][0] * matrix[1][2];
        float SubFactor18 = matrix[1][0] * matrix[2][1] - matrix[2][0] * matrix[1][1];

        Matrix4 Inv = new Matrix4();
        float[][] Inverse = Inv.matrix;
        Inverse[0][0] = + (matrix[1][1] * SubFactor00 - matrix[1][2] * SubFactor01 + matrix[1][3] * SubFactor02);
        Inverse[0][1] = - (matrix[1][0] * SubFactor00 - matrix[1][2] * SubFactor03 + matrix[1][3] * SubFactor04);
        Inverse[0][2] = + (matrix[1][0] * SubFactor01 - matrix[1][1] * SubFactor03 + matrix[1][3] * SubFactor05);
        Inverse[0][3] = - (matrix[1][0] * SubFactor02 - matrix[1][1] * SubFactor04 + matrix[1][2] * SubFactor05);

        Inverse[1][0] = - (matrix[0][1] * SubFactor00 - matrix[0][2] * SubFactor01 + matrix[0][3] * SubFactor02);
        Inverse[1][1] = + (matrix[0][0] * SubFactor00 - matrix[0][2] * SubFactor03 + matrix[0][3] * SubFactor04);
        Inverse[1][2] = - (matrix[0][0] * SubFactor01 - matrix[0][1] * SubFactor03 + matrix[0][3] * SubFactor05);
        Inverse[1][3] = + (matrix[0][0] * SubFactor02 - matrix[0][1] * SubFactor04 + matrix[0][2] * SubFactor05);

        Inverse[2][0] = + (matrix[0][1] * SubFactor06 - matrix[0][2] * SubFactor07 + matrix[0][3] * SubFactor08);
        Inverse[2][1] = - (matrix[0][0] * SubFactor06 - matrix[0][2] * SubFactor09 + matrix[0][3] * SubFactor10);
        Inverse[2][2] = + (matrix[0][0] * SubFactor11 - matrix[0][1] * SubFactor09 + matrix[0][3] * SubFactor12);
        Inverse[2][3] = - (matrix[0][0] * SubFactor08 - matrix[0][1] * SubFactor10 + matrix[0][2] * SubFactor12);

        Inverse[3][0] = - (matrix[0][1] * SubFactor13 - matrix[0][2] * SubFactor14 + matrix[0][3] * SubFactor15);
        Inverse[3][1] = + (matrix[0][0] * SubFactor13 - matrix[0][2] * SubFactor16 + matrix[0][3] * SubFactor17);
        Inverse[3][2] = - (matrix[0][0] * SubFactor14 - matrix[0][1] * SubFactor16 + matrix[0][3] * SubFactor18);
        Inverse[3][3] = + (matrix[0][0] * SubFactor15 - matrix[0][1] * SubFactor17 + matrix[0][2] * SubFactor18);

        float Determinant =
            + matrix[0][0] * Inverse[0][0]
            + matrix[0][1] * Inverse[0][1]
            + matrix[0][2] * Inverse[0][2]
            + matrix[0][3] * Inverse[0][3];

        //Inverse /= Determinant;
        for(int i = 0; i < 4; i++)
            this.setColumn(i, Inv.getColumn(i));
        this.product(1f / Determinant);
    }

    /**
     * Sum that matrix with other
     * @param mat The other matrix
     */
    public void add(Matrix4 mat) {
        for(byte i = 0; i < 4; i++)
            this.setColumn(i, this.getColumn(i).add(mat.getColumn(i)));
    }

    /**
     * Sum that matrix with a value
     * @param f Value to sum
     */
    public void add(float f) {
        for(byte i = 0; i < 4; i++)
            this.setColumn(i, this.getColumn(i).add(f));
    }

    /**
     * Substracts that matrix with other matrix
     * @param mat The other matrix
     */
    public void substract(Matrix4 mat) {
        for(byte i = 0; i < 4; i++)
            this.setColumn(i, this.getColumn(i).substract(mat.getColumn(i)));
    }

    /**
     * Substracts that matrix with a value
     * @param f The value
     */
    public void substract(float f) {
        for(byte i = 0; i < 4; i++)
            this.setColumn(i, this.getColumn(i).substract(f));
    }

    /**
     * Multiply that matrix with a value
     * @param v The value
     */
    public void product(float v) {
        for(byte i = 0; i < 4; i++)
            this.setColumn(i, this.getColumn(i).product(v));
    }

    /**
     * Multiply that matrix with other matrix
     * @param mat The other matrix
     */
    public void product(Matrix4 mat) {
        Vector4 SrcA0 = getColumn(0);
        Vector4 SrcA1 = getColumn(1);
        Vector4 SrcA2 = getColumn(2);
        Vector4 SrcA3 = getColumn(3);

        float[] SrcB0 = mat.matrix[0];
        float[] SrcB1 = mat.matrix[1];
        float[] SrcB2 = mat.matrix[2];
        float[] SrcB3 = mat.matrix[3];

        setRow(0, GLM.sum(GLM.product(SrcB0[0], SrcA0), GLM.product(SrcB0[1], SrcA1), GLM.product(SrcB0[2], SrcA2), GLM.product(SrcB0[3], SrcA3)));
        setRow(1, GLM.sum(GLM.product(SrcB1[0], SrcA0), GLM.product(SrcB1[1], SrcA1), GLM.product(SrcB1[2], SrcA2), GLM.product(SrcB1[3], SrcA3)));
        setRow(2, GLM.sum(GLM.product(SrcB2[0], SrcA0), GLM.product(SrcB2[1], SrcA1), GLM.product(SrcB2[2], SrcA2), GLM.product(SrcB2[3], SrcA3)));
        setRow(3, GLM.sum(GLM.product(SrcB3[0], SrcA0), GLM.product(SrcB3[1], SrcA1), GLM.product(SrcB3[2], SrcA2), GLM.product(SrcB3[3], SrcA3)));
    }

    /**
     * Divide the values of that matrix with a value
     * @param v The value
     */
    public void divide(float v) {
        for(byte i = 0; i < 4; i++)
            this.setColumn(i, this.getColumn(i).divide(v));
    }

    /**
     * Fill the FloatBuffer with the matrix
     * @param buffer FloatBuffer with capacity for 16 or more floats
     * @return the same buffer
     */
    public FloatBuffer fillBuffer(FloatBuffer buffer) {
        if(buffer.capacity() < 16)
            throw new IllegalArgumentException("Buffer size have to be 16 or more, but it is " + buffer.capacity());
        buffer.position(0);
        buffer.put(matrix[0]);
        buffer.put(matrix[1]);
        buffer.put(matrix[2]);
        buffer.put(matrix[3]);
        buffer.compact();
        return buffer;
    }
    
    /**
     * Converts this matrix into a Identity matrix. That replaces
     * all values.
     */
    public void setIdentity() {
        for(int y = 0; y < 4; y++)
            for(int x = 0; x < 4; x++)
                this.matrix[y][x] = 0;

        matrix[0][0] = 1;
        matrix[1][1] = 1;
        matrix[2][2] = 1;
        matrix[3][3] = 1;
    }

    /**
     * Clone that matrix. Is a shortcut of
     * {@code new Matrix4(this); }
     */
    @Override
    public Matrix4 clone() {
        return new Matrix4(this);
    }

    /**
     * Returns a representation of the matrix as String.
     * (Uses UTF8 chars)
     */
    @Override
    public String toString() {
        return String.format("⎛%+.3f, %+.3f, %+.3f, %+.3f⎞\n"
                           + "⎥%+.3f, %+.3f, %+.3f, %+.3f⎥\n"
                           + "⎥%+.3f, %+.3f, %+.3f, %+.3f⎥\n"
                           + "⎝%+.3f, %+.3f, %+.3f, %+.3f⎠",
                           matrix[0][0], matrix[0][1], matrix[0][2], matrix[0][3],
                           matrix[1][0], matrix[1][1], matrix[1][2], matrix[1][3],
                           matrix[2][0], matrix[2][1], matrix[2][2], matrix[2][3],
                           matrix[3][0], matrix[3][1], matrix[3][2], matrix[3][3]);
    }
}
