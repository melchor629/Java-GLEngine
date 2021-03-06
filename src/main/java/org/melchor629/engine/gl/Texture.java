package org.melchor629.engine.gl;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.gl.GLContext.TextureExternalFormat;
import org.melchor629.engine.gl.GLContext.TextureFilter;
import org.melchor629.engine.gl.GLContext.TextureFormat;
import org.melchor629.engine.gl.GLContext.TextureParameter;
import org.melchor629.engine.gl.GLContext.TextureWrap;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.ImageIO;

/**
 * Class for manage textures. Create textures from files, or from
 * the internet, or simply empty textures for use in Framebuffers.
 * Textures stores pixel data in the GPU using an internal format.
 * @author melchor9000
 */
public class Texture implements Erasable {
    private int texture = -1;
    private GLContext.TextureTarget target;
    private int dimensions;
    private int width, height, comp;
    private final GLContext gl;
    private final boolean resizable;
    private GLContext.TextureExternalFormat efmt;
    private GLContext.TextureFormat ifmt;
    private GLContext.type type;

    /**
     * Create a simple empty 2D texture, with minifier and magnifier filter
     * set as {@code LINEAR} and the parameters given.
     * @param format Internal format of the texture
     * @param width Width of the texture
     * @param height Height of the texture
     * @param eformat External format of the texture (should be the same
     *                or similar from {@code format} parameter.
     */
    Texture(GLContext gl, GLContext.TextureFormat format, int width, int height, GLContext.TextureExternalFormat eformat) {
        this.gl = gl;
        texture = gl.genTexture();
        target = GLContext.TextureTarget.TEXTURE_2D;
        dimensions = 2;
        this.width = width;
        this.height = height;
        this.comp = eformat == TextureExternalFormat.RGB ? 3 : 4; //TODO Más general
        this.resizable = true;
        this.ifmt = format;
        this.efmt = eformat;
        this.type = GLContext.type.UNSIGNED_BYTE;

        gl.bindTexture(target, texture);
        gl.texParameteri(target, GLContext.TextureParameter.MIN_FILTER, GLContext.TextureFilter.LINEAR);
        gl.texParameteri(target, GLContext.TextureParameter.MAG_FILTER, GLContext.TextureFilter.LINEAR);
        if(format == TextureFormat.RGB16F) gl.texImage2D(target, 0, format, width, height, 0, eformat, GLContext.type.FLOAT);
        else gl.texImage2D(target, 0, format, width, height, 0, eformat, GLContext.type.UNSIGNED_BYTE);

        gl.addErasable(this);
    }

    /**
     * Constructor for the Texture.Builder
     */
    private Texture(GLContext gl, File file, TextureFilter mag, TextureFilter min,
                    TextureWrap wrap_s, TextureWrap wrap_t, TextureFormat ifmt,
                    TextureExternalFormat efmt, GLContext.TextureTarget target,
                    boolean mipmap, int width, int height) throws IOException {
        this.gl = gl;
        texture = gl.genTexture();
        this.target = target;
        this.resizable = false;
        this.ifmt = ifmt;
        this.efmt = efmt;
        this.type = GLContext.type.UNSIGNED_BYTE;
        
        gl.bindTexture(target, texture);
        gl.texParameteri(target, TextureParameter.MIN_FILTER, min);
        gl.texParameteri(target, TextureParameter.MAG_FILTER, mag);
        gl.texParameteri(target, TextureParameter.WRAP_S, wrap_s);
        gl.texParameteri(target, TextureParameter.WRAP_T, wrap_t);
        
        java.nio.ByteBuffer buffer = null;
        ImageIO.ImageData data = null;
        if(file != null) {
            data = ImageIO.loadImage(file);
            if(data.data == null)
                throw new IOException("Could not decode image");
            width = data.width;
            height = data.height;
            efmt = data.components == 3 ? TextureExternalFormat.RGB : TextureExternalFormat.RGBA;
            buffer = data.data;
            this.comp = data.components;
        } else {
            this.comp = efmt == TextureExternalFormat.RGB ? 3 : 4; //TODO Más general
        }
        gl.texImage2D(target, 0, ifmt, width, height, 0, efmt, GLContext.type.UNSIGNED_BYTE, buffer); //TODO Determinar 1D, 2D, 3D

        if(mipmap)
            gl.generateMipmap(target);
        data.clear.apply(data);
        buffer.clear();
        gl.bindTexture(target, 0);

        this.width = width;
        this.height = height;

        gl.addErasable(this);
    }

    public void bind() {
        if(texture == -1) throw new GLError("glBind", "Cannot bind a deleted texture");
        gl.bindTexture(target, texture);
    }

    public void unbind() {
        gl.bindTexture(target, 0);
    }

    public void clear() {
        bind();
        gl.texImage2D(target, 0, ifmt, width, 0, height, efmt, type);
        unbind();
    }

    public void resize(int width, int height) {
        if(resizable) {
            bind();
            gl.texImage2D(target, 0, ifmt, width, 0, height, efmt, type);
            this.width = width;
            this.height = height;
            unbind();
        } else {
            throw new UnsupportedOperationException("Cannot resize unresizable texture (ex: loaded from a file)");
        }
    }

    public void delete() {
        if(texture == -1) return;
        gl.deleteTexture(texture);
        texture = -1;
    }

