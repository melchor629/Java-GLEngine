package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.View;

/**
 * <p>
 * Implements a listener for when a {@link View} gained the focus.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnFocus {

    /**
     * Tells when a {@link View} has gained the focus. Now, all
     * the keyboard events will be received to the {@link View}.
     */
    void gainedFocus();
}
