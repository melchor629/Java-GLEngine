package org.melchor629.engine.gui;

import org.melchor629.engine.gui.eventListeners.*;
import org.melchor629.engine.input.Keyboard;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Base class for any object renderable for the GUI
 */
public abstract class View {
    protected final long ctx;
    protected float x, y;
    protected float paddingLeft, paddingRight, paddingTop, paddingBottom;
    protected float borderRadiusTopLeft, borderRadiusTopRight, borderRadiusBottomLeft, borderRadiusBottomRight;
    protected float opacity = 1;
    protected Frame frame;
    protected Float width, height;
    protected Color backgroundColor = Color.transparent();
    protected Color color = Color.black();
    protected Image backgroundImage = null;
    protected Gradient backgroundGradient = null;
    protected boolean visible = true;

    private StateAnimation hoverState, clickedState, focusState;
    private boolean hoverStateNotInstalled, clickedStateNotInstalled, focusStateNotInstalled;
    private Map<String, Object> properties = new HashMap<>();
    private List<OnBlur> blurListeners = new ArrayList<>();
    private List<OnCharKey> charKeyListeners = new ArrayList<>();
    private List<OnFocus> focusListeners = new ArrayList<>();
    private List<OnKeyDown> keyDownListeners = new ArrayList<>();
    private List<OnKeyUp> keyUpListeners = new ArrayList<>();
    private List<OnMouseDown> mouseDownListeners = new ArrayList<>();
    private List<OnMouseEnter> mouseEnterListeners = new ArrayList<>();
    private List<OnMouseExit> mouseExitListeners = new ArrayList<>();
    private List<OnMouseMove> mouseMoveListeners = new ArrayList<>();
    private List<OnMouseUp> mouseUpListeners = new ArrayList<>();
    private Map<String, List<OnPropertyChange>> propertyChangeListeners = new TreeMap<>();

    boolean hover = false;
    boolean clicked = false;
    boolean focus = false;
    boolean dirty = true;

    View() {
        ctx = GUI.gui.nvgCtx;
    }

    public void x(float x) {
        opc("x", this.x, x);
        this.x = x;
        frame = null;
    }

    public void y(float y) {
        opc("y", this.y, y);
        this.y = y;
        frame = null;
    }

    public void width(Float width) {
        opc("width", this.width, width);
        this.width = width;
        frame = null;
    }

    public void height(Float height) {
        opc("height", this.height, height);
        this.height = height;
        frame = null;
    }

    public void position(float x, float y) {
        opc("x", this.x, x);
        opc("y", this.y, y);
        this.x = x;
        this.y = y;
        frame = null;
    }

    public void size(Float width, Float height) {
        opc("width", this.width, width);
        opc("height", this.height, height);
        this.width = width;
        this.height = height;
        frame = null;
    }

    public void padding(float padding) {
        opc("paddingLeft", this.paddingLeft, padding);
        opc("paddingRight", this.paddingRight, padding);
        opc("paddingTop", this.paddingTop, padding);
        opc("paddingBottom", this.paddingBottom, padding);
        paddingLeft = paddingRight = paddingTop = paddingBottom = padding;
        frame = null;
    }

    public void padding(float topBotPadding, float leftRightPadding) {
        opc("paddingLeft", this.paddingLeft, leftRightPadding);
        opc("paddingRight", this.paddingRight, leftRightPadding);
        opc("paddingTop", this.paddingTop, topBotPadding);
        opc("paddingBottom", this.paddingBottom, topBotPadding);
        paddingLeft = paddingRight = leftRightPadding;
        paddingTop = paddingBottom = topBotPadding;
        frame = null;
    }

    public void padding(float topPadding, float leftRightPadding, float botPadding) {
        opc("paddingLeft", this.paddingLeft, leftRightPadding);
        opc("paddingRight", this.paddingRight, leftRightPadding);
        opc("paddingTop", this.paddingTop, topPadding);
        opc("paddingBottom", this.paddingBottom, botPadding);
        paddingLeft = paddingRight = leftRightPadding;
        paddingTop = topPadding;
        paddingBottom = botPadding;
        frame = null;
    }

    public void padding(float topPadding, float rightPadding, float botPadding, float leftPadding) {
        opc("paddingLeft", this.paddingLeft, leftPadding);
        opc("paddingRight", this.paddingRight, rightPadding);
        opc("paddingTop", this.paddingTop, topPadding);
        opc("paddingBottom", this.paddingBottom, botPadding);
        paddingLeft = leftPadding;
        paddingRight = rightPadding;
        paddingTop = topPadding;
        paddingBottom = botPadding;
        frame = null;
    }

