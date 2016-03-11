package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.Matrix2;
import org.melchor629.engine.utils.math.Matrix3;
import org.melchor629.engine.utils.math.Matrix4;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

/**
 * Class for create shaders and shaders programs, manage them and
 * make games be better. Shaders can be created from files or passing
 * them as Strings.
 * <p>
 *     A ShaderProgram is a set of Shaders that works together to run in a
 *     part of a rendering. The Shaders can be:
 *     <ul>
 *         <li>Vertex Shader</li>
 *         <li>Fragment Shader</li>
 *         <li>Geometry Shader</li>
 *         <li>Tessellation Shader (<i>4.0 or ARB_tessellation_shader</i>)</li>
 *         <li>Evaluation Shader (<i>4.0 or ARB_tessellation_shader</i>)</li>
 *         <li>Compute Shader (<i>4.3 or ARB_compute_shader</i>)</li>
 *     </ul>
 * </p>
 * <p>
 *     A Shader is a piece of code that runs on a stage of the rendering process.
 *     Vertex Shaders
 *
 * </p>
 * @author melchor9000
 */
public class ShaderProgram implements Erasable {
    private int vertexShader, fragmentShader, geometryShader, shaderProgram;
    private Map<String, Integer> uniforms;
    private List<Attrib> attribs;
    private boolean linked = false, binded = false;
    private GLContext gl;

    private static ShaderProgram currentBindedShader;

    /**
     * Create a Shader program from files, and checks for errors
     * @param vertex Vertex shader file
     * @param fragment Fragment shader file
     * @throws FileNotFoundException If the file doesn't exist or is a directory
     * @throws IOException If an error occurred while reading files
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    ShaderProgram(GLContext gl, File vertex, File fragment) throws IOException {
        this(gl, IOUtils.readFile(vertex), IOUtils.readFile(fragment));
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
    ShaderProgram(GLContext gl, File vertex, File fragment, File geometry) throws IOException {
        this(gl, IOUtils.readFile(vertex), IOUtils.readFile(fragment), IOUtils.readFile(geometry));
    }

    /**
     * Create a Shader program from string, and checks for errors
     * @param vertex Vertex shader source code
     * @param fragment Fragment shader source code
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    ShaderProgram(GLContext gl, String vertex, String fragment) {
        this(gl, vertex, fragment, null);
    }

    /**
     * Create a Shader program from strings, checking for errors for every shader,
     * linking them to a program and checking for linking errors.
     * @param vertex Vertex shader source code
     * @param fragment Fragment shader source code
     * @param geometry Geometry shader source code, can be null
     * @throws GLError If an error was found while compiling shaders or linking program
     */
    ShaderProgram(GLContext gl, String vertex, String fragment, String geometry) {
        this.gl = gl;
        vertexShader = fragmentShader = geometryShader = shaderProgram = -1;
        
        vertexShader = createShader(GLContext.ShaderType.VERTEX, vertex);
        fragmentShader = createShader(GLContext.ShaderType.FRAGMENT, fragment);
        if(geometry != null) {
            geometryShader = createShader(GLContext.ShaderType.GEOMETRY, geometry);
            shaderCheckForErrors(GLContext.ShaderType.GEOMETRY, geometryShader);
        }
        shaderCheckForErrors(GLContext.ShaderType.VERTEX, vertexShader);
        shaderCheckForErrors(GLContext.ShaderType.FRAGMENT, fragmentShader);

        shaderProgram = createProgram();

        gl.addErasable(this);
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
    public void enableAttribs(VertexArrayObject vao, String... attribss) {
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
        int loc = attribs.indexOf(new Attrib(attrib));
        if(loc != -1) {
            loc = attribs.get(loc).location;
            gl.enableVertexAttribArray(loc);
        } else {
            throw new GLError("Attribute named " + attrib + " not found");
        }
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
    public void vertexAttribPointer(String attribName, int type, GLContext.type t, boolean norm, int stride, long offset) {
        int loc = attribs.indexOf(new Attrib(attribName));
        if(loc == -1) throw new GLError("glVertexAttribLocation", "Attrib named '" + attribName + "' does not exist");
        loc = attribs.get(loc).location;
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
        else
            throw new GLError("Uniform named " + name + " not found");
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
        else
            throw new GLError("Uniform named " + name + " not found");
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
        else
            throw new GLError("Uniform named " + name + " not found");
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
        else
            throw new GLError("Uniform named " + name + " not found");
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
        else
            throw new GLError("Uniform named " + name + " not found");
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
        else
            throw new GLError("Uniform named " + name + " not found");
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
        else
            throw new GLError("Uniform named " + name + " not found");
    }

    /**
     * Sets the value for a Vector4 uniform
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
        else
            throw new GLError("Uniform named " + name + " not found");
    }

    /**
     * Sets the value for a mat4 uniform
     * @param name Name of the uniform
     * @param matrix Matrix with the values to be set
     */
    public void setUniformMatrix(String name, Matrix4 matrix) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniformMatrix4(uniforms.get(name), false, GLM.matrixAsArray(matrix));
        else
            throw new GLError("Uniform named " + name + " not found");
    }

    /**
     * Sets the value for a mat3 uniform
     * @param name Name of the uniform
     * @param matrix Matrix with the values to be set
     */
    public void setUniformMatrix(String name, Matrix3 matrix) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniformMatrix4(uniforms.get(name), false, GLM.matrixAsArray(matrix));
        else
            throw new GLError("Uniform named " + name + " not found");
    }

