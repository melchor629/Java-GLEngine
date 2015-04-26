import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.loaders.Collada;
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

    public ColladaScene(Collada c, VisualScene vs) {
        models = new ArrayList<>();
        modelMatrices = new ArrayList<>();
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
        for(Node n : vs.nodes) {
            if(n.isGeometry()) {
                models.add(searchModel(n));
                ModelMatrix model = new ModelMatrix();
                model.setLocation(n.location);
                model.rotate(n.rotation.getRow(1));
                model.rotate(n.rotation.getRow(2));
                model.rotate(n.rotation.getRow(3));
                model.setScale(n.scale);
                modelMatrices.add(model);
            }
        }
    }

    private Model searchModel(Node node) {
        Model m =  Model.searchModel(node.instance.url.substring(1));
        if(m == null)
            throw new NoSuchElementException("Model " + node.instance.url.substring(1) + " is not loaded");
        return m;
    }
}
