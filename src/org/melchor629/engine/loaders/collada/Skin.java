package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 * TODO todo
 * @author melchor9000
 */
public class Skin {
    public String source;
    
    public Skin(Element skin) {
        source = skin.getAttribute("source");
        // ...
    }
}
