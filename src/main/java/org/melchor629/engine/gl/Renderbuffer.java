package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;

/**
 * Class for manage Renderbuffers
 * @author melchor9000
 */
public class RenderBuffer implements Erasable {
    private int rbo;
    private GLContext.TextureFormat format;
    private final GLContext gl;

    RenderBuffer(GLContext gl, GLContext.TextureFormat fmt, int width, int height) {
        this.gl = gl;
        rbo = gl.genRenderbuffer();
        format = fmt;
        gl.bindRenderbuffer(rbo);
        gl.renderbufferStorage(fmt, width, height);
        gl.bindRenderbuffer(0);
        Game.erasableList.add(this);
    }

    public void delete() {
        if(rbo == -1) return;
        gl.deleteRenderbuffer(rbo);
        rbo = -1;
    }

    int _get_rbo_() { return rbo; }
}
