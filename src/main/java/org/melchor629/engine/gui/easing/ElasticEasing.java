package org.melchor629.engine.gui.easing;

/**
 * Elastic easing.<br>
 * <a href="https://github.com/jesusgollonet/processing-penner-easing/raw/master/src/easing_terms_of_use.html">See License</a>
 */
public class ElasticEasing extends Easing {
    public static final Easing easing = new ElasticEasing();

    private ElasticEasing() {}

    //There's a variation functions that the variables a & p are passed as an argument
    //but this versions are not available in this API

    @Override
    public float easeIn(float t, float b, float c, float d) {
        if (t==0) return b;  if ((t/=d)==1) return b+c;
        float p=d*.3f;
        float a=c;
        float s=p/4;
        return -(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )) + b;
    }

    @Override
    public float easeOut(float t, float b, float c, float d) {
        if (t==0) return b;  if ((t/=d)==1) return b+c;
        float p=d*.3f;
        float a=c;
        float s=p/4;
        return (a*(float)Math.pow(2,-10*t) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p ) + c + b);
    }

    @Override
    public float easeInOut(float t, float b, float c, float d) {
        if (t==0) return b;  if ((t/=d/2)==2) return b+c;
        float p=d*(.3f*1.5f);
        float a=c;
        float s=p/4;
        if (t < 1) return -.5f*(a*(float)Math.pow(2,10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )) + b;
        return a*(float)Math.pow(2,-10*(t-=1)) * (float)Math.sin( (t*d-s)*(2*(float)Math.PI)/p )*.5f + c + b;
    }
}
