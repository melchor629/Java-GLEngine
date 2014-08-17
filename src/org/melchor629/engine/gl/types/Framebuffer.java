package org.melchor629.engine.gl.types;

import static org.melchor629.engine.Game.gl;

import org.melchor629.engine.gl.GLError;
import org.melchor629.engine.gl.Renderer;

/**
 * Class for create and manage Framebuffers
 * @author melchor9000
 */
public class Framebuffer {
    protected int fb = -1;
    protected boolean checked;

    /**
     * Generates a framebuffer. To make this framebuffer working,
     * first you have to attach something and at least 1 Color texture or
     * renderbuffer (OpenGL 4.1 or fewer).
     */
    public Framebuffer() {
        fb = gl.genFramebuffer();
    }

    /**
     * Attach a color texture passing an empty texture and a number
     * from 0 to {@code Renderer.getInt(GLGet.MAX_COLOR_ATTACHMENTS) - 1}.
     * <b>Framebuffer don't clean textures!!</b> Also use a texture
     * format valid for a color textures, they are RGB, RGBA, and all
     * similar stuff.
     * @param texture the empty texture
     * @param num number of the color attachment
     * @throws GLError If {@code num} is greater than your GPU Max
     *                 color attachments
     */
    public void attachColorTexture(Texture texture, int num) {
        gl.bindFramebuffer(fb);
        int max = gl.getInt(Renderer.GLGet.MAX_COLOR_ATTACHMENTS);
        if(num > max)
            throw new GLError(String.format("Trying to attach a color attachment nº %d, where max is %d", num, max));
        if(texture.is1D()) {
            gl.framebufferTexture1D(coloratt(num), texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(coloratt(num), texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(coloratt(num), texture.getTarget(), texture._get_texture_(), 0, (int) (num >> 8));
        }
    }

    /**
     * Attach a color renderbuffer passing a renderbuffer and a number
     * from 0 to {@code Renderer.getInt(GLGet.MAX_COLOR_ATTACHMENTS) - 1}.
     * <b>Framebuffer don't clean renderbuffers!!</b> Also, renderbuffer must
     * have a valid format for color, like RGB, RGBA, and all similar stuff.
     * @param rbo a renderbuffer
     * @param num number of the color attachment
     * @throws GLError If {@code num} is greater than your GPU Max
     *                 color attachments
     */
    public void attachColorRenderbuffer(Renderbuffer rbo, int num) {
        gl.bindFramebuffer(fb);
        int max = gl.getInt(Renderer.GLGet.MAX_COLOR_ATTACHMENTS);
        if(num > max)
            throw new GLError(String.format("Trying to attach a color attachment nº %d, where max is %d", num, max));
        gl.framebufferRenderbuffer(coloratt(num), rbo._get_rbo_());
    }

    /**
     * Attach a depth texture passing an empty texture.
     * <b>Framebuffer don't clean textures!!</b> Also use a texture
     * format valid for a depth textures, they are DEPTH_COMPONENT, and all
     * similar stuff. 3D textures uses Z value = 1;
     * @param texture the empty texture
     */
    public void attachDepthTexture(Texture texture) {
        gl.bindFramebuffer(fb);
        if(texture.is1D()) {
            gl.framebufferTexture1D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0, 1);
        }
    }

    /**
     * Attach a depth renderbuffer passing a renderbuffer. <b>Framebuffer
     * don't clean renderbuffers!!</b> Also, renderbuffer must have a valid
     * format of depth, like DEPTH_COMPONENT and the seamless others ending
     * by a number.
     * @param rbo A renderbuffer
     */
    public void attachDepthRenderbuffer(Renderbuffer rbo) {
        gl.bindFramebuffer(fb);
        gl.framebufferRenderbuffer(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, rbo._get_rbo_());
    }

    /**
     * Attach a stencil texture passing an empty texture.
     * <b>Framebuffer don't clean textures!!</b> Also use a texture
     * format valid for a stencil textures, they are STENCIL_INDEX,
     * and all similar stuff. 3D textures uses Z value = 1;
     * @param texture the empty texture
     */
    public void attachStencilTexture(Texture texture) {
        gl.bindFramebuffer(fb);
        if(texture.is1D()) {
            gl.framebufferTexture1D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0, 1);
        }
    }

