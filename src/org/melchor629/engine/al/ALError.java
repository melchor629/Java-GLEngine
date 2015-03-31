package org.melchor629.engine.al;

/**
 * Error threw when an exception or other kind of errors occurred
 * @author melchor9000
 */
public class ALError extends RuntimeException {
	
	protected String msg, alFunc;

	/**
	 * Serial UID for this error
	 */
	private static final long serialVersionUID = -4828686054213396284L;

    /**
     * Create a new ALError with the given message
     * @param msg Message
     */
    public ALError(String msg) {
        super(msg);
        this.msg = msg;
        //this.initCause(this);
    }

    /**
     * Create a new GLError with the given message and specifying the
     * OpenGL Function that send an error
     * @param glFunc OpenGL Function
     * @param msg Error' message
     */
    public ALError(String glFunc, String msg) {
        super(msg);
        this.msg = msg;
        this.alFunc = glFunc;
        this.initCause(this);
    }

    @Override
    public String getMessage() {
        if(alFunc == null)
            return msg;
        else
            return String.format("[%s] %s", alFunc, msg);
    }
}
