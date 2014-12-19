import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import org.melchor629.engine.Game;
import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.Renderer.BufferTarget;
import org.melchor629.engine.gl.Renderer.BufferUsage;
import org.melchor629.engine.gl.Renderer.DrawMode;
import org.melchor629.engine.gl.Renderer.GLEnable;
import org.melchor629.engine.gl.Renderer.StencilFunc;
import org.melchor629.engine.gl.Renderer.StencilOp;
import org.melchor629.engine.gl.Renderer.TextureExternalFormat;
import org.melchor629.engine.gl.Renderer.TextureFormat;
import org.melchor629.engine.gl.Renderer.type;
import org.melchor629.engine.gl.types.BufferObject;
import org.melchor629.engine.gl.types.Framebuffer;
import org.melchor629.engine.gl.types.Renderbuffer;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.Texture;
import org.melchor629.engine.gl.types.VAO;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.mat4;
import org.melchor629.engine.utils.math.vec2;
import org.melchor629.engine.utils.math.vec3;

import static org.melchor629.engine.utils.math.GLM.*;

public final class TestingClass {

    final static String vertex_shader = "#version 150 core\n"
            + "in vec3 position;\n"
            + "in vec3 color;\n" //entra el color y...
            + "out vec3 Color;\n"//va al fragment shader
            + "\n"
            + "in vec2 texcoord;\n" //Lo mismo que con color, pero con
            + "out vec2 Texcoord;\n"//Texcoords
            + "\n"
            + "uniform mat4 model;\n"
            + "uniform mat4 view;\n"
            + "uniform mat4 proj;\n"
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

    public static void main(String[] args) throws IOException {//nada(); System.exit(1);
        Renderer gl = Game.gl = new LWJGLRenderer();
        Timing t = new Timing();
        boolean cr;
        gl.setVsync(true);
        gl.setResizable(true);
        cr = gl.createDisplay((short) 1280, (short) 720, false, "G5 to engine test");
        
        if(!cr) {
        	System.out.printf("Error al crear la ventana...");
        	System.exit(-1);
        }

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
        shader.enableAttribs("position", "color", "texcoord");
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
        shaderQuad.enableAttribs("position", "texcoord");
        shaderQuad.vertexAttribPointer("position", 2, type.FLOAT, false, 16, 0);
        shaderQuad.vertexAttribPointer("texcoord", 2, type.FLOAT, false, 16, 8);
        
        Framebuffer fbo = new Framebuffer();
        Renderbuffer rbo = new Renderbuffer(TextureFormat.DEPTH_STENCIL, 1280, 720);
        Texture texColor = new Texture(TextureFormat.RGB, 1280, 720, TextureExternalFormat.RGB);
        fbo.attachDepthStencilRenderbuffer(rbo);
        fbo.attachColorTexture(texColor, 0);
        fbo.unbind();

        File img = new File("/Users/melchor9000/Dropbox/Conpartido equisdé/sample.png");
        Texture gato, perro;
        gato = new Texture.builder().setFile(img).build();
        img = new File("/Users/melchor9000/Dropbox/Conpartido equisdé/sample2.png");
        perro = new Texture.builder().setFile(img).build();
        
        shader.bind();
        shader.setUniform("kitten", 0);
        shader.setUniform("puppy", 1);

        mat4 proj = perspective(45d, 1280d / 720d, 1d, 10d);
        mat4 view = lookAt(new vec3(2.2f, 2.2f, 2.2f), new vec3(), new vec3(0, 0, 1));
        mat4 rotate;
        float time;
        int x = 0;
        shader.setUniformMatrix("view", view);
        shader.setUniformMatrix("proj", proj);

        t.update();
        while(!gl.windowIsClosing()) {
            fbo.bind();
            vao.bind();
            gl.enable(GLEnable.DEPTH_TEST);
            shader.bind();
            
            gl.setActiveTexture(0);
            gato.bind();
            gl.setActiveTexture(1);
            perro.bind();
            
            gl.clearColor(1, 1, 1, 1);
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);

            time = (float) Math.sin(2 * Math.PI * x++ / ((float) t.fps * 4));
            rotate = rotateMatrix((float) (2 * Math.PI * x / ((float) t.fps * 4)), new vec3(0, 0, 1));
            shader.setUniform("time", time * time);
            shader.setUniformMatrix("model", rotate);

            gl.drawArrays(DrawMode.TRIANGLES, 0, 36);

            gl.enable(GLEnable.STENCIL_TEST);
                gl.stencilFunc(StencilFunc.ALWAYS, 1, 0xFF);
                gl.stencilOp(StencilOp.KEEP, StencilOp.KEEP, StencilOp.REPLACE);
                gl.stencilMask(0xFF);
                gl.depthMask(false);
                gl.clear(Renderer.STENCIL_BUFFER_BIT);
                gl.drawArrays(DrawMode.TRIANGLES, 36, 6);

                gl.stencilFunc(StencilFunc.EQUAL, 1, 0xFF);
                gl.stencilMask(0x00);
                gl.depthMask(true);

                rotate = translateMatrix(rotate, new vec3(0, 0, -1));
                rotate = scaleMatrix(rotate, new vec3(1, 1, -1));
                shader.setUniformMatrix("model", rotate);
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
        System.out.printf("Tiempo: %.2fs\nTotal de fotogramas: %d", (double) t.totalFrames / 60d, t.totalFrames);

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
        System.exit(0);
    }

