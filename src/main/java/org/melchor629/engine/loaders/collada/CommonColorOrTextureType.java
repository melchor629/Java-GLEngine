package org.melchor629.engine.loaders.collada;

import org.melchor629.engine.utils.math.Vector4;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.util.List;

/**
 * Describes a color or a texture for every parameter inside a Common Rendering Effect
 */
public class CommonColorOrTextureType {
    private Vector4 color;
    private Texture texture;
    private String sid;

    public CommonColorOrTextureType(Element element, List<Effect.newparam> newparams) {
        NodeList list = element.getChildNodes();
        for(int i = 0; i < list.getLength(); i++) {
            if(list.item(i) instanceof Element) {
                Element element1 = (Element) list.item(i);
                if(element1.getTagName().equals("color")) parseColor(element1.getTextContent());
                else if(element1.getTagName().equals("texture")) texture = new Texture(element1, newparams);
                sid = element1.getAttribute("sid");
            }
        }
    }

    /**
     * @return the color of this node
     */
    public Vector4 getColor() {
        return color;
    }

    /**
     * The String ID is a string identifier of the node. It's useful
     * for identify which node is
     * @return node's SID
     */
    public String getSid() {
        return sid;
    }

    /**
     * @return texture of this node
     */
    public Texture getTexture() {
        return texture;
    }

    private void parseColor(String text) {
        String[] vector = text.split(" ");
        float r = Float.parseFloat(vector[0]),
                g = Float.parseFloat(vector[1]),
                b = Float.parseFloat(vector[2]),
                a = Float.parseFloat(vector[3]);
        color = new Vector4(r, g, b, a);
    }

    public class Texture {
        private String texture, texturecoord;

        Texture(Element element, List<Effect.newparam> newparams) {
            texture = element.getAttribute("texture");
            texturecoord = element.getAttribute("texcoord");

            for(Effect.newparam np : newparams) {
                if(np.sid.equals(texture)) {
                    if(np.type.equals("sampler2D")) {
                        for(Effect.newparam n : newparams) {
                            if(n.sid.equals(np.value)) {
                                texture = n.value;
                                break;
                            }
                        }
                    } else {
                        texture = np.value;
                    }
                    break;
                }
            }
        }

        /**
         * @return the texture name
         */
        public String getTexture() {
            return texture;
        }

        /**
         * @return something for texture coordinates
         */
        public String getTexturecoord() {
            return texturecoord;
        }
    }
}
