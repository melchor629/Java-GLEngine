package org.melchor629.engine.script.engines;

import jdk.nashorn.api.scripting.ScriptObjectMirror;
import org.melchor629.engine.script.ScriptEngine;
import org.melchor629.engine.script.ScriptError;
import org.melchor629.engine.script.ScriptModule;
import org.melchor629.engine.utils.IOUtils;

import javax.script.ScriptContext;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * JavaScript engine using Oracle Nashorn
 */
public class JavaScript implements ScriptEngine {
    private javax.script.ScriptEngine engine;
    public Map<String, Function<Object[], Object>> functionMap;
    public Map<String, ScriptModule> moduleMap;

    public JavaScript() {
        engine = new javax.script.ScriptEngineManager().getEngineByName("nashorn");
        functionMap = new TreeMap<>();
        moduleMap = new TreeMap<>();

        try {
            engine.getContext().setAttribute("__eng", this, ScriptContext.ENGINE_SCOPE);
            engine.eval(IOUtils.readStream(IOUtils.getResourceAsStream("scripts/js/wrap.js")));
        } catch(Exception ignore) {}
    }

    @Override
    public void addObject(String objectName, Object object) {
        engine.getContext().setAttribute(objectName, object, ScriptContext.ENGINE_SCOPE);
    }

    @Override
    public void addModule(String objectName, ScriptModule module) {
        if(moduleMap.containsKey(objectName)) throw new IllegalArgumentException("Module named "+objectName+" exists");
        moduleMap.put(objectName, module);
    }

    @Override
    public void addFunction(String objectName, Function<Object[], Object> function) {
        if(functionMap.containsKey(objectName)) throw new IllegalArgumentException("Function "+objectName+" exists");
        try {
            engine.eval("function " + objectName + "() {" +
                    "return __exec_func_java_('" + objectName + "', arguments);" +
                "}");
            functionMap.put(objectName, function);
        } catch(Exception ignore) {}
    }

    @Override
    public void executeCode(String code) throws ScriptError {
        try {
            engine.eval(code);
        } catch(javax.script.ScriptException e) {
            ScriptError err =  new ScriptError(e.getMessage(), e.getLineNumber(), e.getColumnNumber(), e.getFileName());
            err.initCause(e);
            throw err;
        }
    }

    @Override
    public List<String> fileExtensions() {
        return Arrays.asList("js", "es6");
    }

    public Object wrapperFunctionCall(String name, ScriptObjectMirror arguments) {
        return functionMap.get(name).apply(arguments.values().toArray());
    }
}
