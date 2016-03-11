package org.melchor629.engine.utils.math;

import org.junit.Test;
import org.melchor629.engine.objects.Camera;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.melchor629.engine.utils.math.ArithmeticOperations.doubles;

/**
 * Testing functions with Matrices & Vertices
 */
public class MatrixOperationTests {

    @Test
    public void perspective() {
        assertEquals(
            new Matrix4().fillWithValues(
                1.358f, 0f, 0f, 0f,
                0f, 2.41421f, 0f, 0f,
                0f, 0f, -1.002f, -1f,
                0f, 0f, -0.2002f, 0f
            ),
            GLM.perspective(GLM.rad(45), 16f/9f, .1f, 100f)
        );
    }

    @Test
    public void ortho() {
        assertEquals(
            new Matrix4().fillWithValues(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, -0.020202f, 0f,
                -0f, -0f, -1.0202f, 1f
            ),
            GLM.ortho(-1f, 1f, -1f, 1f, 1f, 100f)
        );
    }

    @Test
    public void lookAt() {
        assertEquals(
            new Matrix4().fillWithValues(
                0f, -0f, -1f, 0f,
                -1f, 0f, -0f, 0f,
                0f, 1f, -0f, 0f,
                -0f, -0f, 0f, 1f
            ),
            GLM.lookAt(new Vector3(0f, 0f, 0f), new Vector3(1f, 0f, 0f), new Vector3(0f, 0f, 1f))
        );
    }

    @Test
    public void frustum() {
        assertEquals(
            new Matrix4().fillWithValues(
                0.00015625f, 0f, 0f, 0f,
                0f, -0.000277778f, 0f, 0f,
                1f, -1f, -1.002f, -1f,
                0f, 0f, -0.2002f, 0f
            ),
            GLM.frustum(0f, 1280f, 720f, 0f, .1f, 100f)
        );
    }

    @Test
    public void unProject() {

    }

    @Test
    public void translation() {
        assertEquals(
            new Matrix4().fillWithValues(
                1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, 1f, 0f,
                1f, 1f, 1f, 1f
            ),
            GLM.translateMatrix(new Matrix4(), new Vector3(1, 1, 1))
        );

        assertEquals(
            new Matrix<>(4,4, doubles).fillWithValues(
                1d, 0d, 0d, 0d,
                0d, 1d, 0d, 0d,
                0d, 0d, 1d, 0d,
                31289d, 38d, -4839d, 1d
            ),
            GLM.translateMatrix(
                new Matrix<>(4, 4, doubles),
                new Vector<>(3, doubles).fillWithValues(31289d, 38d, -4839d)
            )
        );
    }

    @Test
    public void rotation() {
        assertEquals(
            new Matrix4().fillWithValues(
                1f, 0f, 0f, 0f,
                0f, 0f, 1f, 0f,
                0f, -1f, 0f, 0f,
                0f, 0f, 0f, 1f
            ),
            GLM.rotateMatrix(new Matrix4(), (float) Math.PI/2, new Vector3(1, 0, 0))
        );
        assertEquals(
            new Matrix4().fillWithValues(
                -1f, 0f, 0f, 0f,
                0f, 1f, 0f, 0f,
                0f, 0f, -1f, 0f,
                0f, 0f, 0f, 1f
            ),
            GLM.rotateMatrix(new Matrix4(), (float) Math.PI, new Vector3(0, 1, 0))
        );
        assertEquals(
            new Matrix<>(4, 4, doubles).fillWithValues(
                0.241922d, -0.970296d, 0d, 0d,
                0.970296d, 0.241922d, 0d, 0d,
                0d, 0d, 1d, 0d,
                0d, 0d, 0d, 1d
            ),
            GLM.rotateMatrix(
                new Matrix<>(4, 4, doubles), 4.9567350757,
                new Vector<>(3, doubles).fillWithValues(0d, 0d, 1d)
            )
        );
    }

    @Test
    public void scale() {
        assertEquals(
            new Matrix4().fillWithValues(
                1f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f,
                0f, 0f, 0f, 0f,
                0f, 0f, 0f, 1f
            ),
            GLM.scaleMatrix(new Matrix4(), new Vector3(1, 0, 0))
        );
        assertEquals(
            new Matrix4().fillWithValues(
                1f, 0f, 0f, 0f,
                0f, 2f, 0f, 0f,
                0f, 0f, .5f, 0f,
                0f, 0f, 0f, 1f
            ),
            GLM.scaleMatrix(new Matrix4(), new Vector3(1, 2, .5f))
        );
        assertEquals(
            new Matrix<>(4, 4, doubles).fillWithValues(
                -16d, -16d, -16d, -16d,
                6d, 6d, 6d, 6d,
                1.2d, 1.2d, 1.2d, 1.2d,
                2d, 2d, 2d, 2d
            ),
            GLM.scaleMatrix(
                new Matrix<>(4, 4, doubles).fillWithValue(2d),
                new Vector<>(3, doubles).fillWithValues(-8d, 3d, .6d))
        );
    }

    @Test
    public void model() {
        ModelMatrix model = new ModelMatrix();
        model.translate(1, 1, 1);
        model.rotate((float) Math.toRadians(70), 1, 0, 0);
        model.setScale(1, 2, .5f);
        assertEquals(
            new Matrix4().fillWithValues(
                1f, 0f, 0f, 0f,
                0f, 0.68404f, 1.87939f, 0f,
                0f, -0.469846f, 0.17101f, 0f,
                1f, 1f, 1f, 1f
            ),
            model.getModelMatrix()
        );
    }

    @Test
    public void camera() {
        Camera camera = new Camera(null);
        assertEquals(
            new Matrix4().fillWithValues(
                1.358f, 0f, 0f, 0f,
                0f, 2.41421f, 0f, 0f,
                0f, 0f, -1.22222f, -1f,
                0f, 0f, -2.22222f, 0f
            ),
            camera.getProjectionMatrix()
        );
        assertEquals(
            new Matrix4().fillWithValues(
                0f, -0f, -1f, 0f,
                -1f, 0f, -0f, 0f,
                0f, 1f, -0f, 0f,
                -0f, -0f, 0f, 1f
            ),
            camera.getViewMatrix()
        );

        camera.setPosition(1, -1, 0);
        camera.setRotation(180, 0, 0);
        assertEquals(
            new Matrix4().fillWithValues(
                0f, 0f, 1f, 0f,
                1f, 0f, -0f, 0f,
                -0f, 1f, -0f, 0f,
                1f, -0f, -1f, 1f
            ),
            camera.getViewMatrix()
        );
    }

    @Test
    public void matrixToArray() {
        assertArrayEquals(
            new float[] {
                1, 0, 0, 0,
                3, 2, 5, 1,
                0, 0, 1, 0,
                0, 0, 0, 1
            },
            GLM.matrixAsArray((Matrix4) new Matrix4().setRow(2, new Vector4(3, 2, 5, 1))),
            0.001f
        );
    }
}
