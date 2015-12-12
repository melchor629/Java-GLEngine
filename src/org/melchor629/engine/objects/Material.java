package org.melchor629.engine.objects;

import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.Texture;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.Effect;
import org.melchor629.engine.loaders.collada.Phong;
import org.melchor629.engine.utils.ShaderManager;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.Vector4;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Material with shader
 */
public class Material {
    private String id, name;
    private Vector4 emission, ambient, diffuse, specular, reflective;
    private Texture emissionTex, ambientTex, diffuseTex, specularTex, reflectiveTex;
    private static ShaderProgram phongShader;
    private final static List<Material> materialList;
    private final static Material defaultMaterial;

    static {
        try {
            phongShader = ShaderManager.getInstance().loadShader("Phong", "phong.vs", "phong.fs");
        } catch(IOException ignore) {}
        materialList = new ArrayList<>();
        defaultMaterial = new Material();
    }

    public static void loadMaterials(Collada c) {
        c.materials.forEach(material -> {
            Effect fx = c.searchForEffectWithId(material.instance_effect_url);
            if(fx == null)
                throw new NoSuchElementException("Effect with id " + material.instance_effect_url + " doesn't exists");
            materialList.add(new Material(fx, material.name));
        });
    }

    public static Material getMaterial(String id) {
        int pos = 0;
        while(pos < materialList.size() && (!materialList.get(pos).name.equals(id) || !materialList.get(pos).id.equals(id)))
            pos++;
        return pos < materialList.size() ? materialList.get(pos) : defaultMaterial;
    }

    private Material(Effect effect, String name) {
        id = effect.getId();
        this.name = name;
        if(effect.getPhong() == null)
            throw new IllegalArgumentException("This effect has no Phong as type of Common Effect");

        Phong p = effect.getPhong();
        diffuse = p.getDiffuse().getColor();
    }

    //Default Material constructor
    private Material() {
        id = "";
        name = "Default Material";
        diffuse = new Vector4(1, 1, 1, 1);
    }

    public void enableShaderAttributes(Model model) {
        model.enableAttribs(phongShader, "position", "normal", "texcoord", "color");
    }

    public void prepareMaterialToDraw(Camera camera, ModelMatrix modelMatrix) {
        phongShader.bind();
        phongShader.setUniform("useEmissionTex", emissionTex != null ? 1 : 0);
        phongShader.setUniform("useAmbientTex", ambientTex != null ? 1 : 0);
        phongShader.setUniform("useDiffuseTex", diffuseTex != null ? 1 : 0);
        phongShader.setUniform("useSpecularTex", specularTex != null ? 1 : 0);
        phongShader.setUniform("useReflectiveTex", reflectiveTex != null ? 1 : 0);

        phongShader.setUniform("diffuse", diffuse.x, diffuse.y, diffuse.z, diffuse.w);

        phongShader.setUniformMatrix("model", modelMatrix.getModelMatrix());
        phongShader.setUniformMatrix("view", camera.getViewMatrix());
        phongShader.setUniformMatrix("projection", camera.getProjectionMatrix());
    }
}
