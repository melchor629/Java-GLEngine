package org.melchor629.engine.loaders.collada;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Effect {
    private Phong phong;
    private String id;
    public ArrayList<newparam> params;
    
    public Effect(Element effects) {
        Element profile_COMMON = (Element) effects.getElementsByTagName("profile_COMMON").item(0);
        id = effects.getAttribute("id");
        params = new ArrayList<>();
        org.w3c.dom.NodeList nl = profile_COMMON.getElementsByTagName("newparam");
        for(int i = 0; i < nl.getLength(); i++) {
            Element newparam = (Element) nl.item(i);
            params.add(new newparam(newparam));
        }
        Element technique = (Element) profile_COMMON.getElementsByTagName("technique").item(0);
        if(technique.getElementsByTagName("phong").item(0) != null)
            phong = new Phong((Element) technique.getElementsByTagName("phong").item(0), params);
        else if(technique.getElementsByTagName("blinn").getLength() == 1)
            phong = new Phong((Element) technique.getElementsByTagName("blinn").item(0), params);
        //else ... TODO
    }

    public Phong getPhong() {
        return phong;
    }

    public String getId() {
        return id;
    }

    public class newparam {
        public String sid, type, value;
        
        public newparam(Element p) {
            sid = p.getAttribute("sid");
            if(p.getElementsByTagName("surface").item(0) != null) {
                type = "surface";
                value = ((Element) p.getElementsByTagName("surface").item(0)).getElementsByTagName("init_from").item(0).getTextContent();
            } else if(p.getElementsByTagName("sampler2D").item(0) != null) {
                type = "sampler2D";
                value = ((Element) p.getElementsByTagName("sampler2D").item(0)).getElementsByTagName("source").item(0).getTextContent();
            }
        }
    }
}
