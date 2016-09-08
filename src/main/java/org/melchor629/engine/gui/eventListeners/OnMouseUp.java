package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.MouseEvent;
import org.melchor629.engine.gui.View;

/**
 * <p>
 * Implements a listener for when a mouse button is released
 * on a {@link View}.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnMouseUp {

    /**
     * Tells when a mouse button is released on a {@link View}.
     * @param e Information about the {@link MouseEvent}
     */
    void mouseReleased(MouseEvent e);
}
