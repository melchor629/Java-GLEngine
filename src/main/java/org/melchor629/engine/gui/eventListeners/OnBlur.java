package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.View;

/**
 * <p>
 * Interface for implement the listener for the blur event in a
 * {@link View}.
 * </p>
 * <p>
 * This event is called when a {@link View} that has the focus on
 * it, lost the focus. This listener is called from another {@link Thread}
 * different to where the GUI is rendered. Changing properties
 * from another {@link Thread} could derive to bad things.
 * </p>
 */
public interface OnBlur {

    /**
     * Tells when a {@link View} has lost the focus
     */
    void lostFocus();
}
