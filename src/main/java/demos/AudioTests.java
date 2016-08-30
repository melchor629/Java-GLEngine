package demos;

import com.sun.jna.Native;
import org.melchor629.engine.al.*;
import org.melchor629.engine.loaders.audio.AudioFormat;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioPCM;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.logger.Logger;

import java.io.File;

/**
 * Probando descodificadores de audio
 */
public class AudioTests {
    private static final Logger log = Logger.getLogger(AudioTests.class);

    static final String archivo =
            //"/Volumes/OSX/Música/r4ndom/Nina Simone - Sinnerman (Felix da Housecat Heavenly House Mix).wav";
            "/Volumes/OSX/Música/Floating Points/J&W Beat/Floating Points - K&G Beat (Original Mix).flac";
            //"/Volumes/OSX/Música/lenzman - looking at the stars (the remixes)/01 - Paper Faces (Ivy Lab Remix) [feat. Martyna Baker].mp3";
            //"/Volumes/OSX/Música/r4ndom/Deadmau5 - Live at iTunes Festival 2014.ogg";

    public static void main(String[] args) throws Exception {
        Native.setProtected(true);

        AL al = new LWJGLAudio();
        al.createContext();
        AudioDecoder decoder = AudioDecoder.createDecoderForFile(new File(archivo));
        AudioFormat format;
        AudioPCM data = null;

        if(args.length == 1 && args[0].equals("full")) {
            long timeDecode = System.currentTimeMillis();
            format = decoder.readHeader();
            data = decoder.decodeAll();
            timeDecode = System.currentTimeMillis() - timeDecode;

            String ext = archivo.substring(archivo.lastIndexOf(".") + 1);
            log.info("Time spent decoding %s %dms", ext, timeDecode);

            Buffer sd_buffer = al.createBuffer(data.getDataAsShort(), AL.Format.STEREO16, format.getSampleRate());
            Source sd_source = al.createSource(sd_buffer);
            sd_source.setGain(1.00f);
            sd_source.play();
            Timing.sleep(5000);
            int c;
            do {
                c = System.in.read();
            } while (c != '\n');

            data.cleanUpNativeResources();
            sd_source.stop();
        } else {
            decoder.readHeader();
            StreamingSource sd_source = al.createSource();
            double bufferTime = 0.0;
            for (int i = 0; i < 10; i++) {
                data = decoder.decodeOne();
                sd_source.enqueueBuffer(al.createBuffer(data));
                bufferTime += data.getStoredSeconds();
            }
            bufferTime /= 10.;
            bufferTime *= 1000 / 2;
            System.out.printf("Time waiting between checks: %.0fms\n", bufferTime);
            sd_source.play();

            while (data != null) {
                if (sd_source.getBuffersProcessed() >= 5) {
                    for (int i = 0; i < sd_source.getBuffersProcessed() && data != null; i++) {
                        data = decoder.decodeOne();
                        if (data != null) sd_source.enqueueBuffer(al.createBuffer(data));
                    }
                    sd_source.dequeueBuffers();
                }

                try {
                    Thread.sleep((long) bufferTime);
                } catch (Exception ignore) { }
                if (System.in.available() != 0) {
                    break;
                }
            }

            if (data != null) {
                decoder.delete();
            }
        }

        al.destroyContext();
    }
}
