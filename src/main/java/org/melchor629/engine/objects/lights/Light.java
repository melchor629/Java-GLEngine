package org.melchor629.engine.objects.lights;

import org.melchor629.engine.gl.ShaderProgram;
import org.melchor629.engine.utils.math.Vector3;

/**
 * Base class for a Light
 */
public abstract class Light {
    protected Vector3 position = new Vector3();
    protected Vector3 color = new Vector3(1, 1, 1);
    protected float linearAttenuation = 1.f, quadraticAttenuation = 0.f;

    public void setPosition(Vector3 pos) {
        setPosition(pos.x(), pos.y(), pos.z());
    }

    public void setPosition(float x, float y, float z) {
        position.x(x).y(y).z(z);
    }

    public Vector3 getPosition() {
        return position;
    }

    public void setColor(Vector3 color) {
        setColor(color.x(), color.y(), color.z());
    }

    public void setColor(float r, float g, float b) {
        color.x(r).y(g).z(b);
    }

    public Vector3 getColor() {
        return color;
    }

    public void setLinearAttenuation(float att) {
        linearAttenuation = att;
    }

    public float getLinearAttenuation() {
        return linearAttenuation;
    }

    public void setQuadraticAttenuation(float att) {
        quadraticAttenuation = att;
    }

    public float getQuadraticAttenuation() {
        return quadraticAttenuation;
    }

    public abstract void configureLightInShader(ShaderProgram shader, int i);

    protected float calculateLightSphere() {
        float constant  = 1;
        float lightMax  = Math.max(Math.max(color.x(), color.y()), color.z());
        return (float) (-linearAttenuation + Math.sqrt(Math.pow(linearAttenuation, 2) - 4 * quadraticAttenuation
                * (constant - (256.0 / 5.0) * lightMax))) / (2 * quadraticAttenuation);
    }

    @Override
    public String toString() {
        return String.format("%s = {\nposition: (%s),\ncolor: (%s)\n}", getClass().getSimpleName(), position, color);
    }

    @Override
    public abstract boolean equals(Object o);

    @Override
    public abstract int hashCode();
}
