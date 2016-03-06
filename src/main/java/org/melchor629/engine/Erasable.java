package org.melchor629.engine;

/**
 * A class implementing {@link Erasable} indicates that could
 * has something that should be deleted when it is not necessary
 * anymore.<br>
 * All OpenGL objects like Textures, Shaders, etc. have implemented
 * this interface and indicates that they have to be deleted.
 */
public interface Erasable {

    /**
     * Deletes this resource. This action cannot be undone.
     */
    void delete();
}
