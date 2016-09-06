package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGColor;
import org.melchor629.engine.gui.eventListeners.OnMouseDown;
import org.melchor629.engine.gui.eventListeners.OnMouseUp;
import org.melchor629.engine.input.Keyboard;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Scrollable container
 */
public class ScrollContainer extends Container {
    private boolean showVerticalScroll = true, showHorizontalScroll = true;
    private float maxWidth, maxHeight;
    private float scrollTop, scrollLeft;
    private Container verticalScroll, horizontalScroll;
    private float scrollLeftDesp = 0, scrollTopDesp = 0;
    private long pressedButton = System.currentTimeMillis();
    private boolean verticalScrollGrabbed, horizontalScrollGrabbed;
    private boolean shift;

    /**
     * Creates a scrollable container view with the size and position
     * specified on {@code frame}, 0 padding and transparent
     * background.
     *
     * @param frame size and position
     * @see Frame
     */
    public ScrollContainer(Frame frame) {
        super(frame);

        horizontalScroll = new Container(new Frame(0, height - 15, width - 15, 15));
        verticalScroll = new Container(new Frame(width - 15, 0, 15, height - 15));

        Button left = new Button("<"); //◀
        Button right = new Button(">");//▶
        Button top = new Button("∧");
        Button bottom = new Button("∨");

        left.x(1);
        right.position(width - 30 + 1, 0);
        top.x(1);
        bottom.position(1, height - 30);

        left.disabled = true;
        top.disabled = true;

        left.size(15f, 15f);
        right.size(15f, 15f);
        top.size(15f, 15f);
        bottom.size(15f, 15f);

        left.borderRadius(0);
        right.borderRadius(0);
        top.borderRadius(0);
        bottom.borderRadius(0);

        left.textAlign(TextLabel.VerticalAlign.CENTER, TextLabel.HorizontalAlign.CENTER);
        right.textAlign(TextLabel.VerticalAlign.CENTER, TextLabel.HorizontalAlign.CENTER);
        top.textAlign(TextLabel.VerticalAlign.CENTER, TextLabel.HorizontalAlign.CENTER);
        bottom.textAlign(TextLabel.VerticalAlign.CENTER, TextLabel.HorizontalAlign.CENTER);

        left.addEventListener((OnMouseDown) this::leftButtonDown);
        right.addEventListener((OnMouseDown) this::rightButtonDown);
        top.addEventListener((OnMouseDown) this::topButtonDown);
        bottom.addEventListener((OnMouseDown) this::bottomButtonDown);

        left.addEventListener((OnMouseUp) this::somethingUp);
        right.addEventListener((OnMouseUp) this::somethingUp);
        top.addEventListener((OnMouseUp) this::somethingUp);
        bottom.addEventListener((OnMouseUp) this::somethingUp);

        horizontalScroll.addSubview(left);
        horizontalScroll.addSubview(right);
        verticalScroll.addSubview(top);
        verticalScroll.addSubview(bottom);

        horizontalScroll.visible = verticalScroll.visible = false;
    }

    @Override
    protected void paint() {
        super.paint();

        NVGColor c = NVGColor.malloc();
        nvgTranslate(ctx, x, y);
        if(verticalScroll.visible) {
            Color.hex("#808080B2").convert(c);
            nvgFillColor(ctx, c);
            GUIDrawUtils.drawRectangle(width - 15, 15, 15, height - 45);

            float yque = height / maxHeight;
            float yquo = scrollTop / (maxHeight - height) * (height - 45) * (1 - yque);
            Color.lightGrey().convert(c);
            nvgFillColor(ctx, c);
            GUIDrawUtils.drawRectangle(width - 15, 15 + yquo, 15, (height - 45) * yque);

            verticalScroll.draw();
        }

        if(horizontalScroll.visible) {
            Color.hex("#808080B2").convert(c);
            nvgFillColor(ctx, c);
            GUIDrawUtils.drawRectangle(15, height - 15, width - 45, 15);

            float xque = width / maxWidth;
            float xquo = scrollLeft / (maxWidth - width) * (width - 45) * (1 - xque);
            Color.lightGrey().convert(c);
            nvgFillColor(ctx, c);
            GUIDrawUtils.drawRectangle(15 + xquo, height - 15, (width - 45) * xque, 15);

            horizontalScroll.draw();
        }

        checkScrolls();
        c.free();
    }

