package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.View;
import org.melchor629.engine.input.Keyboard;

/**
 * <p>
 * Implements a listener for when a key from the {@link Keyboard} is
 * pressed in a {@link View}. The {@link View} must have the focus.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnKeyDown {

    /**
     * Tells when a key is pressed on a focused {@link View}. It passes
     * a key code. To know which was pressed, {@link Keyboard#getStringRepresentation(int)}
     * method could help you to identify the key.
     * @param keyboard Keyboard controller
     * @param key key code
     */
    void keyPressed(Keyboard keyboard, int key);
}
