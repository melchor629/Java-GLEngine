package org.melchor629.engine.gui;

import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * GUI button
 */
public class Button extends TextLabel {
    protected boolean disabled;

    public Button(String label) {
        super(label);
        backgroundColor = Color.hex("#808080").alpha(0.7f);
        color = Color.black();
        borderRadius(5);
        setHoverAnimation(new StateAnimation(0.2, new Animation.Property<>("backgroundColor", Color.rgb(0.8, 0.8, 0.8))));
        setClickedAnimation(new StateAnimation(0.2,
                new Animation.Property<>("backgroundColor", Color.rgb(0.7, 0.7, 0.7)),
                new Animation.Property<Object>("color", Color.rgb(0.1, 0.1, 0.1))));
    }

    public void disabled(boolean disabled) {
        opc("disabled", this.disabled, disabled);
        this.disabled = disabled;
    }

    @Override
    protected void onMouseDown(MouseEvent e) {
        if(!disabled) {
            super.onMouseDown(e);
        }
    }

    @Override
    protected void onMouseMove(MouseEvent e) {
        if(!disabled) {
            super.onMouseMove(e);
        }
    }
}