    @Override
    public void width(Float width) {
        super.width(width);
        checkScrolls();
    }

    @Override
    public void height(Float height) {
        super.height(height);
        checkScrolls();
    }

    @Override
    public boolean removeSubview(View subview) {
        boolean in = super.removeSubview(subview);

        if(in) {
            Frame subframe = subview.effectiveFrame();
            if(subframe.x + subframe.width == maxWidth || subframe.y + subframe.height == maxHeight) {
                checkScrolls();
            }
        }

        return in;
    }

    @Override
    protected void onMouseMove(MouseEvent e) {
        super.onMouseMove(e);
        if(verticalScroll.visible && verticalScroll.isInside(e.getX(), e.getY())) {
            verticalScroll.onMouseMove(new MouseEvent(e, e.getX() - verticalScroll.effectiveFrame().x, e.getY() - verticalScroll.effectiveFrame().y));
        } else if(verticalScroll.hover && !verticalScroll.isInside(e.getX(), e.getY())) {
            verticalScroll.onMouseExit(new MouseEvent(e, e.getX() - verticalScroll.effectiveFrame().x, e.getY() - verticalScroll.effectiveFrame().y));
        }

        if(horizontalScroll.visible && horizontalScroll.isInside(e.getX(), e.getY())) {
            horizontalScroll.onMouseMove(new MouseEvent(e, e.getX() - horizontalScroll.effectiveFrame().x, e.getY() - horizontalScroll.effectiveFrame().y));
        } else if(horizontalScroll.hover && !horizontalScroll.isInside(e.getX(), e.getY())) {
            horizontalScroll.onMouseExit(new MouseEvent(e, e.getX() - horizontalScroll.effectiveFrame().x, e.getY() - horizontalScroll.effectiveFrame().y));
        }

        if(horizontalScrollGrabbed) {
            scrollLeft(scrollLeft + e.getXMovement());
        } else if(verticalScrollGrabbed) {
            scrollTop(scrollTop + e.getYMovement());
        }
    }

    @Override
    protected void onMouseExit(MouseEvent e) {
        super.onMouseExit(e);
        if(verticalScroll.visible && verticalScroll.hover) {
            verticalScroll.onMouseExit(new MouseEvent(e, e.getX() - verticalScroll.effectiveFrame().x, e.getY() - verticalScroll.effectiveFrame().y));
        }

        if(horizontalScroll.visible && horizontalScroll.hover) {
            horizontalScroll.onMouseExit(new MouseEvent(e, e.getX() - horizontalScroll.effectiveFrame().x, e.getY() - horizontalScroll.effectiveFrame().y));
        }
    }

    @Override
    protected void onMouseDown(MouseEvent e) {
        super.onMouseDown(e);
        if(verticalScroll.visible && verticalScroll.isInside(e.getX(), e.getY())) {
            verticalScroll.onMouseDown(new MouseEvent(e, e.getX() - verticalScroll.effectiveFrame().x, e.getY() - verticalScroll.effectiveFrame().y));
            verticalScrollGrabbed = true;
        }

        if(horizontalScroll.visible && horizontalScroll.isInside(e.getX(), e.getY())) {
            horizontalScroll.onMouseDown(new MouseEvent(e, e.getX() - horizontalScroll.effectiveFrame().x, e.getY() - horizontalScroll.effectiveFrame().y));
            horizontalScrollGrabbed = true;
        }
    }

    @Override
    protected void onMouseUp(MouseEvent e) {
        super.onMouseUp(e);
        if(verticalScroll.visible && verticalScroll.clicked) {
            verticalScroll.onMouseUp(new MouseEvent(e, e.getX() - verticalScroll.effectiveFrame().x, e.getY() - verticalScroll.effectiveFrame().y));
            verticalScrollGrabbed = false;
        }

        if(horizontalScroll.visible && horizontalScroll.clicked) {
            horizontalScroll.onMouseUp(new MouseEvent(e, e.getX() - horizontalScroll.effectiveFrame().x, e.getY() - horizontalScroll.effectiveFrame().y));
            horizontalScrollGrabbed = false;
        }
    }

