package org.melchor629.engine.utils.math;

/**
 * Utils for Math stuff. See javadoc for every method.
 * This class also contains some shortcuts for some
 * operations (like cross product between 2 vec3).
 * @author melchor9000
 */
public class GLM {

    /**
     * Transform the degree angle to radians
     * @param angle in degrees
     * @return angle in radians
     */
    public static double rad(double angle) {
        return Math.toRadians(angle);
    }

    /**
     * Transform the radians angle to degrees
     * @param angle in radians
     * @return angle in degrees
     */
    public static double deg(double angle) {
        return Math.toDegrees(angle);
    }

    //////////////////////////////////////////////////
    ///    Vertex Utils                            ///
    //////////////////////////////////////////////////

    /**
     * Sums all vectors.
     * @param vec Variable length of vectors
     * @return vector sum of all vectors
     */
    public static Vector3 add(Vector3... vec) {
        Vector3 w = new Vector3();
        for(Vector3 aVec : vec) {
            w.add(aVec);
        }
        return w;
    }

    /**
     * Sums all vectors.
     * @param vec Variable length of vectors
     * @return vector sum of all vectors
     */
    public static Vector4 add(Vector4... vec) {
        Vector4 w = new Vector4();
        for(Vector4 aVec : vec) {
            w.add(aVec);
        }
        return w;
    }

    /**
     * Sums all vectors.
     * @param vec Variable length of vectors
     * @return vector sum of all vectors
     */
    @SafeVarargs
    public static <T extends Number> Vector<T> add(Vector<T>... vec) {
        Vector<T> w = vec[0].clone();
        for(int i = 1; i < vec.length; i++) {
            w.add(vec[i]);
        }
        return w;
    }

    /**
     * Substract all vectors.
     * @param v Variable length of vectors
     * @return vector substraction of all vectors
     */
    public static Vector3 sub(Vector3 u, Vector3 v) {
        return (Vector3) u.clone().substract(v);
    }

    /**
     * Substract all vectors.
     * @param v Variable length of vectors
     * @return vector substraction of all vectors
     */
    public static Vector4 sub(Vector4 u, Vector4 v) {
        return (Vector4) u.clone().substract(v);
    }

    /**
     * Substract all vectors.
     * @param vec Variable length of vectors
     * @return vector substraction of all vectors
     */
    @SafeVarargs
    public static <T extends Number> Vector<T> sub(Vector<T>... vec) {
        Vector<T> w = vec[0].clone();
        for(int i = 1; i < vec.length; i++) {
            w.substract(vec[i]);
        }
        return w;
    }

    /**
     * Multiplies the vector with the value
     * @param value Value to multiply
     * @param vec Vector
     * @return vector multiplied by value
     */
    public static Vector3 product(float value, Vector3 vec) {
        return (Vector3) vec.clone().product(value);
    }

    /**
     * Multiplies the vector with the value
     * @param value Value to multiply
     * @param vec Vector
     * @return vector multiplied by value
     */
    public static Vector4 product(float value, Vector4 vec) {
        return (Vector4) vec.clone().product(value);
    }

    /**
     * Multiplies the vector with the value
     * @param value Value to multiply
     * @param vec Vector
     * @param <T> Type of the numbers
     * @return vector multiplied by the value
     */
    public static <T extends Number> Vector<T> product(T value, Vector<T> vec) {
        return vec.clone().product(value);
    }

    @SuppressWarnings("unchecked")
    private static <T extends Number> Vector<T> cross(Vector<T> a, Vector<T> b) {
        if(b.getNumCoords() != 3)
            throw new ArithmeticException("Vector is not of 3 coordinates");
        T ux = a.getCoord(1), uy = a.getCoord(2), uz = a.getCoord(3);
        T vx = b.getCoord(1), vy = b.getCoord(2), vz = b.getCoord(3);
        return a.fillWithValues(
            a.ops.sub(a.ops.mul(uy, vz), a.ops.mul(vy, uz)),
            a.ops.sub(a.ops.mul(uz, vx), a.ops.mul(vz, ux)),
            a.ops.sub(a.ops.mul(ux, vy), a.ops.mul(vx, uy))
        );
    }