    public final static void nada() throws IOException {
        org.melchor629.engine.utils.IOUtils.Image a;
        a = org.melchor629.engine.utils.IOUtils.readImage(new File("/Users/melchor9000/desktop/Turn Down For What.png"));
        vec2 pos = new vec2(251, 128), oldPos = new vec2(-1, -1);
        ArrayList<Byte> data = new ArrayList<Byte>();
        
        boolean hasNext = true;
        while(hasNext) {
            data.add(getPixel(a, pos));

            byte[] nearPixels = new byte[8];
            for(int i = 0; i < nearPixels.length; i++)
                nearPixels[i] = getPixel(a, getNearPixelsPos(i, pos));

            hasNext = false;
            for(int i = 0; i < nearPixels.length; i++) {
                byte pixel = nearPixels[i];
                
                if(pixel != 0) {
                    vec2 nextPos = getNearPixelsPos(i, pos);
                    if(nextPos.x != oldPos.x || nextPos.y != oldPos.y) {
                        oldPos = pos;
                        pos = nextPos;
                        hasNext = true;
                        break;
                    }
                }
            }
        }

        //Write out the sample
        FileOutputStream fos = new FileOutputStream(new File("/Users/melchor9000/desktop/sample.bin"));
        for(int i = 0; i < data.size(); i++)
            fos.write(data.get(i));
        fos.close();
        
    }

    public final static byte getPixel(org.melchor629.engine.utils.IOUtils.Image a, vec2 pos) {
        //Get RGB from ImageIO.read() ...
        byte r = a.buffer[(int) (pos.y * a.width + pos.x) * a.channels];
        byte g = a.buffer[(int) (pos.y * a.width + pos.x) * a.channels];
        byte b = a.buffer[(int) (pos.y * a.width + pos.x) * a.channels];
        assert(r != b && b != g);
        return r;
    }

    public final static vec2 getNearPixelsPos(int direction, vec2 pos) {
        //Near pixels from where you are
        switch(direction) {
            case 0: return new vec2(pos.x    , pos.y - 1);
            case 1: return new vec2(pos.x + 1, pos.y - 1);
            case 2: return new vec2(pos.x + 1, pos.y    );
            case 3: return new vec2(pos.x + 1, pos.y + 1);
            case 4: return new vec2(pos.x    , pos.y + 1);
            case 5: return new vec2(pos.x - 1, pos.y + 1);
            case 6: return new vec2(pos.x - 1, pos.y    );
            case 7: return new vec2(pos.x - 1, pos.y - 1);
            default: return null;
        }
    }
}
