package org.melchor629.engine.loaders.collada;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Source {
    protected float[] buff;
    public String id;
    protected int stride;

    public Source(Element src) {
        id = src.getAttribute("id");
        int length = Integer.parseInt(((Element) src.getElementsByTagName("float_array").item(0)).getAttribute("count"));
        buff = new float[length];
        stride = Integer.parseInt(((Element) ((Element) src.getElementsByTagName("technique_common").item(0))
                .getChildNodes().item(0)).getAttribute("stride"));
        //TODO no usar un array de Strings, si no ir buscando en el string y obtener el trozo correcto
        String[] float_array = src.getElementsByTagName("float_array").item(0).getTextContent().split(" ");
        for(int i = 0; i < float_array.length; i++) {
            buff[i] = Float.parseFloat(float_array[i]);
        }
    }

    public float getX(int pos) {
        return buff[pos * stride];
    }

    public float getY(int pos) {
        return buff[pos * stride + 1];
    }

    public float getZ(int pos) {
        return buff[pos * stride + 2];
    }
}
