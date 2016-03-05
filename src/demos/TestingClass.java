package demos;

import org.melchor629.engine.Game;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.GLContext.*;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.gl.types.*;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.Matrix4;
import org.melchor629.engine.utils.math.Vector3;

import java.io.File;
import java.io.IOException;

import static org.melchor629.engine.utils.math.GLM.lookAt;
import static org.melchor629.engine.utils.math.GLM.perspective;

public final class TestingClass {

    final static String vertex_shader = "#version 150 core\n"
            + "in vec3 position;\n"
            + "in vec3 color;\n" //entra el color y...
            + "out vec3 Color;\n"//va al fragment shader
            + "\n"
            + "in vec2 texcoord;\n" //Lo mismo que con color, pero con
            + "out vec2 Texcoord;\n"//Texcoords
            + "\n"
            + "uniform Matrix4 model;\n"
            + "uniform Matrix4 view;\n"
            + "uniform Matrix4 proj;\n"
            + "uniform vec3 overrideColor;\n"
            + "\n"
            + "void main() {\n"
            + "    Color = overrideColor * color;\n"
            + "    Texcoord = texcoord;\n"
            + "    gl_Position = proj * view * model * vec4(position, 1.0);\n"
            + "}\n";
        final static String fragment_shader = "#version 150 core\n"
            + "out vec4 outColor;\n"
            + "in vec3 Color;\n"
            + "in vec2 Texcoord;\n"
            + "\n"
            + "uniform sampler2D kitten;\n"
            + "uniform sampler2D puppy;\n"
            + "uniform float time;\n"
            + "\n"
            + "void main() {\n"
            + "    vec4 colKitten = (texture(kitten, Texcoord));\n"
            + "    vec4 colPuppy = texture(puppy, Texcoord);\n"
            + "    outColor = mix(colKitten, colPuppy, time) * vec4(Color, 1.0);\n"
            + "}\n";
        final static String vertex_shader_eff = "#version 150\n"
            + "in vec2 position;\n"
            + "in vec2 texcoord;\n"
            + "out vec2 Texcoord;\n"
            + "void main() {\n"
            + "    Texcoord = texcoord;\n"
            + "    gl_Position = vec4(position, 0.0, 1.0);\n"
            + "}\n";
        final static String fragment_shader_eff = "#version 150\n"
            + "in vec2 Texcoord;\n"
            + "out vec4 outColor;\n"
            + "uniform sampler2D texFramebuffer;\n"
            + "void BW() {\n"
            + "    outColor = texture(texFramebuffer, Texcoord);\n"
            + "    float avg = 0.2126 * outColor.r + 0.7152 * outColor.g + 0.0722 * outColor.b;\n"
            + "    outColor = vec4(avg, avg, avg, 1.0);\n"
            + "}\n"
            + "\n"
            + "void blur() {\n"
            + "    const float blurSizeH = 1.0 / 300.0;\n"
            + "    const float blurSizeV = 1.0 / 200.0;\n"
            + "    vec4 sum = vec4(0.0);\n"
            + "    for(int x = -4; x <= 4; x++)\n"
            + "        for(int y = -4; y <= 4; y++)\n"
            + "            sum += texture(texFramebuffer, vec2(Texcoord.x + x * blurSizeH, Texcoord.y + y * blurSizeV)) / 81.0;\n"
            + "    outColor = sum;"
            + "}\n"
            + "\n"
            + "void main() {\n"
            + "    blur();"
            + "}\n";

