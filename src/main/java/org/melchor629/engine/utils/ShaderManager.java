package org.melchor629.engine.utils;

import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.ShaderProgram;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages a bunch of shaders, ready for reuse them
 */
public class ShaderManager {
    private ArrayList<ShaderProgramWrapper> shaders;
    private final GLContext gl;

    public ShaderManager(GLContext gl) {
        this.gl = gl;
        shaders = new ArrayList<>();
    }

    public ShaderProgram loadShader(String name, File vertex, File fragment) throws IOException {
        return loadShader(name, vertex, fragment, null);
    }

    public ShaderProgram loadShader(String name, File vertex, File fragment, File geometry) throws IOException {
        ShaderProgram shaderProgram;
        if(searchShader(name) != null)
            shaderProgram = searchShader(name);
        else {
            if(geometry != null) shaderProgram = gl.createShader(vertex, fragment, geometry);
            else shaderProgram = gl.createShader(vertex, fragment);

            ShaderProgramWrapper shaderProgramWrapper = new ShaderProgramWrapper();
            shaderProgramWrapper.name = name;
            shaderProgramWrapper.shader = shaderProgram;
            shaders.add(shaderProgramWrapper);
        }
        return shaderProgram;
    }

    public ShaderProgram loadShader(String name, String vertex, String fragment, String geometry) throws IOException {
        ShaderProgram shaderProgram;
        if(searchShader(name) != null)
            shaderProgram = searchShader(name);
        else {
            String vertexStr = IOUtils.readStream(IOUtils.getResourceAsStream(vertex)),
                    fragmentStr = IOUtils.readStream(IOUtils.getResourceAsStream(fragment));
            if(geometry != null)
                shaderProgram = gl.createShader(vertexStr, fragmentStr,
                        IOUtils.readStream(IOUtils.getResourceAsStream(geometry)));
            else
                shaderProgram = gl.createShader(vertexStr, fragmentStr);

            ShaderProgramWrapper shaderProgramWrapper = new ShaderProgramWrapper();
            shaderProgramWrapper.name = name;
            shaderProgramWrapper.shader = shaderProgram;
            shaders.add(shaderProgramWrapper);
        }
        return shaderProgram;
    }

    public ShaderProgram loadShader(String name, String vertex, String fragment) throws IOException {
        return loadShader(name, vertex, fragment, null);
    }

    public ShaderProgram searchShader(String name) {
        int i = 0;
        while(i < shaders.size() && !shaders.get(i).name.equals(name))
            i++;
        return i < shaders.size() ? shaders.get(i).shader : null;
    }

    static class ShaderProgramWrapper {
        String name;
        ShaderProgram shader;

        public String getName() {
            return name;
        }

        public ShaderProgram getShader() {
            return shader;
        }
    }
}
