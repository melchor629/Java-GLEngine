package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;

/**
 * Class for create and manage Framebuffers
 * <p>
 *     A buffer that is used as target of a result of some render. By default
 *     one FrameBuffer is used in the render pipeline, the default one. This
 *     class can create more FrameBuffers to make Post-Processing or another
 *     kind of image processing over renders. FrameBuffers references
 *     {@link Texture}s or {@link RenderBuffer}s where the output of the render
 *     is stored.
 * </p>
 * <p>
 *     A FrameBuffer must have a <i>Color Attachment to work</i>. This attachments
 *     are the {@link Texture}s and {@link RenderBuffer}s referenced before,
 *     and FrameBuffer is able to attach one Depth attachment, one Stencil
 *     attachment and several Color attachments.
 * </p>
 * @author melchor9000
 */
public class FrameBuffer implements Erasable {
    private int fb = -1, lastFb;
    private boolean checked;
    private final GLContext gl;
    private RenderBuffer depthRB, stencilRB;
    private Texture depthTex, stencilTex;
    private Texture[] colorTexs;
    private RenderBuffer[] colorRBs;

    /**
     * Generates a framebuffer. To make this framebuffer working,
     * first you have to attach something and at least 1 Color texture or
     * renderbuffer (OpenGL 4.1 or fewer).
     */
    FrameBuffer(GLContext gl) {
        this.gl = gl;
        fb = gl.genFramebuffer();
        gl.addErasable(this);
        colorTexs = new Texture[gl.getInt(GLContext.GLGet.MAX_COLOR_ATTACHMENTS)];
        colorRBs = new RenderBuffer[gl.getInt(GLContext.GLGet.MAX_COLOR_ATTACHMENTS)];
    }

