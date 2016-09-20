package org.melchor629.engine.utils.math;

/**
 * Class that manages transformation for some object. It has
 * three properties: Location, Rotation &amp; Scale. Modifing any
 * of this values will not create the final matrix, only when
 * it is wanted to get the matrix. Is posible to create more
 * than one ModelMatrix instance, is not like OpenGL 2.
 * @author melchor9000
 */
public class ModelMatrix {
    protected Vector3 loc, rot, scale;
    protected Matrix4 matrix;
    
    /**
     * Creates a model matrix
     */
    public ModelMatrix() {
        loc = new Vector3(0, 0, 0);
        rot = new Vector3(0, 0, 0);
        scale = new Vector3(1, 1, 1);
        matrix = null;
    }
    
    /**
     * Sets the location of the object (in the Matrix representation)
     * in the following values of the coords.
     * @param x X coord
     * @param y Y coord
     * @param z Z coord
     * @return Itself, for chaining methods
     */
    public ModelMatrix setLocation(float x, float y, float z) {
        setVector(loc, x, y, z);
        return this;
    }

    /**
     * Sets the location of the object (in the Matrix representation)
     * in the following values of the coords.
     * @param loc new location
     * @return Itself, for chaining methods
     */
    public ModelMatrix setLocation(Vector3 loc) {
        this.loc = loc;
        matrix = null;
        return this;
    }
    
    /**
     * Translates the object (in the Matrix representation), like OpenGL 2
     * {@code glTranslate3f(float x, float y, float z) }.
     * @param x Amount of values to translate over X axis
     * @param y Amount of values to translate over Y axis
     * @param z Amount of values to translate over Z axis
     * @return Itself, for chaining methods
     */
    public ModelMatrix translate(float x, float y, float z) {
        loc.add(new Vector3(x, y, z));
        matrix = null;
        return this;
    }

    /**
     * Translates the object (in the Matrix representation), like OpenGL 2
     * {@code glTranslate3f(float x, float y, float z)}, but with a Vector.
     * @param v Amount of units to translate
     * @return Itself, for chaining methods
     */
    public ModelMatrix translate(Vector3 v) {
        return this.translate(v.x(), v.y(), v.z());
    }
    
    /**
     * Sets the rotation of the object in the following angles from
     * the axes.
     * @param pitch Pitch axis (X)
     * @param yaw Yaw axis (Y)
     * @param roll Roll axis (Z)
     * @return Itself, for chaining methods
     */
    public ModelMatrix setRotation(float pitch, float yaw, float roll) {
        setVector(rot, pitch, yaw, roll);
        return this;
    }
    
    /**
     * Rotate the object along the axis selected and with certain angle,
     * like OpenGL 2 function {@code glRotatef(float angle, float x, float
     * y, float z) }.
     * @param angle Angle of the rotation
     * @param x Amount of the angle to rotate along the X axis
     * @param y Amount of the angle to rotate along the Y axis
     * @param z Amount of the angle to rotate along the Z axis
     * @return Itself, for chaining methods
     */
    public ModelMatrix rotate(float angle, float x, float y, float z) {
        if(Float.isInfinite(angle)) return this;
        while(angle > Math.PI * 2)
            angle -= Math.PI * 2;

        rot.x(angle * x + rot.x());
        rot.y(angle * y + rot.y());
        rot.z(angle * z + rot.z());
        
        if(rot.x() > 2*Math.PI)
            rot.x(rot.x() - 2 * (float) Math.PI);
        if(rot.y() > 2*Math.PI)
            rot.y(rot.y() - 2 * (float) Math.PI);
        if(rot.z() > 2*Math.PI)
            rot.z(rot.z() - 2 * (float) Math.PI);

        matrix = null;
        return this;
    }

    /**
     * Rotate the object along the axis selected and with certain angle,
     * like OpenGL 2 function {@code glRotatef(float angle, float x, float
     * y, float z) }, using a Vector as axis.
     * @param v Axis for the rotation
     * @param angle Amount of degrees to rotate
     * @return Itself, for chaining methods
     */
    public ModelMatrix rotate(Vector3 v, float angle) {
        return this.rotate(angle, v.x(), v.y(), v.z());
    }