    /**
     * Computes the cross product in all vectors.
     * @param vecs Vectors to make cross products
     * @param <T> type of the numbers
     * @return result of all cross products
     * @throws ArithmeticException if any vector has not 3 coordinates
     */
    @SafeVarargs
    public static <T extends Number> Vector<T> cross(Vector<T>... vecs) {
        Vector<T> vec = vecs[0].clone();
        if(vec.getNumCoords() == 3) {
            for(int i = 1; i < vecs.length; i++) {
                cross(vec, vecs[i]);
            }
        }
        return vec;
    }

    /**
     * Negates the vector (is like: (-1) * v)
     * @param v Vector
     * @return Vector negated
     */
    @SuppressWarnings("unchecked")
    public static <T extends Vector<? extends Number>> T negate(T v) {
        return (T) v.clone().negate();
    }

    /**
     * Calculate the angle between two vectors
     * @param u First vector
     * @param v Second vector
     * @return angle in degrees
     */
    public static float angle(Vector3 u, Vector3 v) {
        return (float) Math.toDegrees(u.angle(v));
    }

    /**
     * Calculate the angle between two vectors
     * @param u First vector
     * @param v Second vector
     * @return angle in degrees
     */
    public static float angle(Vector4 u, Vector4 v) {
        return (float) Math.toDegrees(u.angle(v));
    }

    /**
     * Calculate the angle between two vectors
     * @param u First vector
     * @param v Second vector
     * @param <T> type of the numbers
     * @return angle in degrees
     */
    public static <T extends Number> float angle(Vector<T> u, Vector<T> v) {
        return (float) Math.toDegrees(u.angle(v));
    }


    //////////////////////////////////////////////////
    ///    Matrix Utils                            ///
    //////////////////////////////////////////////////
    @SafeVarargs
    public static <T extends Number> Matrix<T> add(Matrix<T>... mats) {
        if(mats.length > 0) {
            Matrix<T> matrix = mats[0].clone();

            for(int i = 1; i < mats.length; i++) {
                matrix.add(mats[i]);
            }

            return matrix;
        }
        return null;
    }

    @SafeVarargs
    public static <T extends Number> Matrix<T> substract(Matrix<T>... mats) {
        if(mats.length > 0) {
            Matrix<T> matrix = mats[0].clone();

            for(int i = 1; i < mats.length; i++) {
                matrix.substract(mats[i]);
            }

            return matrix;
        }
        return null;
    }

    @SafeVarargs
    public static <T extends Number> Matrix<T> product(Matrix<T>... mats) {
        if(mats.length > 0) {
            Matrix<T> matrix = mats[0].clone();

            for(int i = 1; i < mats.length; i++) {
                matrix.multiply(mats[i]);
            }

            return matrix;
        }
        return null;
    }

    /**
     * Transposes the matrix
     * @param mat Matrix to transpose
     * @return Return a new matrix transpose of {@code mat}
     */
    public static Matrix4 transpose(Matrix4 mat) {
        return (Matrix4) mat.clone().transpose();
    }
    
    /**
     * Create a translation matrix with the translation vector
     * @param mat Model Matrix
     * @param trans Vector indicating the translation
     * @return Translation Matrix
     */
    public static Matrix4 translateMatrix(Matrix4 mat, Vector3 trans) {
        Matrix4 result = (Matrix4) mat.clone();
        Vector<Float> a = mat.getRow(1), b = mat.getRow(2), c = mat.getRow(3), d = mat.getRow(4);
        a.product(trans.x());
        b.product(trans.y());
        c.product(trans.z());
        result.setRow(4, add(a, b, c, d));
        return result;
    }