    public void paddingLeft(float padding) {
        opc("paddingLeft", this.paddingLeft, padding);
        paddingLeft = padding;
        frame = null;
    }

    public void paddingRight(float padding) {
        opc("paddingRight", this.paddingRight, padding);
        paddingRight = padding;
        frame = null;
    }

    public void paddingTop(float padding) {
        opc("paddingTop", this.paddingTop, padding);
        paddingTop = padding;
        frame = null;
    }

    public void paddingBottom(float padding) {
        opc("paddingBottom", this.paddingBottom, padding);
        paddingBottom = padding;
        frame = null;
    }

    public void borderRadius(float borderRadius) {
        opc("borderRadiusTopLeft", this.borderRadiusTopLeft, borderRadius);
        opc("borderRadiusTopRight", this.borderRadiusTopRight, borderRadius);
        opc("borderRadiusBottomLeft", this.borderRadiusBottomLeft, borderRadius);
        opc("borderRadiusBottomRight", this.borderRadiusBottomRight, borderRadius);
        borderRadiusTopLeft = borderRadiusTopRight = borderRadiusBottomLeft = borderRadiusBottomRight = borderRadius;
        frame = null;
    }

    public void borderRadius(float a, float b) {
        opc("borderRadiusTopLeft", this.borderRadiusTopLeft, a);
        opc("borderRadiusTopRight", this.borderRadiusTopRight, b);
        opc("borderRadiusBottomLeft", this.borderRadiusBottomLeft, b);
        opc("borderRadiusBottomRight", this.borderRadiusBottomRight, a);
        borderRadiusTopLeft = borderRadiusBottomRight = a;
        borderRadiusTopRight = borderRadiusBottomLeft = b;
    }

    public void borderRadius(float topLeft, float b, float bottomRight) {
        opc("borderRadiusTopLeft", this.borderRadiusTopLeft, topLeft);
        opc("borderRadiusTopRight", this.borderRadiusTopRight, b);
        opc("borderRadiusBottomLeft", this.borderRadiusBottomLeft, b);
        opc("borderRadiusBottomRight", this.borderRadiusBottomRight, bottomRight);
        borderRadiusTopLeft = topLeft;
        borderRadiusBottomRight = bottomRight;
        borderRadiusTopRight = borderRadiusBottomLeft = b;
    }

    public void borderRadius(float topLeft, float topRight, float bottomRight, float bottomLeft) {
        opc("borderRadiusTopLeft", this.borderRadiusTopLeft, topLeft);
        opc("borderRadiusTopRight", this.borderRadiusTopRight, topRight);
        opc("borderRadiusBottomLeft", this.borderRadiusBottomLeft, bottomLeft);
        opc("borderRadiusBottomRight", this.borderRadiusBottomRight, bottomRight);
        borderRadiusTopLeft = topLeft;
        borderRadiusBottomRight = bottomRight;
        borderRadiusTopRight = topRight;
        borderRadiusBottomLeft = bottomLeft;
    }

    public void borderRadiusTopLeft(float borderRadius) {
        opc("borderRadiusTopLeft", this.borderRadiusTopLeft, borderRadius);
        borderRadiusTopLeft = borderRadius;
        frame = null;
    }

    public void borderRadiusTopRight(float borderRadius) {
        opc("borderRadiusTopRight", this.borderRadiusTopRight, borderRadius);
        borderRadiusTopRight = borderRadius;
        frame = null;
    }

    public void borderRadiusBottomLeft(float borderRadius) {
        opc("borderRadiusBottomLeft", this.borderRadiusBottomLeft, borderRadius);
        borderRadiusBottomLeft = borderRadius;
        frame = null;
    }

    public void borderRadiusBottomRight(float borderRadius) {
        opc("borderRadiusBottomRight", this.borderRadiusBottomRight, borderRadius);
        borderRadiusBottomRight = borderRadius;
        frame = null;
    }

    public void opacity(float opacity) {
        opc("opacity", this.opacity, opacity);
        this.opacity = opacity;
    }

    public void backgroundColor(Color bgcolor) {
        opc("backgroundColor", this.backgroundColor, bgcolor);
        backgroundColor = bgcolor;
    }

    public void color(Color color) {
        opc("color", this.color, color);
        this.color = color;
    }

