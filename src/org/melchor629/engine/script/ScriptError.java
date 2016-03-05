package org.melchor629.engine.script;

/**
 *
 */
public class ScriptError extends RuntimeException {
    public final int line, column;
    public final String file;

    public ScriptError(String msg) {
        super(msg);
        line = column = -1;
        file = null;
    }

    public ScriptError(String msg, int line, int column) {
        super(":" + line + ":" + column + " " + msg);
        this.line = line;
        this.column = column;
        this.file = null;
    }

    public ScriptError(String msg, int line, int column, String file) {
        super(file + ":" + line + ":" + column + " " + msg);
        this.line = line;
        this.column = column;
        this.file = file;
    }
}