    /**
     * Builds a translation matrix with the translation vector
     * @param mat input matrix
     * @param trans translation
     * @param <T> type of the numbers
     * @return input matrix translated
     */
    public static <T extends Number> Matrix<T> translateMatrix(Matrix<T> mat, Vector<T> trans) {
        if(mat.getColumns() != 4 || mat.getRows() != 4)
            throw new IllegalArgumentException("Matrix is not 4x4");
        if(trans.getNumCoords() != 3)
            throw new IllegalArgumentException("Vector has not 3 coordinates");

        Matrix<T> result = mat.clone();
        Vector<T> a = mat.getRow(1), b = mat.getRow(2), c = mat.getRow(3), d = mat.getRow(4);
        a.product(trans.getCoord(1));
        b.product(trans.getCoord(2));
        c.product(trans.getCoord(3));
        result.setRow(4, add(a, b, c, d));
        return result;
    }

    /**
     * Create a rotation matrix with the angle and the axis on which rotate
     * @param angle rotate angle (in radians)
     * @param v axis vector
     * @return matrix with rotation transformation
     */
    public static Matrix4 rotateMatrix(Matrix4 m, float angle, Vector3 v) {
        Matrix4 rot = new Matrix4();
        rot.fillWithValue(0f);
        float c = (float) Math.cos(angle);
        float s = (float) Math.sin(angle);
        Vector3 axis = (Vector3) v.clone().normalize();
        Vector3 temp = (Vector3) axis.clone().product(1 - c);

        rot.setValueAt(1,1, c + temp.x() * axis.x());
        rot.setValueAt(1,2, 0 + temp.x() * axis.y() + s * axis.z());
        rot.setValueAt(1,3, 0 + temp.x() * axis.z() - s * axis.y());

        rot.setValueAt(2,1, 0 + temp.y() * axis.x() - s * axis.z());
        rot.setValueAt(2,2, c + temp.y() * axis.y());
        rot.setValueAt(2,3, 0 + temp.y() * axis.z() + s * axis.x());

        rot.setValueAt(3,1, 0 + temp.z() * axis.x() + s * axis.y());
        rot.setValueAt(3,2, 0 + temp.z() * axis.y() - s * axis.x());
        rot.setValueAt(3,3, c + temp.z() * axis.z());

        Matrix4 result = new Matrix4();
        result.fillWithValue(0f);
        result.setRow(1,
            m.getRow(1).product(rot.getValueAt(1,1))
                .add(m.getRow(2).product(rot.getValueAt(1,2)))
                .add(m.getRow(3).product(rot.getValueAt(1,3))));
        result.setRow(2,
            m.getRow(1).product(rot.getValueAt(2,1))
                .add(m.getRow(2).product(rot.getValueAt(2,2)))
                .add(m.getRow(3).product(rot.getValueAt(2,3))));
        result.setRow(3,
            m.getRow(1).product(rot.getValueAt(3,1))
                .add(m.getRow(2).product(rot.getValueAt(3,2)))
                .add(m.getRow(3).product(rot.getValueAt(3,3))));
        result.setRow(4, m.getRow(4));
        return result;
    }

