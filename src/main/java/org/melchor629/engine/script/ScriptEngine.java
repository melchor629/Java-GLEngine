package org.melchor629.engine.script;

import java.util.List;
import java.util.function.Function;

/**
 * Interface that all scripting engines should implement for a correct function of the
 * system
 */
public interface ScriptEngine {

    /**
     * Adds a global object to this engine.
     * @param objectName Global object name
     * @param object Java Object in which map to
     */
    void addObject(String objectName, Object object);

    /**
     * Adds a module to this engine. A module is instanciate
     * on every {@code require(moduleName: String)} call.
     * @param moduleName Module name
     * @param module Module class
     * @see ScriptModule
     */
    void addModule(String moduleName, ScriptModule module);

    /**
     * Adds a global function to this engine
     * @param functionName Global function name
     * @param function Java Function in which map to
     */
    void addFunction(String functionName, Function<Object[], Object> function);

    /**
     * Evaluates some code on this engine
     * @param code Code to be evaluated
     * @throws ScriptError if an internal error occurrs while evaluating
     */
    void executeCode(String code) throws ScriptError;

    /**
     * Implementors have to return all posible filename extensions
     * supported by its engine.
     * @return All posible filename extensions for this engine
     */
    List<String> fileExtensions();
}
