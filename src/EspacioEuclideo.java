import org.melchor629.engine.Game;
import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.types.BufferObject;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.Texture;
import org.melchor629.engine.gl.types.VAO;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.LWJGLKeyboard;
import org.melchor629.engine.input.LWJGLMouse;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.vec3;

import java.nio.FloatBuffer;
import java.util.Random;
import java.util.TreeSet;

/**
 * No hacer caso a esto
 */
public class EspacioEuclideo {
    static {
        System.setProperty("jna.library.path", System.getProperty("engine.natives", "build/binaries/engineSharedLibrary/release"));
    }

    private static final Random rand = new Random();
    public static final int WIDTH = 1280, HEIGHT = 720;
    private static Renderer gl;

    public static void main(String... args) {
        try {
            _main();
        } catch(Throwable e) {
            e.printStackTrace();
        }

        gl.destroyDisplay();
        System.exit(0);
    }

    public static void _main() throws Throwable {
        Game.gl = gl = new LWJGLRenderer();
        Timing t = Timing.getGameTiming();
        gl.setResizable(false);
        gl.createDisplay(WIDTH, HEIGHT, false, "Espacio Euclídeo", Renderer.GLVersion.GL33);
        gl.setVsync(true);

        Keyboard keyboard = Game.keyboard = new LWJGLKeyboard();
        Mouse mouse = Game.mouse = new LWJGLMouse();
        Camera camera = new Camera();
        camera.setClipPanes(0.1, 100);
        camera.setMovementMultiplier(0);
        camera.setMouseSensibility(0);

        VAO puntos_vao = new VAO();
        BufferObject plano_vbo = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);
        BufferObject puntos_vbo = new BufferObject(Renderer.BufferTarget.ARRAY_BUFFER, Renderer.BufferUsage.STATIC_DRAW);

        FloatBuffer puntos = generarEspacioEuclídeo();
        int cantidad = puntos.capacity() / 3;
        plano_vbo.fillBuffer(new float[] {
                0,  1, -1, 0, 1,
                0, -1, -1, 1, 1,
                0, -1,  1, 1, 0,

                0,  1,  1, 0, 0,
                0,  1, -1, 0, 1,
                0, -1,  1, 1, 0,
        });
        puntos_vbo.fillBuffer(puntos);
        puntos.clear();

        ShaderProgram puntos_shader = new ShaderProgram(
                IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/espacioEuclideo.vs.glsl")),
                IOUtils.readStream(IOUtils.getResourceAsStream("shaders/espEucl/espacioEuclideo.fs.glsl")));
        Texture euclides_tex = new Texture.builder().setStreamToFile(IOUtils.getResourceAsStream("img/euklid.png"))
                .setMin(Renderer.TextureFilter.LINEAR_MIPMAP_LINEAR).setMipmap(true).build();

        puntos_vao.bind();
        puntos_shader.bind();
        plano_vbo.bind();
        puntos_shader.vertexAttribPointer("punto", 3, Renderer.type.FLOAT, false, 5*4, 0);
        puntos_shader.vertexAttribPointer("texcoord", 2, Renderer.type.FLOAT, false, 5<<2, 3<<2);
        puntos_vbo.bind();
        puntos_shader.vertexAttribPointer("puntos", 3, Renderer.type.FLOAT, false, 3*4, 0);
        gl.vertexAttribDivisor(puntos_shader.getAttributeLocation("puntos"), 1);
        puntos_shader.enableAttrib("punto");
        puntos_shader.enableAttrib("texcoord");
        puntos_shader.enableAttrib("puntos");
        puntos_vbo.unbind();
        puntos_vao.unbind();

        gl.enable(Renderer.GLEnable.DEPTH_TEST);
        gl.enable(Renderer.GLEnable.BLEND);
        gl.enable(Renderer.GLEnable.CULL_FACE);
        gl.blendFunc(Renderer.BlendOption.SRC_ALPHA, Renderer.BlendOption.ONE_MINUS_SRC_ALPHA);

        puntos_shader.setColorOutput("color", 0);
        puntos_shader.setUniformMatrix("projection", camera.getProjectionMatrix());
        puntos_shader.setUniform("euclides", 0);
        puntos_shader.setUniform("opacity", 1.0f);

        while(!gl.windowIsClosing()) {
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
            gl.clearColor(0.1f, 0.1f,0.1f,1);

            float opacity = 1.f;
            if(camera.getPosition().x < 50.f)
                opacity = camera.getPosition().x / 50.f;
            else if(camera.getPosition().x > WIDTH + HEIGHT - 50.f)
                opacity = (WIDTH + HEIGHT - camera.getPosition().x) / 50.f;
            opacity = Math.max(0, opacity);
            puntos_shader.setUniform("opacity", opacity);

            puntos_shader.setUniformMatrix("view", camera.getViewMatrix());
            gl.setActiveTexture(0);
            euclides_tex.bind();
            puntos_vao.bind();
            gl.drawArraysInstanced(Renderer.DrawMode.TRIANGLES, 0, 6, cantidad);
            puntos_vao.unbind();

            if(keyboard.isKeyPressed("k"))
                gl.enable(Renderer.GLEnable.FRAMEBUFFER_SRGB);
            else
                gl.disable(Renderer.GLEnable.FRAMEBUFFER_SRGB);

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

        puntos_shader.delete();
        puntos_vbo.delete();
        plano_vbo.delete();
        puntos_shader.delete();
    }

    private static FloatBuffer generarEspacioEuclídeo() {
        FloatBuffer puntos = BufferUtils.createFloatBuffer(10000 * 3);
        TreeSet<vec3> puntos_lista = new TreeSet<>((o1, o2) ->
                Math.round((o2.length() - o1.length()) * 1000.f)
        );

        for(int i = 0; i < puntos.capacity() / 3; i++) {
            vec3 punto = generarPunto((WIDTH + HEIGHT), WIDTH / 20.f, HEIGHT / 20.f);
            while(!esVálido(punto) || !puntos_lista.add(punto))
                punto = generarPunto((WIDTH + HEIGHT), WIDTH / 20.f, HEIGHT / 20.f);
        }

        for(vec3 punto : puntos_lista) {
            puntos.put(punto.x).put(punto.y).put(punto.z);
        }

        return (FloatBuffer) puntos.flip();
    }

    private static boolean esVálido(vec3 punto) {
        return Math.abs(punto.x) > 10f && Math.abs(punto.y) > .5f && Math.abs(punto.z) > .5f;
    }

    private static vec3 generarPunto(float max_x, float max_y, float max_z) {
        return new vec3(rand.nextFloat() * max_x, rand.nextFloat() * max_y - max_y / 2f, rand.nextFloat() * max_z - max_z / 2f);
    }
}
