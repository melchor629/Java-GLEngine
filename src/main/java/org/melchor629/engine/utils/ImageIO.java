package org.melchor629.engine.utils;

import org.lwjgl.system.MemoryStack;

import java.io.File;
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

    public static ImageData loadImage(final File path) {
        if(hasLWJGL()) {
            return lwjgl_loadImage(path);
        }

        return null;
    }

    public static void writeImage(final File path, final ImageData imageData) {
        if(hasLWJGL() && lwjgl_canWrite(path))
            lwjgl_writeImage(path, imageData);
    }

    private static boolean hasLWJGL() {
        try {
            Class.forName("org.lwjgl.system.Platform");
            return true;
        } catch(ClassNotFoundException ignore) {
            return false;
        }
    }

    private static ImageData lwjgl_loadImage(final File path) {
        ImageData data = new ImageData();
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer x = stack.ints(0),
                      y = stack.ints(0),
                   comp = stack.ints(0);
            data.data = org.lwjgl.stb.STBImage.stbi_load(path.getAbsolutePath(), x, y, comp, 0);
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

    private static boolean lwjgl_canWrite(final File path) {
        String ext = path.getAbsolutePath().substring(path.getAbsolutePath().lastIndexOf(".") + 1);
        return ext.equalsIgnoreCase("bmp") || ext.equalsIgnoreCase("png") || ext.equalsIgnoreCase("tga");
    }

    private static void lwjgl_writeImage(final File path, final ImageData imageData) {
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
