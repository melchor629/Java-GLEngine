package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 * Geometry Element
 * @author melchor9000
 */
public class Geometry {
    public String id;
    public String name;
    //public Element asset;
    //public Element extra;
    public Mesh mesh;

    public Geometry(Element geo) {
        id = geo.getAttribute("id");
        name = geo.getAttribute("name");
        if(geo.getElementsByTagName("mesh").item(0) != null)
            mesh = new Mesh((Element) geo.getElementsByTagName("mesh").item(0));
    }
}
