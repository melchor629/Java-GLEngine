package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Source {
    public java.nio.FloatBuffer buff;
    public String id;
    protected int stride, count;

    public Source(Element src) {
        id = src.getAttribute("id");
        count = Integer.parseInt(((Element) src.getElementsByTagName("float_array").item(0)).getAttribute("count"));
        stride = Integer.parseInt(((Element) ((Element) src.getElementsByTagName("technique_common").item(0))
                .getElementsByTagName("accessor").item(0)).getAttribute("stride"));
        
        buff = org.melchor629.engine.utils.BufferUtils.createFloatBuffer(count);
        String floats = src.getElementsByTagName("float_array").item(0).getTextContent();
        int pos = 0, newPos = 0;
        for(int i = 0; i < count; i++) {
            newPos = floats.indexOf(' ', pos + 1);
            if(newPos != -1)
                buff.put(Float.parseFloat(floats.substring(pos, newPos)));
            else
                buff.put(Float.parseFloat(floats.substring(pos)));
            pos = newPos;
        }
        
        buff.flip();
    }
    
    public int count() {
        return count;
    }
/*
    public float getX(int pos) {
        return buff[pos * stride];
    }

    public float getY(int pos) {
        return buff[pos * stride + 1];
    }

    public float getZ(int pos) {
        return buff[pos * stride + 2];
    }*/
}
