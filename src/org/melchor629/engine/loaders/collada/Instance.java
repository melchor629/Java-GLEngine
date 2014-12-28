package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public abstract class Instance {
    public String url, sid, name;
    
    public Instance(Element inst) {
        url = inst.getAttribute("url");
        sid = inst.getAttribute("sid");
        name = inst.getAttribute("name");
    }
}
