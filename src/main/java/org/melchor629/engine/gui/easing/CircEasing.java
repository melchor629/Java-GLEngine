package org.melchor629.engine.gui.easing;

/**
 * Circ easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class CircEasing extends Easing {
    public static final Easing easing = new CircEasing();

    private CircEasing() {}

    @Override
    public float easeIn(float t, float b, float c, float d) {
        return -c * ((float)Math.sqrt(1 - (t/=d)*t) - 1) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return c * (float)Math.sqrt(1 - (t=t/d-1)*t) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        if ((t/=d/2) < 1) return -c/2 * ((float)Math.sqrt(1 - t*t) - 1) + b;
        return c/2 * ((float)Math.sqrt(1 - (t-=2)*t) + 1) + b;
    }
}
