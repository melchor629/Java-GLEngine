package org.melchor629.engine.utils.math;

import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Tests for Vertices
 */
public class VectorsTests {
    private final Vector<Float> vector1;
    private final Vector<Float> vector2;
    private final Vector<Float> vector3;
    private final Vector<Float> vector4;

    public VectorsTests() {
        vector1 = newVector(1, 1f);
        vector2 = newVector(2, 1f, 2f);
        vector3 = newVector(3, 1f, 2f, 3f);
        vector4 = newVector(4, 1f, 2f, 3f, 4f);
    }

    @Test
    public void equality() {
        assertTrue(vector1.equals(newVector(1, 1f)));
        assertTrue(vector2.equals(newVector(2, 1f, 2f)));
        assertTrue(vector3.equals(newVector(3, 1f, 2f, 3f)));
        assertTrue(vector4.equals(newVector(4, 1f, 2f, 3f, 4f)));
        assertTrue(!vector1.equals(newVector(1, 4f)));
        assertTrue(!vector2.equals(newVector(2, 4f, 3f)));
        assertTrue(!vector3.equals(newVector(3, 4f, 3f, 2f)));
        assertTrue(!vector4.equals(newVector(4, 4f, 3f, 2f, 1f)));
    }

    @Test
    public void add() {
        assertEquals(
            newVector(1, 2f),
            GLM.add(vector1, vector1)
        );

        assertEquals(
            newVector(2, 2f, 4f),
            GLM.add(vector2, vector2)
        );

        assertEquals(
            newVector(3, 2f, 4f, 6f),
            GLM.add(vector3, vector3)
        );

        assertEquals(
            newVector(4, 2f, 4f, 6f, 8f),
            GLM.add(vector4, vector4)
        );
    }

    @Test
    public void substract() {
        assertEquals(
            newVector(1, 0f),
            GLM.sub(vector1, vector1)
        );

        assertEquals(
            newVector(2, 0f, 0f),
            GLM.sub(vector2, vector2)
        );

        assertEquals(
            newVector(3, 0f, 0f, 0f),
            GLM.sub(vector3, vector3)
        );

        assertEquals(
            newVector(4, 0f, 0f, 0f, 0f),
            GLM.sub(vector4, vector4)
        );
    }

    @Test
    public void product() {
        assertEquals(
            newVector(1, 4f),
            GLM.product(4f, vector1)
        );

        assertEquals(
            newVector(2, 7f, 14f),
            GLM.product(7f, vector2)
        );

        assertEquals(
            newVector(3, -3f, -6f, -9f),
            GLM.product(-3f, vector3)
        );

        assertEquals(
            newVector(4, -5f, -10f, -15f, -20f),
            GLM.product(-5f, vector4)
        );
    }

    @Test
    public void dot() {
        assertEquals(
            6f,
            vector1.dot(newVector(1, 6f)),
            0.001
        );

        assertEquals(
            4f,
            vector2.dot(newVector(2, 2f, 1f)),
            0.001
        );

        assertEquals(
            0.0,
            vector3.dot(newVector(3, -2f, 1f, 0f)),
            0.001
        );

        assertEquals(
            17f,
            vector4.dot(newVector(4, -8f, -2f, 3f, 5f)),
            0.001
        );
    }

    @Test
    public void module() {
        assertEquals(
            1f,
            vector1.module(),
            0.001
        );

        assertEquals(
            Math.sqrt(5),
            vector2.module(),
            0.001
        );

        assertEquals(
            Math.sqrt(14),
            vector3.module(),
            0.001
        );

        assertEquals(
            Math.sqrt(30),
            vector4.module(),
            0.001
        );
    }

    @Test
    public void distance() {
        assertEquals(
            0f,
            vector1.distance(newVector(1, 1f)),
            0.001
        );

        assertEquals(
            Math.sqrt(9 + 1),
            vector2.distance(newVector(2, 4f, 3f)),
            0.001
        );

        assertEquals(
            Math.sqrt(25 + 25 + 1),
            vector3.distance(newVector(3, -4f, -3f, 2f)),
            0.001
        );

        assertEquals(
            Math.sqrt(9 + 25 + 1 + 25),
            vector4.distance(newVector(4, 4f, -3f, 2f, -1f)),
            0.001
        );
    }

    @Test
    public void normalize() {
        assertEquals(
            newVector(1, 1f),
            vector1.clone().normalize()
        );

        assertEquals(
            newVector(2, 1f, 2f).apply((n) -> n / (float) Math.sqrt(5)),
            vector2.clone().normalize()
        );

        assertEquals(
            newVector(3, 1f, 2f, 3f).apply((n) -> n / (float) Math.sqrt(14)),
            vector3.clone().normalize()
        );

        assertEquals(
            newVector(4, 1f, 2f, 3f, 4f).apply((n) -> n / (float) Math.sqrt(30)),
            vector4.clone().normalize()
        );
    }

    @Test
    public void angle() {
        assertEquals(
            Math.PI,
            vector1.angle(newVector(1, -1f)),
            0.0001
        );

        assertEquals(
            Math.PI / 2,
            vector2.angle(newVector(2, 2f, -1f)),
            0.0001
        );

        assertEquals(
            Math.acos(-4/Math.sqrt(14)/Math.sqrt(29)),
            vector3.angle(newVector(3, -4f, -3f, 2f)),
            0.0001
        );

        assertEquals(
            Math.PI / 2,
            vector4.angle(newVector(4, 4f, -3f, 2f, -1f)),
            0.0001
        );
    }

    @Test
    public void cross() {
        assertEquals(
            new Vector3().fillWithValues(0f, -1f, -4f),
            new Vector3(1, 0, 0).cross(new Vector3(3, -4, 1))
        );

        assertEquals(
            newVector(3, -113f, 289f, -959f),
            GLM.cross(newVector(3, -5f, 8f, 3f), newVector(3, 93f, 43f, 2f))
        );
    }

    static Vector<Float> newVector(int coord, Float... floats) {
        return new Vector<>(coord, ArithmeticOperations.floats)
            .fillWithValues(Arrays.asList(floats));
    }
}
