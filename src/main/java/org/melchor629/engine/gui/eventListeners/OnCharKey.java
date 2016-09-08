package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.View;
import org.melchor629.engine.input.Keyboard;

/**
 * <b>
 * Implements a listener for when a key, that represents a character
 * that can be used in a input view, is pressed on a {@link View}.
 * </b>
 * <p>
 * This event will be called from a different {@link Thread} that where
 * the GUI is rendered. It is not recommended to change properties from
 * another {@link Thread}.
 * </p>
 */
public interface OnCharKey {

    /**
     * Tells when a key that represents a character is pressed. It passes
     * a {@link String} because some characters could be represented in more
     * than one {@code char}.
     * @param keyboard Keyboard controller
     * @param ch Character pressed.
     */
    void characterIntroduced(Keyboard keyboard, String ch);
}
