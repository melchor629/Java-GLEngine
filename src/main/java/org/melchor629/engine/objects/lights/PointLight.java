package org.melchor629.engine.objects.lights;

import org.melchor629.engine.gl.ShaderProgram;

/**
 * Created by melchor9000 on 11/9/16.
 */
public class PointLight extends Light {
    @Override
    public void configureLightInShader(ShaderProgram shader, int i) {
        shader.setUniform("lights[" + i + "].type", 1);
        shader.setUniform("lights[" + i + "].position", position.x(), position.y(), position.z());
        shader.setUniform("lights[" + i + "].color", color.x(), color.y(), color.z());
        shader.setUniform("lights[" + i + "].linear", linearAttenuation);
        shader.setUniform("lights[" + i + "].quadratic", quadraticAttenuation);
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof PointLight) {
            PointLight p = (PointLight) o;
            return p.position.equals(position) && p.color.equals(color);
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        return position.hashCode() * 17 + color.hashCode();
    }
}
