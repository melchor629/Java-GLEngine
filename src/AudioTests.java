import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Buffer;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.flac.FlacDecoder;
import org.melchor629.engine.utils.Timing;

import java.io.File;
import java.nio.ShortBuffer;
import java.util.ArrayList;

/**
 * Probando el FlacDecoder y el FlacEncoder
 */
public class AudioTests {
    static { System.setProperty("jna.library.path", "build/binaries/engineSharedLibrary/x64release"); }

    public static String archivo =
            //"/Volumes/OSX/Música/13. Aria Math.wav";
            "/Volumes/OSX/Música/Muse/Drones/11 - The Globalist.flac";

    public static void main(String[] args) throws Exception {
        AL al = Game.al = new LWJGLAudio();
        Game.erasableList = new ArrayList<>();
        al.createContext();
        AudioDecoder decoder = AudioDecoder.createDecoderForFile(new File(archivo));
        AudioContainer data;
        long timeDecode = System.currentTimeMillis(), timeWrite;
        decoder.readHeader();
        decoder.decode();
        timeDecode = System.currentTimeMillis() - timeDecode;

        timeWrite = System.currentTimeMillis();
        /*WavExporter.export("/Users/melchor9000/Desktop/flac.wav", decoder.getSampleData(), decoder.getTotalSamples(),
                decoder.getSampleRate(), decoder.getChannels());*/
        timeWrite = System.currentTimeMillis() - timeWrite;

        System.out.printf("Time spent decoding flac %d, and time spent to write WAV file %d\n", timeDecode, timeWrite);

        data = decoder.getAudioContainer();
        Buffer sd_buffer = new Buffer(data.getDataAsShort(), AL.Format.STEREO16, data.getSampleRate());
        Source sd_source = new Source(sd_buffer);
        data.cleanUpNativeResources();
        sd_source.setGain(1.00f);
        sd_source.play();
        Timing.sleep(5000);
        int c;
        do{ c = System.in.read(); } while(c != '\n');

        /*boolean ok = FlacLibraryNative.instance.engine_flac_encoder("/Users/melchor9000/Desktop/wav.flac", decoder.attributes, decoder.data,
                (int errCode, String msg) -> System.err.printf("[FLACENCODER] (%d) %s\n", errCode, msg));
        if(!ok) System.err.printf("[FLACENCODER] Ha habido un error\n");*/

        sd_source.stop();
        sd_source.delete();
        al.deleteContext();
    }
}
