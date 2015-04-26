package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Is a type of Common Rendering Effects
 */
public class Phong {
    private CommonColorOrTextureType emission, ambient, diffuse, specular, reflective;
    private float shininess, reflectivity, transparency, index_of_refraction;

    public Phong(Element phong) {
        NodeList node = phong.getElementsByTagName("emission");
        if(node.getLength() == 1)
            emission = new CommonColorOrTextureType((Element) node.item(0));

        node = phong.getElementsByTagName("ambient");
        if(node.getLength() == 1)
            ambient = new CommonColorOrTextureType((Element) node.item(0));

        node = phong.getElementsByTagName("diffuse");
        if(node.getLength() == 1)
            diffuse = new CommonColorOrTextureType((Element) node.item(0));

        node = phong.getElementsByTagName("specular");
        if(node.getLength() == 1)
            specular = new CommonColorOrTextureType((Element) node.item(0));

        node = phong.getElementsByTagName("reflective");
        if(node.getLength() == 1)
            reflective = new CommonColorOrTextureType((Element) node.item(0));

        node = phong.getElementsByTagName("shininess");
        if(node.getLength() == 1)
            shininess = Float.parseFloat(node.item(0).getTextContent());

        node = phong.getElementsByTagName("reflectivity");
        if(node.getLength() == 1)
            reflectivity = Float.parseFloat(node.item(0).getTextContent());

        node = phong.getElementsByTagName("transparency");
        if(node.getLength() == 1)
            transparency = Float.parseFloat(node.item(0).getTextContent());

        node = phong.getElementsByTagName("index_of_refraction");
        if(node.getLength() == 1)
            index_of_refraction = Float.parseFloat(node.item(0).getTextContent());
    }

    public CommonColorOrTextureType getEmission() {
        return emission;
    }

    public CommonColorOrTextureType getAmbient() {
        return ambient;
    }

    public CommonColorOrTextureType getDiffuse() {
        return diffuse;
    }

    public CommonColorOrTextureType getSpecular() {
        return specular;
    }

    public CommonColorOrTextureType getReflective() {
        return reflective;
    }

    public float getShininess() {
        return shininess;
    }

    public float getReflectivity() {
        return reflectivity;
    }

    public float getTransparency() {
        return transparency;
    }

    public float getIndex_of_refraction() {
        return index_of_refraction;
    }
}
