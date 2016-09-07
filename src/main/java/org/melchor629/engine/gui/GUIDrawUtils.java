package org.melchor629.engine.gui;

import java.util.Formatter;

import static org.lwjgl.nanovg.NanoVG.*;
import static org.melchor629.engine.gui.GUI.gui;
import static java.lang.Math.*;

/**
 * Draw things around the screen
 */
public class GUIDrawUtils {

    /**
     * Draws a rectangle filled with the current fill color
     * @param x x coord of the top left corner
     * @param y y coord of the top left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     */
    public static void drawRectangle(float x, float y, float width, float height) {
        nvgBeginPath(gui.nvgCtx);
        nvgRect(gui.nvgCtx, x, y, width, height);
        nvgFill(gui.nvgCtx);
    }


    /**
     * Draws a rectangle filled with the current fill color and
     * bordered with a stroke
     * @param x x coord of the top left corner
     * @param y y coord of the top left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param borderSize border/stroke width
     */
    public static void drawRectangle(float x, float y, float width, float height, float borderSize) {
        nvgStrokeWidth(gui.nvgCtx, borderSize);
        nvgBeginPath(gui.nvgCtx);
        nvgRect(gui.nvgCtx, x, y, width, height);
        nvgFill(gui.nvgCtx);
        nvgStroke(gui.nvgCtx);
    }

    /**
     * Draws a rounded rectangle filled with the current fill color
     * and rounded by {@code borderRadius}. This radius value represents
     * how much pixels should be rounded.
     * @param x x coord of the top left corner
     * @param y y coord of the top left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param borderRadius border radius
     * @see <a href="http://www.w3schools.com/cssref/css3_pr_border-radius.asp">Understanding border-radius (through CSS)</a>
     */
    public static void drawRoundedRectangle(float x, float y, float width, float height, float borderRadius) {
        nvgBeginPath(gui.nvgCtx);
        nvgRoundedRect(gui.nvgCtx, x, y, width, height, borderRadius);
        nvgFill(gui.nvgCtx);
    }

    /**
     * Draws a rounded rectangle filled with the current fill color
     * and rounded every corner differently with its corresponding
     * border radius. This radius represents how much pixels should
     * be rounded.
     * @param x x coord of the top left corner
     * @param y y coord of the top left corner
     * @param width width of the rectangle
     * @param height height of the rectangle
     * @param brTopLeft border radius of the top left corner
     * @param brTopRight border radius of the top right corner
     * @param brBottomLeft border radius of the bottom left corner
     * @param brBottomRight border radius of the bottom right corner
     * @see <a href="http://www.w3schools.com/cssref/css3_pr_border-radius.asp">Understanding border-radius (through CSS)</a>
     */
    public static void drawRoundedRectangle(float x, float y, float width, float height, float brTopLeft, float brTopRight, float brBottomLeft, float brBottomRight) {
        if(brTopLeft < 0.1f && brTopRight < 0.1f && brBottomLeft < 0.1f && brBottomRight < 0.1f) {
            drawRectangle(x, y, width, height);
        } else {
            float k = 1f-0.551915024494f;
            float rxtl = min(brTopLeft, abs(width) * 0.5f) * signum(width), rytl = min(brTopLeft, abs(height) * 0.5f) * signum(height);
            float rxtr = min(brTopRight, abs(width) * 0.5f) * signum(width), rytr = min(brTopRight, abs(height) * 0.5f) * signum(height);
            float rxbl = min(brBottomLeft, abs(width) * 0.5f) * signum(width), rybl = min(brBottomLeft, abs(height) * 0.5f) * signum(height);
            float rxbr = min(brBottomRight, abs(width) * 0.5f) * signum(width), rybr = min(brBottomRight, abs(height) * 0.5f) * signum(height);

            nvgBeginPath(gui.nvgCtx);
            nvgMoveTo(gui.nvgCtx, x, y + rytl);
            nvgLineTo(gui.nvgCtx, x, y + height - rybl);
            nvgBezierTo(gui.nvgCtx, x, y + height - rybl * k, x + rxbl * k, y + height, x + rxbl, y + height);
            nvgLineTo(gui.nvgCtx, x + width - rxbr, y + height);
            nvgBezierTo(gui.nvgCtx, x + width - rxbr * k, y + height, x+width, y+height-rybr*k, x+width, y+height-rybr);
            nvgLineTo(gui.nvgCtx, x + width, y+rytr);
            nvgBezierTo(gui.nvgCtx, x + width, y+rytr*k, x+width-rxtr*k, y, x+width-rxtr, y);
            nvgLineTo(gui.nvgCtx, x + rxtl, y);
            nvgBezierTo(gui.nvgCtx, x + rxtl*k, y, x, y+rytl*k, x, y+rytl);
            nvgClosePath(gui.nvgCtx);
            nvgFill(gui.nvgCtx);
        }
    }

    /**
     * Draws a circle centered in {@code (x, y)} with radius {@code radius}
     * @param x x coord of the circle center
     * @param y y coord of the circle center
     * @param radius radius of the circle
     */
    public static void drawCircle(float x, float y, float radius) {
        drawRoundedRectangle(x - radius, y - radius, radius * 2, radius * 2, radius);
    }

    /**
     * Changes the current stroke width for bordering things
     * @param w new width to set
     */
    public static void setStrokeWidth(float w) {
        nvgStrokeWidth(gui.nvgCtx, w);
    }

    /**
     * Draws a one line {@link String} on a position
     * @param x x coord of the top left corner
     * @param y y coord of the top left corner
     * @param text text to draw
     */
    public static void drawText(float x, float y, String text) {
        nvgSave(gui.nvgCtx);
        nvgTextAlign(gui.nvgCtx, NVG_ALIGN_LEFT | NVG_ALIGN_TOP);
        nvgText(gui.nvgCtx, x, y, text, 0);
        nvgRestore(gui.nvgCtx);
    }

    /**
     * Draws a one line formatted {@link String} on a position
     * @param x x coord of the top left corner
     * @param y y coord of the top left corner
     * @param fmt A format String
     * @param objects Arguments referenced by the format specifiers in the format string
     * @see Formatter Formatter, "Format String Syntax" secion
     * @see String#format(String, Object...) String.format()
     */
    public static void drawText(float x, float y, String fmt, Object... objects) {
        drawText(x, y, String.format(fmt, objects));
    }
}
