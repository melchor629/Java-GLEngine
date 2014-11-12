import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;

import org.lwjgl.util.WaveData;
import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.AL.Format;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.objects.PCMData;


public class AnotherTestingClass {

	public static void main(String[] args) {
		AL al = Game.al = new LWJGLAudio();
		al.createContext();
		
		
	}

	static PCMData getSound(File f) throws FileNotFoundException {
		WaveData d = WaveData.create(new FileInputStream(f));
		PCMData data = new PCMData();
		data.data = new byte[d.data.capacity()];
		((ByteBuffer) d.data.position(0)).get(data.data);
		data.format = intToFormat(d.format);
		data.freq = d.samplerate;
		d.dispose();
		return data;
	}
	
	static AL.Format intToFormat(int f) {
		switch(f) {
			case 0:
				return Format.MONO8;
			case 1:
				return Format.MONO16;
			case 2:
				return Format.STEREO8;
			case 3:
			default:
				return Format.STEREO16;
		}
	}
}
