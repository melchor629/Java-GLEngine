package org.melchor629.engine.utils.math;

/**
 * Utils for Math stuff. See javadoc for every method.
 * This class also contains some shortcuts for some
 * operations (like cross product between 2 vec3).
 * @author melchor9000
 */
public class GLM {

    /**
     * Sinus of the angle
     * @param angle in degrees
     * @return Sinus
     */
    public static double sin(double angle) {
        return Math.sin(Math.toRadians(angle));
    }

    /**
     * Cosine of the angle
     * @param angle in degrees
     * @return Cosine
     */
    public static double cos(double angle) {
        return Math.cos(Math.toRadians(angle));
    }

    /**
     * Trigonometric tangent of the angle
     * @param angle in degrees
     * @return tangent
     */
    public static double tan(double angle) {
        return Math.tan(Math.toRadians(angle));
    }

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
     * Returns the length of the vector (en Español el módulo)
     * @param v Vector
     * @return length of the vector
     */
    public static float length(Vector3 v) {
        return (float) Math.sqrt(dot(v, v));
    }

    /**
     * Returns the length of the vector (en Español el módulo)
     * @param v Vector
     * @return length of the vector
     */
    public static float length(Vector4 v) {
        return (float) Math.sqrt(dot(v, v));
    }

    /**
     * Sums all vectors.
     * @param vec Variable length of vectors
     * @return vector sum of all vectors
     */
    public static Vector3 sum(Vector3... vec) {
        Vector3 w = new Vector3();
        for (Vector3 aVec : vec) {
            w.x += aVec.x;
            w.y += aVec.y;
            w.z += aVec.z;
        }
        return w;
    }

    /**
     * Sums all vectors.
     * @param vec Variable length of vectors
     * @return vector sum of all vectors
     */
    public static Vector4 sum(Vector4... vec) {
        Vector4 w = new Vector4();
        for (Vector4 aVec : vec) {
            w.x += aVec.x;
            w.y += aVec.y;
            w.z += aVec.z;
            w.w += aVec.w;
        }
        return w;
    }

    /**
     * Substract all vectors.
     * @param v Variable length of vectors
     * @return vector substraction of all vectors
     */
    public static Vector3 sub(Vector3 u, Vector3 v) {
        Vector3 w = new Vector3();
        w.x = u.x - v.x;
        w.y = u.y - v.y;
        w.z = u.z - v.z;
        return w;
    }

    /**
     * Substract all vectors.
     * @param v Variable length of vectors
     * @return vector substraction of all vectors
     */
    public static Vector4 sub(Vector4 u, Vector4 v) {
        Vector4 w = new Vector4();
        w.x = u.x - v.x;
        w.y = u.y - v.y;
        w.z = u.z - v.z;
        w.w = u.w - v.w;
        return w;
    }

    /**
     * Multiplies the vector with the value
     * @param value Value to multiply
     * @param vec Vector
     * @return vector multiplied by value
     */
    public static Vector3 product(float value, Vector3 vec) {
        Vector3 v = vec.clone();
        v.x *= value;
        v.y *= value;
        v.z *= value;
        return v;
    }

    /**
     * Multiplies the vector with the value
     * @param value Value to multiply
     * @param vec Vector
     * @return vector multiplied by value
     */
    public static Vector4 product(float value, Vector4 vec) {
        Vector4 v = (Vector4) vec.clone();
        v.x *= value;
        v.y *= value;
        v.z *= value;
        v.w *= value;
        return v;
    }

    /**
     * Make dot product of vectors
     * @param u First vector
     * @param v Second vector
     * @return dot product
     */
    public static float dot(Vector3 u, Vector3 v) {
        return u.x * v.x + u.y * v.y + u.z * v.z;
    }

    /**
     * Make dot product of vectors
     * @param u First vector
     * @param v Second vector
     * @return dot product
     */
    public static float dot(Vector4 u, Vector4 v) {
        return u.x * v.x + u.y * v.y + u.z * v.z + u.w * v.w;
    }

    /**
     * Make cross product of vectors
     * @param u First vector
     * @param v Second vector
     * @return cross product
     */
    public static Vector3 cross(Vector3 u, Vector3 v) {
        Vector3 w = new Vector3();
        w.x = u.y * v.z - v.y * u.z;
        w.y = v.x * u.z - u.x * v.z;
        w.z = u.x * v.y - u.y * v.x;
        return w;
    }

