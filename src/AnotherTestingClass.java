import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.ByteBuffer;
import java.util.Scanner;

import org.lwjgl.util.WaveData;
import org.melchor629.engine.Game;
import org.melchor629.engine.al.AL;
import org.melchor629.engine.al.AL.Format;
import org.melchor629.engine.al.LWJGLAudio;
import org.melchor629.engine.al.types.Source;
import org.melchor629.engine.objects.PCMData;


public class AnotherTestingClass {

	public static void main(String[] args) throws Exception {
		AL al = Game.al = new LWJGLAudio();
		al.createContext();
		
		PCMData AriaMath = getSound(new File("/cdrom/Mierdas/13. Aria Math.wav"));
		Source am = new Source(AriaMath);
		AriaMath = null;

		PCMData Around = getSound(new File("/cdrom/Mierdas/07 Around the World.wav"));
		Source a = new Source(Around);
		Around = null;
		
		PCMData SoundsBetter = getSound(new File("/cdrom/Mierdas/Stardust - Music Sounds Better With You.wav"));
		Source sb = new Source(SoundsBetter);
		SoundsBetter = null;
		
		System.gc();

		Source current = null;
		
		Scanner s = new Scanner(System.in);
		boolean showed = false;
		while(true) {
			if(!showed) {
				System.out.println("  [1] - Aria Math (C418)");
				System.out.println("  [2] - Around the World (Daft Punk)");
				System.out.println("  [3] - Music Sounds Better With You (Stardust)");
				System.out.println("  [0] - Cerrar programa");
				showed = true;
			}
			
			System.out.print("Seleccione canción: ");
			
			short cancion = s.nextShort();
			if(cancion == 1) {
				am.play();
				current = am;
			} else if(cancion == 2) {
				a.play();
				current = a;
			} else if(cancion == 3) {
				sb.play();
				current = sb;
			} else if(cancion == 0) {
				break;
			}
			
			if(current != null) {
				System.out.println("p -> Pause | r -> Play | s -> Parar");
				while(current.getSourceState() != 0x1014) {
					char c = s.next().charAt(0);
					
					if(c == 'p') {
						current.pause();
					} else if(c == 'r') {
						current.play();
					} else if(c == 's') {
						current.stop();
					}
				}
				
				current = null;
			}
		}
		
		s.close();
		am.destroy();
		a.destroy();
		sb.destroy();
		al.deleteContext();
	}

	static PCMData getSound(File f) throws FileNotFoundException {
		WaveData d = WaveData.create(new BufferedInputStream(new FileInputStream(f)));
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
