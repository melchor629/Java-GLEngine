package org.melchor629.engine.gl.types;

import org.melchor629.engine.gl.GLError;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.mat4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;

import static org.melchor629.engine.Game.gl;

/**
 * Class for create shaders and shaders programs, manage them and
 * make games be better. Shaders can be created from files or passing
 * them as Strings.
 * TODO comprobar si el nombre del UNIFORM existe, y si no THROW
 * @author melchor9000
 */
public class ShaderProgram {
    protected int vertexShader, fragmentShader, geometryShader, shaderProgram;
    protected HashMap<String, Integer> uniforms;
    protected Attrib[] attribs;
    protected boolean linked = false, binded = false;

    private static ShaderProgram currentBindedShader;

    /**
     * Create a Shader program from files, and checks for errors
     * @param vertex Vertex shader file
     * @param fragment Fragment shader file
     * @throws FileNotFoundException If the file doesn't exist or is a directory
     * @throws IOException If an error occurred while reading files
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    public ShaderProgram(File vertex, File fragment) throws IOException {
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
    public ShaderProgram(File vertex, File fragment, File geometry) throws IOException {
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
            shaderCheckForErrors(Renderer.ShaderType.GEOMETRY, geometryShader);
        }
        shaderCheckForErrors(Renderer.ShaderType.VERTEX, vertexShader);
        shaderCheckForErrors(Renderer.ShaderType.FRAGMENT, fragmentShader);

        shaderProgram = createProgram();
    }

    /**
     * Bind the Shader Program.<br>
     * If the program was not linked, it will link it.
     */
    public final void bind() {
        if(!linked) {
            gl.attachShader(shaderProgram, vertexShader);
            gl.attachShader(shaderProgram, fragmentShader);
            if(geometryShader != -1)
                gl.attachShader(shaderProgram, geometryShader);
            gl.linkProgram(shaderProgram);
            programCheckForErrors();
            fetchUniforms();
            fetchAttribs();
            linked = true;
        }
        if(currentBindedShader == null || !currentBindedShader.equals(this)) {
            gl.useProgram(shaderProgram);
            binded = true;
            currentBindedShader = this;
        }
    }

    /**
     * Unbind the program
     */
    public final void unbind() {
        if(binded) {
            gl.useProgram(0);
            binded = false;
            currentBindedShader = null;
        }
    }

    /**
     * Explicitly specifies the binding of the user-defined varying out variable {@code attrib}
     * to fragment shader color number {@code colorNumber} for program program.
     * {@code colorNumber} must be less than GL_MAX_DRAW_BUFFERS. This have to be called before
     * link the shaders (done on the first {@link #bind()} call), or no effect will have.
     * @param attrib The name of the user-defined varying out variable whose binding to modify
     * @param colorNumber The color number to bind the user-defined varying out variable to
     */
    public void setColorOutput(String attrib, int colorNumber) {
        gl.bindFragDataLocation(shaderProgram, colorNumber, attrib);
    }

    /**
     * Enable all attribs passed to this method. <b>This method</b> binds for
     * you the shader, but also unbinds it ({@code glUseProgram}).
     * @param vao Vertex Array Object 
     * @param attribss Variable list for Strings, or an array of Strings
     * @see <a href="http://stackoverflow.com/questions/13403807/glvertexattribpointer-raising-gl-invalid-operation">
     *      glVertexAttribPointer raising GL_INVALID_OPERATION</a>
     */
    public void enableAttribs(VAO vao, String... attribss) {
        vao.bind();
        bind();
        for(String attribS : attribss) {
            enableAttrib(attribS);
        }
        fetchAttribs();
        vao.unbind();
    }

    /**
     * Enable one attribs. This method requires to bind a VAO to work.
     * @param attrib An attribute name
     * @see <a href="http://stackoverflow.com/questions/13403807/glvertexattribpointer-raising-gl-invalid-operation">
     *      glVertexAttribPointer raising GL_INVALID_OPERATION</a>
     */
    public void enableAttrib(String attrib) {
        bind();
        int loc = gl.getAttribLocation(shaderProgram, attrib);
        if(loc != -1)
            gl.enableVertexAttribArray(loc);
    }

