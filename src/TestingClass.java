import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.Renderer.BufferTarget;
import org.melchor629.engine.gl.Renderer.BufferUsage;
import org.melchor629.engine.gl.Renderer.DrawMode;
import org.melchor629.engine.gl.Renderer.GLEnable;
import org.melchor629.engine.gl.Renderer.TextureExternalFormat;
import org.melchor629.engine.gl.Renderer.TextureFormat;
import org.melchor629.engine.gl.Renderer.type;
import org.melchor629.engine.gl.types.BufferObject;
import org.melchor629.engine.gl.types.Framebuffer;
import org.melchor629.engine.gl.types.Renderbuffer;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.Texture;
import org.melchor629.engine.gl.types.VAO;

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

    public static void main(String[] args) {
        Renderer gl = new LWJGLRenderer();
        gl.createDisplay((short) 1280, (short) 720, false, "G5 to engine test");
        gl.setVsync(true);
        gl.setResizable(true);
        
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
        shader.enableAttribs("position", "color", "texcoord");
        shader.bind();
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
        
        while(!gl.windowIsClosing()) {
            fbo.bind();
            vao.bind();
            gl.enable(GLEnable.DEPTH_TEST);
            shader.bind();
            
            //Textures TODO
            
            gl.clearColor(1, 1, 1, 1);
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
            
            gl.drawArrays(DrawMode.TRIANGLES, 0, 36);
            
            gl.enable(GLEnable.STENCIL_TEST);
                //TODO
            gl.disable(GLEnable.STENCIL_TEST);
            
            fbo.unbind();
            vaoQuad.bind();
            gl.disable(GLEnable.DEPTH_TEST);
            shaderQuad.bind();
            gl.activeTexture(0);
            texColor.bind();
            
            gl.drawArrays(DrawMode.TRIANGLES, 0, 6);
            
            gl._game_loop_sync(60);
        }
        
        
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

}
