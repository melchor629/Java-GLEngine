package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.View;
import org.melchor629.engine.input.Keyboard;

/**
 * <p>
 * Implements the listener for when a key is released on a
 * focused {@link View}.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnKeyUp {

    /**
     * Tells when a key is released on a focused {@link View}. {@code key}
     * represents a key code for the released key. To identify this key,
     * you can use {@link Keyboard#getStringRepresentation(int)}.
     * @param keyboard Keyboard controller
     * @param key key code
     */
    void keyReleased(Keyboard keyboard, int key);
}
