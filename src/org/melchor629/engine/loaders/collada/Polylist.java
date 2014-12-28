package org.melchor629.engine.loaders.collada;

import java.util.ArrayList;

import org.w3c.dom.Element;

/**
 *
 * @author melchor9000
 */
public class Polylist {
    public String material;
    public int count;
    public int[] vcount;
    public int[] p;
    public ArrayList<Input> inputs;
    private short offset = 0;

    public Polylist(Element pl) {
        material = pl.getAttribute("material");
        count = Integer.parseInt(pl.getAttribute("count"));
        inputs = new ArrayList<Input>();

        org.w3c.dom.NodeList nl = pl.getElementsByTagName("input");
        for(int i = 0; i < nl.getLength(); i++) {
            Element input = (Element) nl.item(i);
            Input in = new Input(input);
            inputs.add(in);
            if(offset < in.offset)
                offset = (short) in.offset;
        }
        
        //TODO no usar un array de Strings, si no ir buscando en el string y obtener el trozo correcto
        //TODO usar IntBuffer y FloatBuffer para el otro caso
        String[] t = pl.getElementsByTagName("vcount").item(0).getTextContent().split(" ");
        vcount = new int[t.length];
        for(int i = 0; i < t.length; i++)
            vcount[i] = Integer.parseInt(t[i]);
        t = pl.getElementsByTagName("p").item(0).getTextContent().split(" ");
        p = new int[t.length];
        for(int i = 0; i < t.length; i++)
            p[i] = Integer.parseInt(t[i]);
    }

    public int getVX(int pos) {
        int Offset = getInput("vertex").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset];
    }

    public int getVY(int pos) {
        int Offset = getInput("vertex").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset + 1 + offset];
    }

    public int getVZ(int pos) {
        int Offset = getInput("vertex").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset + 2 + offset * 2];
    }

    public int getNX(int pos) {
        int Offset = getInput("normal").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset];
    }

    public int getNY(int pos) {
        int Offset = getInput("normal").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset + 1 + offset];
    }

    public int getNZ(int pos) {
        int Offset = getInput("normal").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset + 2 + offset * 2];
    }

    public int getTX(int pos) {
        int Offset = getInput("texcoord").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset];
    }

    public int getTY(int pos) {
        int Offset = getInput("texcoord").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset + 1 + offset];
    }

    public int getTZ(int pos) {
        int Offset = getInput("texcoord").offset;
        return p[pos * vcount[pos / 2] * (offset+1) + Offset + 2 + offset * 2];
    }

    public Input getInput(String semantic) {
        for(Input input : inputs)
            if(input.semantic.toLowerCase().equals(semantic.toLowerCase()))
                return input;
        return null;
    }

    public static class Input {
        public String semantic;
        public String source;
        public int offset;

        public Input(Element input) {
            semantic = input.getAttribute("semantic");
            source = input.getAttribute("source");
            offset = Integer.parseInt(input.getAttribute("offset"));
        }
    }
}
