package demos;

import org.lwjgl.nanovg.NVGColor;
import org.lwjgl.nanovg.NanoVG;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.*;
import org.melchor629.engine.gui.GUIDrawUtils;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.utils.MemoryUtils;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.ImageIO;
import org.melchor629.engine.utils.math.Vector3;
import org.melchor629.engine.window.LWJGLWindow;
import org.melchor629.engine.window.WindowBuilder;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.Random;
import java.util.TreeSet;

/**
 * No hacer caso a esto
 */
public class EspacioEuclideo extends Game {
    private static final Random rand = new Random();
    private static int WIDTH = 1280;
    private static int HEIGHT = 720;

    public static void main(String... args) {
        new EspacioEuclideo();
    }

    private static TreeSet<Vector3> generarEspacioEuclídeo() {
        TreeSet<Vector3> puntos_lista = new TreeSet<>((o1, o2) ->
                Math.round((o2.module() - o1.module()) * 1000.f)
        );

        for(int i = 0; i < 10000; i++) {
            Vector3 punto = generarPunto((WIDTH + HEIGHT), WIDTH / 20.f, HEIGHT / 20.f);
            while(!esVálido(punto) || !puntos_lista.add(punto))
                punto = generarPunto((WIDTH + HEIGHT), WIDTH / 20.f, HEIGHT / 20.f);
        }

        return puntos_lista;
    }

    private static boolean esVálido(Vector3 punto) {
        return Math.abs(punto.x()) > 10f && Math.abs(punto.y()) > .5f && Math.abs(punto.z()) > .5f;
    }

    private static Vector3 generarPunto(float max_x, float max_y, float max_z) {
        return new Vector3(
            rand.nextFloat() * max_x,
            rand.nextFloat() * max_y - max_y / 2f,
            rand.nextFloat() * max_z - max_z / 2f
        );
    }

    private static int obtenerVisibles(FloatBuffer puntos_buff, Camera cam, TreeSet<Vector3> puntos) {
        final int cantidad[] = {0};
        puntos_buff.flip().clear();
        puntos.stream().filter((vec) -> {
            float x = vec.x() - cam.getPosition().x(),
                y = vec.y() - cam.getPosition().y(),
                z = vec.z() - cam.getPosition().z();
            return Math.sqrt(x*x + y*y + z*z) <= 100.0 && x > 0;
        }).forEach((vec) -> {
            puntos_buff.put(vec.x()).put(vec.y()).put(vec.z());
            cantidad[0]++;
        });
        puntos_buff.flip();
        return cantidad[0];
    }

    private static void listaABuffer(TreeSet<Vector3> puntos, FloatBuffer buff) {
        buff.flip().clear();
        for(Vector3 punto : puntos) {
            buff.put(punto.x()).put(punto.y()).put(punto.z());
        }
        buff.flip();
    }

    private EspacioEuclideo() {
        super(new LWJGLWindow.Builder()
                .setResizable(false)
                .setVisible(false)
                .setOpenGLContextVersion(WindowBuilder.OpenGLContextVersion.GL_33)
                .setTitle("Espacio Euclídeo")
                .create(1280, 720), true);
        startEngine();
    }

    private Camera camera;
    private VertexArrayObject puntos_vao, phosphorEffectVao;
    private ShaderProgram puntos_shader, phosphorEffect;
    private TreeSet<Vector3> puntos;
    private BufferObject puntos_vbo;
    private FloatBuffer puntos_buff;
    private Texture currentFrame, previousFrame, current;
    private FrameBuffer sceneFB;
    private int glWIDTH;
    private int glHEIGHT;
    private int cantidad, pasos = 0;
    private boolean phosphorEffectEnabler = false;

    private Texture loadTexture(String path) throws IOException {
        return gl.createTextureBuilder().setStreamToFile(IOUtils.getResourceAsStream(path))
            .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
    }

