import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.gl.types.*;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.LWJGLKeyboard;
import org.melchor629.engine.input.LWJGLMouse;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.ImageIO;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.Vector3;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.Random;
import java.util.TreeSet;

/**
 * No hacer caso a esto
 */
public class EspacioEuclideo {
    static {
        System.setProperty("jna.library.path", System.getProperty("engine.natives", "build/binaries/engineSharedLibrary/x64Release"));
    }

    private static final Random rand = new Random();
    private static int WIDTH = 1280;
    private static int HEIGHT = 720;
    private static int glWIDTH;
    private static int glHEIGHT;
    private static Window window;
    private static boolean phosphorEffectEnabler = false;
    private static Texture current;

    public static void main(String... args) {
        try {
            _main();
        } catch(Throwable e) {
            e.printStackTrace();
        }

        window.destroyWindow();
        System.exit(0);
    }

    private static void _main() throws Throwable {
        Game.erasableList = new ArrayList<>();
        Game.window = window = new LWJGLWindow();
        window.setResizable(false);
        window.setVisible(false);
        window.setContextProfileAndVersion(Window.OpenGLContextVersion.GL_33);
        GLContext gl = Game.gl = window.createWindow(1280, 720, "Espacio Euclídeo");
        window.setVsync(true);
        Timing t = Timing.getGameTiming();

        WIDTH = window.getWindowSize().width;
        HEIGHT = window.getWindowSize().height;
        glWIDTH = window.getFramebufferSize().width;
        glHEIGHT = window.getFramebufferSize().height;
        System.out.printf("%s %s %f\n", window.getWindowSize(), window.getFramebufferSize(), window.getPixelScaleFactor());

        Keyboard keyboard = Game.keyboard = new LWJGLKeyboard();
        Mouse mouse = Game.mouse = new LWJGLMouse();
        Camera camera = new Camera();
        camera.setAspectRation(glWIDTH, glHEIGHT);
        camera.setClipPanes(0.1, 100);
        camera.setMovementMultiplier(0);
        camera.setMouseSensibility(0);

        VAO puntos_vao = new VAO();
        BufferObject plano_vbo = new BufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        BufferObject puntos_vbo = new BufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STREAM_DRAW);

        TreeSet<Vector3> puntos = generarEspacioEuclídeo();
        FloatBuffer puntos_buff = BufferUtils.createFloatBuffer(puntos.size() * 3);
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
        //puntos.clear();
        System.out.printf("Cantidad de euclides en total: %d\n", puntos.size());

