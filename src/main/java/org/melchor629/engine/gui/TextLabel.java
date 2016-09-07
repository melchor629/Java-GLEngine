package org.melchor629.engine.gui;

import java.util.NoSuchElementException;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Text label
 */
public class TextLabel extends View {
    protected String label;
    protected VerticalAlign align = VerticalAlign.LEFT;
    protected HorizontalAlign align2 = HorizontalAlign.CENTER;
    protected float fontSize = 14;
    protected String fontName;

    public TextLabel(String label) {
        this.label = label;
        color = Color.black();
        backgroundColor = Color.transparent();
    }

    public void textAlign(VerticalAlign align) {
        this.align = align;
    }

    public void textAlign(HorizontalAlign align) {
        this.align2 = align;
    }

    public void textAlign(VerticalAlign va, HorizontalAlign ha) {
        align = va;
        align2 = ha;
    }

    public void fontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public void font(String font) {
        if(!GUI.gui.isFontLoaded(font)) throw new NoSuchElementException("Font named " + font + " is not loaded");
        fontName = font;
    }

    @Override
    protected void paint() {
        backgroundColor.setAsFillColor();

        nvgBeginPath(ctx);
        GUIDrawUtils.drawRoundedRectangle(frame.x, frame.y, frame.width, frame.height, borderRadiusTopLeft, borderRadiusTopRight, borderRadiusBottomLeft, borderRadiusBottomRight);

        this.color.setAsFillColor();
        if(width != null || height != null) nvgIntersectScissor(ctx, frame.x, frame.y, frame.width, frame.height);
        nvgFontSize(ctx, fontSize);

        int alignHor = NVG_ALIGN_TOP, alignVert = NVG_ALIGN_LEFT;
        float fx = 0, fy = 0;
        if(align == VerticalAlign.LEFT || width == null) {
            alignVert = NVG_ALIGN_LEFT;
            fx = 0;
        } else if(align == VerticalAlign.CENTER) {
            alignVert = NVG_ALIGN_CENTER;
            fx = width / 2;
        } else if(align == VerticalAlign.RIGHT) {
            alignVert = NVG_ALIGN_RIGHT;
            fx = width;
        }

        if(align2 == HorizontalAlign.TOP || height == null) {
            alignHor = NVG_ALIGN_TOP;
            fy = 0;
        } else if(align2 == HorizontalAlign.CENTER) {
            alignHor = NVG_ALIGN_MIDDLE;
            fy = height / 2;
        } else if(align2 == HorizontalAlign.BOTTOM) {
            alignHor = NVG_ALIGN_BOTTOM;
            fy = height;
        }

        if(fontName != null) GUI.gui.setFont(fontName, fontSize);
        nvgTextAlign(ctx, alignHor | alignVert);
        nvgText(ctx, x + fx, y + fy, label, 0);
    }

    @Override
    public Frame effectiveFrame() {
        if(frame == null) {
            nvgSave(ctx);
            float[] bounds = new float[4];
            nvgTextAlign(ctx, NVG_ALIGN_TOP | NVG_ALIGN_LEFT);
            nvgFontSize(ctx, fontSize);
            nvgTextBounds(ctx, x, y, label, 0, bounds);
            float width = this.width != null ? this.width : bounds[2] - bounds[0];
            float height = this.height != null ? this.height : bounds[3] - bounds[1];
            frame = new Frame(bounds[0] - paddingRight, bounds[1] - paddingBottom , width + paddingLeft + paddingRight, height + paddingTop + paddingBottom);
            nvgRestore(ctx);
        }
        return frame;
    }

    public enum VerticalAlign {
        LEFT, CENTER, RIGHT
    }

    public enum HorizontalAlign {
        TOP, CENTER, BOTTOM
    }
}