    /**
     * Turn the vector given into a normalized one
     * @param v Vector to be normalized
     * @return normalized vector
     */
    public static Vector3 normalize(Vector3 v) {
        Vector3 nrm = new Vector3();
        float modulo = length(v);
        if(modulo == 0) return nrm; //No dividir entre 0
        nrm.x = v.x / modulo;
        nrm.y = v.y / modulo;
        nrm.z = v.z / modulo;
        return nrm;
    }

    /**
     * Turn the vector given into a normalized one
     * @param v Vector to be normalized
     * @return normalized vector
     */
    public static Vector4 normalize(Vector4 v) {
        Vector4 nrm = new Vector4();
        float modulo = length(v);
        nrm.x = v.x / modulo;
        nrm.y = v.y / modulo;
        nrm.z = v.z / modulo;
        nrm.w = v.w / modulo;
        return nrm;
    }

    /**
     * Negates the vector (is like: (-1) * v)
     * @param v Vector
     * @return Vector negated
     */
    public static Vector3 negate(Vector3 v) {
        Vector3 vec = new Vector3();
        vec.x = - v.x;
        vec.y = - v.y;
        vec.z = - v.z;
        return vec;
    }

    /**
     * Negates the vector (is like: (-1) * v)
     * @param v Vector
     * @return Vector negated
     */
    public static Vector4 negate(Vector4 v) {
        Vector4 vec = new Vector4();
        vec.x = - v.x;
        vec.y = - v.y;
        vec.z = - v.z;
        vec.w = - v.w;
        return vec;
    }

    /**
     * Calculate the angle between two vectors
     * @param u First vector
     * @param v Second vector
     * @return angle in degrees
     */
    public static float angle(Vector3 u, Vector3 v) {
        float dot = dot(u, v);
        float uv = length(u) * length(v);
        return (float) Math.toDegrees(Math.acos(dot / uv));
    }

    /**
     * Calculate the angle between two vectors
     * @param u First vector
     * @param v Second vector
     * @return angle in degrees
     */
    public static float angle(Vector4 u, Vector4 v) {
        float dot = dot(u, v);
        float uv = length(u) * length(v);
        return (float) Math.toDegrees(Math.acos(dot / uv));
    }


    //////////////////////////////////////////////////
    ///    Matrix Utils                            ///
    //////////////////////////////////////////////////
    @SafeVarargs
    public static <T extends Number> Matrix<T> sum(Matrix<T>... mats) {
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
        Matrix4 met = new Matrix4(mat);
        met.setRow(0, mat.getColumn(0));
        met.setRow(1, mat.getColumn(1));
        met.setRow(2, mat.getColumn(2));
        met.setRow(3, mat.getColumn(2));
        return met;
    }

    /**
     * Computes the determinant of the matrix
     * @param m Matrix
     * @return Determinant of the matrix
     */
    public static float determinant(Matrix4 m) {
        float SubFactor00 = m.get(2, 2) * m.get(3, 3) - m.get(3, 2) * m.get(2, 3);
        float SubFactor01 = m.get(2, 1) * m.get(3, 3) - m.get(3, 1) * m.get(2, 3);
        float SubFactor02 = m.get(2, 1) * m.get(3, 2) - m.get(3, 1) * m.get(2, 2);
        float SubFactor03 = m.get(2, 0) * m.get(3, 3) - m.get(3, 0) * m.get(2, 3);
        float SubFactor04 = m.get(2, 0) * m.get(3, 2) - m.get(3, 0) * m.get(2, 2);
        float SubFactor05 = m.get(2, 0) * m.get(3, 1) - m.get(3, 0) * m.get(2, 1);

        Vector4 DetCof = new Vector4(
            + (m.get(1, 1) * SubFactor00 - m.get(1, 2) * SubFactor01 + m.get(1, 3) * SubFactor02),
            - (m.get(1, 0) * SubFactor00 - m.get(1, 2) * SubFactor03 + m.get(1, 3) * SubFactor04),
            + (m.get(1, 0) * SubFactor01 - m.get(1, 1) * SubFactor03 + m.get(1, 3) * SubFactor05),
            - (m.get(1, 0) * SubFactor02 - m.get(1, 1) * SubFactor04 + m.get(1, 2) * SubFactor05));

        return
            m.get(0, 0) * DetCof.x + m.get(0, 1) * DetCof.y +
            m.get(0, 2) * DetCof.z + m.get(0, 3) * DetCof.w;
    }
    
