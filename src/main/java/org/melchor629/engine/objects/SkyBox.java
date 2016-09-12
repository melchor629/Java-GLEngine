package org.melchor629.engine.objects;

import org.melchor629.engine.gl.*;
import org.melchor629.engine.utils.math.Matrix4;
import org.melchor629.engine.utils.math.Vector4;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * This class allows you to render a simple SkyBox and load the
 * textures needed for the cubemap texture.<br>
 * Skybox needs to be renderer at the end of the 3D rendering,
 * because is optimized enough to only call the fragment shader
 * only when there's nothing in front of the sky.
 */
public class SkyBox {
    private static final String vert =
            "#version 330 core\n" +
            "layout (location = 0) in vec3 position;\n" +
            "out vec3 texCoords;\n" +
            "uniform mat4 projection;\n" +
            "uniform mat4 view;\n" +
            "\n" +
            "void main() {\n" +
            "    vec4 pos = projection * view * vec4(position, 1.0);\n" +
            "    gl_Position = pos.xyww;" +
            "    texCoords = position;" +
            "}";
    private static final String frag =
            "#version 330 core\n" +
            "in vec3 texCoords;\n" +
            "out vec4 color;\n" +
            "uniform samplerCube skybox;" +
            "\n" +
            "void main() {\n" +
            "    color = texture(skybox, texCoords);\n" +
            "}";

    private CubemapTexture cubemap;
    private ShaderProgram skyboxShader;
    private VertexArrayObject skyboxVao;
    private final GLContext gl;

    /**
     * Creates a SkyBox and loads its textures. Textures are loaded using
     * {@link GLContext#createCubemap(GLContext.TextureFormat, File, String)}.
     * @param gl the OpenGL Context
     * @param path path to the folder containing the 6 images
     * @param ext extension of the images
     * @throws FileNotFoundException if some file don't exist
     * @see GLContext#createCubemap(GLContext.TextureFormat, File, String)
     */
    public SkyBox(GLContext gl, File path, String ext) throws FileNotFoundException {
        this.gl = gl;
        cubemap = gl.createCubemap(GLContext.TextureFormat.RGB, path, ext);
        skyboxShader = gl.createShader(vert, frag);
        skyboxVao = gl.createVertexArrayObject();
        BufferObject cube = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);

        cube.fillBuffer(new float[] {
                // Positions
                -1.0f,  1.0f, -1.0f,
                -1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f, -1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,

                -1.0f, -1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f,
                -1.0f, -1.0f,  1.0f,

                -1.0f,  1.0f, -1.0f,
                 1.0f,  1.0f, -1.0f,
                 1.0f,  1.0f,  1.0f,
                 1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f,  1.0f,
                -1.0f,  1.0f, -1.0f,

                -1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                 1.0f, -1.0f, -1.0f,
                 1.0f, -1.0f, -1.0f,
                -1.0f, -1.0f,  1.0f,
                 1.0f, -1.0f,  1.0f
        });

        skyboxVao.bind();
        skyboxShader.bind();
        cube.bind();
        skyboxShader.vertexAttribPointer("position", 3, GLContext.type.FLOAT, false, 3 * 4, 0);
        skyboxShader.enableAttrib("position");
        skyboxShader.setColorOutput("color", 0);
        cube.unbind();
        skyboxShader.unbind();
        skyboxVao.unbind();
    }

    /**
     * Renders the SkyBox. This method should be called at the end of
     * the 3D rendering, not before.
     * @param camera camera
     */
    public void render(Camera camera) {
        int func = gl.getInt(GLContext.GLGet.DEPTH_FUNC);
        int mode = gl.getInt(GLContext.GLGet.CULL_FACE_MODE);
        gl.depthFunc(GLContext.DepthFunction.LEQUAL);
        gl.cullFace(GLContext.CullFaceMode.FRONT);

        Matrix4 view = (Matrix4) camera.getViewMatrix().clone();
        view.setColumn(4, new Vector4(0, 0, 0, 0));
        view.setRow(4, new Vector4(0, 0, 0, 0));
        skyboxShader.bind();
        skyboxShader.setUniformMatrix("projection", camera.getProjectionMatrix());
        skyboxShader.setUniformMatrix("view", view);
        skyboxVao.bind();
        gl.setActiveTexture(0);
        cubemap.bind();
        gl.drawArrays(GLContext.DrawMode.TRIANGLES, 0, 36);
        cubemap.unbind();
        skyboxVao.unbind();
        skyboxShader.unbind();

        gl.depthFunc(GLContext.valueOf(GLContext.DepthFunction.class, func));
        gl.cullFace(GLContext.valueOf(GLContext.CullFaceMode.class, mode));
    }
}
