package org.melchor629.engine.objects;

import org.melchor629.engine.Game;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.Keyboard.OnKeyboardEvent;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.input.Mouse.OnMouseClickEvent;
import org.melchor629.engine.input.Mouse.OnMouseMoveEvent;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.Matrix4;
import org.melchor629.engine.utils.math.Vector3;

/**
 * Simple Camera class
 * @author melchor9000
 */
//TODO add to OnResize Listener for projection to change
public class Camera implements OnKeyboardEvent, OnMouseMoveEvent, OnMouseClickEvent {
    protected Vector3 pos, dir, up;
    protected Vector3 rot;
    protected Vector3 speed;
    protected double aspect;
    protected double fov;
    protected double near, far;
    
    private Matrix4 view, proj;
    private boolean needsUpdateView = true, needsUpdateProj = true;
    private double mouseSensibility, movementMultiplier;
    
    /**
     * Creates a camera with position (0, 0, 0) &amp; rotation 0º &amp; 0º,
     * a FOV of 45º, an aspect ratio of 16:9, and a near distance of
     * 1 and far 10.
     */
    public Camera() {
        pos = new Vector3();
        dir = new Vector3(1, 0, 0);
        up = new Vector3(0, 0, 1);
        rot = new Vector3();
        speed = new Vector3();
        fov = 45;
        near = 1;
        far = 10;
        aspect = 16. / 9.;
        mouseSensibility = movementMultiplier = 1;
        
        initListeners();
    }
    
    /**
     * Creates a camera with position {@code pos} &amp; rotation defined
     * by {@code rot} in degrees. A FOV of 45º, an aspect ratio of 16:9,
     * and a near distance of 1 and far 10.
     * @param pos Initial position
     * @param rot Initial rotation of pitch (x), yaw (y) and roll (z)
     */
    public Camera(Vector3 pos, Vector3 rot) {
        this.pos = pos;
        this.rot = rot;
        up = new Vector3(0, 0, 1);
        speed = new Vector3();
        //Init direction
        dir.x((float) (Math.cos(Math.toRadians(rot.x())) * Math.cos(Math.toRadians(rot.y()))));
        dir.z((float) (Math.sin(Math.toRadians(rot.y()))));
        dir.y((float) (Math.sin(Math.toRadians(rot.x())) * Math.cos(Math.toRadians(rot.y()))));
        fov = 45;
        near = 1;
        far = 10;
        aspect = 16. / 9.;
        mouseSensibility = movementMultiplier = 1;
        
        initListeners();
    }
    
    /**
     * Creates a camera with a position {@code pos}, pointing to
     * {@code dir} and with the head looking at {@code up}, like
     * {@code GLM::lookAt(vec3, vec3, vec3)}. A FOV of 45º, an
     * aspect ratio of 16:9, and a near distance of 1 and far 10.
     * @param pos Initial position
     * @param dir Direction to look at
     * @param up Where the head points at
     */
    public Camera(Vector3 pos, Vector3 dir, Vector3 up) {
        this.pos = pos;
        this.dir = dir;
        this.up = up == null ? new Vector3(0, 0, 1) : up;
        speed = new Vector3();
        //Init rotation
        this.dir.normalize();
        this.rot = new Vector3();
        rot.y((float) Math.toDegrees(Math.asin(this.dir.z())));
        rot.x((float) Math.toDegrees(Math.acos(this.dir.x() / Math.cos(Math.toRadians(rot.y())))));
        fov = 45;
        near = 1;
        far = 10;
        aspect = 16. / 9.;
        mouseSensibility = movementMultiplier = 1;
        
        //Seleccionando el cuadrante correcto según donde apunta
        if(rot.x() >= 90.f && dir.z() < 0f)
            rot.x(-90.f);
        
        initListeners();
    }

    /**
     * Updates all matrices if needed. Is not necessary call this method
     * every frame
     */
    public void updateIfNeeded() {
        if(needsUpdateView) {
            getViewMatrix();
        }

        if(needsUpdateProj) {
            getProjectionMatrix();
        }
    }
    
