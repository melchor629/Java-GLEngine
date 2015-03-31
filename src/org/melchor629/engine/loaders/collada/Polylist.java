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
    public java.nio.IntBuffer vcount, p;
    public ArrayList<Input> inputs;
    private short offset = 0;

    public Polylist(Element pl) {
        material = pl.getAttribute("material");
        count = Integer.parseInt(pl.getAttribute("count"));
        inputs = new ArrayList<>();

        org.w3c.dom.NodeList nl = pl.getElementsByTagName("input");
        for(int i = 0; i < nl.getLength(); i++) {
            Element input = (Element) nl.item(i);
            Input in = new Input(input);
            inputs.add(in);
            if(offset < in.offset)
                offset = (short) in.offset;
        }

        vcount = org.melchor629.engine.utils.BufferUtils.createIntBuffer(count);
        String t = pl.getElementsByTagName("vcount").item(0).getTextContent();
        int pos = 0, newPos = 0, pCount = 0;
        for(int i = 0; i < count; i++) {
            newPos = t.indexOf(' ', pos + 1);
            int val;
            if(newPos != -1)
                val = Integer.parseInt(t.substring(pos, newPos), 10);
            else
                val = Integer.parseInt(t.substring(pos), 10);
            vcount.put(val);
            pCount += val;
            pos = newPos + 1;
        }

        pCount *= inputs.size(); //(vcount[0] -> m caras) * n inputs
        p = org.melchor629.engine.utils.BufferUtils.createIntBuffer(pCount);
        t = pl.getElementsByTagName("p").item(0).getTextContent();
        pos = 0;
        for(int i = 0; i < pCount; i++) {
            newPos = t.indexOf(' ', pos + 1);
            int val;
            if(newPos != -1)
                val = Integer.parseInt(t.substring(pos, newPos), 10);
            else
                val = Integer.parseInt(t.substring(pos), 10);
            p.put(val);
            pos = newPos + 1;
        }
        
        vcount.flip();
        p.flip();
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
            
            String offset_s = input.getAttribute("offset");
            offset = offset_s.equals("") ? 0 : Integer.parseInt(offset_s);
        }
    }
}