    /**
     * Attach a stencil renderbuffer passing a renderbuffer. <b>Framebuffer
     * don't clean renderbuffers!!</b> Also, renderbuffer must have a valid
     * format of stencil, like STENCIL_INDEX and the seamless others ending
     * by a number.
     * @param rbo A renderbuffer
     */
    public void attachStencilRenderbuffer(Renderbuffer rbo) {
        gl.bindFramebuffer(fb);
        gl.framebufferRenderbuffer(Renderer.FramebufferAttachment.STENCIL_ATTACHMENT, rbo._get_rbo_());
    }


    /**
     * Attach a depth-stencil texture passing an empty texture.
     * <b>Framebuffer don't clean textures!!</b> Also use a texture
     * format {@code DEPTH_STENCIL}. 3D textures uses Z value = 1;
     * @param texture the empty texture
     */
    public void attachDepthStencilTexture(Texture texture) {
        gl.bindFramebuffer(fb);
        if(texture.is1D()) {
            gl.framebufferTexture1D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(Renderer.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0, 1);
        }
    }

    /**
     * Attach a depth-stencil renderbuffer passing a renderbuffer. <b>Framebuffer
     * don't clean renderbuffers!!</b> Also, renderbuffer must have format
     * {@code DEPTH_STENCIL}.
     * @param rbo A renderbuffer
     */
    public void attachDepthStencilRenderbuffer(Renderbuffer rbo) {
        gl.bindFramebuffer(fb);
        gl.framebufferRenderbuffer(Renderer.FramebufferAttachment.DEPTH_STENCIL_ATTACHMENT, rbo._get_rbo_());
    }

    /**
     * Binds this Framebuffer. Also, this checks for errors, if was not
     * checked before, so can throw an error if there is one.
     * @throws GLError If the FBO was deleted or incomplete
     */
    public void bind() {
        if(fb == -1) throw new GLError("glBindFramebuffer", "Cannot bind a deleted framebuffer");
        gl.bindFramebuffer(fb);
        checkForErrors();
    }

    /**
     * Unbinds this framebuffer
     */
    public void unbind() {
        gl.bindFramebuffer(0);
    }

    /**
     * Deletes this framebuffer, cannot be undone. If FBO was deleted before,
     * no exception is thrown, is safe.
     */
    public void delete() {
        if(fb == -1) return;
        gl.deleteFramebuffer(fb);
        fb = -1;
    }

    @Override
    protected void finalize() {
        delete();
    }

    /**
     * Transform the number to a {@link Renderer.FramebufferAttachment}
     * @param num Number from 0 to 32(?)
     * @return the equivalent to this enum
     */
    protected final Renderer.FramebufferAttachment coloratt(int num) {
        switch(num) {
            case 1: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT1;
            case 2: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT2;
            case 3: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT3;
            case 4: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT4;
            case 5: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT5;
            case 6: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT6;
            case 7: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT7;
            case 8: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT8;
            default: return Renderer.FramebufferAttachment.COLOR_ATTACHMENT;
        }
    }

    /**
     * Check for errors in this Framebuffer
     * @see <a href="https://www.opengl.org/sdk/docs/man3/xhtml/glCheckFramebufferStatus.xml">glCheckFramebufferStatus</a>
     * @throws GLError if framebuffer status is not equal to {@code GL_FRAMEBUFFER_COMPLETE}
     */
    protected final void checkForErrors() {
        if(checked) return;
        Renderer.FramebufferStatus status = gl.checkFramebufferStatus();
        if(status != Renderer.FramebufferStatus.COMPLETE)
            throw new GLError("glCheckFramebufferStatus", "Framebuffer status is not good: " + status.toString());
        checked = true;
    }
}
