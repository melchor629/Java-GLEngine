package org.melchor629.engine.loaders.collada;

import org.melchor629.engine.utils.math.Matrix4;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.melchor629.engine.utils.math.Vector3;

/**
 *
 * @author melchor9000
 */
public class Node {
    public Vector3 location = new Vector3(), scale = new Vector3();
    public Matrix4 rotation;
    public String id, name, type;
    public Instance instance;

    public Node(Element node) {
        id = node.getAttribute("id");
        name = node.getAttribute("name");
        type = node.getAttribute("type");
        if(!node.getAttribute("type").equals("NODE")) return; //TODO
        strToVector(location, (Element) node.getElementsByTagName("translate").item(0));
        strToVector(scale, (Element) node.getElementsByTagName("scale").item(0));
        rotation(node.getElementsByTagName("rotate"));

        if(node.getElementsByTagName("instance_camera").item(0) != null)
            instance = new Instance_Camera((Element) node.getElementsByTagName("instance_camera").item(0));
        else if(node.getElementsByTagName("instance_light").item(0) != null)
            instance = new Instance_Light((Element) node.getElementsByTagName("instance_light").item(0));
        else if(node.getElementsByTagName("instance_geometry").item(0) != null)
            instance = new Instance_Geometry((Element) node.getElementsByTagName("instance_geometry").item(0));
        else if(node.getElementsByTagName("instance_controller").item(0) != null)
            instance = new Instance_Controller((Element) node.getElementsByTagName("instance_controller").item(0));
    }

    public boolean isCamera() {
        return instance instanceof Instance_Camera;
    }

    public boolean isLight() {
        return instance instanceof Instance_Light;
    }

    public boolean isGeometry() {
        return instance instanceof Instance_Geometry;
    }

    public boolean isController() {
        return instance instanceof Instance_Controller;
    }

    private void strToVector(Vector3 v, Element loc) {
        String[] l = loc.getTextContent().split(" ");
        v.x(Float.parseFloat(l[0]));
        v.y(Float.parseFloat(l[1]));
        v.z(Float.parseFloat(l[2]));
    }

    private void rotation(NodeList Lrot) {
        rotation = new Matrix4();
        for(int i = 0; i < Lrot.getLength(); i++) {
            Element rot = (Element) Lrot.item(i);
            String[] r = rot.getTextContent().split(" ");
            if(rot.getAttribute("sid").equals("rotationZ")) {
                rotation.setValueAt(1, 1, Float.parseFloat(r[0]));
                rotation.setValueAt(1, 2, Float.parseFloat(r[1]));
                rotation.setValueAt(1, 3, Float.parseFloat(r[2]));
                rotation.setValueAt(1, 4, Float.parseFloat(r[3]));
            } else if(rot.getAttribute("sid").equals("rotationY")) {
                rotation.setValueAt(2, 1, Float.parseFloat(r[0]));
                rotation.setValueAt(2, 2, Float.parseFloat(r[1]));
                rotation.setValueAt(2, 3, Float.parseFloat(r[2]));
                rotation.setValueAt(2, 4, Float.parseFloat(r[3]));
            } else if(rot.getAttribute("sid").equals("rotationX")) {
                rotation.setValueAt(3, 1, Float.parseFloat(r[0]));
                rotation.setValueAt(3, 2, Float.parseFloat(r[1]));
                rotation.setValueAt(3, 3, Float.parseFloat(r[2]));
                rotation.setValueAt(3, 4, Float.parseFloat(r[3]));
            }
        }
    }
}
