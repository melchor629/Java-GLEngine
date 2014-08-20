package org.melchor629.engine.gl.types;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import org.melchor629.engine.gl.GLError;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.mat4;

import static org.melchor629.engine.Game.gl;

/**
 * Class for create shaders and shaders programs, manage them and
 * make games be better. Shaders can be created from files or passing
 * them as Strings.
 * @author melchor9000
 */
public class ShaderProgram {
    protected int vertexShader, fragmentShader, geometryShader, shaderProgram;
    protected HashMap<String, Integer> uniforms;
    protected Attrib[] attribs;

    /**
     * Create a Shader program from files, and checks for errors
     * @param vertex Vertex shader file
     * @param fragment Fragment shader file
     * @throws FileNotFoundException If the file doesn't exist or is a directory
     * @throws IOException If an error occurred while reading files
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    public ShaderProgram(File vertex, File fragment) throws FileNotFoundException, IOException {
        this(IOUtils.readFile(vertex), IOUtils.readFile(fragment));
    }

    /**
     * Create a Shader program from files, and checks for errors
     * @param vertex Vertex shader file
     * @param fragment Fragment shader file
     * @param geometry Geometry shader file
     * @throws FileNotFoundException If the file doesn't exist or is a directory
     * @throws IOException If an error occurred while reading files
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    public ShaderProgram(File vertex, File fragment, File geometry) throws FileNotFoundException, IOException {
        this(IOUtils.readFile(vertex), IOUtils.readFile(fragment), IOUtils.readFile(geometry));
    }

    /**
     * Create a Shader program from string, and checks for errors
     * @param vertex Vertex shader source code
     * @param fragment Fragment shader source code
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    public ShaderProgram(String vertex, String fragment) {
        this(vertex, fragment, null);
    }

    /**
     * Create a Shader program from strings, checking for errors for every shader,
     * linking them to a program and checking for linking errors.
     * @param vertex Vertex shader source code
     * @param fragment Fragment shader source code
     * @param geometry Geometry shader source code, can be null
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    public ShaderProgram(String vertex, String fragment, String geometry) {
        vertexShader = fragmentShader = geometryShader = shaderProgram = -1;
        
        vertexShader = createShader(Renderer.ShaderType.VERTEX, vertex);
        fragmentShader = createShader(Renderer.ShaderType.FRAGMENT, fragment);
        if(geometry != null) {
            geometryShader = createShader(Renderer.ShaderType.GEOMETRY, geometry);
            shaderCheckForErrors(geometryShader);
        }
        shaderCheckForErrors(vertexShader);
        shaderCheckForErrors(fragmentShader);

        shaderProgram = createProgram();
        programCheckForErrors();
        
        bind();
        fetchUniforms();
        fetchAttribs();
        unbind();
    }

    public final void bind() {
        gl.useProgram(shaderProgram);
    }

    public final void unbind() {
        gl.useProgram(0);
    }

    /**
     * Enable all attribs passed to this method. <b>This method</b> binds for
     * you the shader, but also unbinds it ({@code glUseProgram}).
     * @param attribss Variable list for Strings, or an array of Strings
     */
    public void enableAttribs(String... attribss) {
        bind();
        for(String attribS : attribss) {
            int loc = gl.getAttribLocation(shaderProgram, attribS);
            if(loc != -1)
                gl.enableVertexAttribArray(loc);
        }
        fetchAttribs();
        unbind();
    }

    /**
     * Specify the location and data format of the array of generic vertex
     * attributes at {@code attribName} to use when rendering.
     * @param attribName Name of the attribute
     * @param type Specifies the number of components per generic vertex attribute.
     *             Must be 1, 2, 3, 4
     * @param t Specifies the data type of each component in the array
     * @param norm Specifies whether fixed-point data values should be normalized
     * @param stride Specifies the byte offset between consecutive generic vertex attributes.
     * @param offset Specifies a offset of the first component of the first generic vertex attribute in the array
     */
    public void vertexAttribPointer(String attribName, int type, Renderer.type t, boolean norm, int stride, long offset) {
        int loc = gl.getAttribLocation(shaderProgram, attribName);
        if(loc == -1) throw new GLError("glVertexAttribLocation", "Attrib named '" + attribName + "' does not exist");
        gl.vertexAttribPointer(loc, type, t, norm, stride, offset);
    }

    /**
     * Delete the shader program.
     */
    public void delete() {
        if(vertexShader != -1) {
            gl.deleteShader(vertexShader);
            vertexShader = -1;
        }
        if(fragmentShader != -1) {
            gl.deleteShader(fragmentShader);
            fragmentShader = -1;
        }
        if(geometryShader != -1) {
            gl.deleteShader(geometryShader);
            geometryShader = -1;
        }
        if(shaderProgram != -1) {
            gl.deleteProgram(shaderProgram);
            shaderProgram = -1;
        }
    }

    public void setUniform(String name, int v0) {
        bind();
        gl.uniform1i(uniforms.get(name), v0);
    }