    /**
     * Attach a color texture passing an empty texture and a number
     * from 0 to {@code GLContext.getInt(GLGet.MAX_COLOR_ATTACHMENTS) - 1}.
     * <b>FrameBuffer don't clean textures!!</b> Also use a texture
     * format valid for a color textures, they are RGB, RGBA, and all
     * similar stuff.
     * @param texture the empty texture
     * @param num number of the color attachment
     * @throws GLError If {@code num} is greater than your GPU Max
     *                 color attachments
     */
    public void attachColorTexture(Texture texture, int num) {
        gl.bindFramebuffer(fb);
        int max = gl.getInt(GLContext.GLGet.MAX_COLOR_ATTACHMENTS);
        if(num > max)
            throw new GLError(String.format("Trying to attach a color attachment nº %d, where max is %d", num, max));
        if(texture.is1D()) {
            gl.framebufferTexture1D(coloratt(num), texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(coloratt(num), texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(coloratt(num), texture.getTarget(), texture._get_texture_(), 0, num >> 8);
        }
        colorTexs[num] = texture;
    }

    /**
     * Attach a color renderbuffer passing a renderbuffer and a number
     * from 0 to {@code GLContext.getInt(GLGet.MAX_COLOR_ATTACHMENTS) - 1}.
     * <b>FrameBuffer don't clean renderbuffers!!</b> Also, renderbuffer must
     * have a valid format for color, like RGB, RGBA, and all similar stuff.
     * @param rbo a renderbuffer
     * @param num number of the color attachment
     * @throws GLError If {@code num} is greater than your GPU Max
     *                 color attachments
     */
    public void attachColorRenderbuffer(RenderBuffer rbo, int num) {
        gl.bindFramebuffer(fb);
        int max = gl.getInt(GLContext.GLGet.MAX_COLOR_ATTACHMENTS);
        if(num > max)
            throw new GLError(String.format("Trying to attach a color attachment nº %d, where max is %d", num, max));
        gl.framebufferRenderbuffer(coloratt(num), rbo._get_rbo_());
        colorRBs[num] = rbo;
    }

    /**
     * Attach a depth texture passing an empty texture.
     * <b>FrameBuffer don't clean textures!!</b> Also use a texture
     * format valid for a depth textures, they are DEPTH_COMPONENT, and all
     * similar stuff. 3D textures uses Z value = 1;
     * @param texture the empty texture
     */
    public void attachDepthTexture(Texture texture) {
        gl.bindFramebuffer(fb);
        if(texture.is1D()) {
            gl.framebufferTexture1D(GLContext.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(GLContext.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(GLContext.FramebufferAttachment.DEPTH_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0, 1);
        }
        depthTex = texture;
    }

    /**
     * Attach a depth renderbuffer passing a renderbuffer. <b>FrameBuffer
     * don't clean renderbuffers!!</b> Also, renderbuffer must have a valid
     * format of depth, like DEPTH_COMPONENT and the seamless others ending
     * by a number.
     * @param rbo A renderbuffer
     */
    public void attachDepthRenderbuffer(RenderBuffer rbo) {
        gl.bindFramebuffer(fb);
        gl.framebufferRenderbuffer(GLContext.FramebufferAttachment.DEPTH_ATTACHMENT, rbo._get_rbo_());
        depthRB = rbo;
    }

    /**
     * Attach a stencil texture passing an empty texture.
     * <b>FrameBuffer don't clean textures!!</b> Also use a texture
     * format valid for a stencil textures, they are STENCIL_INDEX,
     * and all similar stuff. 3D textures uses Z value = 1;
     * @param texture the empty texture
     */
    public void attachStencilTexture(Texture texture) {
        gl.bindFramebuffer(fb);
        if(texture.is1D()) {
            gl.framebufferTexture1D(GLContext.FramebufferAttachment.STENCIL_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(GLContext.FramebufferAttachment.STENCIL_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(GLContext.FramebufferAttachment.STENCIL_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0, 1);
        }
        stencilTex = texture;
    }

    /**
     * Attach a stencil renderbuffer passing a renderbuffer. <b>FrameBuffer
     * don't clean renderbuffers!!</b> Also, renderbuffer must have a valid
     * format of stencil, like STENCIL_INDEX and the seamless others ending
     * by a number.
     * @param rbo A renderbuffer
     */
    public void attachStencilRenderbuffer(RenderBuffer rbo) {
        gl.bindFramebuffer(fb);
        gl.framebufferRenderbuffer(GLContext.FramebufferAttachment.STENCIL_ATTACHMENT, rbo._get_rbo_());
        stencilRB = rbo;
    }


    /**
     * Attach a depth-stencil texture passing an empty texture.
     * <b>FrameBuffer don't clean textures!!</b> Also use a texture
     * format {@code DEPTH_STENCIL}. 3D textures uses Z value = 1;
     * @param texture the empty texture
     */
    public void attachDepthStencilTexture(Texture texture) {
        gl.bindFramebuffer(fb);
        if(texture.is1D()) {
            gl.framebufferTexture1D(GLContext.FramebufferAttachment.DEPTH_STENCIL_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is2D()) {
            gl.framebufferTexture2D(GLContext.FramebufferAttachment.DEPTH_STENCIL_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0);
        } else if(texture.is3D()) {
            gl.framebufferTexture3D(GLContext.FramebufferAttachment.DEPTH_STENCIL_ATTACHMENT, texture.getTarget(), texture._get_texture_(), 0, 1);
        }
        depthTex = stencilTex = texture;
    }

    /**
     * Attach a depth-stencil renderbuffer passing a renderbuffer. <b>FrameBuffer
     * don't clean renderbuffers!!</b> Also, renderbuffer must have format
     * {@code DEPTH_STENCIL}.
     * @param rbo A renderbuffer
     */
    public void attachDepthStencilRenderbuffer(RenderBuffer rbo) {
        gl.bindFramebuffer(fb);
        gl.framebufferRenderbuffer(GLContext.FramebufferAttachment.DEPTH_STENCIL_ATTACHMENT, rbo._get_rbo_());
        depthRB = stencilRB = rbo;
    }

    /**
     * Binds this FrameBuffer. Also, this checks for errors, if was not
     * checked before, so can throw an error if there is one.
     * @throws GLError If the FBO was deleted or incomplete
     */
    public void bind() {
        if(fb == -1) throw new GLError("glBindFramebuffer", "Cannot bind a deleted framebuffer");
        lastFb = gl.getInt(GLContext.GLGet.DRAW_FRAMEBUFFER_BINDING);
        gl.bindFramebuffer(fb);
        checkForErrors();
    }

    /**
     * Binds this FrameBuffer as a read Buffer.
     * @throws GLError If the FBO was deleted or incomplete
     */
    public void bindForRead() {
        if(fb == -1) throw new GLError("glBindFramebuffer", "Cannot bind a deleted framebuffer");
        lastFb = gl.getInt(GLContext.GLGet.READ_FRAMEBUFFER_BINDING);
        gl.bindFramebufferRead(fb);
    }

    /**
     * Binds this FrameBuffer as a draw/write Buffer.
     * @throws GLError If the FBO was deleted or incomplete
     */
    public void bindForDraw() {
        if(fb == -1) throw new GLError("glBindFramebuffer", "Cannot bind a deleted framebuffer");
        lastFb = gl.getInt(GLContext.GLGet.DRAW_FRAMEBUFFER_BINDING);
        gl.bindFramebufferWrite(fb);
    }

    /**
     * Unbinds this framebuffer
     */
    public void unbind() {
        gl.bindFramebuffer(lastFb);
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

    public Texture getColorTexAttachment(int num) {
        return colorTexs[num];
    }

    public Texture getDepthTexAttachment() {
        return depthTex;
    }

    public Texture getStencilTexAttachment() {
        return stencilTex;
    }

    public RenderBuffer getColorRBAttachment(int num) {
        return colorRBs[num];
    }

    public RenderBuffer getDepthRBAttachment() {
        return depthRB;
    }

    public RenderBuffer getStencilRBAttachment() {
        return stencilRB;
    }

    /**
     * Transform the number to a {@link GLContext.FramebufferAttachment}
     * @param num Number from 0 to 32(?)
     * @return the equivalent to this enum
     */
    private GLContext.FramebufferAttachment coloratt(int num) {
        switch(num) {
            case 1: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT1;
            case 2: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT2;
            case 3: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT3;
            case 4: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT4;
            case 5: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT5;
            case 6: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT6;
            case 7: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT7;
            case 8: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT8;
            default: return GLContext.FramebufferAttachment.COLOR_ATTACHMENT;
        }
    }

    /**
     * Check for errors in this FrameBuffer
     * @see <a href="https://www.opengl.org/sdk/docs/man3/xhtml/glCheckFramebufferStatus.xml">glCheckFramebufferStatus</a>
     * @throws GLError if framebuffer status is not equal to {@code GL_FRAMEBUFFER_COMPLETE}
     */
    private void checkForErrors() {
        if(checked) return;
        GLContext.FramebufferStatus status = gl.checkFramebufferStatus();
        if(status != GLContext.FramebufferStatus.COMPLETE)
            throw new GLError("glCheckFramebufferStatus", "FrameBuffer status is not good: " + status.toString());
        checked = true;
    }
}
