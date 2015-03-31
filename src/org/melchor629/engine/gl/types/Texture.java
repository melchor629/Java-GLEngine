package org.melchor629.engine.gl.types;

import static org.melchor629.engine.Game.gl;

import java.io.File;
import java.io.IOException;

import org.melchor629.engine.gl.GLError;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.Renderer.TextureExternalFormat;
import org.melchor629.engine.gl.Renderer.TextureFilter;
import org.melchor629.engine.gl.Renderer.TextureFormat;
import org.melchor629.engine.gl.Renderer.TextureParameter;
import org.melchor629.engine.gl.Renderer.TextureWrap;
import org.melchor629.engine.utils.IOUtils;

/**
 * Class for manage textures. Create textures from files, or from
 * the internet, or simply empty textures for use in Framebuffers.
 * @author melchor9000
 */
public class Texture {
    protected int texture = -1;
    protected Renderer.TextureTarget target;
    protected int dimensions;

    /**
     * Create a simple empty 2D texture, with minifier and magnifier filter
     * set as {@code LINEAR} and the parameters given.
     * @param format Internal format of the texture
     * @param width Width of the texture
     * @param height Height of the texture
     * @param eformat External format of the texture (should be the same
     *                or similar from {@code format} parameter.
     */
    public Texture(Renderer.TextureFormat format, int width, int height, Renderer.TextureExternalFormat eformat) {
        texture = gl.genTexture();
        target = Renderer.TextureTarget.TEXTURE_2D;
        dimensions = 2;

        gl.bindTexture(target, texture);
        gl.texParameteri(target, Renderer.TextureParameter.MIN_FILTER, Renderer.TextureFilter.LINEAR);
        gl.texParameteri(target, Renderer.TextureParameter.MAG_FILTER, Renderer.TextureFilter.LINEAR);
        gl.texImage2D(target, 0, format, width, height, 0, eformat, Renderer.type.UNSIGNED_BYTE);
    }

    /**
     * Constructor for the Texture.Builder
     */
    Texture(File file, TextureFilter mag, TextureFilter min, TextureWrap wrap_s,
            TextureWrap wrap_t, TextureFormat ifmt, TextureExternalFormat efmt, Renderer.TextureTarget target,
            boolean mipmap, int width, int height) throws IOException {
        texture = gl.genTexture();
        this.target = target;
        
        gl.bindTexture(target, texture);
        gl.texParameteri(target, TextureParameter.MIN_FILTER, min);
        gl.texParameteri(target, TextureParameter.MAG_FILTER, mag);
        gl.texParameteri(target, TextureParameter.WRAP_S, wrap_s);
        gl.texParameteri(target, TextureParameter.WRAP_T, wrap_t);
        
        //java.nio.ByteBuffer buffer = null;
        byte[] buffer = null;
        if(file != null) {
            IOUtils.Image image = IOUtils.readImage(file);
            width = image.width;
            height = image.height;
            efmt = image.alpha ? TextureExternalFormat.RGBA : TextureExternalFormat.RGB;
            buffer = image.buffer;
        }
        gl.texImage2D(target, 0, ifmt, width, height, 0, efmt, Renderer.type.UNSIGNED_BYTE, buffer); //TODO Determinar 1D, 2D, 3D
        //TODO Mipmaps
        gl.bindTexture(target, 0);
    }

    public void bind() {
        if(texture == -1) throw new GLError("glBind", "Cannot bind a deleted texture");
        gl.bindTexture(target, texture);
    }

    public void unbind() {
        gl.bindTexture(target, 0);
    }

    public void delete() {
        if(texture == -1) return;
        gl.deleteTexture(texture);
        texture = -1;
    }

    public Renderer.TextureTarget getTarget() {
        return target;
    }

    public boolean is1D() {
        return dimensions == 1;
    }

    public boolean is2D() {
        return dimensions == 2;
    }

    public boolean is3D() {
        return dimensions == 3;
    }