    /**
     * Explicitly specifies the binding of the user-defined varying out variable {@code attrib}
     * to fragment shader color number {@code colorNumber} for program program.
     * {@code colorNumber} must be less than GL_MAX_DRAW_BUFFERS. This have to be called before
     * link the shaders (done on the first {@link #bind()} call), or no effect will have.
     * Shortcut for {@link #setColorOutput(java.lang.String, int)}.
     * @param attrib The name of the user-defined varying out variable whose binding to modify
     * @param colorNumber The color number to bind the user-defined varying out variable to
     */
    public void bindFragDataLocation(String attrib, int colorNumber) {
        setColorOutput(attrib, colorNumber);
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

    /**
     * Sets the value for a int or a sampler type uniform
     * @param name Name of the uniform
     * @param v0 Value to be set
     */
    public void setUniform(String name, int v0) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform1i(uniforms.get(name), v0);
    }

    /**
     * Sets the value for a ivec2 uniform
     * @param name Name of the uniform
     * @param v0 1st Value to be set
     * @param v1 2nd Value to be set
     */
    public void setUniform(String name, int v0, int v1) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform2i(uniforms.get(name), v0, v1);
    }

    /**
     * Sets the value for a ivec3 uniform
     * @param name Name of the uniform
     * @param v0 1st Value to be set
     * @param v1 2nd Value to be set
     * @param v2 3rd Value to be set
     */
    public void setUniform(String name, int v0, int v1, int v2) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform3i(uniforms.get(name), v0, v1, v2);
    }

    /**
     * Sets the value for a ivec4 uniform
     * @param name Name of the uniform
     * @param v0 1st value to be set
     * @param v1 2nd value to be set
     * @param v2 3rd value to be set
     * @param v3 4th value to be set
     */
    public void setUniform(String name, int v0, int v1, int v2, int v3) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform4i(uniforms.get(name), v0, v1, v2, v3);
    }

    /**
     * Sets the value for a float uniform
     * @param name Name of the uniform
     * @param v0 Value to be set
     */
    public void setUniform(String name, float v0) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform1f(uniforms.get(name), v0);
    }

    /**
     * Sets the value for a vec2 uniform
     * @param name Name of the uniform
     * @param v0 1st value to be set
     * @param v1 2nd value to be set
     */
    public void setUniform(String name, float v0, float v1) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform2f(uniforms.get(name), v0, v1);
    }

    /**
     * Sets the value for a vec3 uniform
     * @param name Name of the uniform
     * @param v0 1st value to be set
     * @param v1 2nd value to be set
     * @param v2 3rd value to be set
     */
    public void setUniform(String name, float v0, float v1, float v2) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform3f(uniforms.get(name), v0, v1, v2);
    }

    /**
     * Sets the value for a vec4 uniform
     * @param name Name of the uniform
     * @param v0 1st value to be set
     * @param v1 2nd value to be set
     * @param v2 3rd value to be set
     * @param v3 4th value to be set
     */
    public void setUniform(String name, float v0, float v1, float v2, float v3) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniform4f(uniforms.get(name), v0, v1, v2, v3);
    }

    //TODO hacer para mat2 y mat3
    /**
     * Sets the value for a mat4 uniform
     * @param name Name of the uniform
     * @param matrix Matrix with the values to be set
     */
    public void setUniformMatrix(String name, mat4 matrix) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniformMatrix4(uniforms.get(name), false, GLM.matrixAsArray(matrix));
    }

    /**
     * Returns the Uniform location from the shader
     * @param name Uniform name
     * @return its location
     */
    public int getUniformLocation(String name) {
        return uniforms.get(name);
    }

    /**
     * Returns the Attribute location from the shader
     * @param name Attribute name
     * @return its location
     */
    public int getAttributeLocation(String name) {
        return gl.getAttribLocation(shaderProgram, name);
    }

    public boolean equals(Object o) {
        return o instanceof ShaderProgram && ((ShaderProgram) o).shaderProgram == shaderProgram;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
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
     * @param shader shader to check
     * @throws GLError If COMPILE_STATUS is not GL_TRUE (an error occurred
     *                 while compiling)
     */
    protected final void shaderCheckForErrors(Renderer.ShaderType type, int shader) {
        int status = gl.getShader(shader, Renderer.GLGetShader.COMPILE_STATUS);
        if(status != 1)
            throw new GLError(type.name(), gl.getShaderInfoLog(shader));
    }

    /**
     * Check for errors. If there is an error, then will throw an GLError
     * @throws GLError If LINK_STATUS is not GL_TRUE (an error occurred
     *                 while linking)
     */
    protected final void programCheckForErrors() {
        int status = gl.getProgram(shaderProgram, Renderer.GLGetProgram.LINK_STATUS);
        if(status != 1) throw new GLError(gl.getProgramInfoLog(shaderProgram));
    }

    /**
     * Create a program, attaching all shaders
     * @return the program
     */
    protected final int createProgram() {
        return gl.createProgram();
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
        uniforms = new HashMap<>(count);
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
