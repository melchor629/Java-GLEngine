package org.melchor629.engine.gui;

import org.melchor629.engine.gui.easing.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * <p>
 * Animate properties from {@link View}s. Create an animation is
 * simply create a new {@link Animation} object passing the properties
 * that is wanted to animate at once.</p>
 * <p>
 * {@code new Animation(view, 1, new Animation.Property<>("frame", new Frame(10, 10, 200, 300)).startAnimation()}
 * </p>
 * <p>
 * This code will animate the current values for position and size of
 * the {@link View} over a second to end with position of (10, 10) and
 * size of (200, 300).
 * </p>
 * <p>
 * You can also add a starting delay with {@link #setDelay(double)},
 * change the Easing function with {@link #setEasing(Easing, Easing.Type)},
 * or change the animation duration with {@link #setDuration(double)}.
 * </p>
 * <p>
 * Remember always to call {@link #startAnimation()} to make the animation
 * begin. It is possible to recall {@link #startAnimation()} to make the animation
 * again, but it will always take the current values of the animated properties,
 * so call {@link #startAnimation()} and call it again will have no effect.
 * </p>
 * <p>
 * Is possible to chain animations that will start when the first is done.
 * To chain animations, call {@link #chainAnimation(Animation)}. Only is
 * possible to chain one animation. Also, you can listen for the onEnd
 * event, that is called when the animation ends, {@link #onEndListener(Consumer)}.
 * </p>
 * <p>
 * Animations can be stopped in any time if it's needed. Call the method
 * {@link #stopAnimation()}. But has some consequences.
 * </p>
 */
public class Animation {
    private List<Property<?>> properties;
    private View view;
    private double initialTime, duration, delay;
    private Easing easing = LinearEasing.easing;
    private Easing.Type easingType = Easing.Type.NONE;
    private Animation nextOne;
    private Consumer<Animation> onEnd;
    private boolean stopAnimationCalled = false, reverse = false;
    private int repeat = 1, repeatIteration;

    /**
     * Creates a new animation on a {@code property}'s {@link View} with
     * a {@code duration} and 0 delay.<br><br>
     * <b>NOTE:</b> Animate a frame will modify {@link View#x}, {@link View#y},
     * {@link View#width} and {@link View#height} instead of {@link View#frame}.
     * @param view View to animate
     * @param duration Duration of the animation in seconds
     * @param property Property to animate
     * @param <T> Type of the property to animate
     */
    public <T> Animation(View view, double duration, Property<T> property) {
        this.view = view;
        properties = new ArrayList<>(1);
        properties.add(property);
        setDuration(duration);
    }

    /**
     * Creates a new animation on some {@code properties} of a {@link View} with
     * a {@code duration} and 0 delay.<br><br>
     * <b>NOTE:</b> Animate a frame will modify {@link View#x}, {@link View#y},
     * {@link View#width} and {@link View#height} instead of {@link View#frame}.
     * @param view View to animate
     * @param duration Duration of the animation in seconds
     * @param properties Properties to animate
     */
    public Animation(View view, double duration, Property<?>... properties) {
        this.view = view;
        this.properties = new ArrayList<>(properties.length);
        Collections.addAll(this.properties, properties);
        setDuration(duration);
    }

    /**
     * Creates a new animation on some {@code properties} of a {@link View} with
     * a {@code duration} and 0 delay.<br><br>
     * <b>NOTE:</b> Animate a frame will modify {@link View#x}, {@link View#y},
     * {@link View#width} and {@link View#height} instead of {@link View#frame}.
     * @param view View to animate
     * @param duration Duration of the animation in seconds
     * @param properties Properties to animate
     */
    public Animation(View view, double duration, Collection<Property<?>> properties) {
        this.view = view;
        this.properties = new ArrayList<>(properties);
        setDuration(duration);
    }

    /**
     * Sets a delay to the start of the animation.
     * @param delay Start delay of the animation in seconds
     * @return this
     */
    public Animation setDelay(double delay) {
        if(delay < 0) throw new IllegalArgumentException("delay cannot be negative");
        this.delay = delay;
        return this;
    }

    /**
     * Changes the easing function of the animation. The ease type is set
     * to {@link Easing.Type#NONE} and only works for {@link LinearEasing}.
     * @param easing The new easing function
     * @return this
     * @see Easing Easing, for easing functions
     */
    public Animation setEasing(Easing easing) {
        if(easing == null) throw new NullPointerException("easing cannot be null");
        this.easing = easing;
        this.easingType = Easing.Type.NONE;
        return this;
    }

    /**
     * Changes the easing function and type of the animation.
     * @param easing The new easing function
     * @param type The type of the easing function
     * @return this
     * @see Easing Easing, for easing functions
     * @see Easing.Type Easing Type
     */
    public Animation setEasing(Easing easing, Easing.Type type) {
        if(easing == null) throw new NullPointerException("easing cannot be null");
        if(type == null) throw new NullPointerException("type cannot be null");
        this.easing = easing;
        this.easingType = type;
        return this;
    }

    /**
     * Changes the duration of the animation.
     * @param duration The new duration in seconds
     * @return this
     */
    public Animation setDuration(double duration) {
        if(duration < 0) throw new IllegalArgumentException("duration cannot be negative");
        this.duration = duration;
        return this;
    }

    /**
     * Reverses the animation or not
     * @param reverse True to reverse, false otherwise
     * @return this
     */
    public Animation setReverseAnimation(boolean reverse) {
        this.reverse = reverse;
        return this;
    }

    /**
     * Makes this animation repeat {@code repeat} times. By default
     * this value is 1. 0 means infinite times.
     * @param repeat Repeat iterations for the animation
     * @return this
     */
    public Animation setRepeat(int repeat) {
        this.repeat = repeat;
        return this;
    }

    /**
     * Adds the animation that will start when this ends. Only is
     * possible to chain one animation. Don't call {@link #startAnimation()}
     * on the {@code animation} object, bad things could happen.
     * @param animation Animation to chain
     * @return this
     */
    public Animation chainAnimation(Animation animation) {
        nextOne = animation;
        return this;
    }

    /**
     * Listen for the {@code onEnd} event, emitted when the animation ends.
     * Only can listen one callback.
     * @param f Callback
     * @return this
     */
    public Animation onEndListener(Consumer<Animation> f) {
        onEnd = f;
        return this;
    }

    /**
     * Starts the animation
     */
    public void startAnimation() {
        properties.forEach(property -> {
            if(property.to.getClass().equals(Frame.class)) {
                @SuppressWarnings("unchecked") Property<Frame> f = (Property<Frame>) property;
                Frame ff = view.effectiveFrame();
                Float width = view.width;
                Float height = view.height;
                if(width == null) {
                    width = ff.width - view.paddingLeft - view.paddingRight;
                }
                if(height == null) {
                    height = ff.height - view.paddingTop - view.paddingBottom;
                }
                f.from = new Frame(
                        view.x,
                        view.y,
                        width,
                        height
                );
            } else {
                property.from = view.getProperty(property.name);
            }
        });

        stopAnimationCalled = false;
        repeatIteration = repeat - 1; //TODO implementar repeat
        initialTime = org.lwjgl.glfw.GLFW.glfwGetTime() + delay;
        GUI.gui.executeOnce(this::doAnimation);
    }

    /**
     * Stops the animation. Doing that, the chained animation won't be started
     * (<i>if there's one</i>) and the {@code onEnd} event won't be emitted.
     * If there's a chained animation, this will stop this animation too.
     */
    public void stopAnimation() {
        stopAnimationCalled = true;
        if(nextOne != null) nextOne.stopAnimation();
    }

    /**
     * Method that does the animation on the properties
     */
    @SuppressWarnings("unchecked")
    private void doAnimation() {
        properties.forEach(property -> {
            Class<?> clazz = property.from.getClass();
            float delta = (float) (org.lwjgl.glfw.GLFW.glfwGetTime() - initialTime);

            if(delta >= 0 && !stopAnimationCalled) {
                if(reverse) delta = 1 - delta;
                if (clazz.equals(Color.class)) {
                    Property<Color> p = (Property<Color>) property;
                    Color color = view.getProperty(property.name);
                    if(color != null) {
                        color.r(ease(delta, p.from.r(), p.to.r() - p.from.r()));
                        color.g(ease(delta, p.from.g(), p.to.g() - p.from.g()));
                        color.b(ease(delta, p.from.b(), p.to.b() - p.from.b()));
                        color.alpha(ease(delta, p.from.alpha(), p.to.alpha() - p.from.alpha()));
                    }
                } else if(clazz.equals(Float.class)) {
                    Property<Float> p = (Property<Float>) property;
                    float f = ease(delta, p.from, p.to - p.from);
                    view.setProperty(property.name, f);
                } else if(clazz.equals(Integer.class)) {
                    Property<Integer> p = (Property<Integer>) property;
                    float f = ease(delta, p.from, p.to - p.from);
                    view.setProperty(property.name, (int) f);
                } else if(clazz.equals(Frame.class)) {
                    Property<Frame> p = (Property<Frame>) property;
                    view.setProperty("x", ease(delta, p.from.x, p.to.x - p.from.x));
                    view.setProperty("y", ease(delta, p.from.y, p.to.y - p.from.y));
                    view.setProperty("width", ease(delta, p.from.width, p.to.width - p.from.width));
                    view.setProperty("height", ease(delta, p.from.height, p.to.height - p.from.height));
                } else {
                    if(ease(delta, 0, 1) < 0.5) {
                        view.setProperty(property.name, property.from);
                    } else {
                        view.setProperty(property.name, property.to);
                    }
                }
                view.setProperty("frame", null);
                if(reverse) delta = 1 - delta;
            }

            if(delta < duration && !stopAnimationCalled) GUI.gui.executeOnce(this::doAnimation);
            else if(!stopAnimationCalled) {
                if(!reverse) {
                    if (clazz.equals(Color.class)) {
                        view.setProperty(property.name, ((Property<Color>) property).to.clone());
                    } else {
                        view.setProperty(property.name, property.to);
                    }
                } else {
                    if (clazz.equals(Color.class)) {
                        view.setProperty(property.name, ((Property<Color>) property).from.clone());
                    } else {
                        view.setProperty(property.name, property.from);
                    }
                }

                if(nextOne != null) {
                    nextOne.startAnimation();
                }
                if(onEnd != null) {
                    onEnd.accept(this);
                }
            }
        });
    }

    void alterPropertiesFinalValue(Function<String, ?> alterFunction) {
        properties.forEach(property -> {
            try {
                Property.class.getDeclaredField("to").set(property, alterFunction.apply(property.name));
            } catch (IllegalAccessException | NoSuchFieldException ignore) {}
        });
    }

    /**
     * Calls the correct easing method depending on the type
     * @param t time
     * @param b start value
     * @param c difference between values
     * @return value for the property in that time using a easing function
     */
    private float ease(float t, float b, float c) {
        switch(easingType) {
            case NONE: return easing.ease(t, b, c, (float) duration);
            case IN: return easing.easeIn(t, b, c, (float) duration);
            case OUT: return easing.easeOut(t, b, c, (float) duration);
            case IN_OUT: return easing.easeInOut(t, b, c, (float) duration);
            default: throw new RuntimeException("This is impossible :(");
        }
    }

    /**
     * Represents a property to animate. Stores the property name and
     * the final value to change at the end of the animation
     * @param <T> Type of the property
     */
    public static class Property<T> {
        String name;
        private T from, to;

        /**
         * Creates a new property for the animation and its change
         * @param name Name of the property
         * @param to Final value for the animation of the property
         */
        public Property(String name, T to) {
            this.name = name;
            this.to = to;
        }
    }
}
