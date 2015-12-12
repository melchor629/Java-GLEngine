import com.sun.jna.Native;
import org.melchor629.engine.Erasable;
import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Buffer;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.flac.FlacDecoder;
import org.melchor629.engine.utils.Timing;
import org.melchor629.engine.utils.WavExporter;

import java.io.File;
import java.util.ArrayList;

/**
 * Probando el FlacDecoder y el FlacEncoder
 */
public class AudioTests {
    static { System.setProperty("jna.library.path", "build/binaries/engineSharedLibrary/x64release"); }

    public static String archivo =
            //"/Volumes/OSX/Música/13. Aria Math.wav";
            //"/Volumes/OSX/Música/Muse/Drones/11 - The Globalist.flac";
            //"/Volumes/OSX/Música/Floating Points/Floating Points - Vacuum Boogie.mp3";
            "/Volumes/OSX/Música/Deadmau5 - Live at iTunes Festival 2014.ogg";

    public static void main(String[] args) throws Exception {
        Native.setProtected(true);

        AL al = Game.al = new LWJGLAudio();
        Game.erasableList = new ArrayList<>();
        al.createContext();
        AudioDecoder decoder = AudioDecoder.createDecoderForFile(new File(archivo));
        AudioContainer data;
        long timeDecode = System.currentTimeMillis(), timeWrite;
        decoder.readHeader();
        decoder.decode();
        timeDecode = System.currentTimeMillis() - timeDecode;
        data = decoder.getAudioContainer();

        timeWrite = System.currentTimeMillis();
        /*WavExporter.export("/Users/melchor9000/Desktop/flac.wav", data.getDataAsShort(), data.getNumberOfSamples(),
                data.getSampleRate(), data.getChannels());*/
        timeWrite = System.currentTimeMillis() - timeWrite;

        String ext = archivo.substring(archivo.lastIndexOf(".") + 1);
        System.out.printf("Time spent decoding %s %dms, and time spent to write WAV file %dms\n", ext, timeDecode, timeWrite);

        data.getDataAsShort().clear();
        Buffer sd_buffer = new Buffer(data.getDataAsShort(), AL.Format.STEREO16, data.getSampleRate());
        Source sd_source = new Source(sd_buffer);
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
