package org.melchor629.engine.loaders;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.melchor629.engine.loaders.collada.Controller;
import org.melchor629.engine.loaders.collada.Effect;
import org.melchor629.engine.loaders.collada.Geometry;
import org.melchor629.engine.loaders.collada.Image;
import org.melchor629.engine.loaders.collada.Material;
import org.melchor629.engine.loaders.collada.VisualScene;
import org.melchor629.engine.utils.Timing;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * http://www.khronos.org/files/collada_reference_card_1_4.pdf
 * @author melchor9000
 */
public class Collada {
    protected File cFile;
    //protected Element collada;
    public ArrayList<Geometry> geometry;
    public ArrayList<Image> images;
    public ArrayList<Material> materials;
    public ArrayList<Effect> effects;
    public ArrayList<VisualScene> visual_scenes;
    public ArrayList<Controller> controllers;
    /** $('scene instance_visual_scene').attr('url') **/
    public String The_Scene;

    public Collada(File file) throws IOException, SAXException {
        cFile = file;
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = null;
        
        try {
            dBuilder = dbFactory.newDocumentBuilder();
        } catch (ParserConfigurationException e) {
            System.out.printf("Error que nunca deberia haber salido...\n"); //TODO Logger
            e.printStackTrace();
        }
        
        Document doc = dBuilder.parse(file);
     
        doc.getDocumentElement().normalize();
        Element collada = doc.getDocumentElement();
        geometry(collada);
        image(collada);
        materials(collada);
        effects(collada);
        visual_scenes(collada);
        controllers(collada);
        collada = null;
    }

    public Geometry searchForGeometryWithId(String id) {
        boolean found = false;
        int i = 0;

        if(id.startsWith("#"))
            id = id.substring(1);
        while(!found && i < geometry.size())
            found = geometry.get(i++).id.equals(id);
        return found ? geometry.get(--i) : null;
    }
    
    public void disposeData() {
        for(Geometry g : geometry)
            g.mesh.disposeData();

        new Thread(new Runnable() {
            public void run() {
                Timing.sleep(1000);
                System.gc();
            }
        }).start();
    }

    protected void finalize() {
        disposeData();
    }

    protected void geometry(Element collada) {
        geometry = new ArrayList<>();
        NodeList nl = ((Element) collada.getElementsByTagName("library_geometries").item(0))
                .getElementsByTagName("geometry");
        for(int i = 0; i < nl.getLength(); i++) {
            Element geo = (Element) nl.item(i);
            geometry.add(new Geometry(geo));
        }
    }

    protected void image(Element collada) {
        images = new ArrayList<>();
        NodeList nl = ((Element) collada.getElementsByTagName("library_images").item(0))
                .getElementsByTagName("image");
        for(int i = 0; i < nl.getLength(); i++) {
            Element img = (Element) nl.item(i);
            images.add(new Image(img));
        }
    }

    protected void materials(Element collada) {
        materials = new ArrayList<>();
        NodeList nl = ((Element) collada.getElementsByTagName("library_materials").item(0))
                .getElementsByTagName("material");
        for(int i = 0; i < nl.getLength(); i++) {
            Element mat = (Element) nl.item(i);
            materials.add(new Material(mat));
        }
    }

    protected void effects(Element collada) {
        effects = new ArrayList<>();
        NodeList nl = ((Element) collada.getElementsByTagName("library_effects").item(0))
                .getElementsByTagName("effect");
        for(int i = 0; i < nl.getLength(); i++) {
            Element effect = (Element) nl.item(i);
            effects.add(new Effect(effect));
        }
    }

    protected void visual_scenes(Element collada) {
        visual_scenes = new ArrayList<>();
        NodeList nl = ((Element) collada.getElementsByTagName("library_visual_scenes").item(0))
                .getElementsByTagName("visual_scene");
        for(int i = 0; i < nl.getLength(); i++) {
            Element scene = (Element) nl.item(i);
            visual_scenes.add(new VisualScene(scene));
        }

        The_Scene = ((Element) ((Element) collada.getElementsByTagName("scene").item(0))
            .getElementsByTagName("instance_visual_scene").item(0)).getAttribute("url");
    }

    protected void controllers(Element collada) {
        controllers = new ArrayList<>();
        NodeList nl = ((Element) collada.getElementsByTagName("library_controllers").item(0))
                .getElementsByTagName("controller");
        for(int i = 0; i < nl.getLength(); i++) {
            Element cntrl = (Element) nl.item(i);
            controllers.add(new Controller(cntrl));
        }
    }
}