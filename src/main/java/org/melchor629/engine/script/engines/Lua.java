package org.melchor629.engine.script.engines;

import org.luaj.vm2.LuaError;
import org.luaj.vm2.LuaValue;
import org.melchor629.engine.script.ScriptEngine;
import org.melchor629.engine.script.ScriptError;
import org.melchor629.engine.script.ScriptModule;

import javax.script.ScriptContext;
import javax.script.ScriptException;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * Lua engine
 */
public class Lua implements ScriptEngine {
    private javax.script.ScriptEngine engine;
    private Map<String, ScriptModule> moduleMap;
    private Map<String, Function<Object[], Object>> functionMap;

    public Lua() {
        engine = new javax.script.ScriptEngineManager().getEngineByName("luaj");
        moduleMap = new TreeMap<>();
        functionMap = new TreeMap<>();

        try {
            addObject("__eng", this);
            engine.eval(
                    "function loadmodule(name)\n" +
                        "return __eng:initModule(name)\n" +
                    "end\n");
        } catch(Exception ignore) {}
    }

    @Override
    public void addObject(String objectName, Object object) {
        engine.getContext().setAttribute(objectName, object, ScriptContext.ENGINE_SCOPE);
    }

    @Override
    public void addModule(String moduleName, ScriptModule module) {
        if(moduleMap.containsKey(moduleName)) throw new IllegalArgumentException("Module named "+moduleName+" exists");
        moduleMap.put(moduleName, module);
    }

    @Override
    public void addFunction(String functionName, Function<Object[], Object> function) {
        if(functionMap.containsKey(functionName)) throw new IllegalArgumentException("Function "+functionName+" exists");
        try {
            engine.eval(
                "function " + functionName + "(...)\n" +
                    "return __eng:callFunction(\"" + functionName + "\", ...)\n" +
                "end\n");
        } catch(ScriptException e) {
            throw new ScriptError(e.getMessage(), e.getLineNumber(), e.getColumnNumber(), e.getFileName());
        }
        functionMap.put(functionName, function);
    }

    @Override
    public void executeCode(String code) throws ScriptError {
        try {
            engine.eval(code);
        } catch(ScriptException e) {
            ScriptError err =  new ScriptError(e.getMessage(), e.getLineNumber(), e.getColumnNumber(), e.getFileName());
            err.initCause(e);
            throw err;
        } catch(LuaError e) {
            ScriptError err =  new ScriptError(e.getMessage());
            err.initCause(e);
            throw err;
        }
    }

    @Override
    public List<String> fileExtensions() {
        return Collections.singletonList("lua");
    }

    public Object callFunction(String name, Object[] args) {
        if(functionMap.containsKey(name)) {
            Object ret = functionMap.get(name).apply(args);
            if(ret instanceof String) {
                return LuaValue.valueOf((String) ret);
            } else if(ret instanceof Number) {
                return LuaValue.valueOf(((Number) ret).intValue());
            } else if(ret instanceof Boolean) {
                return LuaValue.valueOf((Boolean) ret);
            }
            return ret;
        }
        return null;
    }

    public ScriptModule initModule(String name) {
        if(moduleMap.containsKey(name)) {
            return moduleMap.get(name);
        }
        return null;
    }
}
