package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;

/**
 * Vertex Array Object class.
 * <p>
 *     A Vertex Array Object stores all of the state needed to supply vertex
 *     data. Stores the format of the vertex data as well as the {@link BufferObject}
 *     providing the vertex data arrays. VAO only stores references to this
 *     {@link BufferObject}s. Any change to one of theese, will be seen on this
 *     VAO.
 * </p>
 * <p>
 *     An VAO has to enable the vertex attributes to be able to be used in
 *     the shaders.
 * </p>
 * @author melchor9000
 */
public class VertexArrayObject implements Erasable {
    private int vao = -1;
    private final GLContext gl;

    /**
     * Generates a Vertex Array Buffer and then it's binded
     */
    VertexArrayObject(GLContext gl) {
        this.gl = gl;
        vao = gl.genVertexArray();
        gl.addErasable(this);
    }

    /**
     * Binds this VAO
     * @throws GLError If VAO was deleted
     */
    public void bind()  {
        if(vao != -1)
            gl.bindVertexArray(vao);
        else
            throw new GLError("This Vertex Array Object was deleted before");
    }

    /**
     * Unbinds this VAO. Shortcut for {@code glBindVertexArray(0)}
     */
    public void unbind() {
        gl.bindVertexArray(0);
    }

    /**
     * Delete this VAO.
     */
    public void delete() {
        if(vao == -1) return;
        gl.deleteVertexArray(vao);
        vao = -1;
    }
}