    public GLContext.TextureTarget getTarget() {
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
    public boolean equals(Object o) {
        return (o instanceof Texture) && ((Texture) o).texture == texture;
    }

    @Override
    public int hashCode() {
        return texture * 17 + dimensions;
    }

    /**
     * Helper class for building textures, really helpful
     * @author melchor9000
     */
    public static class Builder {
        private File file;
        private InputStream imageStream;
        private GLContext.TextureFilter mag, min;
        private GLContext.TextureWrap wrap_s, wrap_t;
        private GLContext.TextureFormat ifmt;
        private GLContext.TextureExternalFormat efmt;
        private GLContext.TextureTarget target;
        private boolean mipmap;
        private int width, height;
        private GLContext gl;

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
         * @param gl OpenGL Context
         */
        public Builder(GLContext gl) {
            this.gl = gl;
            this.file = null;
            this.imageStream = null;
            this.mag = GLContext.TextureFilter.LINEAR;
            this.min = GLContext.TextureFilter.LINEAR;
            this.wrap_s = GLContext.TextureWrap.REPEAT;
            this.wrap_t = GLContext.TextureWrap.REPEAT;
            this.ifmt = GLContext.TextureFormat.RGBA;
            this.efmt = GLContext.TextureExternalFormat.RGBA;
            this.mipmap = false;
            this.width = -1000;
            this.height = -1000;
            this.target = GLContext.TextureTarget.TEXTURE_2D;
        }

        /**
         * Set a file for load a texture
         * @param file the file to set
         * @return itself
         */
        public Builder setFile(File file) {
            this.file = file;
            return this;
        }

        /**
         * Set a file for load a texture
         * @param file the path file to set
         * @return itself
         */
        public Builder setFile(String file) {
            this.file = new File(file);
            return this;
        }

        /**
         * Sets a {@link InputStream} as texture to be read, instead
         * of a simple file.
         * @param is Input stream to be read as texture
         * @return itself
         */
        public Builder setStreamToFile(InputStream is) {
            this.imageStream = is;
            return this;
        }

        /**
         * Sets the magnifier filter for the texture
         * @param mag the filter to set
         * @return itself
         */
        public Builder setMag(GLContext.TextureFilter mag) {
            this.mag = mag;
            return this;
        }

        /**
         * Set the minifier filter for the texture
         * @param min the filter to set
         * @return itself
         */
        public Builder setMin(GLContext.TextureFilter min) {
            this.min = min;
            return this;
        }

        /**
         * Set how the texture will be wrapped on the left and
         * right borders.
         * @param wrap_s the wrap method to set
         * @return itself
         */
        public Builder setWrap_s(GLContext.TextureWrap wrap_s) {
            this.wrap_s = wrap_s;
            return this;
        }

        /**
         * Set how the texture will be wrapped on the top and
         * bottom borders.
         * @param wrap_t the wrap method to set
         * @return itself
         */
        public Builder setWrap_t(GLContext.TextureWrap wrap_t) {
            this.wrap_t = wrap_t;
            return this;
        }

        /**
         * Set how the textire will be wrapped on all borders.
         * @param wrap the wrap method to set
         * @return the builder
         */
        public Builder setWrap(GLContext.TextureWrap wrap) {
            setWrap_s(wrap);
            return setWrap_t(wrap);
        }

        /**
         * Set the format of the image internally.
         * @param ifmt the format to set
         * @return itself
         */
        public Builder setIfmt(GLContext.TextureFormat ifmt) {
            this.ifmt = ifmt;
            return this;
        }

        /**
         * Set the format of the external image. Is not needed to call
         * this function if you load a texture from disk.
         * @param efmt the format to set
         * @return itself
         */
        public Builder setEfmt(GLContext.TextureExternalFormat efmt) {
            this.efmt = efmt;
            return this;
        }

        /**
         * Set whether the GPU will generate MipMaps or not.
         * @param mipmap the mipmap to set
         * @return itself
         */
        public Builder setMipmap(boolean mipmap) {
            this.mipmap = mipmap;
            return this;
        }

        /**
         * The width of the image, not needed for texture loading.
         * @param width the width to set
         * @return itself
         */
        public Builder setWidth(int width) {
            this.width = width;
            return this;
        }

        /**
         * The height of the image, not needed for texture loading.
         * @param height the height to set
         * @return itself
         */
        public Builder setHeight(int height) {
            this.height = height;
            return this;
        }

        /**
         * Sets the OpenGL Texture target for this texture
         * @param target Texture Target
         * @return this
         */
        public Builder setTarget(GLContext.TextureTarget target) {
            this.target = target;
            return this;
        }

        /**
         * Creates a {@link Texture} for the options given in the Builder
         * @return the Texture
         * @throws IOException if there's an error while reading the texture from a file
         */
        public Texture build() throws IOException {
            if(file != null)
                return new Texture(gl, file, mag, min, wrap_s, wrap_t, ifmt, efmt, target, mipmap, width, height);
            else if(imageStream != null) {
                File temp = IOUtils.createTempFile();
                FileOutputStream fos = new FileOutputStream(temp);
                byte[] buff = new byte[8192];
                while(imageStream.available() > 0) {
                    int read = imageStream.read(buff);
                    fos.write(buff, 0, read);
                }
                return new Texture(gl, temp, mag, min, wrap_s, wrap_t, ifmt, efmt, target, mipmap, width, height);
            } else
                throw new IllegalArgumentException("Trying to create a texture without a file o stream to read");
        }
    }
}