    /**
     * Rotate the object along the axis selected and with certain angle,
     * like OpenGL 2 function {@code glRotatef(float angle, float x, float
     * y, float z) }, using a Vector as axis and its fourth component as
     * angle.
     * @param v Axis and angle for the rotation
     * @return Itself, for chaining methods
     */
    public ModelMatrix rotate(Vector4 v) {
        return this.rotate(v.w(), v.x(), v.y(), v.z());
    }
    
    /**
     * Set the scale of the object in a non uniform scaling.
     * Scales the object an amount of desired scale over an
     * axis.
     * @param x Amount of scale factor over X axis
     * @param y Amount of scale factor over Y axis
     * @param z Amount of scale factor over Z axis
     * @return Itself, for chaining methods
     */
    public ModelMatrix setScale(float x, float y, float z) {
        setVector(scale, x, y, z);
        return this;
    }

    /**
     * Set the scale of the object in a non uniform scaling.
     * Scales the object an amount of desired scale over an
     * axis.
     * @param scale Amount of scale factor as a vector
     * @return Itself, for chaining methods
     */
    public ModelMatrix setScale(Vector3 scale) {
        this.scale = scale;
        matrix = null;
        return this;
    }
    
    /**
     * Produces a nonuniform scaling along the x, y, and z
     * axes. The three parameters indicate the desired scale
     * factor along each of the three axes. From {@code
     * glScalef(float x, float y, float z)} function of OpenGL 2.
     * @param x Specify scale factor along the X axis
     * @param y Specify scale factor along the Y axis
     * @param z Specify scale factor along the Z axis
     * @return Itself, for chaining methods
     */
    public ModelMatrix scale(float x, float y, float z) {
        scale.x(scale.x() + x);
        scale.y(scale.y() + y);
        scale.z(scale.z() + z);
        matrix = null;
        return this;
    }

    /**
     * Produces a nonuniform scaling along the x, y, and z
     * axes. The three parameters indicate the desired scale
     * factor along each of the three axes. From {@code
     * glScalef(float x, float y, float z)} function of OpenGL 2,
     * using a Vector
     * @param v Amount of units to scale
     * @return Itself, for chaining methods
     */
    public ModelMatrix scale(Vector3 v) {
        return this.scale(v.x(), v.y(), v.z());
    }
    
    /**
     * Sets all values to initial:<br>
     * &nbsp;&nbsp;- Location: (0, 0, 0)<br>
     * &nbsp;&nbsp;- Rotation: (0, 0, 0)<br>
     * &nbsp;&nbsp;- Scale: (1, 1, 1)<br>
     * @return Itself, for chaining methods
     */
    public ModelMatrix setIdentity() {
        scale.fillWithValues(1f, 1f, 1f);
        rot.substract(rot);
        loc.substract(loc);
        matrix.setIdentity();
        return this;
    }

    /**
     * @return the current location
     */
    public Vector3 getLocation() {
        return loc;
    }

    /**
     * @return the current rotation (in radians)
     */
    public Vector3 getRotation() {
        return rot;
    }

    /**
     * @return the current scale
     */
    public Vector3 getScale() {
        return scale;
    }
    
    /**
     * Obtains the model matrix from the Location,
     * Rotation and Scale set
     * @return Model Matrix
     */
    public Matrix4 getModelMatrix() {
        if(matrix == null) calculateMatrix();
        return matrix;
    }
    
    private void calculateMatrix() {
        matrix = GLM.translateMatrix(new Matrix4(), loc);
        matrix = GLM.rotateMatrix(matrix, rot.x(), new Vector3(1, 0, 0));
        matrix = GLM.rotateMatrix(matrix, rot.y(), new Vector3(0, 1, 0));
        matrix = GLM.rotateMatrix(matrix, rot.z(), new Vector3(0, 0, 1));
        matrix = GLM.scaleMatrix(matrix, scale);
    }

    private void setVector(Vector3 vec, float x, float y, float z) {
        vec.x(x);
        vec.y(y);
        vec.z(z);
        calculateMatrix();
    }
}
