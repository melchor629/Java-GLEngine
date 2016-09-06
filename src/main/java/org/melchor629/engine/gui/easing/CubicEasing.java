package org.melchor629.engine.gui.easing;

/**
 * Cubic easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class CubicEasing extends Easing {
    public static final Easing easing = new CubicEasing();

    private CubicEasing() {}

    @Override
    public float easeIn(float t, float b, float c, float d) {
        return c*(t/=d)*t*t + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return c*((t=t/d-1)*t*t + 1) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        if ((t/=d/2) < 1) return c/2*t*t*t + b;
        return c/2*((t-=2)*t*t + 2) + b;
    }
}
