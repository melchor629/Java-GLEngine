package org.melchor629.engine.loaders.collada;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class VisualScene {
    public ArrayList<Node> nodes;
    public String id, name;

    public VisualScene(Element scene) {
        nodes = new ArrayList<Node>();
        org.w3c.dom.NodeList nl = scene.getElementsByTagName("node");
        for(int i = 0; i < nl.getLength(); i++) {
            Element node = (Element) nl.item(i);
            nodes.add(new Node(node));
        }
        id = scene.getAttribute("id");
        name = scene.getAttribute("name");
    }
}
