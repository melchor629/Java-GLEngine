package demos;

import org.melchor629.engine.script.ScriptEngineManager;
import org.melchor629.engine.script.ScriptModule;

public class ScriptsTesingClass implements ScriptModule {
    public static void main(String[] args) {
        ScriptEngineManager.addFunction("pruebaFuncion", arguments -> {
            System.out.println("pruebaFuncion");
            return null;
        });

        ScriptEngineManager.addObject("pruebaObjeto", new ScriptsTesingClass());
        ScriptEngineManager.addModule("module", ScriptsTesingClass.class);

        ScriptEngineManager.executeCode("js",
            "(function() {\n" +
                "print('==== JavaScript Code ====');\n" +
                "print(pruebaFuncion());\n" +
                "pruebaObjeto.testFunction();\n" +
                "var modulo = require('module');\n" +
                "modulo.testFunction();" +
                "print('')\n" +
            "})();");

        ScriptEngineManager.executeCode("lua",
            "io.write(\"==== Lua Code ====\\n\")\n" +
            "local __res = pruebaFuncion()\n" +
            "if __res ~= nil then\n" +
                "io.write(__res)\n" +
            "else\n" +
                "io.write(\"nil\\n\")\n" +
            "end\n" +
            "pruebaObjeto:testFunction()\n" +
            "modulo = loadmodule(\"module\")\n" +
            "modulo:testFunction()\n" +
            "io.write(\"\\n\")\n");
    }

    public void testFunction() {
        System.out.println("testFunction");
    }

    @Override
    public void moduleDidLoad() {
        System.out.println("module loaded");
    }
}