    @Override
    protected void onWheelMove(MouseEvent e) {
        super.onWheelMove(e);
        scrollLeft(scrollLeft + e.getHorizontalWheelMovement());
        if(shift) {
            scrollLeft(scrollLeft + e.getWheelMovement());
        } else {
            scrollTop(scrollTop + e.getWheelMovement());
        }
    }

    @Override
    protected void onKeyDown(Keyboard k, int key) {
        super.onKeyDown(k, key);
        shift = k.isKeyPressed("LEFT_SHIFT") || k.isKeyPressed("RIGHT_SHIFT");
    }

    @Override
    protected void onKeyUp(Keyboard k, int key) {
        super.onKeyUp(k, key);
        shift = k.isKeyPressed("LEFT_SHIFT") || k.isKeyPressed("RIGHT_SHIFT");
    }

    private void checkScrolls() {
        maxWidth = maxHeight = 0;
        horizontalScroll.visible(false);
        verticalScroll.visible(false);
        subViews.forEach(subView -> {
            Frame subFrame = subView.effectiveFrame();
            if(subFrame.x + subFrame.width > width && showHorizontalScroll) {
                horizontalScroll.visible(true);
                maxWidth = Math.max(maxWidth, subFrame.x + subFrame.width);
            }
            if(subFrame.y + subFrame.height > height && showVerticalScroll) {
                verticalScroll.visible(true);
                maxHeight = Math.max(maxHeight, subFrame.y + subFrame.height);
            }
        });

        if(horizontalScroll.visible) {
            maxHeight += 15;
        }

        if(verticalScroll.visible) {
            maxWidth += 15;
        }

        if(System.currentTimeMillis() - pressedButton >= 500 && (System.currentTimeMillis() / 200) % 2 == 0) {
            if(scrollLeftDesp != 0) {
                scrollLeft(scrollLeft + scrollLeftDesp);
            } else if(scrollTopDesp != 0) {
                scrollTop(scrollTop + scrollTopDesp);
            }
        }

        if(scrollTop + height > maxHeight) {
            scrollTop(maxHeight - height);
            scrollTopDesp = 0;
        } else if(scrollTop < 0) {
            scrollTop(0);
            scrollTopDesp = 0;
        }

        if(scrollLeft + width > maxWidth) {
            scrollLeft(maxWidth - width);
            scrollLeftDesp = 0;
        } else if(scrollLeft < 0) {
            scrollLeft(0);
            scrollLeftDesp = 0;
        }

        if(scrollTop == 0) {
            ((Button) verticalScroll.getSubview(0)).disabled = true;
        } else if(scrollTop == maxHeight - height) {
            ((Button) verticalScroll.getSubview(1)).disabled = true;
        } else {
            ((Button) verticalScroll.getSubview(0)).disabled = false;
            ((Button) verticalScroll.getSubview(1)).disabled = false;
        }

        if(scrollLeft == 0) {
            ((Button) horizontalScroll.getSubview(0)).disabled = true;
        } else if(scrollLeft == maxWidth - width) {
            ((Button) horizontalScroll.getSubview(1)).disabled = true;
        } else {
            ((Button) horizontalScroll.getSubview(0)).disabled = false;
            ((Button) horizontalScroll.getSubview(1)).disabled = false;
        }
    }

    private void leftButtonDown(MouseEvent e) {
        scrollLeft(Math.max(0, scrollLeft - 5));
        scrollLeftDesp = -5;
        pressedButton = System.currentTimeMillis();
    }

    private void rightButtonDown(MouseEvent e) {
        scrollLeft(Math.min(maxWidth - width, scrollLeft + 5));
        scrollLeftDesp = +5;
        pressedButton = System.currentTimeMillis();
    }

    private void bottomButtonDown(MouseEvent e) {
        scrollTop(Math.min(maxHeight - height, scrollTop + 5));
        scrollTopDesp = +5;
        pressedButton = System.currentTimeMillis();
    }

    private void topButtonDown(MouseEvent e) {
        scrollTop(Math.max(0, scrollTop - 5));
        scrollTopDesp = -5;
        pressedButton = System.currentTimeMillis();
    }

    private void somethingUp(MouseEvent e) {
        scrollTopDesp = scrollLeftDesp = 0;
    }

    private void scrollTop(float s) {
        scrollTop = s;
        paddingTop = -scrollTop;
    }

    private void scrollLeft(float s) {
        scrollLeft = s;
        paddingLeft = -scrollLeft;
    }
}
