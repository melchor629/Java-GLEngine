package org.melchor629.engine.loaders.collada;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Image {
    public String id, name, format;
    public int width, height, depth;
    public String init_from;

    public Image(Element img) {
        id = img.getAttribute("id");
        name = img.getAttribute("name");
        format = img.getAttribute("format");
        init_from = img.getElementsByTagName("init_from").item(0).getTextContent();
        try {
            init_from = URLDecoder.decode(init_from, "UTF-8").replace("%2B", "+");
        } catch (UnsupportedEncodingException e) {}
        String s = img.getAttribute("width");
        width = Integer.parseInt(s.length() != 0 ? s : "1", 10);
        s = img.getAttribute("height");
        height = Integer.parseInt(s.length() != 0 ? s : "1", 10);
        s = img.getAttribute("depth");
        depth = Integer.parseInt(s.length() != 0 ? s : "1", 10);
        /*String s = img.getAttributeValue("width"); TODO
        width = Integer.getInteger(s != null ? s : "-1");
        s = img.getAttributeValue("height");
        height = Integer.getInteger(s != null ? s : "-1");
        s = img.getAttributeValue("depth");
        depth = Integer.getInteger(s != null ? s : "-1");*/
    }
}
