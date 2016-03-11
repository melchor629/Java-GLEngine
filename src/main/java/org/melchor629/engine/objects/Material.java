package org.melchor629.engine.objects;

import org.melchor629.engine.Game;
import org.melchor629.engine.gl.ShaderProgram;
import org.melchor629.engine.gl.Texture;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.CommonColorOrTextureType;
import org.melchor629.engine.loaders.collada.Effect;
import org.melchor629.engine.loaders.collada.Phong;
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
    private float shininess;
    private ShaderProgram phongShader;
    private final static List<Material> materialList;

    static {
        materialList = new ArrayList<>();
    }

    public static void loadMaterials(Game game, Collada c) throws IOException {
        c.materials.forEach(material -> {
            Effect fx = c.searchForEffectWithId(material.instance_effect_url);
            if(fx == null)
                throw new NoSuchElementException("Effect with id " + material.instance_effect_url + " doesn't exists");
            Material mat = new Material(game, fx, material);
            materialList.add(mat);
        });
    }

    public static Material getMaterial(String id) {
        if(id.startsWith("#")) id = id.substring(1);
        int pos = 0;
        while(pos < materialList.size() && (!materialList.get(pos).name.equals(id) && !materialList.get(pos).id.equals(id)))
            pos++;
        return pos < materialList.size() ? materialList.get(pos) : null;
    }

    private Material(Game game, Effect effect, org.melchor629.engine.loaders.collada.Material material) {
        id = material.id;
        this.name = material.name;
        if(effect.getPhong() == null)
            throw new IllegalArgumentException("This effect has no Phong as type of Common Effect");

        Phong p = effect.getPhong();
        diffuse = colorOrDefault(p.getDiffuse(), 1, 0, 1);
        specular = colorOrDefault(p.getSpecular(), 1, 1, 1);
        ambient = colorOrDefault(p.getAmbient(), .1f, .1f, .1f);
        emission = colorOrDefault(p.getEmission(), 0, 0, 0);
        reflective = colorOrDefault(p.getReflective(), 1, 1, 1);
        diffuseTex = textureOrNull(game, p.getDiffuse());
        specularTex = textureOrNull(game, p.getSpecular());
        ambientTex = textureOrNull(game, p.getAmbient());
        emissionTex = textureOrNull(game, p.getEmission());
        reflectiveTex = textureOrNull(game, p.getReflective());
        shininess = p.getShininess();
        phongShader(game);
    }

    //Default Material constructor
    public Material(Game game) {
        id = "";
        name = "Default Material";
        diffuse = new Vector4(1, 0, 1, 1);
        specular = new Vector4(1, 1, 1, 1);
        ambient = emission = reflective = new Vector4(0, 0, 0, 1);
        shininess = 32;
        phongShader(game);
    }

    private void phongShader(Game game) {
        try {
            phongShader = game.getShaderManager().loadShader("Phong", "phong.vs", "phong.fs");
        } catch(IOException e) {
            RuntimeException r = new RuntimeException("Phong shader not found");
            r.initCause(e);
            throw r;
        }
    }

    private Vector4 colorOrDefault(CommonColorOrTextureType o, float r, float g, float b) {
        if(o != null && o.getColor() != null)
            return o.getColor();
        return new Vector4(r, g, b, 1);
    }

    private Texture textureOrNull(Game game, CommonColorOrTextureType o) {
        if(o != null && o.getTexture() != null)
             return game.getTextureManager().searchTexture(o.getTexture().getTexture());
        return null;
    }

    public void enableShaderAttributes(Model model) {
        model.enableAttribs(phongShader, "position", "normal", "texCoord", "color");
    }

    public void prepareMaterialToDraw(Camera camera, ModelMatrix modelMatrix) {
        phongShader.bind();

        phongShader.setUniform("material.useEmissionTex", emissionTex != null ? 1 : 0);
        phongShader.setUniform("material.useAmbientTex", ambientTex != null ? 1 : 0);
        phongShader.setUniform("material.useDiffuseTex", diffuseTex != null ? 1 : 0);
        phongShader.setUniform("material.useSpecularTex", specularTex != null ? 1 : 0);
        phongShader.setUniform("material.useReflectiveTex", reflectiveTex != null ? 1 : 0);

        phongShader.setUniform("material.diffuse", diffuse.x(), diffuse.y(), diffuse.z());
        if (specular != null)
            phongShader.setUniform("material.specular", specular.x(), specular.y(), specular.z());
        else
            phongShader.setUniform("material.specular", 1, 1, 1);
        if (ambient != null)
            phongShader.setUniform("material.ambient", ambient.x(), ambient.y(), ambient.z());
        if (emission != null)
            phongShader.setUniform("material.emission", emission.x(), emission.y(), emission.z());
        if (reflective != null)
            phongShader.setUniform("material.reflective", reflective.x(), reflective.y(), reflective.z());

        phongShader.setUniform("material.shininess", shininess);

        phongShader.setUniformMatrix("model", modelMatrix.getModelMatrix());
        phongShader.setUniformMatrix("view", camera.getViewMatrix());
        phongShader.setUniformMatrix("projection", camera.getProjectionMatrix());
        phongShader.setUniform("cameraPos", camera.getPosition().x(), camera.getPosition().y(), camera.getPosition().z());
        phongShader.setUniform("cameraDir", camera.getLookingAtDirection().x(), camera.getLookingAtDirection().y(),
                camera.getLookingAtDirection().z());

        //TODO Eliminar
        //phongShader.setUniform("light.pos", 4.076245f, 1.005454f, 5.903862f);
        phongShader.setUniform("light.pos", camera.pos.x(), camera.pos.y(), camera.pos.z());
        phongShader.setUniform("light.dir", camera.getLookingAtDirection().x(), camera.getLookingAtDirection().y(),
                camera.getLookingAtDirection().z());
        phongShader.setUniform("light.cutOff", (float) Math.toRadians(12.5));
        phongShader.setUniform("light.outerCutOff", (float) Math.toRadians(17.5));
        phongShader.setUniform("light.emission", 1.f, 1, 1, 1.f);
        phongShader.setUniform("light.ambient", .1f, .1f, .1f, 1);
        phongShader.setUniform("light.c", 1);
        phongShader.setUniform("light.l", 0.09f);
        phongShader.setUniform("light.q", 0.032f);
    }
}
