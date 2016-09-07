package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.melchor629.engine.input.Keyboard;

import java.util.*;
import java.util.stream.*;

/**
 * View that contains other views (as subviews)
 */
public class Container extends View {
    protected List<View> subViews;

    /**
     * Creates a container view with the size and position
     * specified on {@code frame}, 0 padding and transparent
     * background.
     * @param frame size and position
     * @see Frame
     */
    public Container(Frame frame) {
        super();
        subViews = new ArrayList<>();
        this.frame = frame;
        x = frame.x;
        y = frame.y;
        width = frame.width;
        height = frame.height;
        backgroundColor = Color.transparent();
    }

    @Override
    protected void paint() {
        NVGColor c = NVGColor.calloc();

        NanoVG.nvgSave(ctx);
        NanoVG.nvgIntersectScissor(ctx, frame.x, frame.y, frame.width, frame.height);
        NanoVG.nvgTranslate(ctx, x + paddingLeft, y + paddingTop);
        subViews.forEach(v -> {
            NanoVG.nvgSave(ctx);
            v.draw();
            NanoVG.nvgRestore(ctx);
        });
        NanoVG.nvgRestore(ctx);

        /*Color.rgb(1, 0.6, 0.8).convert(c);
        NanoVG.nvgStrokeColor(ctx, c);
        NanoVG.nvgStrokeWidth(ctx, 2);
        NanoVG.nvgBeginPath(ctx);
        NanoVG.nvgRect(ctx, frame.x, frame.y, frame.width, frame.height);
        NanoVG.nvgStroke(ctx);*/
        c.free();
    }

    @Override
    public void width(Float width) {
        if(width == null) throw new NullPointerException("Width cannot be null");
        super.width(width);
    }

    @Override
    public void height(Float height) {
        if(height == null) throw new NullPointerException("Height cannot be null");
        super.height(height);
    }

    public void addSubview(View subview) {
        subViews.add(subview);
    }

    public boolean removeSubview(View subView) {
        return subViews.remove(subView);
    }

    public View getSubview(int i) {
        if(i >= 0 && i < subViews.size()) {
            return subViews.get(i);
        } else if(i >= 0) {
            throw new IndexOutOfBoundsException("Index exceeds the total number of subviews");
        } else {
            throw new IndexOutOfBoundsException("Negative " + i + " index is not valid");
        }
    }

    public int subViewsCount() {
        return subViews.size();
    }

    @Override
    public Frame effectiveFrame() {
        if(frame == null) {
            frame = new Frame(x, y, width, height);
        }
        return frame;
    }

    @Override
    protected void onMouseMove(MouseEvent m) {
        super.onMouseMove(m);
        if(visible) {
            View[] lastView = new View[1];
            last(subViews.stream()
                    .filter(v -> v.visible)
                    .filter(v -> v.isInside(m.getX() - paddingLeft, m.getY() - paddingTop)))
                    .forEach(v -> {
                        v.onMouseMove(new MouseEvent(m, m.getX() - v.effectiveFrame().x - paddingLeft, m.getY() - v.effectiveFrame().y - paddingTop));
                        lastView[0] = v;
                    });
            subViews.stream()
                    .filter(v -> v.visible && v.hover)
                    .filter(v -> !v.isInside(m.getX() - paddingLeft, m.getY() - paddingTop) || lastView[0] == null || !lastView[0].equals(v))
                    .forEach(v -> v.onMouseExit(new MouseEvent(m, m.getX() - v.effectiveFrame().x - paddingLeft, m.getY() - v.effectiveFrame().y - paddingTop)));
        }
    }

    @Override
    protected void onMouseDown(MouseEvent m) {
        super.onMouseDown(m);
        if(visible) {
            last(subViews.stream()
                    .filter(v -> v.visible)
                    .filter(v -> v.isInside(m.getX() - paddingLeft, m.getY() - paddingTop)))
                    .forEach(v -> v.onMouseDown(new MouseEvent(m, m.getX() - v.effectiveFrame().x - paddingLeft, m.getY() - v.effectiveFrame().y - paddingTop)));
            subViews.stream()
                    .filter(v -> v.visible && v.focus)
                    .filter(v -> !v.isInside(m.getX() - paddingLeft, m.getY() - paddingTop))
                    .forEach(View::onBlur);
        }
    }

    @Override
    protected void onMouseUp(MouseEvent m) {
        super.onMouseUp(m);
        if(visible) {
            subViews.stream()
                    .filter(v -> v.visible && v.clicked)
                    .forEach(v -> v.onMouseUp(new MouseEvent(m, m.getX() - v.effectiveFrame().x - paddingLeft, m.getY() - v.effectiveFrame().y - paddingTop)));
        }
    }

    @Override
    protected void onMouseExit(MouseEvent m) {
        super.onMouseExit(m);
        if(visible) {
            subViews.stream()
                    .filter(v -> v.hover)
                    .forEach(v -> v.onMouseExit(new MouseEvent(m, m.getX() - v.effectiveFrame().x - paddingLeft, m.getY() - v.effectiveFrame().y - paddingTop)));
        }
    }

    @Override
    protected void onWheelMove(MouseEvent m) {
        super.onWheelMove(m);
        if(visible) {
            last(subViews.stream()
                    .filter(v -> v.visible && v.hover))
                    .forEach(v -> v.onWheelMove(new MouseEvent(m, m.getX() - v.effectiveFrame().x - paddingLeft, m.getY() - v.effectiveFrame().y - paddingTop)));
        }
    }

    @Override
    protected void onKeyDown(Keyboard k, int key) {
        super.onKeyDown(k, key);
        if(visible) {
            subViews.stream()
                    .filter(v -> v.visible && v.focus)
                    .forEach(v -> v.onKeyDown(k, key));
        }
    }

    @Override
    protected void onKeyUp(Keyboard k, int key) {
        super.onKeyDown(k, key);
        if(visible) {
            subViews.stream()
                    .filter(v -> v.visible && v.focus)
                    .forEach(v -> v.onKeyUp(k, key));
        }
    }

    @Override
    protected void onCharKey(Keyboard k, String r) {
        super.onCharKey(k, r);
        if(visible) {
            subViews.stream()
                    .filter(v -> v.visible && v.focus)
                    .forEach(v -> v.onCharKey(k, r));
        }
    }

    @Override
    protected void onBlur() {
        super.onBlur();
        if(visible) {
            subViews.stream()
                    .filter(v -> v.focus)
                    .forEach(View::onBlur);
        }
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> last(Stream<T> stream) {
        Object[] o = new Object[1];
        stream.forEach(t -> o[0] = t);
        if(o[0] != null) {
            return Collections.singletonList((T) o[0]);
        } else {
            return Collections.emptyList();
        }
    }
}