    /**
     * @return the View Matrix with position, direction &amp; up vectors
     */
    public Matrix4 getViewMatrix() {
        if(needsUpdateView) {
            view = GLM.lookAt(pos, GLM.add(pos, dir), up);
            needsUpdateView = false;
        }
        return view;
    }

    /**
     * @return the Projection Matrix of the "lens camera"
     */
    public Matrix4 getProjectionMatrix() {
        if(needsUpdateProj) {
            proj = GLM.perspective(GLM.rad(fov), aspect, near, far);
            needsUpdateProj = false;
        }
        return proj;
    }

    /**
     * @return the Field Of View of the "lens"
     */
    public final double getFOV() {
        return fov;
    }

    /**
     * Changes the FOV (<i>Field Of View</i>) of the camera. FOV can
     * values between 10.0 and 179.0
     * @param fov new FOV value
     */
    public final void setFOV(double fov) {
        this.fov = fov;
        if(this.fov > 179.f)
            this.fov = 179.f;
        else if(this.fov < 10.f)
            this.fov = 10.f;
        needsUpdateProj = true;
    }

    /**
     * Sets the clip panes. This values sets the minimum distance that an object
     * will start to render and the maximum distance where an object will render.
     * @param near Minimum distance
     * @param far Maximum distance
     */
    public final void setClipPanes(double near, double far) {
        if(near <= 0.0) near = 0.1;
        this.near = near;
        this.far = far;
        needsUpdateProj = true;
    }

    /**
     * Sets the aspect ratio calculating from the {@code width} and {@code height}.
     * @param width width
     * @param height height
     */
    public final void setAspectRation(double width, double height) {
        this.aspect = width / height;
        needsUpdateProj = true;
    }

    /**
     * Sets the position of the camera
     * @param x X Coordinate
     * @param y Y Coordinate
     * @param z Z Coordinate
     */
    public final void setPosition(float x, float y, float z) {
        this.pos.x(x).y(y).z(z);
        needsUpdateView = true;
    }

    /**
     * Sets the position of the camera
     * @param pos New position as vector
     */
    public final void setPosition(Vector3 pos) {
        setPosition(pos.x(), pos.y(), pos.z());
    }

    /**
     * @return the position of the camera
     */
    public final Vector3 getPosition() {
        return pos;
    }

    /**
     * This method returns the rotation as a representation of Degrees
     * rotated over (X, Y, Z) axis.
     * @return the rotation of the camera
     */
    public final Vector3 getRotation() {
        return rot;
    }

    /**
     * Changes the rotation of the camera in Degrees
     * @param x Rotation over X axis
     * @param y Rotation over Y axis
     * @param z Rotation over Z axis
     */
    public final void setRotation(float x, float y, float z) {
        this.rot.x(x).y(y).z(z);
        dir.x((float) (Math.cos(Math.toRadians(rot.x())) * Math.cos(Math.toRadians(rot.y()))));
        dir.z((float) (Math.sin(Math.toRadians(rot.y()))));
        dir.y((float) (Math.sin(Math.toRadians(rot.x())) * Math.cos(Math.toRadians(rot.y()))));
        dir.normalize();
        needsUpdateView = true;
    }

    /**
     * Changes the rotation of the camera in Degrees. Every vector component
     * specifies the rotation on every axis
     * @param rot Rotation of the camera as vector
     */
    public final void setRotation(Vector3 rot) {
        setRotation(rot.x(), rot.y(), rot.z());
    }

    /**
     * Gets the direction where the camera is looking at
     * @return direction
     */
    public final Vector3 getLookingAtDirection() {
        return dir;
    }

    /**
     * Gets the calculated speed from the camera movement. Speed is calculated
     * everyframe with the difference between the last frame with the current.
     * @return speed of the camera
     */
    public final Vector3 getSpeed() {
        Vector3 sp = (Vector3) speed.clone();
        sp.product((float) (1f/Timing.getGameTiming().frameTime));
        return sp;
    }

    /**
     * Returns the current value of the Movement Multiplier. This value
     * represents how faster (or slower) the camera moves from the base
     * movement. The base movement is 5 pixels per second.
     * @return Movement multiplier value
     */
    public double getMovementMultiplier() {
        return movementMultiplier;
    }

