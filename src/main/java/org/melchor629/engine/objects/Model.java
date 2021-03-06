package org.melchor629.engine.objects;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.BufferObject;
import org.melchor629.engine.gl.ShaderProgram;
import org.melchor629.engine.gl.VertexArrayObject;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.*;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.math.ModelMatrix;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * From Dart experiment
 */
public class Model implements Erasable {
    private VertexArrayObject vao;
    private BufferObject vertexBuffer, normalBuffer, texCoordBuffer, indexBuffer;
    private final Geometry geometry;
    private final GLContext gl;

    private static ArrayList<Model> models;

    /**
     * Load all models from a Collada document
     * @param gl OpenGL Context object
     * @param c Collada document
     */
    public static void loadModels(GLContext gl, Collada c) {
        if(models == null)
            models = new ArrayList<>(c.geometry.size());
        models.addAll(c.geometry.stream().map(g -> new Model(gl, g)).collect(Collectors.toList()));
    }

    /**
     * Searche a model from its ID
     * @param str model's ID
     * @return the Model or null
     */
    public static Model searchModel(String str) {
        if(models == null)
            throw new IllegalAccessError("Cannot search for models if they are not loaded");
        int i = 0;
        while(i < models.size() && !models.get(i).geometry.id.equals(str))
            i++;
        return i < models.size() ? models.get(i) : null;
    }

    /**
     * Deletes all models from GPU and Memory
     */
    public static void deleteModels() {
        models.forEach(Model::delete);
        models.clear();
    }

    /**
     * Loads a model from a Collada file
     * @param g Geometry node
     */
    private Model(GLContext gl, Geometry g) {
        this.gl = gl;
        geometry = g;
        generateBuffers();
        geometry.mesh.disposeData();
    }

    private Map<Integer,Integer> obtenerRelación(IntBuffer p, int off, int dist) {
        Map<Integer,Integer> f = new LinkedHashMap<>(p.capacity());

        for(int i = 0; i < p.capacity(); i+=dist) {
            f.put(p.get(i), p.get(i+off));
        }

        return f;
    }

    private FloatBuffer reorganizarVectores(FloatBuffer antes, int comp, Map<Integer,Integer> f) {
        FloatBuffer buff = BufferUtils.createFloatBuffer(f.size() * comp);

        for(int ind : f.keySet()) {
            for(int i = 0; i < comp; i++)
                buff.put(comp * ind + i, antes.get(comp * f.get(ind) + i));
        }

        return buff;
    }

    private void generateBuffers() {
        Mesh m = geometry.mesh;
        int sources = m.sources.size();
        vao = gl.createVertexArrayObject();
        vertexBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        normalBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        indexBuffer = gl.createBufferObject(GLContext.BufferTarget.ELEMENT_ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        if(sources == 3)
            texCoordBuffer = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);

        IntBuffer ibuff = BufferUtils.createIntBuffer(m.polylist.p.capacity() / sources);
        for(int i = 0; i < m.polylist.p.capacity(); i += sources)
            ibuff.put(m.polylist.p.get(i));
        ibuff.flip();
        indexBuffer.fillBuffer(ibuff);

        vertexBuffer.fillBuffer(m.sources.get(0).buff);
        normalBuffer.fillBuffer(reorganizarVectores(m.sources.get(1).buff, 3, obtenerRelación(m.polylist.p, 1, sources)));
        if(sources == 3)
            texCoordBuffer.fillBuffer(reorganizarVectores(m.sources.get(2).buff, 2, obtenerRelación(m.polylist.p, 2, sources)));
        indexBuffer.unbind();
        vertexBuffer.unbind();
    }

    public void enableAttribs(ShaderProgram s, String pos, String norm, String tex) {
        s.bind();
        vao.bind();
        indexBuffer.bind();

        if(pos != null) {
            vertexBuffer.bind();
            s.vertexAttribPointer(pos, 3, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(pos);
        }

        if(norm != null) {
            normalBuffer.bind();
            s.vertexAttribPointer(norm, 3, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(norm);
        }

        if(tex != null && texCoordBuffer != null) {
            texCoordBuffer.bind();
            s.vertexAttribPointer(tex, 2, GLContext.type.FLOAT, false, 0, 0);
            s.enableAttrib(tex);
        }

        vao.unbind();
        s.unbind();
        vertexBuffer.unbind();
        indexBuffer.unbind();
    }

    public void draw(ShaderProgram s, ModelMatrix model) {
        int facesCount = geometry.mesh.polylist.count;
        if(s != null && model != null)
            s.setUniformMatrix("model", model.getModelMatrix());
        vao.bind();
        gl.drawElements(GLContext.DrawMode.TRIANGLES, facesCount * 3, GLContext.type.UNSIGNED_INT, 0);
        vao.unbind();
    }

    public void delete() {
        vao.delete();
        indexBuffer.delete();
        vertexBuffer.delete();
        normalBuffer.delete();
        if(texCoordBuffer != null) texCoordBuffer.delete();
    }
}