    /**
     * Create a translation matrix with the translation vertex
     * @param mat Model Matrix
     * @param trans Vertex indicating the translation
     * @return Translation Matrix
     */
    public static Matrix4 translateMatrix(Matrix4 mat, Vector3 trans) {
        Matrix4 result = new Matrix4(mat);
        Vector4 a = mat.getRow(0), b = mat.getRow(1), c = mat.getRow(2), d = mat.getRow(3);
        a.product(trans.x);
        b.product(trans.y);
        c.product(trans.z);
        Vector4 s = sum(a, b, c, d);
        result.setRow(3, s);
        return result;
    }

    /**
     * Create a rotation matrix with the angle and the axis on which rotate
     * @param angle rotate angle (in radians)
     * @param axis axis vector
     * @return matrix with rotation transformation
     */
    public static Matrix4 rotateMatrix(Matrix4 matrix, float angle, Vector3 axis) {
        float len = axis.length();
        float x = axis.x / len;
        float y = axis.y / len;
        float z = axis.z / len;
        float c = (float) cos(angle);
        float s = (float) sin(angle);
        float C = 1.f - c;
        float m11 = x * x * C + c;
        float m12 = x * y * C - z * s;
        float m13 = x * z * C + y * s;
        float m21 = y * x * C + z * s;
        float m22 = y * y * C + c;
        float m23 = y * z * C - x * s;
        float m31 = z * x * C - y * s;
        float m32 = z * y * C + x * s;
        float m33 = z * z * C + c;
        float t1 = matrix.get(0, 0) * m11 + matrix.get(1, 0) * m21 + matrix.get(2, 0) * m31;
        float t2 = matrix.get(0, 1) * m11 + matrix.get(1, 1) * m21 + matrix.get(2, 1) * m31;
        float t3 = matrix.get(0, 2) * m11 + matrix.get(1, 2) * m21 + matrix.get(2, 2) * m31;
        float t4 = matrix.get(0, 3) * m11 + matrix.get(1, 3) * m21 + matrix.get(2, 3) * m31;
        float t5 = matrix.get(0, 0) * m12 + matrix.get(1, 0) * m22 + matrix.get(2, 0) * m32;
        float t6 = matrix.get(0, 1) * m12 + matrix.get(1, 1) * m22 + matrix.get(2, 1) * m32;
        float t7 = matrix.get(0, 2) * m12 + matrix.get(1, 2) * m22 + matrix.get(2, 2) * m32;
        float t8 = matrix.get(0, 3) * m12 + matrix.get(1, 3) * m22 + matrix.get(2, 3) * m32;
        float t9 = matrix.get(0, 0) * m13 + matrix.get(1, 0) * m23 + matrix.get(2, 0) * m33;
        float t10 = matrix.get(0, 1) * m13 + matrix.get(1, 1) * m23 + matrix.get(2, 1) * m33;
        float t11 = matrix.get(0, 2) * m13 + matrix.get(1, 2) * m23 + matrix.get(2, 2) * m33;
        float t12 = matrix.get(0, 3) * m13 + matrix.get(1, 3) * m23 + matrix.get(2, 3) * m33;
        matrix.setRow(0, t1, t2, t3, t4);
        matrix.setRow(1, t5, t6, t7, t8);
        matrix.setRow(2, t9, t10, t11, t12);
        return matrix;
    }

    /**
     * Create a scale matrix with the scale vector
     * @param scale vector
     * @return Scale matrix
     */
    public static Matrix4 scaleMatrix(Matrix4 mat, Vector3 scale) {
        Matrix4 result = new Matrix4(0);
        result.setRow(0, mat.getRow(0).product(scale.x));
        result.setRow(1, mat.getRow(1).product(scale.y));
        result.setRow(2, mat.getRow(2).product(scale.z));
        result.setRow(3, mat.getRow(3));
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

        ret.x = dot(mat.getRow(0), vec);
        ret.y = dot(mat.getRow(1), vec);
        ret.z = dot(mat.getRow(2), vec);
        ret.w = dot(mat.getRow(3), vec);
        return ret;
    }

