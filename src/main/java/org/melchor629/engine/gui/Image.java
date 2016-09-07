package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryStack;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.MemoryUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.melchor629.engine.gui.GUI.gui;

/**
 * Image for GUI
 */
public class Image {
    protected int handle;
    protected float width, height, x, y, angle, alpha = 1;

    /**
     * Repeats image in X direction
     */
    public static final int REPEAT_X = 1 << 1;

    /**
     * Repeats image in Y direction
     */
    public static final int REPEAT_Y = 1 << 2;

    /**
     * Flips (inverses) image in Y direction when rendered
     */
    public static final int FLIP_Y   = 1 << 3;

    /**
     * Imaged data has premultiplied alpha
     */
    public static final int PREMULTIPLIED = 1 << 4;

    /**
     * Loads an image from a resource inside a jar. Supports png, jpg, tga, psd,
     * pic and gif formats. The load is synchronously, will block until the read
     * and decode operations are done.
     * @param path path to the resource
     * @param flags flags for the image: {@link #REPEAT_X}, {@link #REPEAT_Y},
     *              {@link #FLIP_Y} and {@link #PREMULTIPLIED}.
     * @return a {@link Image} instance
     * @throws IOException If resource is not found or bad things happen while
     * reading the resource
     * @throws IllegalArgumentException if the image format is not supported.
     */
    public static Image fromResource(String path, int flags) throws IOException {
        if(!isValid(path.substring(path.lastIndexOf(".")))) throw new IllegalArgumentException("Cannot decode this image type");
        InputStream is = IOUtils.getResourceAsStream(path);
        if(is == null) throw new FileNotFoundException("Resource doesn't exist: " + path);

        ByteBuffer img = IOUtils.readInputStream(is);
        int h = nvgCreateImageMem(gui.nvgCtx, flags, img);
        MemoryUtils.free(img);
        if(h == 0) throw new RuntimeException("Could not load image");

        return new Image(h);
    }

    /**
     * Loads an image from a file. Supports png, jpg, tga, psd, pic and gif formats.
     * The load is synchronously, will block until the read and decode operations are done.
     * @param file file to load
     * @param flags flags for the image: {@link #REPEAT_X}, {@link #REPEAT_Y},
     *              {@link #FLIP_Y} and {@link #PREMULTIPLIED}.
     * @return a {@link Image} instance
     * @throws FileNotFoundException If the file is not found
     * @throws IllegalArgumentException if the image format is not supported.
     */
    public static Image fromFile(File file, int flags) throws FileNotFoundException {
        if(!file.exists()) throw new FileNotFoundException("File doesn't exist: " + file);
        if(!isValid(file.getName().substring(file.getName().lastIndexOf(".")))) throw new IllegalArgumentException("Cannot decode this image type");

        int h = nvgCreateImage(gui.nvgCtx, file.getAbsolutePath(), flags);
        if(h == 0) throw new RuntimeException("Could not load image");

        return new Image(h);
    }

    /**
     * Loads an image from a {@link URL}. Supports png, jpg, tga, psd, pic and gif formats.
     * The load is synchronously, will block until the read and decode operations are done.
     * @param url url for the resource to load
     * @param flags flags for the image: {@link #REPEAT_X}, {@link #REPEAT_Y},
     *              {@link #FLIP_Y} and {@link #PREMULTIPLIED}.
     * @return a {@link Image} instance
     * @throws IOException If resource is not found or bad things happen while
     * reading the resource
     * @throws IllegalArgumentException if the image format is not supported.
     */
    public static Image fromURL(URL url, int flags) throws IOException {
        URLConnection c = url.openConnection();
        if(!isValid(c.getContentType())) throw new IllegalArgumentException("Cannot decode this image type");
        c.getInputStream().close();
        ByteBuffer data = IOUtils.readUrl(url);
        int h = nvgCreateImageMem(gui.nvgCtx, flags, data);
        MemoryUtils.free(data);
        if(h == 0) throw new RuntimeException("Could not load image");

        return new Image(h);
    }

    /**
     * Loads an image from memory. Supports png, jpg, tga, psd, pic and gif formats.
     * Also supports raw RGBA image. Buffer is set to initial position. The load is
     * synchronously, will block until decode operation is done.
     * @param buffer image from memory
     * @param flags flags for the image: {@link #REPEAT_X}, {@link #REPEAT_Y},
     *              {@link #FLIP_Y} and {@link #PREMULTIPLIED}.
     * @return a {@link Image} instance
     */
    public static Image fromMemory(ByteBuffer buffer, int flags) {
        buffer.position(0);
        int h = nvgCreateImageMem(gui.nvgCtx, flags, buffer);
        if(h == 0) throw new RuntimeException("Could not load image");
        buffer.position(0);
        return new Image(h);
    }

    private static boolean isValid(String ext) {
        return ext.contains("jpg") || ext.contains("png") || ext.contains("tga") || ext.contains("pic")
                || ext.contains("gif") || ext.contains("psd") || ext.contains("jpeg");
    }

    private Image(int handle) {
        this.handle = handle;
        int w[] = new int[1], h[] = new int[1];
        nvgImageSize(gui.nvgCtx, handle, w, h);
        width = w[0];
        height = h[0];
    }

    /**
     * Changes the starting position of the image. The values {@code (x, y)}
     * corresponds to the top left corner of the image. By default is
     * {@code (0, 0)}.
     * @param x x position
     * @param y y position
     */
    public void position(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Changes the size of the image when drawn. The initial value is
     * the width and height of the source image.
     * @param width width of the drawn image
     * @param height height of the drawn image
     */
    public void size(float width, float height) {
        this.width = width;
        this.height = height;
    }

    /**
     * Changes the opacity of the image when drawn. By default is
     * full opaque, 1.
     * @param alpha opacity of the image
     */
    public void opacity(float alpha) {
        this.alpha = alpha;
    }

    /**
     * Changes the rotation of the image when drawn. By default is 0
     * rad. Angles are in radians.
     * @param angle rotation in radians
     */
    public void rotate(float angle) {
        this.angle = angle;
    }

    /**
     * Sets the current fill paint to this image
     */
    public void setAsFillPaint() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGPaint c = NVGPaint.mallocStack(stack);
            c = nvgImagePattern(gui.nvgCtx, x, y, width, height, angle, handle, alpha, c);
            NanoVG.nvgFillPaint(GUI.gui.nvgCtx, c);
        }
    }

    /**
     * Sets the current stroke paint to this image
     */
    public void setAsStrokePaint() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGPaint c = NVGPaint.mallocStack(stack);
            c = nvgImagePattern(gui.nvgCtx, x, y, width, height, angle, handle, alpha, c);
            NanoVG.nvgStrokePaint(GUI.gui.nvgCtx, c);
        }
    }

    public void free() {
        nvgDeleteImage(gui.nvgCtx, handle);
    }
}
