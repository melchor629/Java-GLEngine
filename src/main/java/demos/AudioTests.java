package demos;

import com.sun.jna.Native;
import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.Buffer;
import org.melchor629.engine.al.Source;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.logger.Logger;

import java.io.File;
import java.util.ArrayList;

/**
 * Probando descodificadores de audio
 */
public class AudioTests {
    private static final Logger log = Logger.getLogger(AudioTests.class);

    public static final String archivo =
            //"/Volumes/OSX/Música/13. Aria Math.wav";
            "/Volumes/OSX/Música/Muse/Drones/11 - The Globalist.flac";
            //"/Volumes/OSX/Música/Floating Points/Floating Points - Vacuum Boogie.mp3";
            //"/Volumes/OSX/Música/Deadmau5 - Live at iTunes Festival 2014.ogg";

    public static void main(String[] args) throws Exception {
        Native.setProtected(true);

        AL al = new LWJGLAudio();
        Game.erasableList = new ArrayList<>();
        al.createContext();
        AudioDecoder decoder = AudioDecoder.createDecoderForFile(new File(archivo));
        AudioContainer data;
        long timeDecode = System.currentTimeMillis();
        decoder.readHeader();
        decoder.decode();
        timeDecode = System.currentTimeMillis() - timeDecode;
        data = decoder.getAudioContainer();

        String ext = archivo.substring(archivo.lastIndexOf(".") + 1);
        log.info("Time spent decoding %s %dms", ext, timeDecode);

        data.getDataAsShort().clear();
        Buffer sd_buffer = al.createBuffer(data.getDataAsShort(), AL.Format.STEREO16, data.getSampleRate());
        Source sd_source = al.createSource(sd_buffer);
        data.cleanUpNativeResources();
        sd_source.setGain(1.00f);
        sd_source.play();
        Timing.sleep(5000);
        int c;
        do{ c = System.in.read(); } while(c != '\n');

        sd_source.stop();
        Game.erasableList.forEach(Erasable::delete);
        al.deleteContext();
    }
}
