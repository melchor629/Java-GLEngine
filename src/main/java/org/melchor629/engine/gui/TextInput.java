package org.melchor629.engine.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import org.lwjgl.nanovg.NVGColor;
import org.melchor629.engine.input.Keyboard;

/**
 * Text Input
 */
public class TextInput extends TextLabel {
    private float cursorPosition;

    public TextInput() {
        super("");
        backgroundColor = Color.rgba(0.2, 0.2, 0.2, 0.75);
        color = Color.rgb(0.9, 0.9, 0.9);
        borderRadius(5);
        padding(3);
        width = 100f;
    }

    @Override
    protected synchronized void paint() {
        super.paint();

        if(focus && (System.currentTimeMillis() / 500) % 2 == 1) {
            NVGColor c = NVGColor.malloc();
            color.convert(c);
            nvgFillColor(ctx, c);
            nvgBeginPath(ctx);
            GUIDrawUtils.drawRectangle(x + cursorPosition, y, 2, frame.height - paddingTop - paddingBottom);
            nvgFill(ctx);
            c.free();
        }
    }

    @Override
    public synchronized Frame effectiveFrame() {
        Frame f = super.effectiveFrame();
        checkSizeOfText();
        return f;
    }

    private synchronized void checkSizeOfText() {
        nvgSave(ctx);
        float[] bounds = new float[4];
        nvgTextAlign(ctx, NVG_ALIGN_TOP | NVG_ALIGN_LEFT);
        nvgFontSize(ctx, fontSize);
        nvgTextBounds(ctx, x, y, label, 0, bounds);
        nvgRestore(ctx);

        if(bounds[2] - bounds[0] > width) {
            textAlign(VerticalAlign.RIGHT);
            cursorPosition = width;
        } else {
            textAlign(VerticalAlign.LEFT);
            cursorPosition = bounds[2] - bounds[0];
        }
    }

    @Override
    public void width(Float width) {
        if(width == null) throw new NullPointerException("width cannot be null");
        super.width(width);
    }

    @Override
    public void onKeyDown(Keyboard keyboard, int key) {
        String rep = keyboard.getStringRepresentation(key);
        if(rep.equals("BACKSPACE") && !label.isEmpty()) {
            label = label.substring(0, label.length() - 1);
            checkSizeOfText();
        } else if(rep.equals("ENTER")) {
            System.out.println(label);
        }
    }

    @Override
    public void onCharKey(Keyboard keyboard, String rep) {
        label += rep;
        checkSizeOfText();
    }
}
