package org.melchor629.engine.gui;

/**
 * Represents a frame of a View, or its position and size
 */
public class Frame implements Cloneable {
    public float x, y, width, height;

    public Frame(float a, float b, float c, float d) {
        x = a;
        y = b;
        width = c;
        height = d;
    }

    @Override
    public Frame clone() {
        try {
            return (Frame) super.clone();
        } catch (CloneNotSupportedException e) {
            return null;
        }
    }
}
