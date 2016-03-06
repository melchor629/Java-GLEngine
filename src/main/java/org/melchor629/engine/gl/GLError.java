package org.melchor629.engine.gl;

/**
 * Exception for GL errors and other stuff related to OpenGL that should not
 * fail, like calling a bind on a deleted VAO.
 * @author melchor9000
 */
public class GLError extends RuntimeException {
    private String msg, glFunc;

    /**
     * Serial UID for this exception
     */
    private static final long serialVersionUID = 408073609527855782L;

    /**
     * Create a new GLError with the given message
     * @param msg Message
     */
    public GLError(String msg) {
        super(msg);
        this.msg = msg;
    }

    /**
     * Create a new GLError with the given message and specifying the
     * OpenGL Function that send an error
     * @param glFunc OpenGL Function
     * @param msg Error' message
     */
    public GLError(String glFunc, String msg) {
        super(msg);
        this.msg = msg;
        this.glFunc = glFunc;
    }

    @Override
    public String getMessage() {
        if(glFunc == null)
            return msg;
        else
            return String.format("[%s] %s", glFunc, msg);
    }
}