        ShaderProgram puntos_shader = new ShaderProgram(
                IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/espacioEuclideo.vs.glsl")),
                IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/espacioEuclideo.fs.glsl")));
        Texture euclides_tex = current = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/euklid.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
        Texture aleks_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/aleks.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
        Texture falloutPipboy_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/fallout-pipboy.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
        Texture melchor_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/melchor.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
        Texture pato_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/pato.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
        Texture doge_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/doge.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();
        Texture andres_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/andres.png"))
                .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();

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

        /**
         * Post-processing effects
         */
        BufferObject screen_vbo = new BufferObject(GLContext.BufferTarget.ARRAY_BUFFER, GLContext.BufferUsage.STATIC_DRAW);
        screen_vbo.fillBuffer(new float[] {
                1, -1, 0, 0,
                1,  1, 1, 0,
               -1,  1, 1, 1,
               -1, -1, 0, 1,
                1, -1, 0, 0,
               -1,  1, 1, 1
        });
        Renderbuffer currentFrameDepth = new Renderbuffer(GLContext.TextureFormat.DEPTH24_STENCIL8, glWIDTH, glHEIGHT);
        Texture currentFrame = new Texture(GLContext.TextureFormat.RGB, glWIDTH, glHEIGHT, GLContext.TextureExternalFormat.RGB);
        Texture previousFrame = new Texture(GLContext.TextureFormat.RGB, glWIDTH, glHEIGHT, GLContext.TextureExternalFormat.RGB);
        Framebuffer sceneFB =  new Framebuffer();
        sceneFB.attachColorTexture(currentFrame, 0);
        sceneFB.attachDepthStencilRenderbuffer(currentFrameDepth);
        sceneFB.unbind();

        VAO phosphorEffectVao = new VAO();
        ShaderProgram phosphorEffect = new ShaderProgram(
                IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/base.vs.glsl")),
                IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/phosphor.fs.glsl"))
        );
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

        keyboard.addListener((Keyboard.OnPressKeyEvent) (self, key) -> {
            if(self.getStringRepresentation(key) == null) return;
            if(self.getStringRepresentation(key).equals("F"))
                phosphorEffectEnabler = !phosphorEffectEnabler;
            if(self.getStringRepresentation(key).equals("P")) {
                ByteBuffer data = BufferUtils.createByteBuffer(glWIDTH * glHEIGHT * 4);
                gl.readBuffer(GLContext.CullFaceMode.FRONT);
                gl.readPixels(0, 0, glWIDTH, glHEIGHT, GLContext.TextureFormat.RGB, GLContext.type.UNSIGNED_BYTE, data.asIntBuffer());
                new Thread(() -> {
                    ImageIO.ImageData d = new ImageIO.ImageData(glWIDTH, glHEIGHT, 3, data);
                    ImageIO.writeImage(new File("/Users/melchor9000/Desktop/mae.png"), d);
                }, "Screenshot saver").start();
            }

            if(self.getStringRepresentation(key).equals("1"))
                current = euclides_tex;
            if(self.getStringRepresentation(key).equals("2"))
                current = falloutPipboy_tex;
            if(self.getStringRepresentation(key).equals("3"))
                current = pato_tex;
            if(self.getStringRepresentation(key).equals("4"))
                current = melchor_tex;
            if(self.getStringRepresentation(key).equals("5"))
                current = aleks_tex;
            if(self.getStringRepresentation(key).equals("6"))
                current = doge_tex;
            if(self.getStringRepresentation(key).equals("7"))
                current = andres_tex;
        });

        window.showWindow();
        while(!window.windowShouldClose()) {
            float opacity = 1.f;
            if(camera.getPosition().x < 50.f)
                opacity = camera.getPosition().x / 50.f;
            else if(camera.getPosition().x > WIDTH + HEIGHT - 50.f)
                opacity = (WIDTH + HEIGHT - camera.getPosition().x) / 50.f;
            opacity = Math.max(0, opacity);
            puntos_shader.bind();
            puntos_shader.setUniform("opacity", opacity);

            int cantidad = obtenerVisibles(puntos_buff, camera, puntos);
            puntos_vbo.fillBuffer(puntos_buff);
            System.out.printf("Cantidad de euclides: %d     \r", cantidad);

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

            if(keyboard.isKeyPressed("k"))
                gl.enable(GLContext.GLEnable.FRAMEBUFFER_SRGB);
            else
                gl.disable(GLContext.GLEnable.FRAMEBUFFER_SRGB);

            if(camera.getPosition().x > WIDTH + HEIGHT + 50f)
                camera.setPosition(0, camera.getPosition().y, camera.getPosition().z);
            if(t.frameTime < 1)
                camera.setPosition(camera.getPosition().x + 25 * (float) t.frameTime, camera.getPosition().y, camera.getPosition().z);

            gl._game_loop_sync(60);
            t.update();
            keyboard.fireEvent(t.frameTime);
            mouse.update(t.frameTime);
            camera.updateIfNeeded();
        }

        Game.erasableList.forEach(Erasable::delete);
    }

    private static TreeSet<Vector3> generarEspacioEuclídeo() {
        FloatBuffer puntos = BufferUtils.createFloatBuffer(10000 * 3);
        TreeSet<Vector3> puntos_lista = new TreeSet<>((o1, o2) ->
                Math.round((o2.length() - o1.length()) * 1000.f)
        );

        for(int i = 0; i < puntos.capacity() / 3; i++) {
            Vector3 punto = generarPunto((WIDTH + HEIGHT), WIDTH / 20.f, HEIGHT / 20.f);
            while(!esVálido(punto) || !puntos_lista.add(punto))
                punto = generarPunto((WIDTH + HEIGHT), WIDTH / 20.f, HEIGHT / 20.f);
        }

        return puntos_lista;
    }

    private static boolean esVálido(Vector3 punto) {
        return Math.abs(punto.x) > 10f && Math.abs(punto.y) > .5f && Math.abs(punto.z) > .5f;
    }

    private static Vector3 generarPunto(float max_x, float max_y, float max_z) {
        return new Vector3(rand.nextFloat() * max_x, rand.nextFloat() * max_y - max_y / 2f, rand.nextFloat() * max_z - max_z / 2f);
    }

    private static int obtenerVisibles(FloatBuffer puntos_buff, Camera cam, TreeSet<Vector3> puntos) {
        final int cantidad[] = {0};
        puntos_buff.flip().clear();
        puntos.stream().filter((vec) -> {
            float x = vec.x - cam.getPosition().x, y = vec.y - cam.getPosition().y, z = vec.z - cam.getPosition().z;
            return Math.sqrt(x*x + y*y + z*z) <= 100.0 && x > 0;
        }).forEach((vec) -> {
            puntos_buff.put(vec.x).put(vec.y).put(vec.z);
            cantidad[0]++;
        });
        puntos_buff.flip();
        return cantidad[0];
    }

    private static void listaABuffer(TreeSet<Vector3> puntos, FloatBuffer buff) {
        buff.flip().clear();
        for(Vector3 punto : puntos) {
            buff.put(punto.x).put(punto.y).put(punto.z);
        }
        buff.flip();
    }
}