    /**
     * Builds a rotation matrix with a rotation angle and rotation axis
     * @param m initial matrix
     * @param angle rotation angle
     * @param v rotation axis
     * @param <T> type of the numbers
     * @return initial matrix rotated
     */
    public static <T extends Number> Matrix<T> rotateMatrix(Matrix<T> m, T angle, Vector<T> v) {
        if(m.getColumns() != 4 || m.getRows() != 4)
            throw new IllegalArgumentException("Matrix is not 4x4");
        if(v.getNumCoords() != 3)
            throw new IllegalArgumentException("Vector has not 3 coordinates");

        ArithmeticOperations<T> o = m.ops;
        Matrix<T> rot = m.clone().fillWithValue(o.convert(0));
        T c = o.convert(Math.cos(angle.doubleValue()));
        T s = o.convert(Math.sin(angle.doubleValue()));
        Vector<T> axis = v.clone().normalize();
        Vector<T> temp = axis.clone().product(o.sub(o.convert(1), c));

        rot.setValueAt(1,1, o.add(c, o.mul(temp.getCoord(1), axis.getCoord(1))));
        rot.setValueAt(1,2, o.add(o.mul(temp.getCoord(1), axis.getCoord(2)), o.mul(s, axis.getCoord(3))));
        rot.setValueAt(1,3, o.sub(o.mul(temp.getCoord(1), axis.getCoord(3)), o.mul(s, axis.getCoord(2))));

        rot.setValueAt(2,1, o.sub(o.mul(temp.getCoord(2), axis.getCoord(1)), o.mul(s, axis.getCoord(3))));
        rot.setValueAt(2,2, o.add(c, o.mul(temp.getCoord(2), axis.getCoord(2))));
        rot.setValueAt(2,3, o.add(o.mul(temp.getCoord(2), axis.getCoord(3)), o.mul(s, axis.getCoord(1))));

        rot.setValueAt(3,1, o.add(o.mul(temp.getCoord(3), axis.getCoord(1)), o.mul(s, axis.getCoord(2))));
        rot.setValueAt(3,2, o.sub(o.mul(temp.getCoord(3), axis.getCoord(2)), o.mul(s, axis.getCoord(1))));
        rot.setValueAt(3,3, o.add(c, o.mul(temp.getCoord(3), axis.getCoord(3))));

        Matrix<T> result = m.clone().fillWithValue(m.ops.convert(0));
        result.setRow(1,
            m.getRow(1).product(rot.getValueAt(1,1))
                .add(m.getRow(2).product(rot.getValueAt(1,2)))
                .add(m.getRow(3).product(rot.getValueAt(1,3))));
        result.setRow(2,
            m.getRow(1).product(rot.getValueAt(2,1))
                .add(m.getRow(2).product(rot.getValueAt(2,2)))
                .add(m.getRow(3).product(rot.getValueAt(2,3))));
        result.setRow(3,
            m.getRow(1).product(rot.getValueAt(3,1))
                .add(m.getRow(2).product(rot.getValueAt(3,2)))
                .add(m.getRow(3).product(rot.getValueAt(3,3))));
        result.setRow(4, m.getRow(4));
        return result;
    }

    /**
     * Create a scale matrix with the scale vector
     * @param scale vector
     * @return Scale matrix
     */
    public static Matrix4 scaleMatrix(Matrix4 mat, Vector3 scale) {
        Matrix4 result = new Matrix4();
        result.fillWithValue(0f);
        result.setRow(1, mat.getRow(1).product(scale.x()));
        result.setRow(2, mat.getRow(2).product(scale.y()));
        result.setRow(3, mat.getRow(3).product(scale.z()));
        result.setRow(4, mat.getRow(4));
        return result;
    }

    /**
     * Builds a scale matrix with the scale vector
     * @param mat initial matrix
     * @param scale scale vector
     * @param <T> type of numbers
     * @return initial matrix scaled
     */
    public static <T extends Number> Matrix<T> scaleMatrix(Matrix<T> mat, Vector<T> scale) {
        if(mat.getColumns() != 4 || mat.getRows() != 4)
            throw new IllegalArgumentException("Matrix is not 4x4");
        if(scale.getNumCoords() != 3)
            throw new IllegalArgumentException("Vector has not 3 coordinates");

        Matrix<T> result = mat.clone().fillWithValue(mat.ops.convert(0));
        result.setRow(1, mat.getRow(1).product(scale.getCoord(1)));
        result.setRow(2, mat.getRow(2).product(scale.getCoord(2)));
        result.setRow(3, mat.getRow(3).product(scale.getCoord(3)));
        result.setRow(4, mat.getRow(4));
        return result;
    }
    
    /**
     * Multiply the matrix by the vector
     * @param mat Matrix
     * @param vec Vector
     * @return resulting vector
     */
    public static Vector4 mul(Matrix4 mat, Vector4 vec) {
        Vector4 ret = new Vector4();

        ret.x(mat.getRow(1).dot(vec));
        ret.y(mat.getRow(2).dot(vec));
        ret.z(mat.getRow(3).dot(vec));
        ret.w(mat.getRow(4).dot(vec));
        return ret;
    }

