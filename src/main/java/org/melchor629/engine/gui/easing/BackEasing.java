package org.melchor629.engine.gui.easing;

/**
 * Back easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class BackEasing extends Easing {
    public static final Easing easing = new BackEasing();

    private BackEasing() {}

    //There's a variation functions that the variable s is passed as an argument
    //but this versions are not available in this API

    @Override
    public float easeIn(float t, float b, float c, float d) {
        float s = 1.70158f;
        return c*(t/=d)*t*((s+1)*t - s) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        float s = 1.70158f;
        return c*((t=t/d-1)*t*((s+1)*t + s) + 1) + b;
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        float s = 1.70158f;
        if ((t/=d/2) < 1) return c/2*(t*t*(((s*=(1.525f))+1)*t - s)) + b;
        return c/2*((t-=2)*t*(((s*=(1.525f))+1)*t + s) + 2) + b;
    }
}
