package org.melchor629.engine.utils.math;

import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Testing maths stuff
 */
public class MatricesTests {
    private Matrix<Float> matrix1x1;
    private Matrix<Float> matrix2x2;
    private Matrix<Float> matrix3x3;
    private Matrix<Float> matrix4x4;
    private Matrix<Float> matrix3x2;

    private static final ArithmeticOperations<Float> ops = ArithmeticOperations.floats;

    public MatricesTests() {
        matrix1x1 = newMatrix(1, 1, asList(1.f));

        matrix2x2 = newMatrix(2, 2, asList(1.f, 2.f, 3.f, 4.f));
        matrix3x3 = newMatrix(3, 3, asList(
            1.f, 2.f, 3.f,
            4.f, 5.f, 6.f,
            7.f, 8.f, 9.f
        ));
        matrix4x4 = newMatrix(4, 4, asList(
            1.f, 2.f, 3.f, 4.f,
            5.f, 6.f, 7.f, 8.f,
            9.f, 10f, 11f, 12f,
            13f, 14f, 15f, 16f
        ));
        matrix3x2 = newMatrix(3, 2, asList(
            1.f, 2.f, 3.f,
            4.f, 5.f, 6.f
        ));
    }

    @Test
    public void equality() {
        //Check for same matrices
        assertTrue(matrix1x1.equals(newMatrix(1, 1, asList(1.f))));

        assertTrue(matrix2x2.equals(newMatrix(2, 2, asList(
            1.f, 2.f,
            3.f, 4.f
        ))));

        assertTrue(matrix3x3.equals(newMatrix(3, 3, asList(
            1.f, 2.f, 3.f,
            4.f, 5.f, 6.f,
            7.f, 8.f, 9.f
        ))));

        assertTrue(matrix4x4.equals(newMatrix(4, 4, asList(
            1.f, 2.f, 3.f, 4.f,
            5.f, 6.f, 7.f, 8.f,
            9.f, 10f, 11f, 12f,
            13f, 14f, 15f, 16f
        ))));

        assertTrue(matrix3x2.equals(newMatrix(3, 2, asList(
            1.f, 2.f, 3.f,
            4.f, 5.f, 6.f
        ))));

        //Check for different matrices
        assertTrue(!matrix1x1.equals(newMatrix(1, 1, asList(-1f))));

        assertTrue(!matrix2x2.equals(newMatrix(2, 2, asList(
            -1f, -2f,
            -3f, -4f
        ))));

        assertTrue(!matrix3x3.equals(newMatrix(3, 3, asList(
            -1f, 2.f, 3.f,
            4.f, -5f, 6.f,
            7.f, 8.f, -9f
        ))));

        assertTrue(!matrix4x4.equals(newMatrix(4, 4, asList(
            1.f, 2.f, 3.f, -4f,
            5.f, 6.f, -7f, 8.f,
            9.f, 10f, 11f, 12f,
            13f, 14f, 15f, 16f
        ))));

        assertTrue(!matrix3x2.equals(newMatrix(3, 2, asList(
            1.f, 2.f, 3.f,
            4.f, 5.f, -6f
        ))));
    }

    @Test
    public void sum() {
        assertEquals(
            newMatrix(1, 1, asList(7.f)),
            GLM.add(matrix1x1, newMatrix(1, 1, asList(6.f)))
        );

        assertEquals(
            newMatrix(2, 2, asList(
                2.f, 4.f,
                6.f, 8.f
            )),
            GLM.add(matrix2x2, matrix2x2)
        );

        assertEquals(
            newMatrix(3, 3, asList(
                2.f, 4.f, 6.f,
                8.f, 10f, 12f,
                14f, 16f, 18f
            )),
            GLM.add(matrix3x3, matrix3x3)
        );

        assertEquals(
            newMatrix(4, 4, asList(
                2.f, 4.f, 6.f, 8.f,
                10f, 12f, 14f, 16f,
                18f, 20f, 22f, 24f,
                26f, 28f, 30f, 32f
            )),
            GLM.add(matrix4x4, matrix4x4)
        );

        assertEquals(
            newMatrix(3, 2, asList(
                2.f, 4.f, 6.f,
                8f, 10f, 12f
            )),
            GLM.add(matrix3x2, matrix3x2)
        );
    }

    @Test
    public void substract() {
        assertEquals(
            newMatrix(1, 1, asList(-5f)),
            GLM.substract(matrix1x1, newMatrix(1, 1, asList(6.f)))
        );

        assertEquals(
            newMatrix(2, 2, asList(
                0.f, 0.f,
                0.f, 0.f
            )),
            GLM.substract(matrix2x2, matrix2x2)
        );

        assertEquals(
            newMatrix(3, 3, asList(
                0.f, 0.f, 0.f,
                0.f, 0.f, 0.f,
                0.f, 0.f, 0.f
            )),
            GLM.substract(matrix3x3, matrix3x3)
        );

        assertEquals(
            newMatrix(4, 4, asList(
                0.f, 0.f, 0.f, 0.f,
                0.f, 0.f, 0.f, 0.f,
                0.f, 0.f, 0.f, 0.f,
                0.f, 0.f, 0.f, 0.f
            )),
            GLM.substract(matrix4x4, matrix4x4)
        );

        assertEquals(
            newMatrix(3, 2, asList(
                0.f, 0.f, 0.f,
                0.f, 0.f, 0.f
            )),
            GLM.substract(matrix3x2, matrix3x2)
        );
    }

