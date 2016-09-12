package demos;

import org.melchor629.engine.Game;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.Texture;
import org.melchor629.engine.gl.WindowBuilder;
import org.melchor629.engine.gui.Color;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.objects.DeferredRendering;
import org.melchor629.engine.objects.SkyBox;
import org.melchor629.engine.objects.lights.Light;
import org.melchor629.engine.objects.lights.PointLight;
import org.melchor629.engine.utils.IOUtils;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.Vector3;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

/**
 * Aqui probando esta técnica de renderizado
 */
public class DeferredRenderingTest extends Game {
    private ArrayList<Vector3> positions;
    private ArrayList<Light> lights;
    private DeferredRendering r;
    private Cube cube;
    private ModelMatrix modelMatrix;
    private Camera camera;
    private Texture colorTexture, specularTexture;
    private SkyBox skybox;

    protected DeferredRenderingTest() {
        super(new LWJGLWindow.Builder()
                .setResizable(false)
                .setDoubleBuffered(true)
                .setTitle("Deferred Rendering")
                .setVisible(false)
                .setOpenGLContextVersion(WindowBuilder.OpenGLContextVersion.GL_33)
                .create(1280, 720), true);
    }

    @Override
    public void init() {
        camera = new Camera(this);
        camera.setClipPanes(0.1, 100);
        r = new DeferredRendering(window, camera);
        cube = new Cube(gl);
        modelMatrix = new ModelMatrix();
        Random random = new Random();
        positions = new ArrayList<>();
        lights = new ArrayList<>();

        try {
            gui.loadSystemFont("Courier New", "Courier New");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        try {
            colorTexture = gl.createTextureBuilder()
                    .setStreamToFile(IOUtils.getResourceAsStream("img/container2.png"))
                    .setIfmt(GLContext.TextureFormat.RGB)
                    .setEfmt(GLContext.TextureExternalFormat.RGB)
                    .setMin(GLContext.TextureFilter.NEAREST_MIPMAP_LINEAR)
                    .setMag(GLContext.TextureFilter.NEAREST_MIPMAP_LINEAR)
                    .setMipmap(true).build();
        } catch (IOException e) {
            System.err.println("Cannot load resource img/container2.png");
            e.printStackTrace();
        }

        try {
            specularTexture = gl.createTextureBuilder()
                    .setStreamToFile(IOUtils.getResourceAsStream("img/container2_specular.png"))
                    .setIfmt(GLContext.TextureFormat.RGB)
                    .setEfmt(GLContext.TextureExternalFormat.RGB)
                    .setMin(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR)
                    .setMag(GLContext.TextureFilter.LINEAR_MIPMAP_LINEAR)
                    .setMipmap(true).build();
        } catch (IOException e) {
            System.err.println("Cannot load resource img/container2_specular.png");
            e.printStackTrace();
        }

        for(int i = 0; i < 100; i++) {
            positions.add(new Vector3((i * 4) % 40 - 20, -1, (i / 10) * 4 - 10));
        }

        for(int i = 0; i < 30; i++) {
            Light light = new PointLight();
            light.setPosition(new Vector3(random.nextFloat() * 40 - 20, random.nextFloat() * 5 - 2.5f, -random.nextFloat() * 10));
            light.setColor(new Vector3(random.nextFloat() / 2 + .5f, random.nextFloat() / 2 + .5f, random.nextFloat() / 2 + .5f));
            light.setLinearAttenuation(0.5f);
            lights.add(light);
        }

        r.setLights(lights);

        gl.enable(GLContext.GLEnable.BLEND);
        gl.enable(GLContext.GLEnable.DEPTH_TEST);
        gl.enable(GLContext.GLEnable.CULL_FACE);
        gl.blendFunc(GLContext.BlendOption.SRC_ALPHA, GLContext.BlendOption.ONE_MINUS_SRC_ALPHA);
        gl.clearColor(0, 0, 0, 0);

        cube.bindStuff(0, 1, 2, -1);

        try {
            skybox = new SkyBox(gl, new File("/Users/melchor9000/Downloads/skybox"), "jpg");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void render() {
        r.startRenderPass();

        gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT);
        gl.setActiveTexture(0);
        colorTexture.bind();
        gl.setActiveTexture(1);
        specularTexture.bind();
        for(Vector3 pos : positions) {
            modelMatrix.setLocation(pos);
            r.setModelMatrix(modelMatrix);
            cube.draw();
        }
        modelMatrix.setScale(0.1f, 0.1f, 0.1f);
        for(Light l : lights) {
            modelMatrix.setLocation(l.getPosition());
            r.setModelMatrix(modelMatrix);
            cube.draw();
        }
        modelMatrix.setScale(1f, 1f, 1f);

        r.endRenderPass();
        skybox.render(camera);
    }

    @Override
    public void gui(long ctx) {
        Color.white().setAsFillColor();
        gui.setFontSize(30);
        gui.drawText(10, 20, "Camera: %s", camera.getPosition());
        gui.drawText(10, 50, "FPS: %d", t.fps);
        gui.markForRedraw();
    }

    @Override
    public void closing() {

    }

    public static void main(String... args) {
        new DeferredRenderingTest().startEngine();
    }
}
