package org.melchor629.engine.utils;

import org.melchor629.engine.gl.types.ShaderProgram;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Manages a bunch of shaders, ready for reuse them
 */
public class ShaderManager {
    private ArrayList<ShaderProgramWrapper> shaders;
    private static final ShaderManager instance = new ShaderManager();

    public static ShaderManager getInstance() {
        return instance;
    }

    private ShaderManager() {
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
            if(geometry != null) shaderProgram = new ShaderProgram(vertex, fragment, geometry);
            else shaderProgram = new ShaderProgram(vertex, fragment);

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
                shaderProgram = new ShaderProgram(vertexStr, fragmentStr,
                        IOUtils.readStream(IOUtils.getResourceAsStream(geometry)));
            else
                shaderProgram = new ShaderProgram(vertexStr, fragmentStr);

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
