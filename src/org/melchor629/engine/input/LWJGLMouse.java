package org.melchor629.engine.input;

import java.lang.reflect.Field;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.melchor629.engine.Game;
import org.melchor629.engine.gl.LWJGLRenderer;
import org.melchor629.engine.utils.math.vec2;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author melchor9000
 */
public class LWJGLMouse extends Mouse {
    private long window;
    
    public LWJGLMouse() {
        super();
        window = ((LWJGLRenderer) Game.gl).window;
        
        glfwSetMouseButtonCallback(window, new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if(action == GLFW_PRESS)
                    LWJGLMouse.this.mousePressed[button] = true;
                else if(action == GLFW_RELEASE)
                    LWJGLMouse.this.mousePressed[button] = false;

                LWJGLMouse.this.shiftPressed = (mods & GLFW_MOD_SHIFT) == 1;
                LWJGLMouse.this.controlPressed = (mods & GLFW_MOD_CONTROL) == 1;
                LWJGLMouse.this.altPressed = (mods & GLFW_MOD_ALT) == 1;
                LWJGLMouse.this.superPressed = (mods & GLFW_MOD_SUPER) == 1;
            }
        });
        
        glfwSetCursorPosCallback(window, new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                vec2 oldPos = LWJGLMouse.this.pos;
                LWJGLMouse.this.dPos.x = (float) xpos - oldPos.x;
                LWJGLMouse.this.dPos.y = oldPos.y - (float) ypos;

                LWJGLMouse.this.pos.x = (float) xpos;
                LWJGLMouse.this.pos.y = (float) ypos;
                
                LWJGLMouse.this.fireOtherEvent();
            }
        });
        
        glfwSetScrollCallback(window, new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                LWJGLMouse.this.wheel.x = (float) xoffset;
                LWJGLMouse.this.wheel.y = (float) yoffset;
                LWJGLMouse.this.fireOtherEvent();
            }
        });
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Mouse#isKeyPressed(java.lang.String)
     */
    @Override
    public boolean isKeyPressed(String key) {
        int keyCode;

        try {
            Field f = GLFW.class.getField("GLFW_MOUSE_BUTTON_" + key.toUpperCase());
            keyCode = f.getInt(null);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException e) {
            keyCode = 19;
        }

        return this.mousePressed[keyCode];
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Mouse#isCaptured()
     */
    @Override
    public boolean isCaptured() {
        return glfwGetInputMode(window, GLFW_CURSOR) != GLFW_CURSOR_NORMAL;
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Mouse#setCaptured(boolean)
     */
    @Override
    public void setCaptured(boolean c) {
        glfwSetInputMode(window, GLFW_CURSOR, c ? GLFW_CURSOR_DISABLED : GLFW_CURSOR_NORMAL);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Mouse#setCursorPosition(float, float)
     */
    @Override
    public void setCursorPosition(float x, float y) {
        glfwSetCursorPos(window, x, y);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Mouse#setCursorPosition(org.melchor629.engine.utils.math.vec2)
     */
    @Override
    public void setCursorPosition(vec2 position) {
        setCursorPosition(position.x, position.y);
    }

}
