package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * A man in a box
 */
public class Box extends View {
    public Box() {
        super();
        width = 0f;
        height = 0f;
    }

    @Override
    protected void paint() {
    }

    @Override
    public Frame effectiveFrame() {
        if(frame == null) {
            frame = new Frame(x, y, width, height);
        }
        return frame;
    }
}
