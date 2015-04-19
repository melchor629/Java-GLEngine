import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Buffer;
import org.melchor629.engine.al.types.Listener;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.Renderer.BufferTarget;
import org.melchor629.engine.gl.Renderer.BufferUsage;
import org.melchor629.engine.gl.Renderer.DrawMode;
import org.melchor629.engine.gl.Renderer.GLEnable;
import org.melchor629.engine.gl.types.BufferObject;
import org.melchor629.engine.gl.types.ShaderProgram;
import org.melchor629.engine.gl.types.VAO;
import org.melchor629.engine.input.Keyboard;
import org.melchor629.engine.input.LWJGLKeyboard;
import org.melchor629.engine.input.LWJGLMouse;
import org.melchor629.engine.input.Mouse;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.Flac;
import org.melchor629.engine.loaders.collada.Node;
import org.melchor629.engine.objects.Camera;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.mat4;
import org.melchor629.engine.utils.math.vec3;

import java.io.File;
import java.util.ArrayList;

public class AnotherTestingClass {
    static { System.setProperty("jna.library.path", "build/binaries/engineSharedLibrary"); }
    
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
        Renderer gl = Game.gl = new LWJGLRenderer();
        AL al = Game.al = new LWJGLAudio();
        Timing t = Timing.getGameTiming();
        boolean cr;
        gl.setResizable(true);
        cr = gl.createDisplay(1280, 720, false, "Eso...");
        gl.setVsync(true);
        al.createContext();
        Keyboard keyboard = Game.keyboard = new LWJGLKeyboard();
        Mouse mouse = Game.mouse = new LWJGLMouse();
        Camera camera = new Camera();
        camera.setClipPanes(0.1, 100);
        
        if(!cr) {
            System.out.printf("Error al crear la ventana...");
            System.exit(-1);
        }

        Buffer sound_buffer;
        Source sound_source = null;
        try {
            Flac sound = new Flac(FLACDecoder.archivo, true);
            sound.decode();
            sound_buffer = new Buffer(sound.getSampleData(), AL.Format.MONO16, sound.getSampleRate());
            sound_source = new Source(sound_buffer);
            sound_source.setPosition(new vec3(7.5f, 0.f, 0.f));
            sound.clear();
        } catch(Exception ignore) {}
        
        Collada c = null;
        try {
            c = new Collada(new File("mierda.dae"));
        } catch(Exception e) {
            e.printStackTrace();
            gl.destroyDisplay();
            System.exit(1);
        }

        ArrayList<Meshy> meshies = new ArrayList<>();
        for(Node n : c.visual_scenes.get(0).nodes) {
            if(n.isGeometry()) {
                meshies.add(new Meshy(c, n));
            }
        }
        
        ShaderProgram s = new ShaderProgram(vertex_shader, fragment_shader);
        s.bindFragDataLocation("outColor", 0);
        s.bind();
        for(Meshy m : meshies)
            m.enableAttribs(s, "position", "normal", "color");
        s.unbind();

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
        
        while(!gl.windowIsClosing()) {
            sound_source.setVelocity(new vec3(0,0,0));
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
            s.setUniformMatrix("view", camera.getViewMatrix());
            s.setUniformMatrix("project", camera.getProjectionMatrix());

            s.setUniformMatrix("model", cubeModel.getModelMatrix());
            cube.draw();
            //cubeModel.rotate((float) (0.4 * Math.PI * t.frameTime), 1, 0, 0);
            //cubeModel.rotate((float) (-.2 * Math.PI * t.frameTime), 0, 1, 0);
            //cubeModel.rotate((float) (0.5 * Math.PI * t.frameTime), 0, 0, 1);

            for(Meshy mesh : meshies) {
                mesh.draw(s);
            }
            
            gl._game_loop_sync(60);
            t.split("gpu");
            t.update();
            keyboard.fireEvent(t.frameTime);
            mouse.update(t.frameTime);
            camera.updateIfNeeded();
            Listener.setPosition(camera.getPosition());
            Listener.setOrientation(camera.getLookingAtDirection(), new vec3(0, 0, 1));
            Listener.setVelocity(camera.getSpeed());
            t.split("cpu");
            //System.out.printf("CPU: %.6f\tGPU: %.6f\n", t.getSplitTime("cpu"), t.getSplitTime("gpu"));
        }
        
        s.delete();
        for(Meshy mesh : meshies)
            mesh.delete();
        sound_source.destroy();