    final int _get_texture_() { return texture; }

    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        delete();
    }

    /**
     * Helper class for building textures, really helpful
     * @author melchor9000
     */
    public static class builder {
        private File file;
        private Renderer.TextureFilter mag, min;
        private Renderer.TextureWrap wrap_s, wrap_t;
        private Renderer.TextureFormat ifmt;
        private Renderer.TextureExternalFormat efmt;
        private Renderer.TextureTarget target;
        private boolean mipmap;
        private int width, height;

        /**
         * Starts building a new Texture. This constructor set as default
         * all values for a new Texture. Default values are:<br>
         * &nbsp;&nbsp;- Empty texture<br>
         * &nbsp;&nbsp;- Filters (min and mag) set to LINEAR<br>
         * &nbsp;&nbsp;- Format set to RGBA<br>
         * &nbsp;&nbsp;- Texture target to GL_TEXTURE_2D<br>
         * &nbsp;&nbsp;- No generate mipmaps<br>
         * &nbsp;&nbsp;- Width and height set to negative values,
         *               they have to be changed.
         */
        public builder() {
            this.file = null;
            this.mag = Renderer.TextureFilter.LINEAR;
            this.min = Renderer.TextureFilter.LINEAR;
            this.wrap_s = Renderer.TextureWrap.REPEAT;
            this.wrap_t = Renderer.TextureWrap.REPEAT;
            this.ifmt = Renderer.TextureFormat.RGBA;
            this.efmt = Renderer.TextureExternalFormat.RGBA;
            this.mipmap = false;
            this.width = -1000;
            this.height = -1000;
            this.target = Renderer.TextureTarget.TEXTURE_2D;
        }

        /**
         * Set a file for load a texture
         * @param file the file to set
         */
        public builder setFile(File file) {
            this.file = file;
            return this;
        }

        /**
         * Sets the magnifier filter for the texture
         * @param mag the filter to set
         */
        public builder setMag(Renderer.TextureFilter mag) {
            this.mag = mag;
            return this;
        }

        /**
         * Set the minifier filter for the texture
         * @param min the filter to set
         */
        public builder setMin(Renderer.TextureFilter min) {
            this.min = min;
            return this;
        }

        /**
         * Set how the texture will be wrapped on the left and
         * right borders.
         * @param wrap_s the wrap method to set
         */
        public builder setWrap_s(Renderer.TextureWrap wrap_s) {
            this.wrap_s = wrap_s;
            return this;
        }

        /**
         * Set how the texture will be wrapped on the top and
         * bottom borders.
         * @param wrap_t the wrap method to set
         */
        public builder setWrap_t(Renderer.TextureWrap wrap_t) {
            this.wrap_t = wrap_t;
            return this;
        }

        /**
         * Set the format of the image internally.
         * @param ifmt the format to set
         */
        public builder setIfmt(Renderer.TextureFormat ifmt) {
            this.ifmt = ifmt;
            return this;
        }

        /**
         * Set the format of the external image. Is not needed to call
         * this function if you load a texture from disk.
         * @param efmt the format to set
         */
        public builder setEfmt(Renderer.TextureExternalFormat efmt) {
            this.efmt = efmt;
            return this;
        }

        /**
         * Set whether the GPU will generate MipMaps or not.
         * @param mipmap the mipmap to set
         */
        public builder setMipmap(boolean mipmap) {
            this.mipmap = mipmap;
            return this;
        }

        /**
         * The width of the image, not needed for texture loading.
         * @param width the width to set
         */
        public builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * The height of the image, not needed for texture loading.
         * @param height the height to set
         */
        public builder setHeight(int height) {
            this.height = height;
            return this;
        }

        public builder setTarget(Renderer.TextureTarget target) {
            this.target = target;
            return this;
        }

        public Texture build() throws IOException {
            return new Texture(file, mag, min, wrap_s, wrap_t, ifmt, efmt, target, mipmap, width, height);
        }
    }
}
