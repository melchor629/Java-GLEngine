package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Instance_Geometry extends Instance {
    public String url, instance_material_symbol, instance_material_target;

    /**
     * @param inst
     */
    public Instance_Geometry(Element inst) {
        super(inst);
        url = inst.getAttribute("url");
        if(inst.getElementsByTagName("bind_material").item(0) != null) {
            Element instance_material = (Element) ((Element) ((Element) inst.getElementsByTagName("bind_material").item(0))
                    .getElementsByTagName("technique_common")).getElementsByTagName("instance_material").item(0);
            instance_material_symbol = instance_material.getAttribute("symbol");
            instance_material_target = instance_material.getAttribute("target");
        }
    }

}
