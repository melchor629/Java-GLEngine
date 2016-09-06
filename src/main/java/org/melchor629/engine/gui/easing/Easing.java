package org.melchor629.engine.gui.easing;

/**
 * Base class for easing animations.<br>
 * Available easing functions, provided by this API:
 * <ul>
 *     <li>{@link LinearEasing}</li>
 *     <li>{@link QuadEasing}</li>
 *     <li>{@link CubicEasing}</li>
 *     <li>{@link QuartEasing}</li>
 *     <li>{@link QuintEasing}</li>
 *     <li>{@link SineEasing}</li>
 *     <li>{@link ExpoEasing}</li>
 *     <li>{@link ElasticEasing}</li>
 *     <li>{@link CircEasing}</li>
 *     <li>{@link BounceEasing}</li>
 *     <li>{@link BackEasing}</li>
 * </ul>
 * @see <a href="http://upshots.org/actionscript/jsas-understanding-easing">Understanding easing</a>
 * @see <a href="https://github.com/jesusgollonet/processing-penner-easing/tree/master/src">Where this code is based on</a>
 * @see <a href="http://easings.net/">Easing functions in action</a>
 */
public abstract class Easing {

    public enum Type {
        NONE, IN, OUT, IN_OUT
    }

    /**
     * @param t the current time/position in any kind of unit
     * @param b the initial value of the property
     * @param c the difference from the initial and the end value of the property
     * @param d the total time/position in any kind of unit
     * @return value of the property for the time/position {@code t}
     */
    public float ease(float t, float b, float c, float d) {
        return b;
    }

    /**
     * @param t the current time/position in any kind of unit
     * @param b the initial value of the property
     * @param c the difference from the initial and the end value of the property
     * @param d the total time/position in any kind of unit
     * @return value of the property for the time/position {@code t}
     */
    public abstract float easeIn(float t, float b, float c, float d);

    /**
     * @param t the current time/position in any kind of unit
     * @param b the initial value of the property
     * @param c the difference from the initial and the end value of the property
     * @param d the total time/position in any kind of unit
     * @return value of the property for the time/position {@code t}
     */
    public abstract float easeOut(float t, float b, float c, float d);

    /**
     * @param t the current time/position in any kind of unit
     * @param b the initial value of the property
     * @param c the difference from the initial and the end value of the property
     * @param d the total time/position in any kind of unit
     * @return value of the property for the time/position {@code t}
     */
    public abstract float easeInOut(float t, float b, float c, float d);

}
