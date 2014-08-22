package org.melchor629.engine.al.types;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.vec3;

import static org.melchor629.engine.Game.al;

/**
 * Class for manage the listener of a Sound System through OpenAL
 * @author melchor9000
 */
public class Listener {

    /**
     * "<b>Master gain</b>", the global volume, because is the volume
     * of the listener.
     * @param value Volume to be set [0.0, 1.0]
     */
    public static final void setGain(float value) {
        al.listenerf(AL.Listener.GAIN, value);
    }

    /**
     * Sets the position of the listener as 3 floats
     * @param x X component of the position
     * @param y Y component of the position
     * @param z Z component of the position
     */
    public static final void setPosition(float x, float y, float z) {
        al.listener3f(AL.Listener.POSITION, x, y, z);
    }

    /**
     * Sets the position of the listener as 3 integers
     * @param x X component of the position
     * @param y Y component of the position
     * @param z Z component of the position
     */
    public static final void setPosition(int x, int y, int z) {
        al.listener3i(AL.Listener.POSITION, x, y, z);
    }

    /**
     * Sets the position of the listener as a vec3
     * @param vec vector indicating the position
     */
    public static final void setPosition(vec3 vec) {
        setPosition(vec.x, vec.y, vec.z);
    }
    
    //TODO setPosition(ivec3)

    /**
     * Sets the velocity of the listener (perfect for Doppler effect)
     * with three floats.
     * @param vx X velocity component
     * @param vy Y velocity component
     * @param vz Z velocity component
     */
    public static final void setVelocity(float vx, float vy, float vz) {
        al.listener3f(AL.Listener.VELOCITY, vx, vy, vz);
    }

    /**
     * Sets the velocity of the listener (perfect for Doppler effect)
     * with three integers.
     * @param vx X velocity component
     * @param vy Y velocity component
     * @param vz Z velocity component
     */
    public static final void setVelocity(int vx, int vy, int vz) {
        al.listener3i(AL.Listener.VELOCITY, vx, vy, vz);
    }

    /**
     * Sets the velocity of the listener (perfect for Doppler effect)
     * with a {@link org.melchor629.engine.utils.math.vec3 vec3}.
     * @param vec Velocity as vector
     */
    public static final void setVelocity(vec3 vec) {
        setVelocity(vec.x, vec.y, vec.z);
    }

    //TODO setVelocity(ivec3)

    /**
     * Sets the orientation of the listener with two vectors represented
     * by 3 floats for every vector. Orientation, in conjunction with
     * {@link #setPosition(float, float, float)} turns the sound outputted
     * by OpenAL into a 3D space. Vector <i>at</i>...
     * @param atx X component of vector <i>at</i>
     * @param aty Y component of vector <i>at</i>
     * @param atz Z component of vector <i>at</i>
     * @param upx
     * @param upy
     * @param upz
     */
    public static final void setOrientation(float atx, float aty, float atz, float upx, float upy, float upz) {
        al.listener(AL.Listener.ORIENTATION, new float[] {atx, aty, atz, upx, upy, upz});
    }

    public static final void setOrientation(int atx, int aty, int atz, int upx, int upy, int upz) {
        al.listener(AL.Listener.ORIENTATION, new int[] {atx, aty, atz, upx, upy, upz});
    }

    public static final void setOrientation(vec3 at, vec3 up) {
        setOrientation(at.x, at.y, at.z, up.x, up.y, up.z);
    }

    //TODO setOrientation(ivec3 at, ivec3 up)

    /**
     * Calculates the <i>at</i> vector with the given rotaion of the camera,
     * and then it applies to the orientation.
     * @param rot Vector with the rotation of the camera/listener
     * @param up Up vector
     */
    public static final void calculateAndSetOrientation(vec3 rot, vec3 up) {
        float x = (float) (GLM.sin(-rot.x - 90.d));
        float y = (float) GLM.sin(-rot.y - 90.d);
        float z = (float) GLM.cos(-rot.x - 90.d);
        setOrientation(x, y, z, up.x, up.y, up.z);
    }
}
