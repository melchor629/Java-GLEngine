package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.MouseEvent;
import org.melchor629.engine.gui.View;

/**
 * <p>
 * Implements a listener for when the mouse cursor that is inside a
 * {@link View} is moved.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnMouseMove {

    /**
     * Tells when the mouse cursor, that is inside a {@link View}, is
     * moved inside it. You can now the relative position of the cursor
     * with {@link MouseEvent#getX()} and {@link MouseEvent#getY()},
     * its absolute position and the difference (in screen pixels) from
     * the current and last position (aka Movement).
     * @param e Information about the {@link MouseEvent}
     */
    void mouseMoved(MouseEvent e);
}
