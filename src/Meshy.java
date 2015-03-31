import org.melchor629.engine.Game;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.types.BufferObject;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.VAO;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.Geometry;
import org.melchor629.engine.loaders.collada.Instance_Geometry;
import org.melchor629.engine.loaders.collada.Mesh;
import org.melchor629.engine.loaders.collada.Node;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.math.ModelMatrix;

/**
 * From Dart experiment
 */
public class Meshy {
    VAO vao;
    BufferObject vbo, ebo;
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
        model.translate(node.location);
        model.rotate(node.rotation.getRow(1));
        model.rotate(node.rotation.getRow(2));
        model.rotate(node.rotation.getRow(3));
        model.scale(node.scale);

        generateBuffers();
        geometry.mesh.disposeData();
    }

    private void generateBuffers() {
        Mesh m = geometry.mesh;
        vao = new VAO();
        vbo = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);
        ebo = new BufferObject(Renderer.BufferTarget.ELEMENT_ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);

        //vao.bind();
        ebo.bind();
        ebo.fillBuffer(m.polylist.p);

        vbo.bind();
        vbo.initPartialFillBuffer(4 * m.polylist.count * 3 * 3 * m.sources.size());

        int vPos = 0, nPos = m.polylist.count * 3 * 3 * 4, tPos = nPos * 2;
        java.nio.FloatBuffer vBuff = BufferUtils.createFloatBuffer(3),
                nBuff = BufferUtils.createFloatBuffer(3),
                tBuff = null;
        if(m.sources.size() == 3)
            tBuff = BufferUtils.createFloatBuffer(2);
        for(int i = 0; i < m.polylist.count * 3; i++) {
            int vcount = m.polylist.vcount.get(i / 3);
            int indexVert = m.polylist.p.get(2 * i) * vcount,
                indexNormal = m.polylist.p.get(2 * i + 1) * vcount,
                indexText = -1;

            if(tBuff != null)
                indexText = m.polylist.p.get(2 * i * 2) * vcount;

            vBuff.put(m.sources.get(0).buff.get(indexVert));
            vBuff.put(m.sources.get(0).buff.get(indexVert + 1));
            vBuff.put(m.sources.get(0).buff.get(indexVert + 2));
            nBuff.put(m.sources.get(1).buff.get(indexNormal));
            nBuff.put(m.sources.get(1).buff.get(indexNormal + 1));
            nBuff.put(m.sources.get(1).buff.get(indexNormal + 2));
            if(tBuff != null) {
                tBuff.put(m.sources.get(2).buff.get(indexText));
                tBuff.put(m.sources.get(2).buff.get(indexText + 1));
            }

            vbo.partiallyFillBuffer(vPos, vBuff);
            vbo.partiallyFillBuffer(nPos, nBuff);
            if(tBuff != null) {
                vbo.partiallyFillBuffer(tPos, tBuff);
                tBuff.clear();
            }
            vBuff.clear();
            nBuff.clear();

            vPos += 4 * 3;
            nPos += 4 * 3;
            tPos += 4 * 2;
        }

        //vao.unbind();
        ebo.unbind();
        vbo.unbind();
    }

    public void enableAttribs(ShaderProgram s, String... attribs) {
        int facesCount = geometry.mesh.polylist.count;
        vao.bind();
        ebo.bind();
        vbo.bind();

        s.vertexAttribPointer(attribs[0], 3, Renderer.type.FLOAT, false, 3 * 4, 0);
        s.vertexAttribPointer(attribs[1], 3, Renderer.type.FLOAT, false, 3 * 4, facesCount * 3 * 3 * 4);
        s.vertexAttribPointer(attribs[2], 3, Renderer.type.FLOAT, false, 3 * 4, facesCount * 3 * 3 * 4); //TODO delete
        s.enableAttribs(vao, attribs);

        vao.unbind();
        vbo.unbind();
        ebo.bind();
    }

    public void draw(ShaderProgram s) {
        int facesCount = geometry.mesh.polylist.count;
        s.setUniformMatrix("model", model.getModelMatrix());
        vao.bind();
        //vbo.bind();
        //ebo.bind();
        Game.gl.drawArrays(Renderer.DrawMode.TRIANGLES, 0, facesCount * 3);
        vao.unbind();
    }

    public void delete() {
        vao.delete();
        ebo.delete();
        vbo.delete();
    }
}
