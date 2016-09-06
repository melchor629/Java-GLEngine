package org.melchor629.engine.objects.material;

/**
 * Created by melchor9000 on 12/6/16.
 */
public class Material {
    private static volatile int idCount = 0;
    private int id = idCount++;
    private String name = "";
    private boolean fog = false;
    private boolean lights = true;
    private BlendingMode blending;
    private float opacity = 1.0f;
    private boolean transparent = false;
    private boolean depth = true;
    private int vertexColors = 0; //1 = Vertex, 2 = Face
    private boolean visible = true;
    private boolean needsUpdate;

    public enum BlendingMode {
        Disabled, Normal, Additive, Substractive, Multiply, Custom
    }

    public boolean needToBeUpdated() {
        return needsUpdate;
    }

    public void needsUpdate(boolean u) {
        needsUpdate = u;
    }
}
