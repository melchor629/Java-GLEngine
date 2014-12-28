package org.melchor629.engine.loaders.collada;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 * Mesh Element
 * @author melchor9000
 */
public class Mesh {
    public ArrayList<Source> sources;
    public Polylist polylist;
    public Polylist.Input vertices;
    public String vertices_id;

    public Mesh(Element mesh) {
        sources = new ArrayList<Source>();
        //for(Element source : getChildren(mesh, "source"))
        //    sources.add(new Source(source));
        org.w3c.dom.NodeList nl = mesh.getElementsByTagName("source");
        for(int i = 0; i < nl.getLength(); i++) {
            Element source = (Element) nl.item(i);
            sources.add(new Source(source));
        }

        if(mesh.getElementsByTagName("polylist").item(0) != null)
            polylist = new Polylist((Element) mesh.getElementsByTagName("polylist").item(0));
        vertices = new Polylist.Input((Element) ((Element) mesh.getElementsByTagName("vertices").item(0)).getElementsByTagName("input").item(0));
        vertices_id = ((Element) mesh.getElementsByTagName("vertices").item(0)).getAttribute("id");
    }
}
