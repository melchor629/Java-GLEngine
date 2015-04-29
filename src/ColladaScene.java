import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.Instance_Geometry;
import org.melchor629.engine.loaders.collada.Material;
import org.melchor629.engine.loaders.collada.Node;
import org.melchor629.engine.loaders.collada.VisualScene;
import org.melchor629.engine.objects.Model;
import org.melchor629.engine.utils.math.ModelMatrix;

import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Loads a Collada Scene and renders it
 */
public class ColladaScene {
    private ArrayList<Model> models;
    private ArrayList<ModelMatrix> modelMatrices;
    private ArrayList<org.melchor629.engine.objects.Material> materials;

    public ColladaScene(Collada c, VisualScene vs) {
        models = new ArrayList<>();
        modelMatrices = new ArrayList<>();
        materials = new ArrayList<>();
        loadModelsInScene(c, vs);
    }

    //TODO Eliminar basicamente porque cada objecto tendrá un mismo shader para el material
    public void enableAttributes(ShaderProgram sh, String... args) {
        for(Model model : models)
            model.enableAttribs(sh, args);
    }

    //TODO No pasar un shader por lo anterior...
    public void render(ShaderProgram s) {
        for(int i = 0; i < models.size(); i++)
            models.get(i).draw(s, modelMatrices.get(i));
    }

    private void loadModelsInScene(Collada c, VisualScene vs) {
        vs.nodes.forEach(n -> {
            if(n.isGeometry()) {
                models.add(searchModel(n));
                ModelMatrix model = new ModelMatrix();
                model.setLocation(n.location);
                model.rotate(n.rotation.getRow(1));
                model.rotate(n.rotation.getRow(2));
                model.rotate(n.rotation.getRow(3));
                model.setScale(n.scale);
                modelMatrices.add(model);

                if(((Instance_Geometry) n.instance).instance_material_target != null) {
                    String name = ((Instance_Geometry) n.instance).name;
                    materials.add(org.melchor629.engine.objects.Material.getMaterial(name));
                } else {
                    materials.add(org.melchor629.engine.objects.Material.getMaterial("")); //Default Material
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
