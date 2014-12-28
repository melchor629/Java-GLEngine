package org.melchor629.engine.loaders.collada;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Effect {
    public String emission, ambient, diffuse, specular, id;
    public float shininess, index_of_refraction;
    public ArrayList<newparam> params;
    
    public Effect(Element effects) {
        Element profile_COMMON = (Element) effects.getElementsByTagName("profile_COMMON").item(0);
        id = effects.getAttribute("id");
        Element technique = (Element) profile_COMMON.getElementsByTagName("technique").item(0);
        if(technique.getElementsByTagName("phong").item(0) != null)
            phong((Element) technique.getElementsByTagName("phong").item(0));
        //else ... TODO
        params = new ArrayList<newparam>();
        org.w3c.dom.NodeList nl = profile_COMMON.getElementsByTagName("newparam");
        for(int i = 0; i < nl.getLength(); i++) {
            Element newparam = (Element) nl.item(i);
            params.add(new newparam(newparam));
        }
    }

    private void phong(Element phong) {
        emission = getValue((Element) phong.getElementsByTagName("emission").item(0));
        ambient = getValue((Element) phong.getElementsByTagName("ambient").item(0));
        diffuse = getValue((Element) phong.getElementsByTagName("diffuse").item(0));
        specular = getValue((Element) phong.getElementsByTagName("specular").item(0));
        shininess = getFloatValue((Element) phong.getElementsByTagName("shininess").item(0));
        index_of_refraction = getFloatValue((Element) phong.getElementsByTagName("index_of_refraction").item(0));
    }

    public static boolean isColor(String str) {
        return str.startsWith("C");
    }

    public static boolean isTexture(String str) {
        return str.startsWith("T");
    }

    private String getValue(Element e) {
        if(e.getElementsByTagName("color").item(0) != null)
            return "C"+e.getElementsByTagName("color").item(0).getTextContent();
        else if(e.getElementsByTagName("texture").item(0) != null)
            return "T"+e.getElementsByTagName("color").item(0).getAttributes().getNamedItem("texture").getTextContent();
        return null;
    }
    
    private float getFloatValue(Element e) {
        return Float.parseFloat(((Element) e.getElementsByTagName("float").item(0)).getTextContent());
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
