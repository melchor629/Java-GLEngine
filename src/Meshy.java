import org.melchor629.engine.Game;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.types.BufferObject;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.VAO;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.*;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.math.ModelMatrix;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * From Dart experiment
 */
public class Meshy {
    VAO vao;
    BufferObject vertexBuffer, normalBuffer, texCoordBuffer, indexBuffer, colorBuffer;
    Geometry geometry;
    Node node;
    ModelMatrix model;

    Meshy(Collada c, Node node) {
        if(!node.isGeometry()) {
            throw new IllegalArgumentException("Nodo no es una instancia de Geometría");
        }

        this.node = node;
        String url = ((Instance_Geometry) node.instance).url;
        geometry = c.searchForGeometryWithId(url);
        if(geometry == null)
            throw new InternalError("No se ha encontrado objeto...");

        model = new ModelMatrix();
        model.setLocation(node.location);
        model.rotate(node.rotation.getRow(1));
        model.rotate(node.rotation.getRow(2));
        model.rotate(node.rotation.getRow(3));
        model.setScale(node.scale);

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
        vao = new VAO();
        vertexBuffer = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);
        normalBuffer = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);
        colorBuffer = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);
        indexBuffer = new BufferObject(Renderer.BufferTarget.ELEMENT_ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);
        if(sources == 3)
            texCoordBuffer = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);

        IntBuffer ibuff = BufferUtils.createIntBuffer(m.polylist.p.capacity() / sources);
        for(int i = 0; i < m.polylist.p.capacity(); i += sources)
            ibuff.put(m.polylist.p.get(i));
        ibuff.flip();
        indexBuffer.fillBuffer(ibuff);

        vertexBuffer.fillBuffer(m.sources.get(0).buff);
        normalBuffer.fillBuffer(reorganizarVectores(m.sources.get(1).buff, 3, obtenerRelación(m.polylist.p, 1, sources)));
        colorBuffer.fillBuffer(reorganizarVectores(m.sources.get(1).buff, 3, obtenerRelación(m.polylist.p, 1, sources)));
        if(sources == 3)
            texCoordBuffer.fillBuffer(reorganizarVectores(m.sources.get(2).buff, 2, obtenerRelación(m.polylist.p, 2, sources)));
        indexBuffer.unbind();
        vertexBuffer.unbind();
    }

    public void enableAttribs(ShaderProgram s, String... attribs) {
        s.bind();
        vao.bind();
        indexBuffer.bind();

        vertexBuffer.bind();
        s.vertexAttribPointer(attribs[0], 3, Renderer.type.FLOAT, false, 0, 0);
        s.enableAttrib(attribs[0]);

        normalBuffer.bind();
        s.vertexAttribPointer(attribs[1], 3, Renderer.type.FLOAT, false, 0, 0);
        s.enableAttrib(attribs[1]);

        colorBuffer.bind();
        s.vertexAttribPointer(attribs[2], 3, Renderer.type.FLOAT, false, 0, 0);
        s.enableAttrib(attribs[2]);

        vao.unbind();
        s.unbind();
        vertexBuffer.unbind();
        indexBuffer.unbind();
    }

    public void draw(ShaderProgram s) {
        int facesCount = geometry.mesh.polylist.count;
        s.setUniformMatrix("model", model.getModelMatrix());
        vao.bind();
        Game.gl.drawElements(Renderer.DrawMode.TRIANGLES, facesCount * 3, Renderer.type.UNSIGNED_INT, 0);
        AnotherTestingClass.printError();
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
