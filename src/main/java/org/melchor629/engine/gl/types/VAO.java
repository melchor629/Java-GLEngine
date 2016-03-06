package org.melchor629.engine.gl.types;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.GLError;

/**
 * Vertex Array Object class
 * @author melchor9000
 */
public class VAO implements Erasable {
    protected int vao = -1;

    /**
     * Generates a Vertex Array Buffer and then it's binded
     */
    public VAO() {
        vao = Game.gl.genVertexArray();
        Game.erasableList.add(this);
    }

    /**
     * Binds this VAO
     * @throws GLError If VAO was deleted
     */
    public void bind()  {
        if(vao != -1)
            Game.gl.bindVertexArray(vao);
        else
            throw new GLError("This Vertex Array Object was deleted before");
    }

    /**
     * Unbinds this VAO. Shortcut for {@code glBindVertexArray(0)}
     */
    public void unbind() {
        Game.gl.bindVertexArray(0);
    }

    /**
     * Delete this VAO.
     */
    public void delete() {
        if(vao == -1) return;
        Game.gl.deleteVertexArray(vao);
        vao = -1;
    }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        delete();
    }
}