    public static void main(String[] args) throws IOException {
        Window window = Game.window = new LWJGLWindow();
        Timing t = Timing.getGameTiming();
        window.setVsync(true);
        window.setResizable(true);
        window.createWindow(1280, 720, "G5 to engine test");
        GLContext gl = Game.gl = window.createContext();

        VAO vao = new VAO();
        vao.bind();

        BufferObject ebo = new BufferObject(BufferTarget.ELEMENT_ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
        ebo.fillBuffer(new int[] {
                0, 1, 2,
                2, 3, 0
        });

        BufferObject vbo = new BufferObject(BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
        vbo.fillBuffer(new float[] {
              //Position           | Color           | Texcoords
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,

                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,

                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,

                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                 0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                 0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                -0.5f, -0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                -0.5f, -0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,

                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                 0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 1.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                 0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 1.0f, 0.0f,
                -0.5f,  0.5f,  0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 0.0f,
                -0.5f,  0.5f, -0.5f, 1.0f, 1.0f, 1.0f, 0.0f, 1.0f,
                
                //Plano suelo
               -1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                1.0f,  1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
                1.0f,  1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 1.0f, 1.0f,
               -1.0f,  1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 1.0f,
               -1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
        });

        ShaderProgram shader = new ShaderProgram(vertex_shader, fragment_shader);
        shader.setColorOutput("outColor", 0);
        shader.bind();
        shader.enableAttribs(vao, "position", "color", "texcoord");
        shader.vertexAttribPointer("position", 3, type.FLOAT, false, 8 * 4, 0);
        shader.vertexAttribPointer("color", 3, type.FLOAT, false, 8 * 4, 3 * 4);
        shader.vertexAttribPointer("texcoord", 2, type.FLOAT, false, 8 * 4, 6 * 4);
        
        VAO vaoQuad = new VAO();
        vaoQuad.bind();
        BufferObject vboQuad = new BufferObject(BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
        vboQuad.fillBuffer(new float[] {
               -1.0f,  1.0f,  0.0f, 1.0f,
                1.0f,  1.0f,  1.0f, 1.0f,
                1.0f, -1.0f,  1.0f, 0.0f,

                1.0f, -1.0f,  1.0f, 0.0f,
               -1.0f, -1.0f,  0.0f, 0.0f,
               -1.0f,  1.0f,  0.0f, 1.0f
        });
        
        ShaderProgram shaderQuad = new ShaderProgram(vertex_shader_eff, fragment_shader_eff);
        shaderQuad.enableAttribs(vaoQuad, "position", "texcoord");
        shaderQuad.vertexAttribPointer("position", 2, type.FLOAT, false, 16, 0);
        shaderQuad.vertexAttribPointer("texcoord", 2, type.FLOAT, false, 16, 8);
        
        Framebuffer fbo = new Framebuffer();
        Renderbuffer rbo = new Renderbuffer(TextureFormat.DEPTH_STENCIL, 1280, 720);
        Texture texColor = new Texture(TextureFormat.RGB, 1280, 720, TextureExternalFormat.RGB);
        fbo.attachDepthStencilRenderbuffer(rbo);
        fbo.attachColorTexture(texColor, 0);
        fbo.unbind();

        File img = new File("/Users/melchor9000/Dropbox/Conpartido equisdé/sample.png");
        Texture gato, perro;
        gato = new Texture.builder().setFile(img).build();
        img = new File("/Users/melchor9000/Dropbox/Conpartido equisdé/sample2.png");
        perro = new Texture.builder().setFile(img).build();
        
        shader.bind();
        shader.setUniform("kitten", 0);
        shader.setUniform("puppy", 1);

        Matrix4 proj = perspective(45d, 1280d / 720d, 1d, 10d);
        Matrix4 view = lookAt(new Vector3(2.2f, 2.2f, 2.2f), new Vector3(), new Vector3(0, 0, 1));
        ModelMatrix model = new ModelMatrix();
        float time;
        int x = 0;
        shader.setUniformMatrix("view", view);
        shader.setUniformMatrix("proj", proj);
        
        org.lwjgl.glfw.GLFW.glfwSwapInterval(1);

        t.update();
        while(!window.windowShouldClose()) {
            fbo.bind();
            vao.bind();
            gl.enable(GLEnable.DEPTH_TEST);
            shader.bind();
            
            gl.setActiveTexture(0);
            gato.bind();
            gl.setActiveTexture(1);
            perro.bind();
            
            gl.clearColor(1, 1, 1, 1);
            gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT);

            model.setIdentity();
            time = (float) Math.sin(2 * Math.PI * x++ / ((float) t.fps * 4));
            model.rotate((float) (2 * Math.PI * x / ((float) t.fps * 4)), 0, 0, 1);
            shader.setUniform("time", time * time);
            shader.setUniformMatrix("model", model.getModelMatrix());

            gl.drawArrays(DrawMode.TRIANGLES, 0, 36);
            //gl.drawElements(DrawMode.TRIANGLES, 6, type.UNSIGNED_INT, 0);

            gl.enable(GLEnable.STENCIL_TEST);
                gl.stencilFunc(StencilFunc.ALWAYS, 1, 0xFF);
                gl.stencilOp(StencilOp.KEEP, StencilOp.KEEP, StencilOp.REPLACE);
                gl.stencilMask(0xFF);
                gl.depthMask(false);
                gl.clear(GLContext.STENCIL_BUFFER_BIT);
                gl.drawArrays(DrawMode.TRIANGLES, 36, 6);

                gl.stencilFunc(StencilFunc.EQUAL, 1, 0xFF);
                gl.stencilMask(0x00);
                gl.depthMask(true);

                model.setLocation(0, 0, -1).setScale(1, 1, -1);
                shader.setUniformMatrix("model", model.getModelMatrix());
                shader.setUniform("overrideColor", 0.3f, 0.3f, 0.3f);
                gl.drawArrays(DrawMode.TRIANGLES, 0, 36);
                shader.setUniform("overrideColor", 1.0f, 1.0f, 1.0f);
            gl.disable(GLEnable.STENCIL_TEST);

            fbo.unbind();
            vaoQuad.bind();
            gl.disable(GLEnable.DEPTH_TEST);
            shaderQuad.bind();
            gl.setActiveTexture(0);
            texColor.bind();

            gl.drawArrays(DrawMode.TRIANGLES, 0, 6);

            t.update();
            gl._game_loop_sync(60);
        }
        System.out.printf("Tiempo: %.2fs\nTotal de fotogramas: %d\nMedia FPS:%.2f\n", t.totalTime(), t.totalFrames, t.totalFrames/t.totalTime());

        vao.delete();
        ebo.delete();
        vbo.delete();
        shader.delete();
        vaoQuad.delete();
        vboQuad.delete();
        shaderQuad.delete();
        fbo.delete();
        rbo.delete();
        texColor.delete();
        window.destroyWindow();
        System.exit(0);
    }
}
