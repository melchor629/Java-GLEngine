package org.melchor629.engine.objects;

import org.melchor629.engine.loaders.collada.Effect;

/**
 * Material with shader
 */
public class Material {
    private String id;

    private Material(Effect effect) {
        id = effect.getId();

    }
}