    public void backgroundImage(Image image) {
        opc("backgroundImage", this.backgroundImage, image);
        this.backgroundImage = image;
        this.backgroundGradient = null;
    }

    public void backgroundImage(Gradient gradient) {
        opc("backgroundImage", this.backgroundImage, gradient);
        this.backgroundImage = null;
        this.backgroundGradient = gradient;
    }

    public void visible(boolean visible) {
        opc("visible", this.visible, visible);
        this.visible = visible;
    }

    public void addEventListener(OnBlur listener) {
        blurListeners.add(listener);
    }

    public void addEventListener(OnCharKey listener) {
        charKeyListeners.add(listener);
    }

    public void addEventListener(OnFocus listener) {
        focusListeners.add(listener);
    }

    public void addEventListener(OnKeyDown listener) {
        keyDownListeners.add(listener);
    }

    public void addEventListener(OnKeyUp listener) {
        keyUpListeners.add(listener);
    }

    public void addEventListener(OnMouseDown listener) {
        mouseDownListeners.add(listener);
    }

    public void addEventListener(OnMouseEnter listener) {
        mouseEnterListeners.add(listener);
    }

    public void addEventListener(OnMouseExit listener) {
        mouseExitListeners.add(listener);
    }

    public void addEventListener(OnMouseMove listener) {
        mouseMoveListeners.add(listener);
    }

    public void addEventListener(OnMouseUp listener) {
        mouseUpListeners.add(listener);
    }

    public void addPropertyObserver(String name, OnPropertyChange listener) {
        getProperty(name);
        if(!propertyChangeListeners.containsKey(name)) {
            propertyChangeListeners.put(name, new ArrayList<>());
        }
        propertyChangeListeners.get(name).add(listener);
    }

    public boolean removeEventListener(OnBlur listener) {
        return blurListeners.remove(listener);
    }

    public boolean removeEventListener(OnCharKey listener) {
        return charKeyListeners.remove(listener);
    }

    public boolean removeEventListener(OnFocus listener) {
        return focusListeners.remove(listener);
    }

    public boolean removeEventListener(OnKeyDown listener) {
        return keyDownListeners.remove(listener);
    }

    public boolean removeEventListener(OnKeyUp listener) {
        return keyUpListeners.remove(listener);
    }

    public boolean removeEventListener(OnMouseDown listener) {
        return mouseDownListeners.remove(listener);
    }

    public boolean removeEventListener(OnMouseEnter listener) {
        return mouseEnterListeners.remove(listener);
    }

    public boolean removeEventListener(OnMouseExit listener) {
        return mouseExitListeners.remove(listener);
    }

    public boolean removeEventListener(OnMouseMove listener) {
        return mouseMoveListeners.remove(listener);
    }

    public boolean removeEventListener(OnMouseUp listener) {
        return mouseUpListeners.remove(listener);
    }

    public boolean removePropertyObserver(String name, OnPropertyChange listener) {
        getProperty(name);
        return propertyChangeListeners.containsKey(name) && propertyChangeListeners.get(name).remove(listener);
    }

    public void setHoverAnimation(StateAnimation anim) {
        hoverState = anim;
        hoverStateNotInstalled = true;
    }

    public void setClickedAnimation(StateAnimation anim) {
        clickedState = anim;
        clickedStateNotInstalled = true;
    }

    public void setFocusAnimation(StateAnimation anim) {
        focusState = anim;
        focusStateNotInstalled = true;
        if(focusState != null) focusState.installOnView(this);
    }

    public final void draw() {
        if(!GUI.gui.isRenderingGUI) throw new IllegalStateException("Cannot draw object if not rendering the GUI");
        if(frame == null) effectiveFrame();
        if(visible) {
            nvgGlobalAlpha(ctx, opacity);
            paintBackground();
            paint();
            nvgGlobalAlpha(ctx, 1);
        }
        dirty = false;
    }

    /**
     * Changes the value of a property in the view object
     * @param name Name of the property
     * @param property Value for the property
     * @param <T> Type of the value
     */
    public final <T> void setProperty(String name, T property) {
        Class<?> clazz = this.getClass();
        boolean doneSetting = false;
        while(!clazz.equals(Object.class) && !doneSetting) {
            try {
                Object old = clazz.getDeclaredMethod(name).invoke(this);
                clazz.getDeclaredMethod(name, property.getClass()).invoke(this, property);
                doneSetting = true;
                opc(name, old, property);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NullPointerException ignore2) {
                try {
                    Object old = clazz.getDeclaredField(name).get(this);
                    clazz.getDeclaredField(name).set(this, property);
                    doneSetting = true;
                    opc(name, old, property);
                } catch (IllegalAccessException | NoSuchFieldException ignore) { }
            }
            clazz = clazz.getSuperclass();
        }
        if(!doneSetting) throw new NoSuchFieldError("No property called '" + name + "'");
    }

