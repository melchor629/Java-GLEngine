package org.melchor629.engine.gui.easing;

/**
 * Sine easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class SineEasing extends Easing {
    public static final Easing easing = new SineEasing();

    private SineEasing() {}

    @Override
    public float easeIn(float t, float b, float c, float d) {
        return -c * (float)Math.cos(t/d * (Math.PI/2)) + c + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        return c * (float)Math.sin(t/d * (Math.PI/2)) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        return -c/2 * ((float)Math.cos(Math.PI*t/d) - 1) + b;
    }
}
