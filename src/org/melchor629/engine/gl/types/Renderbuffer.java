package org.melchor629.engine.gl.types;

import static org.melchor629.engine.Game.gl;

import org.melchor629.engine.gl.Renderer;

/**
 * Class for manage Renderbuffers
 * @author melchor9000
 */
public class Renderbuffer {
    protected int rbo;
    protected Renderer.TextureFormat format;

    public Renderbuffer(Renderer.TextureFormat fmt, int width, int height) {
        rbo = gl.genRenderbuffer();
        format = fmt;
        gl.bindRenderbuffer(rbo);
        gl.renderbufferStorage(fmt, width, height);
    }

    public void delete() {
        if(rbo == -1) return;
        gl.deleteRenderbuffer(rbo);
        rbo = -1;
    }

    @Override
    protected void finalize() {
        delete();
    }

    int _get_rbo_() { return rbo; };
}