    /**
     * Gets a property value of the view object
     * @param name Name of the property
     * @param <T> Type of the value
     * @return The value of null if doesn't exists
     */
    @SuppressWarnings("unchecked")
    public final <T> T getProperty(String name) {
        Class<?> clazz = this.getClass();
        while(!clazz.equals(Object.class)) {
            try {
                return (T) clazz.getDeclaredField(name).get(this);
            } catch (IllegalAccessException | NoSuchFieldException ignore) {
                try {
                    return (T) clazz.getDeclaredMethod(name).invoke(this);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | NullPointerException ignore2) {}
            }
            clazz = clazz.getSuperclass();
        }
        throw new NoSuchFieldError("No property called '" + name + "'");
    }

    protected abstract void paint();

    public abstract Frame effectiveFrame();

    private void paintBackground() {
        nvgSave(ctx);
        nvgTranslate(ctx, frame.x, frame.y);
        if(backgroundImage != null) {
            backgroundImage.setAsFillPaint();
        } else if(backgroundGradient != null) {
            backgroundGradient.setAsFillPaint();
        } else {
            backgroundColor.setAsFillColor();
        }
        GUIDrawUtils.drawRoundedRectangle(0, 0, frame.width, frame.height, borderRadiusTopLeft, borderRadiusTopRight, borderRadiusBottomLeft, borderRadiusBottomRight);
        nvgFill(ctx);
        nvgRestore(ctx);
    }

    protected void opc(String name, Object old, Object new_) {
        if(old != null && old != new_ && !old.equals(new_)) {
            if(propertyChangeListeners.containsKey(name))
                propertyChangeListeners.get(name).forEach(p -> p.propertyChanged(name, old, new_));
            markDirty();
        }
    }

    protected void markDirty() {
        dirty = true;
        GUI.gui.dirty = true;
    }

    protected void onMouseDown(MouseEvent e) {
        clicked = true;
        mouseDownListeners.forEach(v -> v.mousePressed(e));
        if(clickedState != null && clickedStateNotInstalled) {
            clickedState.installOnView(this);
            clickedStateNotInstalled = false;
        }
        if(clickedState != null)
            clickedState.activate();
        if(!focus)
            onFocus();
    }

    protected void onMouseUp(MouseEvent e) {
        clicked = false;
        mouseUpListeners.forEach(v -> v.mouseReleased(e));
        if(clickedState != null)
            clickedState.deactivate();
    }

    protected void onMouseEnter(MouseEvent e) {
        hover = true;
        mouseEnterListeners.forEach(v -> v.mouseEntered(e));
        if(hoverState != null) {
            if(hoverStateNotInstalled) {
                hoverState.installOnView(this);
                hoverStateNotInstalled = true;
            }
            hoverState.activate();
        }
    }

    protected void onMouseExit(MouseEvent e) {
        hover = false;
        mouseExitListeners.forEach(v -> v.mouseExited(e));
        if(hoverState != null)
            hoverState.deactivate();
    }

    protected void onMouseMove(MouseEvent e) {
        mouseMoveListeners.forEach(v -> v.mouseMoved(e));
        if(!hover) {
            onMouseEnter(e);
        }
    }

    protected void onWheelMove(MouseEvent e) {}

    protected void onFocus() {
        focus = true;
        focusListeners.forEach(OnFocus::gainedFocus);
    }

    protected void onBlur() {
        focus = false;
        blurListeners.forEach(OnBlur::lostFocus);
    }

    protected void onKeyDown(Keyboard keyboard, int key) {
        keyDownListeners.forEach(v -> v.keyPressed(keyboard, key));
    }

    protected void onKeyUp(Keyboard keyboard, int key) {
        keyUpListeners.forEach(v -> v.keyReleased(keyboard, key));
    }

    protected void onCharKey(Keyboard keyboard, String ch) {
        charKeyListeners.forEach(v -> v.characterIntroduced(keyboard, ch));
    }

    boolean isInside(float px, float py) {
        Frame f = effectiveFrame();
        return f.x <= px && px <= f.x + f.width && f.y <= py && py <= f.y + f.height;
    }
}