    /**
     * Multiply both matrix into a new matrix
     * @param a Matrix
     * @param b Matrix
     * @return = a * b
     */
    public static Matrix4 mul(Matrix4 a, Matrix4 b) {
        Matrix4 res = a.clone();
        res.product(b);
        return res;
    }

    /**
     * Compute the inverse of the matrix into a new one
     * @param mat Matrix
     * @return new Matrix with the inverse of the first
     */
    public static Matrix4 inverse(Matrix4 mat) {
        Matrix4 r = mat.clone();
        r.inverse();
        return r;
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
        
        ortho.set(0, 0, (float) (2f / (right - left)));
        ortho.set(1, 1, (float) (2f / (top - bottom)));
        ortho.set(2, 2, (float) -(2f / (zFar - zNear)));
        ortho.set(3, 0, (float) -((right + left) / (right - left)));
        ortho.set(3, 1, (float) -((top + bottom) / (top - bottom)));
        ortho.set(3, 2, (float) -((zFar + zNear) / (zFar - zNear)));
        
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
        Matrix4 perspective = new Matrix4(0);

        if(aspect == 0 || zFar == zNear)
            throw new IllegalArgumentException("aspect cannot be 0 | zFar is equal zNear, both are not possible");
        if(Math.abs(fov) >= 180)
            throw new IllegalArgumentException("FOV cannot be greater than 180º");

        double range = tan(fov / 2D) * zNear;
        double left = -range * aspect;
        double right = range * aspect;
        double bottom = -range;
        
        perspective.set(0, 0, (float) ((2f * zNear) / (right - left)));
        perspective.set(1, 1, (float) ((2f * zNear) / (range - bottom)));
        perspective.set(2, 2, (float) -((zFar + zNear) / (zFar - zNear)));
        perspective.set(2, 3, - 1F);
        perspective.set(3, 2, (float) -((2D * zFar * zNear) / (zFar - zNear)));
        
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

        Vector3 f = normalize(sub(center, eye));
        Vector3 u = normalize(up != null ? up : new Vector3(0, 0, 1));
        Vector3 s = normalize(cross(f, u));
        u = cross(s, f);

        look.setColumn(0, s, 0);
        look.setColumn(1, u, 0);
        look.setColumn(2, negate(f), 0);

        look.set(3, 0, -dot(s, eye));
        look.set(3, 1, -dot(u, eye));
        look.set(3, 2,  dot(f, eye));
        
        return look;
    }

    public static Matrix4 frustum(double left, double right, double bottom, double top, double near, double far) {
        Matrix4 mat = new Matrix4(0);
        mat.set(0, 0, (float) ((2 * near) / (right - left)));
        mat.set(1, 1, (float) ((2 * near) / (top - bottom)));
        mat.set(2, 0, (float) ((right + left) / (right - left)));
        mat.set(2, 1, (float) ((top + bottom) / (top - bottom)));
        mat.set(2, 2, (float) - ((far + near) / (far - near)));
        mat.set(2, 3, -1);
        mat.set(3, 2, (float) - ((2f * far * near) / (far - near)));
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
        Matrix4 Inverse = inverse(mul(proj, model));
        Vector4 tmp = new Vector4(win, 1);

        tmp.x = (tmp.x - viewport.x) / viewport.z;
        tmp.y = (tmp.y - viewport.y) / viewport.w;
        tmp = sub(product(2, tmp), new Vector4(1, 1, 1, 1));

        Vector4 obj = mul(Inverse, tmp);
        obj = product(1f / obj.w, obj);
        return new Vector3(obj);
    }

    public static float[] matrixAsArray(Matrix4 mat) {
        float[] array = new float[16];
        int i = 0;
        for(int x = 0; x < 4; x++)
            for(int y = 0; y < 4; y++)
                array[i++] = mat.matrix[x][y];
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
     * <code> |a-b| &lt; 0.00001 </code><br>
     * @param a first float
     * @param b second float
     **/
    public static boolean floatEqualsFloat(float a, float b) {
        float err = Math.abs((a - b) / b);
        return err <= 0.00001;
    }
    
    /**
     * Because calculing float numbers are really imprecise, this function
     * checks for a little difference between them:<br>
     * <code> |a-b| &lt; 0.00001 </code><br>
     * @param a first float
     * @param b second float
     **/
    public static boolean doubleEqualsDouble(double a, double b) {
        double err = Math.abs((a - b) / b);
        return err <= 0.00001;
    }
}