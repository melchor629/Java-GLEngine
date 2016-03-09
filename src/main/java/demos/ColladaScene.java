package demos;

import org.melchor629.engine.loaders.collada.Instance_Geometry;
import org.melchor629.engine.loaders.collada.Node;
import org.melchor629.engine.loaders.collada.VisualScene;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.objects.Material;
import org.melchor629.engine.objects.Model;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.Vector4;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Loads a Collada Scene and renders it
 */
public class ColladaScene {
    private ArrayList<Model> models;
    private ArrayList<ModelMatrix> modelMatrices;
    private ArrayList<Material> materials;

    public ColladaScene(VisualScene vs) {
        models = new ArrayList<>();
        modelMatrices = new ArrayList<>();
        materials = new ArrayList<>();
        loadModelsInScene(vs);
    }

    //TODO Eliminar basicamente porque cada objecto tendrá un mismo shader para el material
    public void enableAttributes() {
        for(int i = 0; i < materials.size(); i++)
            materials.get(i).enableShaderAttributes(models.get(i));//model.enableAttribs(sh, args);
    }

    //TODO No pasar un shader por lo anterior...
    public void render(Camera camera) {
        for(int i = 0; i < models.size(); i++) {
            materials.get(i).prepareMaterialToDraw(camera, modelMatrices.get(i));
            models.get(i).draw(null, null);
        }
    }

    private void loadModelsInScene(VisualScene vs) {
        vs.nodes.forEach(n -> {
            if(n.isGeometry()) {
                models.add(searchModel(n));
                ModelMatrix model = new ModelMatrix();
                model.setLocation(n.location);
                model.rotate((Vector4) n.rotation.getRow(1));
                model.rotate((Vector4) n.rotation.getRow(2));
                model.rotate((Vector4) n.rotation.getRow(3));
                model.setScale(n.scale);
                modelMatrices.add(model);

                if(((Instance_Geometry) n.instance).instance_material_target != null) {
                    String name = ((Instance_Geometry) n.instance).instance_material_target;
                    materials.add(Material.getMaterial(name));
                } else {
                    materials.add(Material.getMaterial("")); //Default Material
                }
            }
        });
    }

    private Model searchModel(Node node) {
        Model m =  Model.searchModel(node.instance.url.substring(1));
        if(m == null)
            throw new NoSuchElementException("Model " + node.instance.url.substring(1) + " is not loaded");
        return m;
    }
}
