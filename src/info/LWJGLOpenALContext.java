package info;

import org.lwjgl.openal.ALCapabilities;
import org.lwjgl.openal.ALContext;
import org.melchor629.engine.utils.logger.Logger;

import java.lang.reflect.Field;

/**
 * Obtains information about the device and the capabilites you can use in this computer
 * from OpenAL
 */
public class LWJGLOpenALContext {
    private static final Logger LOG = Logger.getLogger(LWJGLOpenALContext.class);

    public static void main(String[] args) {
        ALContext context = ALContext.create();
        context.makeCurrent();
        ALCapabilities cap = context.getCapabilities();

        LOG.info("Extensions supported by the context:");
        for(Field f : cap.getClass().getFields()) {
            try {
                if(f.getBoolean(cap))
                    LOG.info(" - %s:", f.getName());
            } catch(Exception ignore) {}
        }

        LOG.info("Extensions supported by the device:");
        for(Field f : context.getDevice().getCapabilities().getClass().getFields()) {
            try {
                if(f.getBoolean(context.getDevice().getCapabilities()))
                    LOG.info(" - %s", f.getName());
            } catch(Exception ignore) {}
        }



        context.destroy();
        context.getDevice().close();
    }

    private LWJGLOpenALContext() {}
}
