package org.melchor629.engine.gui.easing;

/**
 * Linear easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class LinearEasing extends Easing {
    public static final Easing easing = new LinearEasing();

    private LinearEasing() {}

    @Override
    public float ease(float t, float b, float c, float d) {
        return c*t/d+b;
    }

    @Override
    public float easeIn(float t, float b, float c, float d) {
        return c*t/d+b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return c*t/d+b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        return c*t/d+b;
    }
}