    public void setUniform(String name, int v0, int v1) {
        bind();
        gl.uniform2i(uniforms.get(name), v0, v1);
    }

    public void setUniform(String name, int v0, int v1, int v2) {
        bind();
        gl.uniform3i(uniforms.get(name), v0, v1, v2);
    }

    public void setUniform(String name, int v0, int v1, int v2, int v3) {
        bind();
        gl.uniform4i(uniforms.get(name), v0, v1, v2, v3);
    }

    public void setUniform(String name, float v0) {
        bind();
        gl.uniform1f(uniforms.get(name), v0);
    }

    public void setUniform(String name, float v0, float v1) {
        bind();
        gl.uniform2f(uniforms.get(name), v0, v1);
    }

    public void setUniform(String name, float v0, float v1, float v2) {
        bind();
        gl.uniform3f(uniforms.get(name), v0, v1, v2);
    }

    public void setUniform(String name, float v0, float v1, float v2, float v3) {
        bind();
        gl.uniform4f(uniforms.get(name), v0, v1, v2, v3);
    }

    public void setUniformMatrix(String name, mat4 matrix) {
        bind();
        gl.uniformMatrix4(uniforms.get(name), false, GLM.matrixAsArray(matrix));
    }

    @Override
    protected void finalize() {
        delete();
    }

    /**
     * Create a shader of a type and with a source code and then return the shader
     * @param type Type of the shader
     * @param str Source code
     * @return Shader
     */
    protected final int createShader(Renderer.ShaderType type, String str) {
        int shader = gl.createShader(type);
        gl.shaderSource(shader, str);
        gl.compileShader(shader);
        return shader;
    }

    /**
     * Check for errors. If there is an error, then will throw an GLError
     * @param shader
     * @throws GLError If COMPILE_STATUS is not GL_TRUE (an error occurred
     *                 while compiling)
     */
    protected final void shaderCheckForErrors(int shader) {
        int status = gl.getShader(shader, Renderer.GLGetShader.COMPILE_STATUS);
        if(status != 1)
            throw new GLError(gl.getShaderInfoLog(shader));
    }

    /**
     * Check for errors. If there is an error, then will throw an GLError
     * @throws GLError If LINK_STATUS is not GL_TRUE (an error occurred
     *                 while linking)
     */
    protected final void programCheckForErrors() {
        int status = gl.getProgram(shaderProgram, Renderer.GLGetProgram.LINK_STATUS);
        if(status != 1)
            throw new GLError(gl.getShaderInfoLog(shaderProgram));
    }

    /**
     * Create a program, attaching all shaders
     * @return the program
     */
    protected final int createProgram() {
        int program = gl.createProgram();
        gl.attachShader(program, vertexShader);
        gl.attachShader(program, fragmentShader);
        if(geometryShader != -1)
            gl.attachShader(program, geometryShader);
        gl.linkProgram(program);
        return program;
    }

    /**
     * Fetch all (active) uniforms in the program.
     * The method is really easy:<br>
     * &nbsp;&nbsp;· Get the count of uniforms with {@code glGetProgram}
     * with a <i>pname</i> of {@code GL_ACTIVE_UNIFORMS}.<br>
     * &nbsp;&nbsp;· Then obtain the largest uniform name length with
     * {@code glGetProgram} with a <i>pname</i> of
     * {@code GL_ACTIVE_UNIFORM_MAX_LENGTH}.<br>
     * &nbsp;&nbsp;· Then loop in a <b>for</b> loop ({@code i < count})
     * fetching the name of the uniform and its location and storing
     * them into a HashMap.
     */
    private void fetchUniforms() {
        int count = gl.getProgram(shaderProgram, Renderer.GLGetProgram.ACTIVE_UNIFORMS);
        uniforms = new HashMap<String, Integer>(count);
        int strlen = gl.getProgram(shaderProgram, Renderer.GLGetProgram.ACTIVE_UNIFORM_MAX_LENGTH);
        for(int i = 0; i < count; i++) {
            String name = gl.getActiveUniform(shaderProgram, i, strlen);
            int loc = gl.getUniformLocation(shaderProgram, name);
            uniforms.put(name, loc);
        }
    }

    private void fetchAttribs() {
        int count = gl.getProgram(shaderProgram, Renderer.GLGetProgram.ACTIVE_ATTRIBUTES);
        int strlen = gl.getProgram(shaderProgram, Renderer.GLGetProgram.ACTIVE_ATTRIBUTE_MAX_LENGTH);
        attribs = new Attrib[count];
        for(int i = 0; i < count; i++) {
            Attrib a = new Attrib();
            a.name = gl.getActiveAttrib(shaderProgram, i, strlen);
            a.size = gl.getActiveAttribSize(shaderProgram, i);
            a.type = gl.getActiveAttribType(shaderProgram, i);
            a.location = gl.getAttribLocation(shaderProgram, a.name);
            attribs[i] = a;
        }
    }

    /**
     * Struct for store info about an attrib
     * @author melchor9000
     */
    protected static class Attrib {
        String name = null;
        int type = -1;
        int size = 0;
        int location = -1;
    }
}
