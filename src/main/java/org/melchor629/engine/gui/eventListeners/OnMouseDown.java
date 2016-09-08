package org.melchor629.engine.gui.eventListeners;

import org.melchor629.engine.gui.MouseEvent;
import org.melchor629.engine.gui.View;

/**
 * <p>
 * Implements when a mouse button is pressed on a {@link View}.
 * </p>
 * <p>
 * This event is called when this happens. It will be called from
 * another {@link Thread} that is not where the GUI is rendered.
 * Change properties from another {@link Thread} is dangerous.
 * </p>
 */
public interface OnMouseDown {

    /**
     * Tells when a mouse button is pressed on a {@link View}.
     * You can know which key was pressed with {@link MouseEvent#isLeftButtonClicked()},
     * {@link MouseEvent#isRightButtonClicked()} or
     * {@link MouseEvent#isMiddleButtonClicked()}.
     * @param e Info about the {@link MouseEvent}
     */
    void mousePressed(MouseEvent e);
}
