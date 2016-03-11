package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;

/**
 * Vertex Array Object class
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
        Game.erasableList.add(this);
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
