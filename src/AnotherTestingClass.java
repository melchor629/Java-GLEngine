import static org.lwjgl.system.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.system.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.system.glfw.GLFW.glfwTerminate;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.system.glfw.GLFW;
import org.lwjgl.system.glfw.WindowCallback;
import org.lwjgl.system.glfw.WindowCallbackAdapter;
import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.AL.Format;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Listener;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.gl.Renderer;
import org.melchor629.engine.gl.Renderer.GLVersion;
import org.melchor629.engine.objects.PCMData;
import org.melchor629.engine.utils.math.vec3;

public class AnotherTestingClass {
	private static Source current = null;

	public static void mains(String[] args) throws Exception {
		AL al = Game.al = new LWJGLAudio();
		LWJGLRenderer gl = (LWJGLRenderer) (Game.gl = new LWJGLRenderer());

		/*gl.createDisplay(800, 600, false, "OpenGL 2.1", GLVersion.GL21);

		gl.clearColor(0, 0, 0, 1);
		GL11.glPointSize(10.f);

		GL11.glViewport(0, 0, 800, 600);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.f, 800.f, 600.f, 0.f, 0.1f, 100.f);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);

		//GLFW.glfwSwapInterval(1);

		while(!gl.windowIsClosing()) {
			gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
			GL11.glLoadIdentity();
			GL11.glClearColor(0.f, 0, 0, 1);

			GL11.glPushMatrix(); {
				//GL11.glTranslatef(pos.x, pos.y, pos.z+1.1f);
				GL11.glColor3f(1, 1, 1);
				GL11.glBegin(GL11.GL_POINTS);
				GL11.glVertex3f(3, 3, 10.1f);
				GL11.glEnd();
			} GL11.glPopMatrix();

			GL11.glTranslatef(0, 0, 3);
			GL11.glRectf(20, 20, 30, 30);

			GL11.glFlush();
			//gl._game_loop_sync(30);
			GLFW.glfwSwapBuffers(gl.window);
			GLFW.glfwPollEvents();
		}

		gl.destroyDisplay();*/
		
		GLFW.glfwDefaultWindowHints();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, 0);
		GLFW.glfwWindowHint(GLFW.GLFW_RESIZABLE, 1);
		
		int WIDTH = 800;
		int HEIGHT = 600;
		
		long window = GLFW.glfwCreateWindow(WIDTH, HEIGHT, "Hello World!", 0l, 0l);
		WindowCallback.set(window, new WindowCallbackAdapter() {
			public void key(long window, int key, int scancode, int action, int mods) {
				if(key == GLFW.GLFW_KEY_ESCAPE && action == GLFW.GLFW_RELEASE)
					GLFW.glfwSetWindowShouldClose(window, 1);
			}
		});
		
		GLFW.glfwMakeContextCurrent(window);
		GLFW.glfwSwapInterval(1);
		GLFW.glfwShowWindow(window);
		
		GLContext.createFromCurrent();
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, WIDTH, HEIGHT, 0, 0, 1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		GL11.glPointSize(10.0f);
		GL11.glPolygonMode(GL11.GL_FRONT, GL11.GL_SMOOTH);
		double t = 0.0f;
		while(GLFW.glfwWindowShouldClose(window) == 0) {
			GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.1f, 0.5f, 0.1f, 0.0f);

			GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_POINTS);
			GL11.glVertex3d(100*Math.cos(Math.PI*t) + 400, 300 +100*Math.sin(Math.PI*t), 0);
			GL11.glEnd();
			GL11.glPopMatrix();
			t += 1d / 60d;
			
			GLFW.glfwSwapBuffers(window);
			GLFW.glfwPollEvents();
		}

		glfwDestroyWindow(window);
    	glfwMakeContextCurrent(0l);
        glfwTerminate();
	}

	public static void main(String[] args) throws Exception {
		AL al = Game.al = new LWJGLAudio();
		LWJGLRenderer gl = (LWJGLRenderer) (Game.gl = new LWJGLRenderer());
		al.createContext();
		GLFW.glfwWindowHint(GLFW.GLFW_VISIBLE, 0);
		gl.createDisplay(800, 600, false, "OpenAL", GLVersion.GL21);
		
		PCMData AriaMath = getSound(new File("/home/melchor/Música/13. Aria Math.wav"));
		Source am = new Source(AriaMath);
		AriaMath.data2.clear();
		AriaMath = null;

		PCMData Around = getSound(new File("/home/melchor/Música/07 Around the World.wav"));
		Source a = new Source(Around);
		Around.data2.clear();
		Around = null;
		
		PCMData SoundsBetter = getSound(new File("/home/melchor/Música/Stardust - Music Sounds Better With You.wav"));
		Source sb = new Source(SoundsBetter);
		SoundsBetter.data2.clear();
		SoundsBetter = null;
		
		Listener.setPosition(400, 300, 0);
		System.gc();
		
		gl.clearColor(0, 0, 0, 1);
		GL11.glPointSize(10.0f);
		
		GL11.glViewport(0, 0, 800, 600);
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0.f, 800.f, 600.f, 00.f, 0, 1);
		
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
		WindowCallback.set(((LWJGLRenderer) gl).window, new WindowCallbackAdapter() {
			@Override
			public void character(long window, int codepoint) {
				char c = (char) codepoint;
				
				if(current == null) {
					if(c == '1') {
						current = am;
					} else if(c == '2') {
						current = a;
					} else if(c == '3') {
						current = sb;
					}
					
					if(current != null)
						current.play();
				} else {
					if(c == ' ') {
						current.stop();
						current = null;
					} else if(c == 'p') {
						current.pause();
					} else if(c == 'r') {
						current.play();
					} else System.out.printf("%c ", c);
				}
			}

			@Override
			public void key(long window, int key, int scancode, int action, int mods) {
				if(current != null) {
					vec3 pos = current.getPosition();
					//if(action == GLFW.GLFW_PRESS)
					switch(key) {
						case GLFW.GLFW_KEY_UP:
							current.setPosition(new vec3(pos.x, pos.y - 5f, pos.z));
							break;
							
						case GLFW.GLFW_KEY_DOWN:
							current.setPosition(new vec3(pos.x, pos.y + 5f, pos.z));
							break;
							
						case GLFW.GLFW_KEY_RIGHT:
							current.setPosition(new vec3(pos.x + 5f, pos.y, pos.z));
							break;
							
						case GLFW.GLFW_KEY_LEFT:
							current.setPosition(new vec3(pos.x - 5f, pos.y, pos.z));
							break;
					}
					pos = current.getPosition();
				}
			}

			@Override
			public void mouseButton(long window, int button, int action, int mods) {
				// TODO Auto-generated method stub
				
			}

			@Override
			public void cursorPos(long window, double xpos, double ypos) {
				
			}
		});
		GLFW.glfwShowWindow(gl.window);
		GLFW.glfwSwapInterval(1);
		
		while(!gl.windowIsClosing()) {
			gl.clear(Renderer.COLOR_CLEAR_BIT | Renderer.DEPTH_BUFFER_BIT);
			GL11.glClearColor(0.1f, 0.1f, 0.1f, 1);
			
			GL11.glPushMatrix();
			if(current != null) {
				vec3 pos = current.getPosition();
				GL11.glColor3f(1, 1, 1);
				GL11.glBegin(GL11.GL_POINTS);
				GL11.glVertex3f(pos.x, pos.y, pos.z);
				GL11.glEnd();
			} GL11.glPopMatrix();

			GL11.glColor3f(0.6f, 0.8f, 0.7f);
			GL11.glRectf(390, 290, 410, 310);

			GL11.glFlush();
			gl._game_loop_sync(30);
		}
		
		am.destroy();
		a.destroy();
		sb.destroy();

		al.deleteContext();
		gl.destroyDisplay();
	}

	static PCMData getSound(File f) throws FileNotFoundException {
		WaveData d = WaveData.create(new BufferedInputStream(new FileInputStream(f)));
		PCMData data = new PCMData();
		data.data2 = d.data;
		data.format = intToFormat(d.format);
		data.freq = d.samplerate;
		return data;
	}
	
	static AL.Format intToFormat(int f) {
		switch(f) {
			case 0:
				return Format.MONO8;
			case 1:
				return Format.MONO16;
			case 2:
				return Format.STEREO8;
			case 3:
			default:
				return Format.STEREO16;
		}
	}
}