    @Test
    public void product() {
        assertEquals(
            newMatrix(1, 1, asList(1f)),
            GLM.product(matrix1x1, newMatrix(1, 1, asList(1.f)))
        );

        assertEquals(
            newMatrix(2, 2, asList(
                7.f, 10f,
                15f, 22f
            )),
            GLM.product(matrix2x2, matrix2x2)
        );

        assertEquals(
            newMatrix(3, 3, asList(
                30f, 36f, 42f,
                66f, 81f, 96f,
                102f,126f,150f
            )),
            GLM.product(matrix3x3, matrix3x3)
        );

        //Aqui curiosamente se comprueban las dos multiplicaciones
        assertEquals(
            newMatrix(4, 4, asList(
                45.f, 50.f, 55.f, 60.f,
                101f, 114f, 127f, 140f,
                157f, 178f, 199f, 220f,
                213f, 242f, 271f, 300f
            )).multiply(2.f),
            GLM.product(matrix4x4, matrix4x4)
        );

        Matrix<Float> ja = null;
        try {
            ja = GLM.product(matrix3x2, matrix3x2);
        } catch(Exception ignore) {}
        assertEquals(
            null,
            ja
        );
    }

    @Test
    public void complementaryMinor() {
        assertEquals(newMatrix(1, 1, asList(4)), matrix2x2.complementaryMinor(1, 1));

        assertEquals(
            newMatrix(2, 2, asList(
                1.f, 3.f,
                7.f, 9.f
            )),
            matrix3x3.complementaryMinor(2, 2)
        );

        assertEquals(
            newMatrix(3, 3, asList(
                1.f, 2.f, 4.f,
                5.f, 6.f, 8.f,
                9.f, 10f, 12f
            )),
            matrix4x4.complementaryMinor(4, 3)
        );

        assertEquals(
            newMatrix(2, 1, asList(4f, 5f)),
            matrix3x2.complementaryMinor(1, 3)
        );
    }

    @Test
    public void determinant() {
        assertEquals(1f, matrix1x1.determinant(), 0.001);
        assertEquals(-2f, matrix2x2.determinant(), 0.001);
        assertEquals(0.f, matrix3x3.determinant(), 0.001);
        assertEquals(0.f, matrix4x4.determinant(), 0.001);

        float noDet = Float.NaN;
        try {
            noDet = matrix3x2.determinant();
        } catch(Exception ignore) {}

        assertEquals(Float.NaN, noDet, 0.001);
    }

    @Test
    public void transpose() {
        assertEquals(
            newMatrix(1, 1, asList(1.f)),
            matrix1x1.clone().transpose()
        );

        assertEquals(
            newMatrix(2, 2, asList(
                1.f, 3.f,
                2.f, 4.f
            )),
            matrix2x2.clone().transpose()
        );

        assertEquals(
            newMatrix(3, 3, asList(
                1.f, 4.f, 7.f,
                2.f, 5.f, 8.f,
                3.f, 6.f, 9.f
            )),
            matrix3x3.clone().transpose()
        );

        assertEquals(
            newMatrix(4, 4, asList(
                1.f, 5.f, 9.f, 13f,
                2.f, 6.f, 10f, 14f,
                3.f, 7.f, 11f, 15f,
                4.f, 8.f, 12f, 16f
            )),
            matrix4x4.clone().transpose()
        );

        assertEquals(
            newMatrix(2, 3, asList(
                1.f, 4.f,
                2.f, 5.f,
                3.f, 6.f
            )),
            matrix3x2.clone().transpose()
        );
    }

    @Test
    public void inverse() {
        assertEquals(
            newMatrix(1, 1, asList(1.f)),
            matrix1x1.invert()
        );

        assertEquals(
            newMatrix(2, 2, asList(
                -2f, 1.f,
                3f/2, -1/2f
            )),
            matrix2x2.invert()
        );

        Matrix<Float> noInv = null;
        try {
            noInv = matrix3x3.invert();
        } catch(Exception ignore) {}
        assertEquals(null, noInv);

        noInv = null;
        try {
            noInv = matrix3x2.invert();
        } catch(Exception ignore) {}
        assertEquals(null, noInv);
    }

    @Test public void identity() {
        assertEquals(
            newMatrix(1, 1, asList(1.f)),
            matrix1x1.setIdentity()
        );

        assertEquals(
            newMatrix(2, 2, asList(
                1.f, 0.f,
                0.f, 1.f
            )),
            matrix2x2.setIdentity()
        );

        assertEquals(
            newMatrix(3, 3, asList(
                1.f, 0.f, 0.f,
                0.f, 1.f, 0.f,
                0.f, 0.f, 1.f
            )),
            matrix3x3.setIdentity()
        );

        assertEquals(
            newMatrix(4, 4, asList(
                1.f, 0.f, 0.f, 0.f,
                0.f, 1.f, 0.f, 0.f,
                0.f, 0.f, 1.f, 0.f,
                0.f, 0.f, 0.f, 1.f
            )),
            matrix4x4.setIdentity()
        );

        try {
            matrix3x2.setIdentity();
        } catch(Exception ignore) {}
        assertEquals(
            newMatrix(3, 2, asList(
                1.f, 2.f, 3.f,
                4.f, 5.f, 6.f
            )),
            matrix3x2
        );
    }

    static Matrix<Float> newMatrix(int c, int r, List<Float> n) {
        return new Matrix<>(c, r, ops).fillWithValues(n);
    }

    static List<Float> asList(float f) {
        return Collections.singletonList(f);
    }

    static List<Float> asList(Float... f) {
        return Arrays.asList(f);
    }
}