    @Override
    public void init() {
        window.setVsync(true);

        WIDTH = window.getWindowSize().width;
        HEIGHT = window.getWindowSize().height;
        glWIDTH = window.getFramebufferSize().width;
        glHEIGHT = window.getFramebufferSize().height;
        System.out.printf("%s %s %f\n", window.getWindowSize(), window.getFramebufferSize(), window.getPixelScaleFactor());

        camera = new Camera(this, new Vector3(0, 0, 0), new Vector3(1, 0, 0), new Vector3(0, 0, 1));
        camera.setAspectRation(glWIDTH, glHEIGHT);
        camera.setClipPanes(0.1, 100);
        camera.setMovementMultiplier(0);
        camera.setMouseSensibility(0);

        puntos_vao = gl.createVertexArrayObject();
        BufferObject plano_vbo = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        puntos_vbo = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STREAM_DRAW);

        puntos = generarEspacioEuclídeo();
        puntos_buff = MemoryUtils.createFloatBuffer(puntos.size() * 3);
        listaABuffer(puntos, (FloatBuffer) puntos_buff.flip());
        plano_vbo.fillBuffer(new float[] {
                0,  1, -1, 0, 0,
                0, -1, -1, 1, 0,
                0, -1,  1, 1, 1,

                0,  1,  1, 0, 1,
                0,  1, -1, 0, 0,
                0, -1,  1, 1, 1,
        });
        puntos_vbo.fillBuffer(puntos_buff);
        System.out.printf("Cantidad de euclides en total: %d\n", puntos.size());

        Texture euclides_tex, aleks_tex, falloutPipboy_tex, melchor_tex, pato_tex, doge_tex, andres_tex, amgela_tex;
        try {
            puntos_shader = gl.createShader(
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/espacioEuclideo.vs.glsl")),
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/espacioEuclideo.fs.glsl"))
            );
            euclides_tex = current = loadTexture("img/euklid.png");
            aleks_tex = loadTexture("img/aleks.png");
            falloutPipboy_tex = loadTexture("img/fallout-pipboy.png");
            melchor_tex = loadTexture("img/melchor.png");
            pato_tex = loadTexture("img/pato.png");
            doge_tex = loadTexture("img/doge.png");
            andres_tex = loadTexture("img/andres.png");
            amgela_tex = loadTexture("img/angie.png");
        } catch(IOException e) {
            System.out.printf("Ha habido un problema al cargar las texturas...\n");
            e.printStackTrace();
            window.setWindowShouldClose(true);
            return;
        }

        puntos_vao.bind();
        puntos_shader.bind();
        plano_vbo.bind();
        puntos_shader.vertexAttribPointer("punto", 3, GLContext.type.FLOAT, false, 5<<2, 0);
        puntos_shader.vertexAttribPointer("texcoord", 2, GLContext.type.FLOAT, false, 5<<2, 3<<2);
        puntos_vbo.bind();
        puntos_shader.vertexAttribPointer("puntos", 3, GLContext.type.FLOAT, false, 3*4, 0);
        gl.vertexAttribDivisor(puntos_shader.getAttributeLocation("puntos"), 1);
        puntos_shader.enableAttrib("punto");
        puntos_shader.enableAttrib("texcoord");
        puntos_shader.enableAttrib("puntos");
        puntos_vbo.unbind();
        puntos_vao.unbind();

        gl.enable(GLContext.GLEnable.DEPTH_TEST);
        gl.enable(GLContext.GLEnable.BLEND);
        gl.enable(GLContext.GLEnable.CULL_FACE);
        gl.blendFunc(GLContext.BlendOption.SRC_ALPHA, GLContext.BlendOption.ONE_MINUS_SRC_ALPHA);
        gl.clearColor(0.1f, 0.1f, 0.1f, 1);

        puntos_shader.setColorOutput("color", 0);
        puntos_shader.setUniformMatrix("projection", camera.getProjectionMatrix());
        puntos_shader.setUniform("euclides", 0);
        puntos_shader.setUniform("opacity", 1.0f);

