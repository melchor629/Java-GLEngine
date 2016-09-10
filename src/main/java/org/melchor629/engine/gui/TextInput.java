package org.melchor629.engine.gui;

import static org.lwjgl.nanovg.NanoVG.*;

import org.melchor629.engine.input.Keyboard;

/**
 * Text Input
 */
public class TextInput extends TextLabel {
    private float cursorPosition;
    private boolean backspacePressed;

    public TextInput() {
        super("");
        backgroundColor = Color.rgba(0.2, 0.2, 0.2, 0.75);
        color = Color.rgb(0.9, 0.9, 0.9);
        borderRadius(5);
        padding(3);
        width = 100f;

        GUI.gui.executeOnceDelayed(500 - System.currentTimeMillis() + System.currentTimeMillis() / 500 * 500, this::repeat);
    }

    @Override
    protected synchronized void paint() {
        super.paint();

        if(focus && (System.currentTimeMillis() / 500) % 2 == 1) {
            color.setAsFillColor();
            nvgBeginPath(ctx);
            GUIDrawUtils.drawRectangle(x + cursorPosition + paddingLeft, y + paddingTop, 2, frame.height - paddingTop - paddingBottom);
            nvgFill(ctx);
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
        cursorPosition = nvgTextBounds(ctx, x, y, label, 0, bounds);
        nvgRestore(ctx);

        if(bounds[2] - bounds[0] > width) {
            textAlign(VerticalAlign.RIGHT);
            cursorPosition = width;
            markDirty();
        } else if(textAlign != VerticalAlign.LEFT) {
            textAlign(VerticalAlign.LEFT);
            markDirty();
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
            markDirty();
            backspacePressed = true;
            GUI.gui.executeOnceDelayed(750, this::deleteMaintained);
        } else if(rep.equals("ENTER")) {
            System.out.println(label);
        }
    }

    @Override
    public void onKeyUp(Keyboard keyboard, int key) {
        String rep = keyboard.getStringRepresentation(key);
        if(rep.equals("BACKSPACE")) {
            backspacePressed = false;
        }
    }

    @Override
    public void onCharKey(Keyboard keyboard, String rep) {
        label += rep;
        checkSizeOfText();
        markDirty();
    }

    private void repeat() {
        if(focus) markDirty();
        GUI.gui.executeOnceDelayed(500 - System.currentTimeMillis() + System.currentTimeMillis() / 500 * 500, this::repeat);
    }

    private void deleteMaintained() {
        if(backspacePressed && !label.isEmpty()) {
            label = label.substring(0, label.length() - 1);
            checkSizeOfText();
            markDirty();
            GUI.gui.executeOnceDelayed(75, this::deleteMaintained);
        }
    }
}
