package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;

/**
 * Class for manage Renderbuffers
 * <p>
 *     Render Buffer Object is an OpenGL Object that stores images on it.
 *     Is like a {@link Texture} but is optimized to be used with
 *     {@link FrameBuffer}s.
 * </p>
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
        gl.addErasable(this);
    }

    public void delete() {
        if(rbo == -1) return;
        gl.deleteRenderbuffer(rbo);
        rbo = -1;
    }

    int _get_rbo_() { return rbo; }
}