    /**
     * Multiply both matrix into a new matrix
     * @param a Matrix
     * @param b Matrix
     * @return = a * b
     */
    public static Matrix4 mul(Matrix4 a, Matrix4 b) {
        return (Matrix4) a.clone().multiply(b);
    }

    /**
     * Create an orthographic projection with rectangle values and Z planes.<br>
     * {@code left}, {@code right}, {@code bottom}, {@code top} are the values
     * for the rectangle of the projection. {@code zNear} and {@code zFar} are
     * the values for the clipping panes. Center is (0, 0, 0)
     * @param left Max left value from Center to be represented
     * @param right Max right value from Center to be represented
     * @param bottom Max bottom value from Center to be represented
     * @param top Max top value from Center to be represented
     * @param zNear Min value of Z from Center to be represented
     * @param zFar Max value of Z from Center to be represented
     * @return Matrix with the projection
     */
    public static Matrix4 ortho(double left, double right, double bottom, double top, double zNear, double zFar) {
        Matrix4 ortho = new Matrix4();
        
        ortho.setValueAt(1, 1, (float) (2f / (right - left)));
        ortho.setValueAt(2, 2, (float) (2f / (top - bottom)));
        ortho.setValueAt(3, 3, (float) -(2f / (zFar - zNear)));
        ortho.setValueAt(4, 1, (float) -((right + left) / (right - left)));
        ortho.setValueAt(4, 2, (float) -((top + bottom) / (top - bottom)));
        ortho.setValueAt(4, 3, (float) -((zFar + zNear) / (zFar - zNear)));
        
        return ortho;
    }

    /**
     * Create a perpective projection with the Field of View (FOV), aspect
     * and the clipping panes.<br>
     * {@code fov} (Field of View) is the vertical angle that gives you an extensible
     * area of view. Higher values allows us to see more objects, but also creates
     * a higher lateral distorsion. FOV have to be in degrees.<br>
     * {@code aspect} is the relation between the width and the height: {@code aspect
     * = width/height}. For example, 16:9 is the relation aspect for panoramic videos
     * that can be seen in almost every video (like in Youtube).<br>
     * {@code zNear} and {@code zFar} are the min and max values for Z to be
     * represented. Objects located too close or too far than that values
     * will not be represented.
     * @param fov Field of View, in degrees
     * @param aspect Relation between width and height
     * @param zNear Min Z value for represent vertices
     * @param zFar Max Z value for represent vertices
     * @return Matrix for the projection
     */
    public static Matrix4 perspective(double fov, double aspect, double zNear, double zFar) {
        Matrix4 perspective = new Matrix4();
        perspective.fillWithValue(0f);

        if(floatEquals(aspect, 0, 0.00001) || floatEquals(zFar, zNear, 0.0001))
            throw new IllegalArgumentException("aspect cannot be 0 | zFar is equal zNear, both are not possible");
        if(Math.abs(fov) >= 180)
            throw new IllegalArgumentException("FOV cannot be greater than 180º");

        double range = Math.tan(fov / 2D) * zNear;
        double left = -range * aspect;
        double right = range * aspect;
        double bottom = -range;
        
        perspective.setValueAt(1, 1, (float) ((2f * zNear) / (right - left)));
        perspective.setValueAt(2, 2, (float) ((2f * zNear) / (range - bottom)));
        perspective.setValueAt(3, 3, (float) -((zFar + zNear) / (zFar - zNear)));
        perspective.setValueAt(3, 4, - 1F);
        perspective.setValueAt(4, 3, (float) -((2D * zFar * zNear) / (zFar - zNear)));
        
        return perspective;
    }

