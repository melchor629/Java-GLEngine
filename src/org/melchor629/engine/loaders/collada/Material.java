package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Material {
    public String id, name;
    public String instance_effect_url;

    public Material(Element mat) {
        id = mat.getAttribute("id");
        name = mat.getAttribute("name");
        instance_effect_url = ((Element) mat.getElementsByTagName("instance_effect").item(0)).getAttribute("url");
    }
}
