package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.lwjgl.system.MemoryStack;

public class Color implements Cloneable {

    private float r, g, b, a;

    public Color(float r, float g, float b, float a) {
        this.r = Math.min(Math.max(r, 0), 1);
        this.g = Math.min(Math.max(g, 0), 1);
        this.b = Math.min(Math.max(b, 0), 1);
        this.a = Math.min(Math.max(a, 0), 1);
    }

    public Color(int r, int g, int b, int a) {
        this((float) r / 255f, (float) g / 255f, (float) b / 255f, (float) a / 255f);
    }

    public Color(float r, float g, float b) {
        this(r, g, b, 1);
    }

    public Color(int r, int g, int b) {
        this((float) r / 255f, (float) g / 255f, (float) b / 255f, 1f);
    }

    public float r() {
        return r;
    }

    public float g() {
        return g;
    }

    public float b() {
        return b;
    }

    public float alpha() {
        return a;
    }

    public Color r(float v) {
        r = v;
        return this;
    }

    public Color g(float v) {
        g = v;
        return this;
    }

    public Color b(float v) {
        b = v;
        return this;
    }

    public Color alpha(float v) {
        a = v;
        return this;
    }

    public static Color white() {
        return new Color(1f, 1, 1);
    }

    public static Color black() {
        return new Color(0f, 0, 0);
    }

    public static Color red() {
        return new Color(1f, 0, 0, 1);
    }

    public static Color yellow() {
        return new Color(1f, 1, 0, 1);
    }

    public static Color green() {
        return new Color(0, 1f, 0, 1);
    }

    public static Color cyan() {
        return new Color (0, 1f, 1, 1);
    }

    public static Color blue() {
        return new Color(0, 0, 1f, 1);
    }

    public static Color magenta() {
        return new Color(1f, 0, 1, 1);
    }

    public static Color grey() {
        return hex("#808080");
    }

    public static Color lightGrey() {
        return hex("#D3D3D3");
    }

    public static Color transparent() {
        return new Color(0, 0, 0, 0);
    }

    public static Color hsla(double h, double s, double l, float a) {
        //http://stackoverflow.com/questions/2353211/hsl-to-rgb-color-conversion
        double r, g, b;

        if (s == 0f) {
            r = g = b = l; // achromatic
        } else {
            double q = l < 0.5f ? l * (1 + s) : l + s - l * s;
            double p = 2 * l - q;
            r = hueToRgb(p, q, h + 1f/3f);
            g = hueToRgb(p, q, h);
            b = hueToRgb(p, q, h - 1f/3f);
        }

        return rgba(r, g, b, a);
    }

    public static Color hsl(float h, float s, float l) {
        return hsla(h, s, l, 1);
    }

    public static Color rgb(double r, double g, double b) {
        return new Color((float) r, (float) g, (float) b);
    }

    public static Color rgba(double r, double g, double b, double a) {
        return new Color((float) r, (float) g, (float) b, (float) a);
    }

    public static Color hex(String hex) {
        if(hex.startsWith("#")) {
            if(hex.length() == 7 || hex.length() == 9) {
                int r = Integer.parseInt(hex.substring(1, 3), 16);
                int g = Integer.parseInt(hex.substring(3, 5), 16);
                int b = Integer.parseInt(hex.substring(5, 7), 16);
                int a = 255;
                if (hex.length() == 9)
                    a = Integer.parseInt(hex.substring(7, 9), 16);
                return new Color(r, g, b, a);
            } else if(hex.length() == 4 || hex.length() == 5) {
                int r = Integer.parseInt(hex.substring(1, 2), 16);
                int g = Integer.parseInt(hex.substring(2, 3), 16);
                int b = Integer.parseInt(hex.substring(3, 4), 16);
                int a = 255;
                if (hex.length() == 5)
                    a = Integer.parseInt(hex.substring(4, 5), 16);
                return new Color(r * 16 + r, g * 16 + g, b * 16 + b, a * 16 + a);
            } else {
                throw new IllegalArgumentException("Hex color is not valid");
            }
        } else {
            throw new IllegalArgumentException("Hex color doesn't start with #");
        }
    }

    private static double hueToRgb(double p, double q, double t) {
        if (t < 0f)
            t += 1f;
        if (t > 1f)
            t -= 1f;
        if (t < 1f/6f)
            return p + (q - p) * 6f * t;
        if (t < 1f/2f)
            return q;
        if (t < 2f/3f)
            return p + (q - p) * (2f/3f - t) * 6f;
        return p;
    }

    NVGColor convert(NVGColor color) {
        return NanoVG.nvgRGBAf(r, g, b, a, color);
    }

    /**
     * Sets the current fill color to this color
     */
    public void setAsFillColor() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGColor c = NVGColor.mallocStack(stack);
            NanoVG.nvgFillColor(GUI.gui.nvgCtx, convert(c));
        }
    }
    /**
     * Sets the current stroke color to this color
     */
    public void setAsStrokeColor() {
        try(MemoryStack stack = MemoryStack.stackPush()) {
            NVGColor c = NVGColor.mallocStack(stack);
            NanoVG.nvgStrokeColor(GUI.gui.nvgCtx, convert(c));
        }
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof Color) {
            Color c = (Color) o;
            return c.r == r && c.g == g && c.b == b && c.a == a;
        } else {
            return false;
        }
    }

    @Override
    public int hashCode() {
        int r = (int) (this.r * 255f);
        int g = (int) (this.g * 255f);
        int b = (int) (this.b * 255f);
        int a = (int) (this.a * 255f);
        return a + r * 17 + g * 17 * 17 + b * 17 * 17 * 17;
    }

    @Override
    public String toString() {
        return String.format("Color(%f, %f, %f, %f)", r, g, b, a);
    }

    @Override
    public Color clone() {
        try {
            return (Color) super.clone();
        } catch (CloneNotSupportedException e) {
            return null; //return new Color(r, g, b, a);
        }
    }
}
