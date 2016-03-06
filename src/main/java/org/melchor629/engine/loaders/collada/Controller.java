package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Controller {
    public String id, name;
    public Skin skin;
    //TODO public Morph morph;

    public Controller(Element contr) {
        id = contr.getAttribute("id");
        name = contr.getAttribute("name");
        if(contr.getElementsByTagName("skin").item(0) != null)
            skin = new Skin((Element) contr.getElementsByTagName("skin").item(0));
        //else if(getChild(contr, "morph") != null) //TODO
    }
}
