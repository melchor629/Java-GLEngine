package org.melchor629.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.melchor629.engine.gl.LWJGLWindow;

/**
 * Implementation of {@link Keyboard} class for GLFW
 * through LWJGL.<br>
 * If you want {@link #isKeyPressed(String)} to work correctly,
 * see the link below for the name of every key. {@code GLFW_KEY_} is
 * not needed, only the end.
 * @see <a href="http://www.glfw.org/docs/latest/group__keys.html">
 *       http://www.glfw.org/docs/latest/group__keys.html</a>
 * @author melchor9000
 */
public class LWJGLKeyboard extends Keyboard {
    private static GLFWKeyCallback kCbk;

    public LWJGLKeyboard(LWJGLWindow window) {
        super();
        glfwSetKeyCallback(window.window, kCbk = new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(key == -1) key = 1023;
                if(action == GLFW_PRESS) {
                    LWJGLKeyboard.this.keysPressed[key] = true;
                    LWJGLKeyboard.this.firePressEvent(key);
                } else if(action == GLFW_RELEASE) {
                    LWJGLKeyboard.this.keysPressed[key] = false;
                    LWJGLKeyboard.this.fireReleaseEvent(key);
                }

                LWJGLKeyboard.this.shiftPressed = (mods & GLFW_MOD_SHIFT) == 1 || key == GLFW_KEY_LEFT_SHIFT || key == GLFW_KEY_RIGHT_SHIFT;
                LWJGLKeyboard.this.controlPressed = (mods & GLFW_MOD_CONTROL) == GLFW_MOD_CONTROL;
                LWJGLKeyboard.this.altPressed = (mods & GLFW_MOD_ALT) == GLFW_MOD_ALT;
                LWJGLKeyboard.this.superPressed = (mods & GLFW_MOD_SUPER) == GLFW_MOD_SUPER;
            }
        });
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Keyboard#isKeyPressed(java.lang.String)
     */
    @Override
    public boolean isKeyPressed(String key) {
        int pos;
        try {
            Field f = GLFW.class.getDeclaredField("GLFW_KEY_" + key.toUpperCase());
            pos = f.getInt(null);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            e.printStackTrace();
            pos = -1;
        }
        return this.keysPressed[pos];
    }

    @Override
    public String getStringRepresentation(int keycode) {
        String key = null;

        try {
            for(Field f : GLFW.class.getFields()) {
                try {
                    int code = f.getInt(null);
                    if (code == keycode)
                        key = f.getName().substring(9);
                } catch(IllegalArgumentException ignore) {}
            }
        } catch(SecurityException | IllegalAccessException e) {
            e.printStackTrace();
            key = null;
        }

        return key;
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Keyboard#release()
     */
    @Override
    public void release() {
        kCbk.free();
    }

}
