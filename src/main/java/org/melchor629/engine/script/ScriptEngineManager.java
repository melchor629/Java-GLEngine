package org.melchor629.engine.script;

import org.melchor629.engine.script.engines.JavaScript;
import org.melchor629.engine.script.engines.Lua;
import org.melchor629.engine.utils.logger.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Manages and executes scripts. {@link ScriptEngineManager} let's the programmer
 * bind Java code with the different scripting langs supported by this engine.
 */
public class ScriptEngineManager {
    private static final Logger LOG = Logger.getLogger(ScriptEngineManager.class);
    private static List<ScriptEngine> engines;

    static {
        engines = new ArrayList<>();
        engines.add(new JavaScript());
        engines.add(new Lua());
    }

    /**
     * Adds an object to all engines in their global scope
     * @param objectName Object name
     * @param object Java object to map into scripts
     */
    public static void addObject(String objectName, Object object) {
        engines.forEach(e -> e.addObject(objectName, object));
        LOG.trace("Added object %s to scripts", objectName);
    }

    /**
     * Adds a {@link ScriptModule} to scripts.
     * @param objectName Module name
     * @param module Class to instanciate on a require call
     * @see ScriptModule
     */
    public static void addModule(String objectName, Class<? extends ScriptModule> module) {
        try {
            ScriptModule _module = module.newInstance();
            engines.forEach(e -> e.addModule(objectName, _module));
            LOG.trace("Added module %s to scripts", objectName);
        } catch(Exception e) {
            LOG.throwable("Error adding module to scripts", e);
        }
    }

    /**
     * Adds a global function to scripts.
     * @param objectName Function name
     * @param function Function to be executed when called
     */
    public static void addFunction(String objectName, Function<Object[], Object> function) {
        engines.forEach(e -> e.addFunction(objectName, function));
        LOG.trace("Added function %s to scripts", objectName);
    }

    /**
     * Executes a piece of code of a language. If the language ({@code ext})
     * engine could not be found, {@link NullPointerException} will be throw.
     * Engines could throw {@link ScriptError} when something happend while
     * evaluating the code.
     * @param ext Filename extension for the code
     * @param src The code to be evaluated
     * @throws NullPointerException
     * @throws ScriptError
     */
    public static void executeCode(String ext, String src) throws NullPointerException, ScriptError {
        ScriptEngine engine = findEngineByExtension(ext);
        if(engine != null)
            engine.executeCode(src);
        else
            throw new NullPointerException("Engine not found for script type " + ext);
    }

    /**
     * Finds an engine by filename extension
     * @param js extension
     * @return Engine or null
     */
    private static ScriptEngine findEngineByExtension(String js) {
        for(ScriptEngine engine : engines) {
            if(engine.fileExtensions().contains(js)) {
                return engine;
            }
        }

        return null;
    }
}
