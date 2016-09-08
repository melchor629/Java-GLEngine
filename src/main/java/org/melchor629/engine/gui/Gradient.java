package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryStack;
import org.melchor629.engine.utils.math.Vector2;

import static org.lwjgl.nanovg.NanoVG.nvgBoxGradient;
import static org.lwjgl.nanovg.NanoVG.nvgLinearGradient;
import static org.lwjgl.nanovg.NanoVG.nvgRadialGradient;
import static org.melchor629.engine.gui.GUI.gui;

/**
 * <p>
 * Instead of an {@link Image}, you can also use gradients.
 * </p>
 * <p>
 * Stores a gradient to be drawn in a background of a View or to set
 * as a fill and stroke draw patterns.
 * </p>
 * <p>
 * You can create a {@link Gradient} with the following methods:
 * </p>
 * <ul>
 *     <li>{@link #linearGradient(Vector2, Vector2, Color, Color)}</li>
 *     <li>{@link #linearGradient(float, float, float, float, Color, Color)}</li>
 *     <li>{@link #boxGradient(float, float, float, float, float, float, Color, Color)}</li>
 *     <li>{@link #radialGradient(float, float, float, float, Color, Color)}</li>
 *     <li>{@link #radialGradient(Vector2, float, float, Color, Color)}</li>
 * </ul>
 */
public class Gradient {
    private float a, b, c, d, e, f;
    private Color inside, outside;
    private int type; //0 -> linear  1 -> Box  2 -> Radial

    /**
     * Creates a linear gradient that starts in the position {@code start} with the color
     * {@code innerColor} and ends in the position {@code end} with the color {@code outerColor}.
     * @param start initial position of the gradient
     * @param end end position of the gradient
     * @param innerColor initial color of the gradient
     * @param outerColor end color of the gradient
     * @return {@link Gradient} that represents this linear gradient
     */
    public static Gradient linearGradient(Vector2 start, Vector2 end, Color innerColor, Color outerColor) {
        return linearGradient(start.x(), start.y(), end.x(), end.y(), innerColor, outerColor);
    }

    /**
     * Creates a linear gradient that starts in the position {@code xsx,sy} with the color
     * {@code innerColor} and ends in the position {@code ex,ey} with the color {@code outerColor}.
     * @param sx initial position of the gradient
     * @param sy initial position of the gradient
     * @param ex end position of the gradient
     * @param ey end position of the gradient
     * @param innerColor initial color of the gradient
     * @param outerColor end color of the gradient
     * @return {@link Gradient} that represents this linear gradient
     */
    public static Gradient linearGradient(float sx, float sy, float ex, float ey, Color innerColor, Color outerColor) {
        return new Gradient(0, sx, sy, ex, ey, 0, 0, innerColor, outerColor);
    }

    public static Gradient boxGradient(float x, float y, float width, float height, float radius, float feather, Color innerColor, Color outerColor) {
        return new Gradient(1, x, y, width, height, radius, feather, innerColor, outerColor);
    }

    /**
     * Creates a radial gradient that starts from the {@code innerRadius} with color {@code innerColor}
     * and ends to {@code outerRadius} with color{@code outerColor}, centered in {@code center}.
     * @param center Center of the radial gradient
     * @param innerRadius initial radius for the gradient
     * @param outerRadius final radius for the gradient
     * @param innerColor initial color
     * @param outerColor final color
     * @return {@link Gradient} that represents this radial gradient
     */
    public static Gradient radialGradient(Vector2 center, float innerRadius, float outerRadius, Color innerColor, Color outerColor) {
        return radialGradient(center.x(), center.y(), innerRadius, outerRadius, innerColor, outerColor);
    }

    /**
     * Creates a radial gradient that starts from the {@code innerRadius} with color {@code innerColor}
     * and ends to {@code outerRadius} with color{@code outerColor}, centered in {@code cx,cy}.
     * @param cx Center of the radial gradient
     * @param cy Center of the radial gradient
     * @param innerRadius initial radius for the gradient
     * @param outerRadius final radius for the gradient
     * @param innerColor initial color
     * @param outerColor final color
     * @return {@link Gradient} that represents this radial gradient
     */
    public static Gradient radialGradient(float cx, float cy, float innerRadius, float outerRadius, Color innerColor, Color outerColor) {
        return new Gradient(2, cx, cy, innerRadius, outerRadius, 0, 0, innerColor, outerColor);
    }

    private Gradient(int type, float a, float b, float c, float d, float e, float f, Color i, Color o) {
        this.type = type;
        this.a = a;
        this.b = b;
        this.c = c;
        this.d = d;
        this.e = e;
        this.f = f;
        inside = i;
        outside= o;
    }

    /**
     * Sets the current fill paint to this image
     */
    public void setAsFillPaint() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGPaint c = NVGPaint.mallocStack(stack);
            nvgGradient(c);
            NanoVG.nvgFillPaint(GUI.gui.nvgCtx, c);
        }
    }

    /**
     * Sets the current stroke paint to this image
     */
    public void setAsStrokePaint() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGPaint c = NVGPaint.mallocStack(stack);
            nvgGradient(c);
            NanoVG.nvgStrokePaint(GUI.gui.nvgCtx, c);
        }
    }

    private void nvgGradient(NVGPaint paint) {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGColor icolor = NVGColor.mallocStack(stack), ocolor = NVGColor.mallocStack(stack);
            inside.convert(icolor);
            outside.convert(ocolor);
            if(type == 0) {
                nvgLinearGradient(gui.nvgCtx, a, b, c, d, icolor, ocolor, paint);
            } else if(type == 1) {
                nvgBoxGradient(gui.nvgCtx, a, b, c, d, e, f, icolor, ocolor, paint);
            } else if(type == 2) {
                nvgRadialGradient(gui.nvgCtx, a, b, c, d, icolor, ocolor, paint);
            }
        }
    }
}
