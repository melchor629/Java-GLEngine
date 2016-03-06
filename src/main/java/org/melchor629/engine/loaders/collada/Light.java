package org.melchor629.engine.loaders.collada;

import org.melchor629.engine.utils.math.Vector3;
import org.w3c.dom.Element;

/**
 * Light instance
 */
public class Light {
    private String id, name;
    private Point pointLight;

    public Light(Element light) {
        id = light.getAttribute("id");
        name = light.getAttribute("name");

        Element tec_com = (Element) light.getElementsByTagName("technique_common").item(0);
        if(tec_com.getElementsByTagName("point").getLength() != 0) {
            pointLight = new Point((Element) light.getElementsByTagName("point").item(0));
        }
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Point getPointLight() {
        return pointLight;
    }

    public static class Point {
        Vector3 color;
        double constant, linear, quadratic;

        private Point(Element point) {
            Element colorElem = (Element) point.getElementsByTagName("color").item(0);
            String[] colorParts = colorElem.getTextContent().split(" ");
            color = new Vector3(Float.parseFloat(colorParts[0]), Float.parseFloat(colorParts[1]), Float.parseFloat(colorParts[2]));

            constant = Double.parseDouble(point.getElementsByTagName("constant_attenuation").item(0).getTextContent());
            linear = Double.parseDouble(point.getElementsByTagName("linear_attenuation").item(0).getTextContent());
            quadratic = Double.parseDouble(point.getElementsByTagName("quadratic_attenuation").item(0).getTextContent());
        }
    }
}
