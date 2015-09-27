import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Buffer;
import org.melchor629.engine.al.types.Listener;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.gl.GLContext;
import org.melchor629.engine.gl.GLContext.GLEnable;
import org.melchor629.engine.gl.LWJGLWindow;
import org.melchor629.engine.gl.Window;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.LWJGLKeyboard;
import org.melchor629.engine.input.LWJGLMouse;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.audio.flac.FlacDecoder;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.objects.Material;
import org.melchor629.engine.objects.Model;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.Vector3;

import java.io.File;

public class AnotherTestingClass {
    static { System.setProperty("jna.library.path", "build/binaries/engineSharedLibrary/release"); }
    
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

    public static void colladaxd(String[] args) {
        Window window = Game.window = new LWJGLWindow();
        AL al = Game.al = new LWJGLAudio();
        Timing t = Timing.getGameTiming();
        window.setResizable(true);
        window.setContextProfileAndVersion(Window.OpenGLContextVersion.GL_33);
        GLContext gl = Game.gl = window.createWindow(1280, 720, "Eso...");
        window.setVsync(true);
        al.createContext();
        Keyboard keyboard = Game.keyboard = new LWJGLKeyboard();
        Mouse mouse = Game.mouse = new LWJGLMouse();
        Camera camera = new Camera();
        camera.setClipPanes(0.1, 100);

        Buffer sound_buffer;
        Source sound_source = null;
        /*try {
            FlacDecoder sound = new FlacDecoder(AudioTests.archivo, true);
            sound.decode();
            sound_buffer = new Buffer(sound.getSampleData(), AL.Format.MONO16, sound.getSampleRate());
            sound_source = new Source(sound_buffer);
            sound_source.setPosition(new Vector3(7.5f, 0.f, 0.f));
            sound.clear();
        } catch(Exception ignore) {
        }*/

        Collada c = null;
        try {
            c = new Collada(new File("mierda.dae"));
        } catch(Exception e) {
            e.printStackTrace();
            window.destroyWindow();
            System.exit(1);
        }

        Model.loadModels(c);
        Material.loadMaterials(c);
        ColladaScene cs = new ColladaScene(c.visual_scenes.get(0));

        ShaderProgram s = new ShaderProgram(vertex_shader, fragment_shader);
        s.bindFragDataLocation("outColor", 0);
        s.unbind();
        cs.enableAttributes();
        //s.unbind();

        Cube cube = new Cube();

        ModelMatrix model = new ModelMatrix(),
                cubeModel = new ModelMatrix();
        cubeModel.translate(7.5f, 0.f, 0.f);
        cube.bindStuff(s, "position", "normal", null, "color");

        s.bind();
        s.setUniformMatrix("view", camera.getViewMatrix());
        s.setUniformMatrix("project", camera.getProjectionMatrix());
        s.setUniformMatrix("model", model.getModelMatrix());

        gl.enable(GLEnable.DEPTH_TEST);
        gl.clearColor(1, 1, 1, 1);
        t.split("GL & data");
        sound_source.play();

        while(!window.windowShouldClose()) {
            sound_source.setVelocity(new Vector3(0, 0, 0));
            gl.clear(GLContext.COLOR_CLEAR_BIT | GLContext.DEPTH_BUFFER_BIT);
            s.bind();
            s.setUniformMatrix("view", camera.getViewMatrix());
            s.setUniformMatrix("project", camera.getProjectionMatrix());

            s.setUniformMatrix("model", cubeModel.getModelMatrix());
            cube.draw();

            cs.render(camera);

            gl._game_loop_sync(60);
            t.split("gpu");
            t.update();
            keyboard.fireEvent(t.frameTime);
            mouse.update(t.frameTime);
            camera.updateIfNeeded();
            Listener.setPosition(camera.getPosition());
            Listener.setOrientation(camera.getLookingAtDirection(), new Vector3(0, 0, 1));
            Listener.setVelocity(camera.getSpeed());
            t.split("cpu");
            //System.out.printf("CPU: %.6f\tGPU: %.6f\n", t.getSplitTime("cpu"), t.getSplitTime("gpu"));
        }

        s.delete();
        Model.deleteModels();
        sound_source.delete();

        keyboard.release();
        mouse.release();
        window.destroyWindow();
        al.deleteContext();
    }
    
    public static void main(String[] args) {
        colladaxd(args);
        System.exit(0);
    }

    public static void printError() {
        GLContext.Error err = Game.gl.getError();
        if(err != GLContext.Error.NO_ERROR)
            System.out.printf("Error %s (%d) %s\n", err.toString(), err.errno, Thread.currentThread().getStackTrace()[2]);
    }

}
