package org.melchor629.engine.window;

import org.melchor629.engine.utils.math.Vector2;

/**
 * Represents a Monitor of this computer, lets you retrieve some information
 * and use it to create a {@link Window}.<br><br>
 * Implementations must have a static method to retrieve all monitors available
 * and another one to get the default monitor.
 */
interface Monitor {

    /**
     * Represents the Video Mode of a Monitor
     */
    class VideoMode {
        public final int width, height, refreshRate;

        VideoMode(int w, int h, int r) {
            width = w;
            height = h;
            refreshRate = r;
        }

        @Override public String toString() {
            return width + "x" + height + "@" + refreshRate;
        }
    }

    /**
     * Gets the physical size of the monitor, or an estimation of it, in
     * millimeters.
     * @return physical size in millimeters
     */
    Vector2 getPhysicalSize();

    /**
     * Gets the position in the virtual desktop of the monitor, in screen
     * coordinates.
     * @return virtual position of the monitor
     */
    Vector2 getVirtualPosition();

    /**
     * Returns a human readable name of the monitor
     * @return name
     */
    String getName();

    /**
     * Retrieves the current {@link VideoMode} of this monitor and
     * returns it
     * @return current {@link VideoMode}
     */
    VideoMode getCurrentVideoMode();

    /**
     * Retrieves all available {@link VideoMode}s of this monitor.
     * This modes are to be used by full screen window modes
     * @return available {@link VideoMode}s
     */
    VideoMode[] getAvailableVideoModes();
}
