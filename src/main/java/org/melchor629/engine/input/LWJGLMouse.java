package org.melchor629.engine.input;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.math.Vector2;

import java.lang.reflect.Field;

import static org.lwjgl.glfw.GLFW.*;

/**
 *
 * @author melchor9000
 */
public class LWJGLMouse extends Mouse {
    private long window;
    private GLFWMouseButtonCallback mbCbk;
    private GLFWCursorPosCallback cpCbk;
    private GLFWScrollCallback sCbk;
    
    public LWJGLMouse(long w) {
        super();
        window = w;
        
        glfwSetMouseButtonCallback(window, mbCbk = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                boolean beforeStatus = LWJGLMouse.this.mousePressed[button];
                if(action == GLFW_PRESS)
                    LWJGLMouse.this.mousePressed[button] = true;
                else if(action == GLFW_RELEASE)
                    LWJGLMouse.this.mousePressed[button] = false;
                if(beforeStatus != LWJGLMouse.this.mousePressed[button])
                    LWJGLMouse.this.fireMouseClick();
            }
        });
        
        glfwSetCursorPosCallback(window, cpCbk = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                Vector2 oldPos = LWJGLMouse.this.pos;
                LWJGLMouse.this.dPos.x((float) xpos - oldPos.x());
                LWJGLMouse.this.dPos.y(oldPos.y() - (float) ypos);

                LWJGLMouse.this.pos.x((float) xpos);
                LWJGLMouse.this.pos.y((float) ypos);

                LWJGLMouse.this.fireMouseMove(Timing.getGameTiming().frameTime);
            }
        });
        
        glfwSetScrollCallback(window, sCbk = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                LWJGLMouse.this.wheel.x((float) xoffset);
                LWJGLMouse.this.wheel.y((float) yoffset);
                LWJGLMouse.this.fireWheelMove();
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
     * @see org.melchor629.engine.input.Mouse#setCursorPosition(org.melchor629.engine.utils.math.Vector2)
     */
    @Override
    public void setCursorPosition(Vector2 position) {
        setCursorPosition(position.x(), position.y());
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.input.Mouse#release()
     */
    @Override
    public void release() {
        cpCbk.free();
        mbCbk.free();
        sCbk.free();
    }

}
