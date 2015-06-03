package org.melchor629.engine.clib;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

import java.util.Arrays;
import java.util.List;

/**
 * Native bindings to STBLoader library (stb bindings created by me)
 */
public interface STBLoader extends Library {
    STBLoader instance = (STBLoader) Native.loadLibrary("Engine", STBLoader.class);

    /**
     * Loads a image into a buffer data ready-to-use on OpenGL
     * @param file_path Path to the file
     * @param info information about the loaded image
     */
    void stb_load_image(String file_path, ImageData info);

    /**
     * Frees all memory allocated from this texture
     * @param info Information about the loaded image
     */
    void stb_clear_image(ImageData info);

    /**
     * Write buffer data into a image
     * @param file_path Path to the file
     * @param fmt Format of the file
     * @param info Information about the image to be written
     */
    void stb_write_image(String file_path, String fmt, ImageData info);

    class ImageData extends Structure {
        public Pointer data;
        public int width, height, components;

        protected List getFieldOrder() {
            return Arrays.asList("data", "width", "height", "components");
        }
    }

}