        /*
         * Post-processing effects
         */
        BufferObject screen_vbo = gl.createBufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        screen_vbo.fillBuffer(new float[] {
                1, -1, 0, 0,
                1,  1, 1, 0,
                -1,  1, 1, 1,
                -1, -1, 0, 1,
                1, -1, 0, 0,
                -1,  1, 1, 1
        });
        RenderBuffer currentFrameDepth = gl.createRenderBuffer(GLContext.TextureFormat.DEPTH24_STENCIL8, glWIDTH, glHEIGHT);
        currentFrame = gl.createTexture(GLContext.TextureFormat.RGB8, glWIDTH, glHEIGHT, GLContext.TextureExternalFormat.RGB);
        previousFrame = gl.createTexture(GLContext.TextureFormat.RGB8, glWIDTH, glHEIGHT, GLContext.TextureExternalFormat.RGB);
        sceneFB = gl.createFrameBuffer();
        sceneFB.attachColorTexture(currentFrame, 0);
        sceneFB.attachDepthStencilRenderbuffer(currentFrameDepth);
        sceneFB.unbind();

        phosphorEffectVao = gl.createVertexArrayObject();
        try {
            phosphorEffect = gl.createShader(
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/base.vs.glsl")),
                    IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/phosphor.fs.glsl"))
            );
        } catch(IOException e) {
            System.out.println("Ha habido un problema al cargar PhosphorEffect");
            e.printStackTrace();
            window.setWindowShouldClose(true);
            return;
        }
        phosphorEffect.setUniform("currentFrame", 0);
        phosphorEffect.setUniform("prevFrame", 1);
        phosphorEffect.setColorOutput("color", 0);
        phosphorEffectVao.bind();
        screen_vbo.bind();
        phosphorEffect.vertexAttribPointer("position", 2, GLContext.type.FLOAT, false, 4*4, 0);
        phosphorEffect.vertexAttribPointer("tex_coord", 2, GLContext.type.FLOAT, false, 4*4, 2*4);
        phosphorEffect.enableAttrib("position");
        phosphorEffect.enableAttrib("tex_coord");
        phosphorEffect.setUniform("phosphor", 0.8f);
        phosphorEffectVao.unbind();
        phosphorEffect.unbind();
        plano_vbo.unbind();

        getKeyboard().addListener((Keyboard.OnPressKeyEvent) (self, key) -> {
            if("F".equals(self.getStringRepresentation(key))) {
                phosphorEffectEnabler = !phosphorEffectEnabler;
                if(!phosphorEffectEnabler)
                    post(previousFrame::clear);
            }
            if("P".equals(self.getStringRepresentation(key))) {
                post(() -> {
                    ByteBuffer data = MemoryUtils.createByteBuffer(glWIDTH * glHEIGHT * 3);
                    gl.readBuffer(GLContext.CullFaceMode.FRONT);
                    gl.readPixels(0, 0, glWIDTH, glHEIGHT, GLContext.TextureFormat.RGB, GLContext.type.UNSIGNED_BYTE, data.asIntBuffer());
                    new Thread(() -> {
                        ImageIO.ImageData d = new ImageIO.ImageData(glWIDTH, glHEIGHT, 3, data);
                        ImageIO.writeImage(new File("/Users/melchor9000/Desktop/mae.png"), d);
                        MemoryUtils.free(data);
                    }, "Screenshot saver").start();
                });
            }

            if("1".equals(self.getStringRepresentation(key)))
                current = euclides_tex;
            if("2".equals(self.getStringRepresentation(key)))
                current = falloutPipboy_tex;
            if("3".equals(self.getStringRepresentation(key)))
                current = pato_tex;
            if("4".equals(self.getStringRepresentation(key)))
                current = melchor_tex;
            if("5".equals(self.getStringRepresentation(key)))
                current = aleks_tex;
            if("6".equals(self.getStringRepresentation(key)))
                current = doge_tex;
            if("7".equals(self.getStringRepresentation(key)))
                current = andres_tex;
            if("8".equals(self.getStringRepresentation(key)))
                current = amgela_tex;

            if("F1".equals(self.getStringRepresentation(key)))
                gui.toggleShow();
        });

        window.addResizeEventListener((width, height) -> {
            glWIDTH = window.getFramebufferSize().width;
            glHEIGHT = window.getFramebufferSize().height;
            post(() -> {
                currentFrame.resize(glWIDTH, glHEIGHT);
                previousFrame.resize(glWIDTH, glHEIGHT);
                currentFrameDepth.resize(glWIDTH, glHEIGHT);
            });
            camera.setAspectRation(width, height);
        });

