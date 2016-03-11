package demos;

import org.melchor629.engine.Game;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.Source;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.GLContext.GLEnable;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.gl.ShaderProgram;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.objects.Model;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.Vector3;

import java.io.File;
import java.util.ArrayList;

public class AnotherTestingClass extends Game {
    
    private final static String vertex_shader = "#version 150 core\n"
            + "in vec3 position;\n"
            + "in vec3 normal;"
            + "out vec3 Normal;"
            + "\n"
            + "in vec3 color;"
            + "out vec3 Color;"
            + "\n"
            + "uniform mat4 model;"
            + "uniform mat4 view;"
            + "uniform mat4 project;"
            + "\n"
            + "void main() {"
            + "    Color = color;"
            + "    gl_Position = project * view * model * vec4(position, 1.0);"
            + "    Normal = normal;"
            + "}";
    private final static String fragment_shader = "#version 150 core"
            + "\n"
            + "in vec3 Color;"
            + "in vec3 Normal;"
            + "out vec4 outColor;"
            + ""
            + "void main() {"
            + "    outColor = vec4(Color, 1.0);"
            + "}";

    private final Object lock = new Object();
    private Source sound_source;
    private ShaderProgram s;
    private ColladaScene cs;
    private Camera camera;
    private Cube cube;
    private ModelMatrix cubeModel;
    
    public static void main(String[] args) {
        new AnotherTestingClass().startEngine();
    }

    private AnotherTestingClass() {
        super(new LWJGLWindow(), new LWJGLAudio());
        window.setResizable(true);
        window.setContextProfileAndVersion(Window.OpenGLContextVersion.GL_33);
        title = "Eso...";
        width = 1280;
        height = 720;
    }

    @Override
    public void init() {
        window.setVsync(true);
        camera = new Camera(this);
        camera.setClipPanes(0.1, 100);

        new Thread(() -> {
            try {
                AudioContainer sound;

                synchronized (lock) {
                    AudioDecoder decoder = AudioDecoder.createDecoderForFile(new File(AudioTests.archivo));
                    decoder.readHeader();
                    decoder.decode();
                    sound = decoder.getAudioContainer();
                    this.post(() -> {
                        sound_source = al.createSource(al.createBuffer(sound));
                        sound_source.setPosition(new Vector3(7.5f, 0.f, 0.f));
                        sound_source.play();
                    });
                }

                sound.cleanUpNativeResources();
            } catch(Exception ignore) {ignore.printStackTrace();}
        }, "Canción de fondo").start();

        Collada c = null;
        try {
            //c = new Collada(new File("mierda.dae"));
            c = new Collada(new File("src/main/resources/scenes/mierdolo.dae"));
            c.loadElements();
        } catch(Exception e) {
            e.printStackTrace();
            window.destroyWindow();
            System.exit(1);
        }

        cs = new ColladaScene(c.visual_scenes.get(0));

        s = new ShaderProgram(vertex_shader, fragment_shader);
        s.bindFragDataLocation("outColor", 0);
        s.unbind();
        cs.enableAttributes();
        //s.unbind();

        cube = new Cube();

        ModelMatrix model = new ModelMatrix();
        cubeModel = new ModelMatrix();
        cubeModel.translate(7.5f, 0.f, 0.f);
        cube.bindStuff(s, "position", "normal", null, "color");

        s.bind();
        s.setUniformMatrix("view", camera.getViewMatrix());
        s.setUniformMatrix("project", camera.getProjectionMatrix());
        s.setUniformMatrix("model", model.getModelMatrix());

        gl.enable(GLEnable.DEPTH_TEST);
        gl.enable(GLEnable.CULL_FACE);
        gl.clearColor(1, 1, 1, 1);
    }

    @Override
    public void render() {
        gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT);
        s.bind();
        s.setUniformMatrix("view", camera.getViewMatrix());
        s.setUniformMatrix("project", camera.getProjectionMatrix());

        cubeModel.translate(0.0001f, 0, 0);
        s.setUniformMatrix("model", cubeModel.getModelMatrix());
        cube.draw();

        cs.render(camera);

        camera.updateIfNeeded();
        al.getListener().setPosition(camera.getPosition());
        al.getListener().setOrientation(camera.getLookingAtDirection(), new Vector3(0, 0, 1));
        al.getListener().setVelocity(camera.getSpeed());
    }

    @Override
    public void closing() {
        Model.deleteModels();
    }
}
