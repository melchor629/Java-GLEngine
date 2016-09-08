package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.MouseEvent;
import org.melchor629.engine.gui.View;

/**
 * <p>
 * Implements a listener for when the mouse cursor goes out of
 * a {@link View}.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnMouseExit {

    /**
     * Tells when the mouse cursor goes away from a {@link View}
     * bounds.
     * @param e Information about the {@link MouseEvent}
     */
    void mouseExited(MouseEvent e);
}
