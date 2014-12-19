import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.ShortBuffer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;


public class WaveData {
	public final ByteBuffer data;
	
	public final int format;
	
	public final int samplerate;
	
	private WaveData(ByteBuffer data, int format, int samplerate) {
		this.data = data;
		this.format = format;
		this.samplerate = samplerate;
	}
	
	public void dispose() {
		data.clear();
	}
	
	public static WaveData create(InputStream stream) {
		try {
			return create(AudioSystem.getAudioInputStream(stream));
		} catch(Exception e) {
			System.out.printf("Error al leer WAV\n");
			return null;
		}
	}
	
	public static WaveData create(AudioInputStream ais) {
		AudioFormat audioFormat = ais.getFormat();
		
		int channels = 0;
		if(audioFormat.getChannels() == 1) {
			if(audioFormat.getSampleSizeInBits() == 8)
				channels = 0;
			else if(audioFormat.getSampleSizeInBits() == 16)
				channels = 1;
			else
				assert false: "Illegal sample size";
		} else if(audioFormat.getChannels() == 2) {
			if(audioFormat.getSampleSizeInBits() == 8)
				channels = 2;
			else if(audioFormat.getSampleSizeInBits() == 16)
				channels = 3;
			else
				assert false: "Illegal sample size";
		} else {
			assert false: " Only mono or stereo is supported";
		}
		
		ByteBuffer buffer = null;
		try {
			int available = ais.available();
			if(available <= 0) {
				available = ais.getFormat().getChannels() * (int) ais.getFrameLength() * ais.getFormat().getFrameSize();
			}
			
			byte[] buf = new byte[ais.available()];
			int read = 0, total = 0;
			while((read = ais.read(buf, total, buf.length)) != -1 && total < buf.length) {
				total += read;
			}
			buffer = convertAudioBytes(buf, audioFormat.getSampleSizeInBits() == 16,
					audioFormat.isBigEndian() ? ByteOrder.BIG_ENDIAN : ByteOrder.LITTLE_ENDIAN);
		} catch(IOException e) {
			return null;
		}
		
		WaveData wavedata = new WaveData(buffer, channels, (int) audioFormat.getSampleRate());
		
		try {
			ais.close();
		} catch(Exception e) {
			
		}
		return wavedata;
	}
	
	private static ByteBuffer convertAudioBytes(byte[] audio_bytes, boolean two, ByteOrder order) {
		ByteBuffer dest = ByteBuffer.allocateDirect(audio_bytes.length);
		dest.order(ByteOrder.nativeOrder());
		ByteBuffer src = ByteBuffer.wrap(audio_bytes);
		src.order(order);
		if(two) {
			ShortBuffer dest_short = dest.asShortBuffer();
			ShortBuffer src_short = src.asShortBuffer();
			while(src_short.hasRemaining())
				dest_short.put(src_short.get());
		} else {
			while(src.hasRemaining())
				dest.put(src.get());
		}
		dest.rewind();
		src.clear();
		return dest;
	}
}
