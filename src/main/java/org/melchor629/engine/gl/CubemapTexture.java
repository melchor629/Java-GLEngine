package org.melchor629.engine.gl;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.utils.ImageIO;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Create cubemap textures and manage them.
 */
public class CubemapTexture implements Erasable {
    private final GLContext gl;
    private int tex, bindedTexture;

    /**
     * Create a simple empty cubemap texture, with minifier and magnifier filter
     * set as {@code LINEAR} and the parameters given.
     *
     * @param gl
     * @param format  Internal format of the texture
     * @param width   Width of the texture
     * @param height  Height of the texture
     * @param eformat External format of the texture (should be the same
     *                or similar from {@code format} parameter.
     */
    CubemapTexture(GLContext gl, GLContext.TextureFormat format, int width, int height, GLContext.TextureExternalFormat eformat) {
        this.gl = gl;

        tex = gl.genTexture();
        bind();
        gl.texImage2D(GLContext.TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);
        gl.texImage2D(GLContext.TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_X, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);
        gl.texImage2D(GLContext.TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_Y, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);
        gl.texImage2D(GLContext.TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);
        gl.texImage2D(GLContext.TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_Z, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);
        gl.texImage2D(GLContext.TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.MAG_FILTER, GLContext.TextureFilter.LINEAR);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.MIN_FILTER, GLContext.TextureFilter.LINEAR);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.WRAP_S, GLContext.TextureWrap.CLAMP_TO_EDGE);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.WRAP_T, GLContext.TextureWrap.CLAMP_TO_EDGE);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.WRAP_R, GLContext.TextureWrap.CLAMP_TO_EDGE);
        unbind();

        gl.addErasable(this);
    }

    CubemapTexture(GLContext gl, GLContext.TextureFormat format, File path, String ext) throws FileNotFoundException {
        final String[] things = { "right.", "left.", "top.", "bottom.", "back.", "front." };
        this.gl = gl;
        tex = gl.genTexture();
        bind();

        for(String thing : things) {
            ImageIO.ImageData img = ImageIO.loadImage(new File(path, thing + ext));
            if(img == null) {
                throw new FileNotFoundException("Could not find " + path.getAbsolutePath() + "/" + thing + ext);
            } else {
                GLContext.TextureExternalFormat eformat = GLContext.TextureExternalFormat.RGBA;
                if(img.components == 3) eformat = GLContext.TextureExternalFormat.RGB;
                gl.texImage2D(eso(thing), 0, format, img.width, img.height, 0, eformat, GLContext.type.UNSIGNED_BYTE, img.data);
                img.delete();
            }
        }

        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.MAG_FILTER, GLContext.TextureFilter.LINEAR);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.MIN_FILTER, GLContext.TextureFilter.LINEAR);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.WRAP_S, GLContext.TextureWrap.CLAMP_TO_EDGE);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.WRAP_T, GLContext.TextureWrap.CLAMP_TO_EDGE);
        gl.texParameteri(GLContext.TextureTarget.TEXTURE_CUBE_MAP, GLContext.TextureParameter.WRAP_R, GLContext.TextureWrap.CLAMP_TO_EDGE);
        unbind();

        gl.addErasable(this);
    }

    public void bind() {
        if(tex == -1) throw new GLError("glBind", "Cannot bind a deleted texture");
        bindedTexture = gl.getInt(GLContext.GLGet.TEXTURE_BINDING_CUBE_MAP);
        gl.bindTexture(GLContext.TextureTarget.TEXTURE_CUBE_MAP, tex);
    }

    public void unbind() {
        gl.bindTexture(GLContext.TextureTarget.TEXTURE_CUBE_MAP, bindedTexture);
    }

    @Override
    public void delete() {
        if(tex != -1) {
            gl.deleteTexture(tex);
            tex = -1;
        }
    }

    private GLContext.TextureTarget eso(String i) {
        switch(i) {
            case "right.":  return GLContext.TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_X;
            case "left.":   return GLContext.TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_X;
            case "top.":    return GLContext.TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_Y;
            case "bottom.": return GLContext.TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_Y;
            case "back.":   return GLContext.TextureTarget.TEXTURE_CUBE_MAP_POSITIVE_Z;
            case "front.":  return GLContext.TextureTarget.TEXTURE_CUBE_MAP_NEGATIVE_Z;
            default: return null;
        }
    }
}