        keyboard.release();
        mouse.release();
        gl.destroyDisplay();
        al.deleteContext();
    }
    
    public static void cameraxd(String[] args) {
        Renderer gl = Game.gl = new LWJGLRenderer();
        final Timing t = Timing.getGameTiming();
        boolean cr;
        gl.setVsync(true);
        gl.setResizable(true);
        org.lwjgl.glfw.GLFW.glfwWindowHint(135181, 16); //Activate Antialiasing
        cr = gl.createDisplay(1280, 720, false, "Cámara");
        Keyboard keyboard = Game.keyboard = new LWJGLKeyboard();
        Mouse mouse = Game.mouse = new LWJGLMouse();
        gl.enable(Renderer.GLEnable.MULTISAMPLE); //Enable on OpenGL
        
        if(!cr) {
            System.out.printf("Error al crear la ventana...");
            System.exit(-1);
        }
        
        VAO vao = new VAO();
        BufferObject vbo = new BufferObject(BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);

        vbo.fillBuffer(new float[] {
              //POSICIÓN           | COLOR          | TEXCOORD
               -1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                1.0f, -1.0f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                1.0f,  1.0f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                1.0f,  1.0f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
               -1.0f,  1.0f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
               -1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
        });
        vbo.unbind();

        ShaderProgram s = new ShaderProgram(vertex_shader, fragment_shader);
        s.bindFragDataLocation("outColor", 0);
        s.bind();
        s.enableAttribs(vao, "color", "position", "normal");
        vao.bind();
        vbo.bind();
        s.vertexAttribPointer("position", 3, Renderer.type.FLOAT, false, 8 * 4, 0);
        s.vertexAttribPointer("color", 3, Renderer.type.FLOAT, false, 8 * 4, 3 * 4);
        s.vertexAttribPointer("normal", 3, Renderer.type.FLOAT, false, 8 * 4, 5 * 4);
        vao.unbind();
        s.unbind();
        
        mat4 view, proj;
        final ModelMatrix model = new ModelMatrix(),
            cubeModel = new ModelMatrix();
        final vec3 cameraPos = new vec3(0f, 1f, 2.2f), cameraDir = GLM.product(-1f, cameraPos), cameraUp = new vec3(0, 0, 1);
        
        view = GLM.lookAt(cameraPos, GLM.sum(cameraPos, cameraDir), cameraUp);
        proj = GLM.perspective(45d, 1280d/720d, 1d, 10d);
        
        s.bind();
        s.setUniformMatrix("view", view);
        s.setUniformMatrix("project", proj);
        s.setUniformMatrix("model", model.getModelMatrix());
        
        gl.enable(GLEnable.DEPTH_TEST);
        gl.clearColor(1, 1, 1, 1);
        
        final Camera c = new Camera(cameraPos, cameraDir, cameraUp);
        Cube cube = new Cube();
        cubeModel.setIdentity().translate(0, 0, 1.f);
        cube.bindStuff(s, "position", "normal", null, "color");

        mouse.addListener(new Mouse.OnMouseMoveEvent() {
            @Override
            public void invoke(Mouse self, double f) {
                if(self.getWheelSpeed().y != 0.f) {
                    if(self.isShiftPressed())
                        c.setPosition(c.getPosition().x, c.getPosition().y + self.getWheelSpeed().y * .5f, c.getPosition().z);
                    else if(self.isAltPressed())
                        c.setFOV(c.getFOV() + self.getWheelSpeed().y);
                    else
                        c.setRotation(c.getRotation().x, c.getRotation().y + self.getWheelSpeed().y * 0.3f, c.getRotation().z);
                }
                
                if(self.getWheelSpeed().x != 0.f) {
                    if(self.isShiftPressed())
                        c.setPosition(c.getPosition().x + self.getWheelSpeed().x * .5f, c.getPosition().y, c.getPosition().z);
                    else
                        c.setRotation(c.getRotation().x + self.getWheelSpeed().x * 0.3f, c.getRotation().y, c.getRotation().z);
                }
            }
        });
        
        while(!gl.windowIsClosing()) {
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
            
            model.setIdentity();
            s.bind();
            s.setUniformMatrix("model", model.getModelMatrix());
            s.setUniformMatrix("view", c.getViewMatrix());
            s.setUniformMatrix("project", c.getProjectionMatrix());
            vao.bind();
            gl.drawArrays(DrawMode.TRIANGLES, 0, 6);

            s.setUniformMatrix("model", cubeModel.getModelMatrix());
            cube.draw();
            cubeModel.rotate((float) (0.4 * Math.PI * t.frameTime), 1, 0, 0);
            cubeModel.rotate((float) (-.2 * Math.PI * t.frameTime), 0, 1, 0);
            cubeModel.rotate((float) (0.5 * Math.PI * t.frameTime), 0, 0, 1);

            s.unbind();
            
            gl._game_loop_sync(60);
            t.update();
            keyboard.fireEvent(t.frameTime);
            mouse.update(t.frameTime);
        }
        
        gl.destroyDisplay();
    }
    
    public static void main(String[] args) {
        colladaxd(args);
        //cameraxd(args);
        System.exit(0);
    }

    public static void printError() {
        Renderer.Error err = Game.gl.getError();
        if(err != Renderer.Error.NO_ERROR)
            System.out.printf("Error %s (%d) %s\n", err.toString(), err.errno, Thread.currentThread().getStackTrace()[2]);
    }

}
