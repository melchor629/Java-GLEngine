package info;

import org.lwjgl.openal.*;
import org.melchor629.engine.utils.logger.Logger;

import java.lang.reflect.Field;

/**
 * Obtains information about the device and the capabilites you can use in this computer
 * from OpenAL
 */
public class LWJGLOpenALContext {
    private static final Logger LOG = Logger.getLogger(LWJGLOpenALContext.class);

    public static void main(String[] args) {
        long device = ALC10.alcOpenDevice((String) null);
        long context = ALC10.alcCreateContext(device, (int[]) null);
        ALC10.alcMakeContextCurrent(context);
        ALCCapabilities cap2 = ALC.createCapabilities(device);
        ALCapabilities cap = AL.createCapabilities(cap2);

        LOG.info("Extensions supported by the context:");
        for(Field f : cap.getClass().getFields()) {
            try {
                if(f.getBoolean(cap))
                    LOG.info(" - %s", f.getName());
            } catch(Exception ignore) {}
        }

        LOG.info("Extensions supported by the device:");
        for(Field f : cap2.getClass().getFields()) {
            try {
                if(f.getBoolean(cap2))
                    LOG.info(" - %s", f.getName());
            } catch(Exception ignore) {}
        }

        ALC10.alcDestroyContext(context);
        ALC10.alcCloseDevice(device);
    }

    private LWJGLOpenALContext() {}
}
