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
    private Vector3 pos, dir, up, worldUp, right;
    private float yaw = 0.f, pitch = 0.f, roll = 0.f;
    private Vector3 speed;
    private double aspect;
    private double fov;
    private double near, far;
    private float invertY = 1.f;
    
    private Matrix4 view, proj;
    private boolean needsUpdateView = true, needsUpdateProj = true;
    private double mouseSensibility, movementMultiplier;
    
    /**
     * Creates a camera with position (0, 0, 0) &amp; rotation 0º &amp; 0º,
     * a FOV of 45º, an aspect ratio of 16:9, and a near distance of
     * 1 and far 10.
     * @param game Game object
     */
    public Camera(Game game) {
        pos = new Vector3();
        dir = new Vector3();
        worldUp = new Vector3(0, 1, 0);
        speed = new Vector3();
        fov = 45;
        near = 1;
        far = 10;
        aspect = 16. / 9.;
        mouseSensibility = movementMultiplier = 1;
        updateCameraVectors();
        
        initListeners(game);
    }
    
    /**
     * Creates a camera with position {@code pos} &amp; rotation defined
     * by {@code rot} in degrees. A FOV of 45º, an aspect ratio of 16:9,
     * and a near distance of 1 and far 10.
     * @param game Game object
     * @param pos Initial position
     * @param rot Initial rotation of yaw (x), pitch (y) and roll (z)
     */
    public Camera(Game game, Vector3 pos, Vector3 rot) {
        this.pos = pos;
        yaw = rot.x();
        pitch = rot.y();
        roll = rot.z();
        worldUp = new Vector3(0, 1, 0);
        dir = new Vector3();
        speed = new Vector3();
        fov = 45;
        near = 1;
        far = 10;
        aspect = 16. / 9.;
        mouseSensibility = movementMultiplier = 1;
        
        initListeners(game);
        updateCameraVectors();
    }
    
    /**
     * Creates a camera with a position {@code pos}, pointing to
     * {@code dir} and with the head looking at {@code up}, like
     * {@code GLM::lookAt(vec3, vec3, vec3)}. A FOV of 45º, an
     * aspect ratio of 16:9, and a near distance of 1 and far 10.
     * @param game Game object
     * @param pos Initial position
     * @param dir Direction to look at
     * @param up Where the head points at
     */
    public Camera(Game game, Vector3 pos, Vector3 dir, Vector3 up) {
        this.pos = pos;
        this.dir = dir;
        worldUp = up == null ? new Vector3(0, 1, 0) : up;
        speed = new Vector3();
        //Init rotation
        this.dir.normalize();
        yaw = ((float) Math.toDegrees(Math.asin(this.dir.z())));
        pitch = ((float) Math.toDegrees(Math.acos(this.dir.x() / Math.cos(Math.toRadians(pitch)))));
        fov = 45;
        near = 1;
        far = 10;
        aspect = 16. / 9.;
        mouseSensibility = movementMultiplier = 1;
        
        //Seleccionando el cuadrante correcto según donde apunta
        if(pitch >= 90.f && roll < 0f)
            pitch = -90.f;
        
        initListeners(game);
        updateCameraVectors();
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
        return null;
    }

    /**
     * Changes the rotation of the camera in Degrees
     * @param x Rotation over X axis
     * @param y Rotation over Y axis
     * @param z Rotation over Z axis
     */
    public final void setRotation(float x, float y, float z) {
        yaw = x; pitch = y; roll = z;
        updateCameraVectors();
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
     * Sets the camera rotation to look at the direction {@code dir}
     * @param dir direction to look at
     */
    public final void setLookingAt(Vector3 dir) {
        this.dir.x(dir.x()).y(dir.y()).z(dir.z());
        this.dir.normalize();
        pitch = ((float) Math.toDegrees(Math.asin(this.dir.y())));
        yaw = ((float) Math.toDegrees(Math.acos(this.dir.x() / Math.cos(Math.toRadians(pitch)))));
        roll = 0;
        needsUpdateView = true;
    }

    /**
     * Sets the camera rotation to look at the direction {@code dir}
     * @param x x coordinate of the direction to look at
     * @param y y coordinate of the direction to look at
     * @param z z coordinate of the direction to look at
     */
    public final void setLookingAt(float x, float y, float z) {
        this.dir.x(x).y(y).z(z);
        this.dir.normalize();
        pitch = ((float) Math.toDegrees(Math.asin(this.dir.y())));
        yaw = ((float) Math.toDegrees(Math.acos(this.dir.x() / Math.cos(Math.toRadians(pitch)))));
        roll = 0;
        needsUpdateView = true;
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

    /**
     * If set to true, inverts Y axis from mouse.
     * @param invert whether to invert Y axis or not
     */
    public void setInvertY(boolean invert) {
        invertY = invert ? -1.f : 1.f;
    }

    private void updateCameraVectors() {
        //rot.x() -> Yaw
        //rot.y() -> Pitch
        //rot.z() -> Roll
        dir.x((float) (Math.cos(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))));
        dir.y((float) (Math.sin(Math.toRadians(pitch))));
        dir.z((float) (Math.sin(Math.toRadians(yaw)) * Math.cos(Math.toRadians(pitch))));
        dir.normalize();

        right = (Vector3) GLM.cross(dir, worldUp).normalize();
        up = (Vector3) GLM.cross(right, dir).normalize();
    }



    private void initListeners(Game game) {
        if(game != null) {
            game.getKeyboard().addListener(this);
            game.getMouse().addListener((OnMouseMoveEvent) this);
            game.getMouse().addListener((OnMouseClickEvent) this);
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
            pos.substract(GLM.product(cameraSpeed, right));
        if(self.isKeyPressed("D"))
            pos.add(GLM.product(cameraSpeed, right));
        if(self.isKeyPressed("Q")) {
            if(worldUp.z() != 0.0) pos.z(pos.z() + cameraSpeed);
            else if(worldUp.y() != 0.0) pos.y(pos.y() + cameraSpeed);
        }
        if(self.isKeyPressed("E")) {
            if(worldUp.z() != 0.0) pos.z(pos.z() - cameraSpeed);
            else if(worldUp.y() != 0.0) pos.y(pos.y() - cameraSpeed);
        }
        
        speed.x(pos.x() - x).y(pos.y() - y).z(pos.z() - z);
        if(speed.x() != 0.f || speed.y() != 0.f || speed.z() != 0.f)
            needsUpdateView = true;
    }

    public void invoke(Mouse self, double delta) {
        if(!self.isCaptured()) return;

        float sensibility = (float) delta * 4 * (float) mouseSensibility;

        yaw += self.getMouseSpeed().x() * sensibility; //Yaw
        pitch += self.getMouseSpeed().y() * sensibility * invertY; //Pitch
        if(pitch > 89.f)
            pitch = 89.f;
        else if(pitch < -89.f)
            pitch = -89.f;
        
        updateCameraVectors();
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
                pos.x(), pos.y(), pos.z(), yaw, pitch, roll, speed.x(), speed.y(), speed.z());
    }
}
