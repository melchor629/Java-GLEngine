package org.melchor629.engine.input;

import static org.lwjgl.glfw.GLFW.*;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.LWJGLRenderer;

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

    public LWJGLKeyboard() {
        super();
        glfwSetKeyCallback(((LWJGLRenderer) Game.gl).window, new GLFWKeyCallback() {
            @Override
            public void invoke(long window, int key, int scancode, int action, int mods) {
                if(action == GLFW_PRESS)
                    LWJGLKeyboard.this.keysPressed[key] = true;
                else if(action == GLFW_RELEASE)
                    LWJGLKeyboard.this.keysPressed[key] = false;

                LWJGLKeyboard.this.shiftPressed = (mods & GLFW_MOD_SHIFT) == 1;
                LWJGLKeyboard.this.controlPressed = (mods & GLFW_MOD_CONTROL) == 1;
                LWJGLKeyboard.this.altPressed = (mods & GLFW_MOD_ALT) == 1;
                LWJGLKeyboard.this.superPressed = (mods & GLFW_MOD_SUPER) == 1;
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
            pos = -1;
        }
        return this.keysPressed[pos];
    }

}
