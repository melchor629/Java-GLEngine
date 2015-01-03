import java.io.File;
import java.util.ArrayList;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.melchor629.engine.Game;
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
import org.melchor629.engine.input.Mouse.OnMouseClickEvent;
import org.melchor629.engine.input.Mouse.OnMouseMoveEvent;
import org.melchor629.engine.loaders.Collada;
import org.melchor629.engine.loaders.collada.Geometry;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.GLM;
import org.melchor629.engine.utils.math.ModelMatrix;
import org.melchor629.engine.utils.math.mat4;
import org.melchor629.engine.utils.math.vec3;

public class AnotherTestingClass {
    
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
            + "out vec3 outColor;"
            + ""
            + "void main() {"
            + "    outColor = Color;"
            + "}";

    public static void colladaxd(String[] args) {
        Renderer gl = Game.gl = new LWJGLRenderer();
        Timing t = new Timing();
        boolean cr;
        gl.setVsync(true);
        gl.setResizable(true);
        cr = gl.createDisplay(1280, 720, false, "Eso...");
        
        if(!cr) {
            System.out.printf("Error al crear la ventana...");
            System.exit(-1);
        }
        
        Collada c = null;
        try {
            c = new Collada(new File("/Users/melchor9000/Documents/Programación/JavaBlockWorld/res/sponza/Muñeco Minecraft.dae"));
        } catch(Exception e) {
            e.printStackTrace();
            gl.destroyDisplay();
            System.exit(1);
        }
        
        ArrayList<VAO> vaos = new ArrayList<>(c.geometry.size());
        ArrayList<BufferObject> vbos = new ArrayList<>(c.geometry.size());
        ArrayList<BufferObject> ebos = new ArrayList<>(c.geometry.size());
        
        BufferObject vbo = null, ebo = null;
        VAO vao = null;
        for(Geometry g : c.geometry) {
            vao = new VAO();
            vao.bind();
            
            vbo = new BufferObject(BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
            ebo = new BufferObject(BufferTarget.ELEMENT_ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
            
            ebo.fillBuffer(g.mesh.polylist.p);
            vbo.initPartialFillBuffer(4*(g.mesh.sources.get(0).buff.capacity()+g.mesh.sources.get(1).buff.capacity()+g.mesh.sources.get(2).buff.capacity()));
            vbo.partiallyFillBuffer(0, g.mesh.sources.get(0).buff);
            vbo.partiallyFillBuffer(g.mesh.sources.get(0).buff.capacity() * 4, g.mesh.sources.get(1).buff);
            vbo.partiallyFillBuffer(g.mesh.sources.get(0).buff.capacity() * 4 + g.mesh.sources.get(1).buff.capacity() * 4, g.mesh.sources.get(2).buff);
            
            vaos.add(vao);
            vbos.add(vbo);
            ebos.add(ebo);
        }
        
        vao.unbind();
        //vbo.unbind();
        //ebo.unbind();
        c.disposeData();
        
        ShaderProgram s = new ShaderProgram(vertex_shader, fragment_shader);
        s.bindFragDataLocation("outColor", 0);
        s.bind();
        /*for(VAO _vao : vaos) {
            s.enableAttribs(_vao, "position", "normal", "color");
            s.vertexAttribPointer("position", 3, Renderer.type.FLOAT, false, 3 * 4, 0);
            s.vertexAttribPointer("normal", 3, Renderer.type.FLOAT, false, 3 * 4, 0);
            s.vertexAttribPointer("color", 3, Renderer.type.FLOAT, false, 3 * 4, 0);
        }*/
        s.unbind();
        
        mat4 view, proj;
        ModelMatrix model = new ModelMatrix();
        
        view = GLM.lookAt(new vec3(0, 0, 2), new vec3(), new vec3(0, 0, 1));
        proj = GLM.perspective(60, 1280d/720d, 0.1d, 100d);
        
        s.bind();
        s.setUniformMatrix("view", view);
        s.setUniformMatrix("project", proj);
        s.setUniformMatrix("model", model.getModelMatrix());
        
        gl.enable(GLEnable.DEPTH_TEST);
        gl.clearColor(0, 0, 0, 1);
        
        while(!gl.windowIsClosing()) {
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
            
            for(int i = 0; i < c.geometry.size(); i++) {
                int offsetNormal = c.geometry.get(i).mesh.sources.get(0).count() * 4; //4 -> sizeof(float)
                int facesCount = c.geometry.get(i).mesh.polylist.count;

                s.setUniformMatrix("model", model.getModelMatrix());
                vaos.get(i).bind();
                //vbos.get(i).bind();
                s.vertexAttribPointer("position", 3, Renderer.type.FLOAT, false, 3 * 4, 0);
                s.vertexAttribPointer("normal", 3, Renderer.type.FLOAT, false, 3 * 4, offsetNormal);
                s.vertexAttribPointer("color", 3, Renderer.type.FLOAT, false, 3 * 4, offsetNormal); //No tenemos color :(
                //if(i < 166)
                    gl.drawElements(DrawMode.TRIANGLES, facesCount, Renderer.type.UNSIGNED_INT, 0);
                    //gl.drawArrays(DrawMode.TRIANGLES, 0, offsetNormal/4);
            }
            
            gl._game_loop_sync(60);
            t.update();
        }
        
        s.delete();
        for(int i = 0; i < c.geometry.size(); i++) {
            vaos.get(i).delete();
            vbos.get(i).delete();
            ebos.get(i).delete();
        }
        
        gl.destroyDisplay();
    }
    
    public static void cameraxd(String[] args) {
        Renderer gl = Game.gl = new LWJGLRenderer();
        final Timing t = new Timing();
        boolean cr;
        gl.setVsync(true);
        gl.setResizable(true);
        cr = gl.createDisplay(1280, 720, false, "Cámara");
        Keyboard keyboard = new LWJGLKeyboard();
        Mouse mouse = new LWJGLMouse();
        
        if(!cr) {
            System.out.printf("Error al crear la ventana...");
            System.exit(-1);
        }
        
        VAO vao = new VAO();
        BufferObject vbo = new BufferObject(BufferTarget.ARRAY_BUFFER, BufferUsage.STATIC_DRAW);
        
        vao.bind();
        vbo.fillBuffer(new float[] {
              //POSICIÓN           | COLOR          | TEXCOORD
               -1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f,
                1.0f, -1.0f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
                1.0f,  1.0f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
                1.0f,  1.0f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
               -1.0f,  1.0f, -0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
               -1.0f, -1.0f, -0.5f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f
        });
        vao.unbind();
        
        ShaderProgram s = new ShaderProgram(vertex_shader, fragment_shader);
        s.bindFragDataLocation("outColor", 0);
        s.bind();
        s.enableAttribs(vao, "color", "position", "normal");
        vao.bind();
        s.vertexAttribPointer("position", 3, Renderer.type.FLOAT, false, 8 * 4, 0);
        s.vertexAttribPointer("color", 3, Renderer.type.FLOAT, false, 8 * 4, 3 * 4);
        s.vertexAttribPointer("normal", 3, Renderer.type.FLOAT, false, 8 * 4, 5 * 4);
        vao.unbind();
        s.unbind();
        
        mat4 view, proj;
        final ModelMatrix model = new ModelMatrix();
        final vec3 cameraPos = new vec3(0f, 1f, 2.2f), cameraDir = GLM.product(-1f, cameraPos), cameraUp = new vec3(0, 0, 1);
        final vec3 mousePos = new vec3(), cameraRotation = new vec3();
        
        view = GLM.lookAt(cameraPos, GLM.sum(cameraPos, cameraDir), cameraUp);
        proj = GLM.perspective(45d, 1280d/720d, 1d, 10d);
        
        s.bind();
        s.setUniformMatrix("view", view);
        s.setUniformMatrix("project", proj);
        s.setUniformMatrix("model", model.getModelMatrix());
        
        gl.enable(GLEnable.DEPTH_TEST);
        gl.clearColor(1, 1, 1, 1);
        
        keyboard.addListener(new Keyboard.OnKeyboardEvent() {
            @Override
            public void invoke(Keyboard self, double delta) {
                float cameraSpeed = 1.f * (float) delta;
                float x = cameraPos.x, y = cameraPos.y, z = cameraPos.z;
                
                if(self.isKeyPressed("W"))
                    cameraPos.substract(GLM.product(cameraSpeed, cameraPos));
                if(self.isKeyPressed("S"))
                    cameraPos.add(GLM.product(cameraSpeed, cameraPos));
                if(self.isKeyPressed("A"))
                    cameraPos.substract(GLM.product(cameraSpeed, GLM.normalize(GLM.cross(cameraDir, cameraUp))));
                if(self.isKeyPressed("D"))
                    cameraPos.add(GLM.product(cameraSpeed, GLM.normalize(GLM.cross(cameraDir, cameraUp))));
                if(self.isKeyPressed("Q"))
                    cameraPos.z += cameraSpeed;
                if(self.isKeyPressed("E"))
                    cameraPos.z -= cameraSpeed;
                if(self.isKeyPressed("ESCAPE"))
                    GLFW.glfwSetWindowShouldClose(((LWJGLRenderer) Game.gl).window, 1);
                
                x = cameraPos.x - x; y = cameraPos.y - y; z = cameraPos.z - z;
            }
        });

        mouse.addListener(new OnMouseMoveEvent() {
            @Override
            public void invoke(Mouse self) {
                if(!self.isCaptured()) return;

                if(mousePos.z == 0.0f) {
                    mousePos.x = (float) self.getMousePosition().x;
                    mousePos.y = (float) self.getMousePosition().y;
                    mousePos.z = 1;
                    
                    cameraDir.normalize();
                    cameraRotation.z = (float) Math.toDegrees(Math.asin(cameraDir.z));
                    cameraRotation.x = (float) Math.toDegrees(Math.acos(cameraDir.x / Math.cos(Math.toRadians(cameraRotation.z))));
                    
                    //Seleccionando el cuadrante correcto según donde apunta
                    if(cameraRotation.x >= 90.f && cameraDir.z < 0f)
                        cameraRotation.x = -90.f;
                }

                float sensibility = 60f * (float) t.frameTime;
                float dx = (float) self.getMousePosition().x - mousePos.x, dy = mousePos.y - (float) self.getMousePosition().y; //Reverse Y position
                
                mousePos.x = (float) self.getMousePosition().x;
                mousePos.y = (float) self.getMousePosition().y;
                
                cameraRotation.x += dx * sensibility;
                cameraRotation.z += dy * sensibility;
                
                if(cameraRotation.z > 89.f)
                    cameraRotation.z = 89.f;
                else if(cameraRotation.z < -89.f)
                    cameraRotation.z = -89.f;
                
                cameraDir.x = (float) (Math.cos(Math.toRadians(cameraRotation.x)) * Math.cos(Math.toRadians(cameraRotation.z)));
                cameraDir.z = (float) (Math.sin(Math.toRadians(cameraRotation.z)));
                cameraDir.y = (float) (Math.sin(Math.toRadians(cameraRotation.x)) * Math.cos(Math.toRadians(cameraRotation.z)));
            }
        });
        
        //Arreglar esto
        mouse.addListener(new OnMouseClickEvent() {
            @Override
            public void invoke(Mouse self, double delta) {
                if(self.isKeyPressed("LEFT"))
                    self.setCaptured(!self.isCaptured());
            }
        });
        
        while(!gl.windowIsClosing()) {
            gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
            
            model.setIdentity();
            view = GLM.lookAt(cameraPos, GLM.sum(cameraPos, cameraDir), cameraUp);
            s.bind();
            s.setUniformMatrix("model", model.getModelMatrix());
            s.setUniformMatrix("view", view);
            vao.bind();
            gl.drawArrays(DrawMode.TRIANGLES, 0, 6);
            s.unbind();
            
            gl._game_loop_sync(60);
            t.update();
            keyboard.fireEvent(t.frameTime);
            mouse.fireEvent(t.frameTime);
        }
        
        gl.destroyDisplay();
    }
    
    public static void main(String[] args) {
        //colladaxd(args);
        cameraxd(args);
        System.exit(0);
    }
    
    private static final void printError() {
        Renderer.Error err = Game.gl.getError();
        //if(err != Renderer.Error.NO_ERROR)
            System.out.printf("Error %s (%d)\n", err.toString(), err.errno);
    }

}
