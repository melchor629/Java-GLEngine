package org.melchor629.engine.utils;

import org.lwjgl.system.MemoryStack;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.util.function.Function;

/**
 * Load and writes images using available api
 */
public class ImageIO {
    public static final class ImageData {
        public ByteBuffer data;
        public int width, height, components;
        public Function<ImageData, Void> clear = null;

        private ImageData() {}
        public ImageData(int width, int height, int components, ByteBuffer data) {
            this.data = data;
            this.width = width;
            this.height = height;
            this.components = components;
        }

        public void delete() {
            if(clear != null) {
                clear.apply(this);
            }
        }
    }

    /**
     * Loads an image from a file. Supported formats are
     * <ul>
     * <li>JPEG baseline &amp; progressive (12 bpc/arithmetic not supported, same as stock IJG lib</li>
     * <li>PNG 1/2/4/8-bit-per-channel (16 bpc not supported)</li>
     * <li>TGA (not sure what subset, if a subset)</li>
     * <li>BMP non-1bpp, non-RLE</li>
     * <li>PSD (composited view only, no extra channels, 8/16 bit-per-channel)</li>
     * <li>GIF (*comp always reports as 4-channel)</li>
     * <li>HDR (radiance rgbE format)</li>
     * <li>PIC (Softimage PIC)</li>
     * <li>PNM (PPM and PGM binary only)</li>
     * </ul>
     * @param path path to the file
     * @return {@link ImageData} with info about the image
     * @see org.lwjgl.stb.STBImage STBImage, image decoder
     */
    public static ImageData loadImage(final File path) {
        ImageData data = new ImageData();
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.ints(0),
                      y = stack.ints(0),
                   comp = stack.ints(0);
            data.data = org.lwjgl.stb.STBImage.stbi_load(path.getAbsolutePath(), x, y, comp, 0);
            if(data.data == null) throw new RuntimeException(org.lwjgl.stb.STBImage.stbi_failure_reason());
            data.width = x.get();
            data.height = y.get();
            data.components = comp.get();
            data.clear = o -> {
                org.lwjgl.stb.STBImage.stbi_image_free(o.data);
                return null;
            };
        }
        return data;
    }
    /**
     * Loads an image from a chunk of memory. Supported formats are
     * <ul>
     * <li>JPEG baseline &amp; progressive (12 bpc/arithmetic not supported, same as stock IJG lib</li>
     * <li>PNG 1/2/4/8-bit-per-channel (16 bpc not supported)</li>
     * <li>TGA (not sure what subset, if a subset)</li>
     * <li>BMP non-1bpp, non-RLE</li>
     * <li>PSD (composited view only, no extra channels, 8/16 bit-per-channel)</li>
     * <li>GIF (*comp always reports as 4-channel)</li>
     * <li>HDR (radiance rgbE format)</li>
     * <li>PIC (Softimage PIC)</li>
     * <li>PNM (PPM and PGM binary only)</li>
     * </ul>
     * @param buff the chunk of memory
     * @return {@link ImageData} with info about the image
     * @see org.lwjgl.stb.STBImage STBImage, image decoder
     */
    public static ImageData loadImageFromMemory(final ByteBuffer buff) {
        ImageData data = new ImageData();
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.ints(0),
                      y = stack.ints(0),
                   comp = stack.ints(0);
            data.data = org.lwjgl.stb.STBImage.stbi_load_from_memory(buff, x, y, comp, 0);
            if(data.data == null) throw new RuntimeException(org.lwjgl.stb.STBImage.stbi_failure_reason());
            data.width = x.get();
            data.height = y.get();
            data.components = comp.get();
            data.clear = o -> {
                org.lwjgl.stb.STBImage.stbi_image_free(o.data);
                return null;
            };
        }
        return data;
    }

    /**
     * Loads an image from resources folder. Supported formats are
     * <ul>
     * <li>JPEG baseline &amp; progressive (12 bpc/arithmetic not supported, same as stock IJG lib</li>
     * <li>PNG 1/2/4/8-bit-per-channel (16 bpc not supported)</li>
     * <li>TGA (not sure what subset, if a subset)</li>
     * <li>BMP non-1bpp, non-RLE</li>
     * <li>PSD (composited view only, no extra channels, 8/16 bit-per-channel)</li>
     * <li>GIF (*comp always reports as 4-channel)</li>
     * <li>HDR (radiance rgbE format)</li>
     * <li>PIC (Softimage PIC)</li>
     * <li>PNM (PPM and PGM binary only)</li>
     * </ul>
     * @param file path to the resource
     * @return {@link ImageData} with info about the image
     * @throws IOException if an error occurs while reading the resource
     * @see org.lwjgl.stb.STBImage STBImage, image decoder
     */
    public static ImageData loadImageFromResource(String file) throws IOException {
        ByteBuffer buff = IOUtils.readInputStream(IOUtils.getResourceAsStream(file));
        try {
            return loadImageFromMemory(buff);
        } finally {
            MemoryUtils.free(buff);
        }
    }

    /**
     * Loads an image from a {@link URL}. Supported formats are
     * <ul>
     * <li>JPEG baseline &amp; progressive (12 bpc/arithmetic not supported, same as stock IJG lib</li>
     * <li>PNG 1/2/4/8-bit-per-channel (16 bpc not supported)</li>
     * <li>TGA (not sure what subset, if a subset)</li>
     * <li>BMP non-1bpp, non-RLE</li>
     * <li>PSD (composited view only, no extra channels, 8/16 bit-per-channel)</li>
     * <li>GIF (*comp always reports as 4-channel)</li>
     * <li>HDR (radiance rgbE format)</li>
     * <li>PIC (Softimage PIC)</li>
     * <li>PNM (PPM and PGM binary only)</li>
     * </ul>
     * @param url {@link URL} to the resource
     * @return {@link ImageData} with info about the image
     * @throws IOException if an error occurs while reading the {@link URL}
     * @see org.lwjgl.stb.STBImage STBImage, image decoder
     */
    public static ImageData loadImage(final URL url) throws IOException {
        return loadImageFromMemory(IOUtils.readUrl(url));
    }

    public static void writeImage(final File path, final ImageData imageData) {
        if(path.getAbsolutePath().endsWith("bmp"))
            org.lwjgl.stb.STBImageWrite.stbi_write_bmp(path.getAbsolutePath(), imageData.width, imageData.height,
                    imageData.components, imageData.data);
        else if(path.getAbsolutePath().endsWith("png"))
            org.lwjgl.stb.STBImageWrite.stbi_write_png(path.getAbsolutePath(), imageData.width, imageData.height,
                    imageData.components, imageData.data, imageData.width*imageData.components);
        else if(path.getAbsolutePath().endsWith("tga"))
            org.lwjgl.stb.STBImageWrite.stbi_write_tga(path.getAbsolutePath(), imageData.width, imageData.height,
                    imageData.components, imageData.data);
        //TODO hdr
    }
}