    /**
     * Create a View Matrix for the position ({@code eye}), looking at
     * a point ({@code center}) with the top of head pointing somewhere
     * ({@code up}). {@code up} is usual to be (0, 0, 1), pointing at Z
     * up.
     * @param eye The position of the viewer/camera
     * @param center Point where the viewer/camera is looking at
     * @param up Vector pointing to some axis. Can be null
     * @return View matrix
     */
    public static Matrix4 lookAt(Vector3 eye, Vector3 center, Vector3 up) {
        Matrix4 look = new Matrix4();

        Vector3 f = (Vector3) sub(center, eye).normalize();
        Vector3 s = (Vector3) f.cross(up).normalize();
        Vector3 u = s.cross(f);

        look.setColumn(1, new Vector4(s, 0));
        look.setColumn(2, new Vector4(u, 0));
        look.setColumn(3, new Vector4(negate(f), 0));

        look.setValueAt(4, 1, -s.dot(eye));
        look.setValueAt(4, 2, -u.dot(eye));
        look.setValueAt(4, 3,  f.dot(eye));
        
        return look;
    }

    public static Matrix4 frustum(double left, double right, double bottom, double top, double near, double far) {
        Matrix4 mat = new Matrix4();
        mat.fillWithValue(0.f);
        mat.setValueAt(1, 1, (float) ((2 * near) / (right - left)));
        mat.setValueAt(2, 2, (float) ((2 * near) / (top - bottom)));
        mat.setValueAt(3, 1, (float) ((right + left) / (right - left)));
        mat.setValueAt(3, 2, (float) ((top + bottom) / (top - bottom)));
        mat.setValueAt(3, 3, (float) - ((far + near) / (far - near)));
        mat.setValueAt(3, 4, -1.f);
        mat.setValueAt(4, 3, (float) - ((2f * far * near) / (far - near)));
        return mat;
    }

    /**
     * Transform window coordinates ({@code win}) into object coordinates.
     * @param win Window coordinates
     * @param model Model matrix
     * @param proj Projection matrix
     * @param viewport TODO nose
     * @return unproject vector
     */
    public static Vector3 unProject(Vector3 win, Matrix4 model, Matrix4 proj, Vector4 viewport) {
        Matrix4 Inverse = (Matrix4) mul(proj, model).invert();
        Vector4 tmp = new Vector4(win, 1);

        tmp.x((tmp.x() - viewport.x()) / viewport.z());
        tmp.y((tmp.y() - viewport.y()) / viewport.w());
        tmp = sub(product(2, tmp), new Vector4(1, 1, 1, 1));

        Vector4 obj = mul(Inverse, tmp);
        obj = product(1f / obj.w(), obj);
        return new Vector3(obj.x(), obj.y(), obj.z());
    }

    public static float[] matrixAsArray(Matrix4 mat) {
        float[] array = new float[16];
        int i = 0;
        for(int x = 1; x <= 4; x++)
            for(int y = 1; y <= 4; y++)
                array[i++] = mat.getValueAt(x, y);
        return array;
    }
    
    /**
     * Useful function that checks if a number belongs to an
     * interval.
     * @param value Value to check
     * @param min minimum number of the inverval
     * @param max maximum number of the interval
     **/
    public static boolean belongsToInterval(float value, float min, float max) {
        return min < value && value < max;
    }
    
    /**
     * Useful function that checks if a number belongs to an
     * interval.
     * @param value Value to check
     * @param min minimum number of the inverval
     * @param max maximum number of the interval
     **/
    public static boolean belongsToInterval(double value, double min, double max) {
        return min < value && value < max;
    }
    
    /**
     * Because calculing float numbers are really imprecise, this function
     * checks for a little difference between them:<br>
     * <code> |a-b| &lt; eps </code><br>
     * @param a first float
     * @param b second float
     * @param eps epsilon, acceptable error value
     **/
    public static boolean floatEquals(float a, float b, float eps) {
        float err = Math.abs(a - b);
        return err < eps;
    }
    
    /**
     * Because calculing float numbers are really imprecise, this function
     * checks for a little difference between them:<br>
     * <code> |a-b| &lt; eps </code><br>
     * @param a first float
     * @param b second float
     * @param eps epsilon, acceptable error value
     **/
    public static boolean floatEquals(double a, double b, double eps) {
        double err = Math.abs(a - b);
        return err < eps;
    }
}