    /**
     * Sets the value for a mat2 uniform
     * @param name Name of the uniform
     * @param matrix Matrix with the values to be set
     */
    public void setUniformMatrix(String name, Matrix2 matrix) {
        bind();
        if(uniforms.containsKey(name))
            gl.uniformMatrix4(uniforms.get(name), false, GLM.matrixAsArray(matrix));
        else
            throw new GLError("Uniform named " + name + " not found");
    }

    /**
     * Returns the Uniform location from the shader
     * @param name Uniform name
     * @return its location
     * @throws GLError if the uniform doesn't exist
     */
    public int getUniformLocation(String name) throws GLError {
        if(uniforms.containsKey(name))
            return uniforms.get(name);
        throw new GLError("Uniform named " + name + " not found");
    }

    /**
     * Returns the Attribute location from the shader
     * @param name Attribute name
     * @return its location
     * @throws GLError if the attribute doesn't exist
     */
    public int getAttributeLocation(String name) throws GLError {
        int i = attribs.indexOf(new Attrib(name));
        if(i != -1) return attribs.get(i).location;
        throw new GLError("Attribute named " + name + " not found");
    }

    public boolean equals(Object o) {
        return o instanceof ShaderProgram && ((ShaderProgram) o).shaderProgram == shaderProgram;
    }

    @Override
    public int hashCode() {
        return shaderProgram;
    }

    /**
     * Create a shader of a type and with a source code and then return the shader
     * @param type Type of the shader
     * @param str Source code
     * @return Shader
     */
    protected final int createShader(GLContext.ShaderType type, String str) {
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
    protected final void shaderCheckForErrors(GLContext.ShaderType type, int shader) {
        int status = gl.getShader(shader, GLContext.GLGetShader.COMPILE_STATUS);
        if(status != 1)
            throw new GLError(type.name(), gl.getShaderInfoLog(shader));
    }

    /**
     * Check for errors. If there is an error, then will throw an GLError
     * @throws GLError If LINK_STATUS is not GL_TRUE (an error occurred
     *                 while linking)
     */
    protected final void programCheckForErrors() {
        int status = gl.getProgram(shaderProgram, GLContext.GLGetProgram.LINK_STATUS);
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
        int count = gl.getProgram(shaderProgram, GLContext.GLGetProgram.ACTIVE_UNIFORMS);
        uniforms = new HashMap<>(count);
        int strlen = gl.getProgram(shaderProgram, GLContext.GLGetProgram.ACTIVE_UNIFORM_MAX_LENGTH);
        for(int i = 0; i < count; i++) {
            String name = gl.getActiveUniform(shaderProgram, i, strlen);
            int loc = gl.getUniformLocation(shaderProgram, name);
            uniforms.put(name, loc);
        }
    }

    private void fetchAttribs() {
        int count = gl.getProgram(shaderProgram, GLContext.GLGetProgram.ACTIVE_ATTRIBUTES);
        int strlen = gl.getProgram(shaderProgram, GLContext.GLGetProgram.ACTIVE_ATTRIBUTE_MAX_LENGTH);
        attribs = new ArrayList<>(count);
        for(int i = 0; i < count; i++) {
            attribs.add(new Attrib(i, strlen));
        }
    }

    /**
     * Struct for store info about an attrib
     * @author melchor9000
     */
    protected class Attrib {
        String name = null;
        int type = -1;
        int size = 0;
        int location = -1;

        private Attrib(int i, int strlen) {
            name = gl.getActiveAttrib(shaderProgram, i, strlen);
            size = gl.getActiveAttribSize(shaderProgram, i);
            type = gl.getActiveAttribType(shaderProgram, i);
            location = gl.getAttribLocation(shaderProgram, name);
        }

        private Attrib(String n) {
            this.name = n;
        }

        public boolean equals(Object o) {
            return o instanceof Attrib && name.equals(((Attrib) o).name);
        }
    }
}
