package org.melchor629.engine.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.melchor629.engine.gui.Animation.Property;
import org.melchor629.engine.gui.easing.Easing;
import org.melchor629.engine.gui.easing.LinearEasing;

/**
 * Animations that are triggered when a view state is changed.
 * With state, I refer to hover, clicked and focus.
 * @see View
 */
public class StateAnimation implements Cloneable {
    private List<Property<?>> properties;
    private double duration, delay;
    private Easing easing = LinearEasing.easing;
    private Easing.Type easingType = Easing.Type.NONE;
    private Animation animationActivate, animationDeactivate, currentAnimation;

    public StateAnimation(double duration, Property<?>... properties) {
        this(duration, 0, properties);
    }

    public StateAnimation(double duration, Property<?> property) {
        this(duration, 0, property);
    }

    public StateAnimation(double duration, double delay, Property<?>... properties) {
        this.properties = new ArrayList<>(properties.length);
        Collections.addAll(this.properties, properties);
        this.duration = duration;
        this.delay = delay;
    }

    public StateAnimation(double duration, double delay, Property<?> property) {
        this.properties = new ArrayList<>(1);
        this.properties.add(property);
        this.duration = duration;
        this.delay = delay;
    }

    public StateAnimation setDelay(float delay) {
        this.delay = delay;
        return this;
    }

    public StateAnimation setEasing(Easing easing, Easing.Type type) {
        if(easing == null) throw new NullPointerException("Easing cannot be null");
        if(type == null) throw new NullPointerException("Easing type cannot be null");
        this.easing = easing;
        this.easingType = type;
        return this;
    }

    public StateAnimation chainAnimation(Animation a) {
        animationActivate.chainAnimation(a);
        return this;
    }

    void installOnView(View view) {
        animationActivate = new Animation(view, duration, properties).setDelay(delay).setEasing(easing, easingType);
        animationActivate.onEndListener(a -> currentAnimation = null);

        List<Property<?>> properties2 = new ArrayList<>(properties.size());
        properties.forEach(property -> properties2.add(new Property<>(property.name, copy(view.getProperty(property.name)))));
        animationDeactivate = new Animation(view, duration, properties2).setDelay(delay).setEasing(easing, easingType);
        animationDeactivate.onEndListener(a -> currentAnimation = null);
    }

    void activate() {
        if(currentAnimation != null) currentAnimation.stopAnimation();
        currentAnimation = animationActivate;
        currentAnimation.startAnimation();
    }

    void deactivate() {
        if(currentAnimation != null) currentAnimation.stopAnimation();
        currentAnimation = animationDeactivate;
        currentAnimation.startAnimation();
    }

    @SuppressWarnings("unchecked")
    private <T> T copy(T object) {
        Class<?> clazz = object.getClass();
        if(clazz.equals(Color.class)) {
            return (T) ((Color) object).clone();
        } else if(clazz.equals(Frame.class)) {
            return (T) ((Frame) object).clone();
        }
        return object;
    }
}
