package org.melchor629.engine.gui.easing;

/**
 * Quadratic easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class QuadEasing extends Easing {
    public static final Easing easing = new QuadEasing();

    private QuadEasing() {}

    @Override
    public float easeIn(float t, float b, float c, float d) {
        return c*(t/=d)*t + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return -c *(t/=d)*(t-2) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        if ((t/=d/2) < 1) return c/2*t*t + b;
        return -c/2 * ((--t)*(t-2) - 1) + b;
    }
}
