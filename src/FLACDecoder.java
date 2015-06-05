import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Buffer;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.loaders.Flac;
import org.melchor629.engine.utils.Timing;

/**
 * Probando el FlacDecoder y el FlacEncoder
 */
public class FLACDecoder {
    public static String archivo =
            "/Users/melchor9000/Downloads/Deadmau5 - 2009 - For Lack of a Better Name/10 Deadmau5 - Strobe.flac";
            //"/Users/melchor9000/Downloads/Telegram/(09) [Rammstein] Te Quiero Puta!.flac";
            //"/Users/melchor9000/Desktop/sideral_driving.flac";

    public static void main(String[] args) throws Exception {
        AL al = Game.al = new LWJGLAudio();
        al.createContext();
        Flac decoder = new Flac(archivo, true);
        long timeDecode = System.currentTimeMillis(), timeWrite;
        decoder.decode();
        timeDecode = System.currentTimeMillis() - timeDecode;

        timeWrite = System.currentTimeMillis();
        /*WavExporter.export("/Users/melchor9000/Desktop/flac.wav", decoder.getSampleData(), decoder.getTotalSamples(),
                decoder.getSampleRate(), decoder.getChannels());*/
        timeWrite = System.currentTimeMillis() - timeWrite;

        System.out.printf("Time spent decoding flac %d, and time spent to write WAV file %d\n", timeDecode, timeWrite);

        Buffer sd_buffer = new Buffer(decoder.getSampleData(), AL.Format.MONO16, decoder.getSampleRate());
        Source sd_source = new Source(sd_buffer);
        decoder.clear();
        sd_source.setGain(1.00f);
        sd_source.play();
        Timing.sleep(5000);
        while(System.in.read() != '\n');

        /*boolean ok = FlacLibraryNative.instance.engine_flac_encoder("/Users/melchor9000/Desktop/wav.flac", decoder.attributes, decoder.data,
                (int errCode, String msg) -> System.err.printf("[FLACENCODER] (%d) %s\n", errCode, msg));
        if(!ok) System.err.printf("[FLACENCODER] Ha habido un error\n");*/

        sd_source.stop();
        sd_source.delete();
        decoder.clear();
        al.deleteContext();
    }
}
