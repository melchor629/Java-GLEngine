package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.View;

/**
 * <p>
 * Interface for implement a Observer (o Property Change) about a property
 * of a {@link View}.
 * </p>
 * <p>
 * This method could be called before the value is set in the View object,
 * or after is set. As an Observer, it is not recommended to change the value
 * of the property inside the method because is something really unexpected and
 * is contrary to the Observers for this API. Also, cannot throw Exceptions because
 * can derive to unrecoverable states.
 * </p>
 * <p>
 * {@link #propertyChanged(String, Object, Object)} passes the name of the
 * property changed, so you can stack various properties to the same piece
 * of code.
 * </p>
 * <p>
 * This event can be emitted from any {@link Thread}.
 * </p>
 */
public interface OnPropertyChange {

    /**
     * Indicates that the property named {@code name} was changed from
     * the {@code old} value to the new {@code current} value.
     * @param name name of the property
     * @param old old value
     * @param current new value
     */
    void propertyChanged(String name, Object old, Object current);
}
