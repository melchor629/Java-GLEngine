package org.melchor629.engine.script;

/**
 * Represents a module loaded by a script. A module is an object that is
 * instanciated once when is loaded/required on a script and contain everything
 * you want to export to scripts in the easy way, and scoped into an object.<br>
 * To retreive the module in a script, you should see method
 * {@link ScriptEngine#addModule(String, ScriptModule)} of the specified script
 * language, that can be found in {@link org.melchor629.engine.script.engines}
 * package.
 */
public interface ScriptModule {

    /**
     * Called when the module is loaded by the first time by a script
     */
    void moduleDidLoad();
}