    /**
     * Sets the new value of the Movement Multiplier. This value
     * represents how faster (or slower) the camera moves from the base
     * movement. The base movement is 5 pixels per second.
     * @param movementMultiplier new value for movement multiplier
     */
    public void setMovementMultiplier(double movementMultiplier) {
        this.movementMultiplier = movementMultiplier;
    }

    /**
     * Returns the current value of mouse sensibility. This value
     * represents how faster (or slower) the camera rotates from the
     * base speed, which is 4º per second per mouse movement.
     * @return Mouse sensibility
     */
    public double getMouseSensibility() {
        return mouseSensibility;
    }

    /**
     * Sets the new value of the Mouse Sensibility. This value
     * represents how faster (or slower) the camera rotates from the
     * base speed, which is 4º per second per mouse movement.
     * @param mouseSensibility new value for mouse sensibility
     */
    public void setMouseSensibility(double mouseSensibility) {
        this.mouseSensibility = mouseSensibility;
    }



    private void initListeners() {
        if(Game.keyboard != null && Game.mouse != null) {
            Game.keyboard.addListener(this);
            Game.mouse.addListener((OnMouseMoveEvent) this);
            //Arreglar esto
            Game.mouse.addListener((OnMouseClickEvent) this);
        }
    }

    public void invoke(Keyboard self, double delta) {
        float cameraSpeed = 5.f * (float) delta * (float) movementMultiplier;
        float x = pos.x(), y = pos.y(), z = pos.z();
        
        if(self.isKeyPressed("W"))
            pos.add(GLM.product(cameraSpeed, dir));
        if(self.isKeyPressed("S"))
            pos.substract(GLM.product(cameraSpeed, dir));
        if(self.isKeyPressed("A"))
            pos.substract(GLM.product(cameraSpeed, GLM.cross(dir, up).normalize()));
        if(self.isKeyPressed("D"))
            pos.add(GLM.product(cameraSpeed, GLM.cross(dir, up).normalize()));
        if(self.isKeyPressed("Q"))
            pos.z(pos.z() + cameraSpeed);
        if(self.isKeyPressed("E"))
            pos.z(pos.z() - cameraSpeed);
        if(self.isKeyPressed("ESCAPE"))
            Game.window.setWindowShouldClose(true);
        
        speed.x(pos.x() - x).y(pos.y() - y).z(pos.z() - z);
        if(speed.x() != 0.f || speed.y() != 0.f || speed.z() != 0.f)
            needsUpdateView = true;
    }

    public void invoke(Mouse self, double delta) {
        if(!self.isCaptured()) return;

        float sensibility = (float) delta * 4 * (float) mouseSensibility;
        
        rot.x(rot.x() - self.getMouseSpeed().x() * sensibility);
        rot.y(rot.y() + self.getMouseSpeed().y() * sensibility);
        
        if(rot.y() > 89.f)
            rot.y(89.f);
        else if(rot.y() < -89.f)
            rot.y(-89.f);
        
        dir.x((float) (Math.cos(Math.toRadians(rot.x())) * Math.cos(Math.toRadians(rot.y()))));
        dir.z((float) (Math.sin(Math.toRadians(rot.y()))));
        dir.y((float) (Math.sin(Math.toRadians(rot.x())) * Math.cos(Math.toRadians(rot.y()))));
        dir.normalize();
        
        needsUpdateView = true;
    }

    public void invoke(Mouse self) {
        if(self.isKeyPressed("LEFT")) {
            self.setCaptured(!self.isCaptured());
        }
    }

    /**
     * A {@link String} representation of the camera
     */
    public String toString() {
        return String.format("Camera [\n    (%.3f, %.3f, %.3f),\n    (%.3fº, %.3fº, %.3fº),\n    (%.1f, %.1f, %.1f) px/s\n]",
                pos.x(), pos.y(), pos.z(), rot.x(), rot.y(), rot.z(), speed.x(), speed.y(), speed.z());
    }
}
