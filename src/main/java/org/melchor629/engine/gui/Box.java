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
        NVGColor cc = NVGColor.calloc();
        backgroundColor.convert(cc);

        nvgFillColor(ctx, cc);
        nvgBeginPath(ctx);
        nvgRect(ctx, frame.x, frame.y, frame.width, frame.height);
        nvgFill(ctx);

        cc.free();
    }

    @Override
    public Frame effectiveFrame() {
        if(frame == null) {
            frame = new Frame(x, y, width, height);
        }
        return frame;
    }
}
