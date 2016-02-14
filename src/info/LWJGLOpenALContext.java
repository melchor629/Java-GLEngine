package info;

import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALContext;

import java.lang.reflect.Field;

/**
 * Obtains information about the device and the capabilites you can use in this computer
 * from OpenAL
 */
public class LWJGLOpenALContext {
    public static void main(String[] args) {
        ALContext context = ALContext.create();
        context.makeCurrent();
        ALCapabilities cap = context.getCapabilities();

        System.out.println("Extensions supported by the context:");
        for(Field f : cap.getClass().getFields()) {
            try {
                if(f.getBoolean(cap))
                    System.out.printf(" - %s:\n", f.getName());
            } catch(Exception ignore) {}
        }

        System.out.println("\nExtensions supported by the device:");
        for(Field f : context.getDevice().getCapabilities().getClass().getFields()) {
            try {
                if(f.getBoolean(context.getDevice().getCapabilities()))
                    System.out.printf(" - %s\n", f.getName());
            } catch(Exception ignore) {}
        }



        context.destroy();
        context.getDevice().close();
    }
}