        try {
            gui.loadFont("Ubuntu", new File("/Users/melchor9000/Library/Fonts/Ubuntu-R.ttf"));
        } catch (FileNotFoundException ignore) {}
        gui.setFont("Ubuntu", 20);
        gui.hide();

        window.showWindow();
    }

    @Override
    public void render() {
        float opacity = 1.f;
        if(camera.getPosition().x() < 50.f)
            opacity = camera.getPosition().x() / 50.f;
        else if(camera.getPosition().x() > WIDTH + HEIGHT - 50.f)
            opacity = (WIDTH + HEIGHT - camera.getPosition().x()) / 50.f;
        opacity = Math.max(0, opacity);
        puntos_shader.bind();
        puntos_shader.setUniform("opacity", 1.f + 0.f*opacity);

        if(pasos++ % 5 == 0) {
            postInBackground(() -> {
                final int cantidad_ = obtenerVisibles(puntos_buff, camera, puntos);
                post(() -> {
                    puntos_vbo.fillBuffer(puntos_buff);
                    cantidad = cantidad_;
                });
            });
            pasos %= 5;
        }

        if(phosphorEffectEnabler)
            sceneFB.bind();
        gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT);

        puntos_shader.setUniformMatrix("view", camera.getViewMatrix());
        gl.setActiveTexture(0);
        current.bind();
        puntos_vao.bind();
        gl.drawArraysInstanced(GLContext.DrawMode.TRIANGLES, 0, 6, cantidad);
        puntos_vao.unbind();
        sceneFB.unbind();

        if(phosphorEffectEnabler) {
            gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT);
            gl.disable(GLContext.GLEnable.DEPTH_TEST);
            phosphorEffect.bind();
            phosphorEffectVao.bind();
            gl.setActiveTexture(0);
            currentFrame.bind();
            gl.setActiveTexture(1);
            previousFrame.bind();
            gl.drawArrays(GLContext.DrawMode.TRIANGLES, 0, 6);
            phosphorEffectVao.unbind();
            phosphorEffect.unbind();
            gl.enable(GLContext.GLEnable.DEPTH_TEST);

            gl.setActiveTexture(0);
            previousFrame.bind();
            gl.copyTexImage2D(GLContext.TextureTarget.TEXTURE_2D, 0, GLContext.TextureFormat.RGB, 0, 0, glWIDTH, glHEIGHT);
        }

        if(getKeyboard().isKeyPressed("k"))
            gl.enable(GLContext.GLEnable.FRAMEBUFFER_SRGB);
        else
            gl.disable(GLContext.GLEnable.FRAMEBUFFER_SRGB);

        if(camera.getPosition().x() > WIDTH + HEIGHT + 50f)
            camera.setPosition(0, camera.getPosition().y(), camera.getPosition().z());
            camera.setPosition(camera.getPosition().x() + 25 * (float) t.frameTime, camera.getPosition().y(), camera.getPosition().z());
        camera.updateIfNeeded();
    }

    @Override
    public void gui(long nvgCtx) {
        gui.markForRedraw();
        NVGColor c = NVGColor.calloc();
        NanoVG.nvgFillColor(nvgCtx, NanoVG.nvgRGBAf(0f, 0f, 0f, 0.65f, c));
        GUIDrawUtils.drawRoundedRectangle(0, 0, 205, 145, 0, 0, 0, 5);
        NanoVG.nvgFillColor(nvgCtx, NanoVG.nvgRGBA((byte) 255, (byte) 192, (byte) 0, (byte) 255, c));
        gui.setFontSize(20);
        int i = 0;
        gui.drawText(10, 30 + 20*(i++), "==== Debug info ====");
        gui.drawText(10, 30 + 20*(i++), "%d FPS (%.2f)", this.t.fps, 1/this.t.frameTime);
        gui.drawText(10, 30 + 20*(i++), "%d visible points", cantidad);
        gui.drawText(10, 30 + 20*(i++), "%sabled drugs", phosphorEffectEnabler ? "En" : "Dis");
        gui.drawText(10, 30 + 20*(i++), "%.0f u of X position", camera.getPosition().x());
        gui.drawText(10, 30 + 20 * i, "==================");
        c.free();
    }

    @Override
    public void closing() {
        MemoryUtils.free(puntos_buff);
    }
}
