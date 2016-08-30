package org.melchor629.engine.loaders.audio.vorbis;

import com.sun.jna.ptr.IntByReference;
import org.melchor629.engine.loaders.audio.AudioFormat;
import org.melchor629.engine.loaders.audio.AudioPCM;
import org.melchor629.engine.nativeBridge.LibVorbisFile;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.utils.MemoryUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Decodes an ogg vorbis file
 */
public class VorbisDecoder extends AudioDecoder {
    private LibVorbisFile.OggVorbis_File vf;
    private File file;

    @Override
    public AudioFormat readHeader() throws IOException {
        synchronized (this) {
            vf = new LibVorbisFile.OggVorbis_File();
            String _filePath = file.getAbsolutePath();
            int ret = LibVorbisFile.ov_fopen(_filePath, vf);
            if (ret == -132) {
                //OV_ENOTVORBIS
                throw new AudioDecoderException("File is not a vorbis stream");
            } else if (ret == -133) {
                //OV_BADHEADER
                throw new AudioDecoderException("Corrupted header (may no be ogg file)");
            } else if (ret < 0) {
                throw new AudioDecoderException("Internal vorbis decoder error");
            }

            LibVorbisFile.vorbis_info.byReference vinfo = LibVorbisFile.ov_info(vf, -1);
            format.setChannels(vinfo.channels);
            format.setSampleRate(vinfo.rate.intValue());
            format.setSamples(LibVorbisFile.ov_pcm_total(vf, -1).longValue());
            format.setBitDepth(AudioFormat.BitDepth.INT16); //Solo soportaremos 16 (vorbis solo soporta 8 y 16)
        }
        return format;
    }

    @Override
    public AudioPCM decodeAll() throws IOException {
        if(vf == null) return null;
        ByteBuffer pcm = MemoryUtils.createByteBuffer((int) format.getNumberOfSamples() * format.getChannels() * 2);
        synchronized (this) {
            boolean eof = false;
            byte[] buff = new byte[4096];
            IntByReference currentSection = new IntByReference();

            while (!eof) {
                long ret = LibVorbisFile.ov_read(vf, buff, 4096, 0, 2, 1, currentSection).longValue();
                if (ret == 0) {
                    eof = true;
                } else if (ret < 0) {
                    System.err.println("Error in VorbisDecoder " + ret);
                    break;
                } else {
                    pcm.put(buff, 0, (int) ret);
                }
            }

            delete();
        }
        return new AudioPCM(format, pcm);
    }

    @Override
    public AudioPCM decodeOne() throws IOException {
        if(vf == null) return null;
        ByteBuffer pcm;
        synchronized (this) {
            byte[] buff = new byte[format.getSampleRate() / 50 * format.getChannels() * format.getBitDepth().bitDepth / 8];
            IntByReference currentSection = new IntByReference();

            long ret = LibVorbisFile.ov_read(vf, buff, buff.length, 0, 2, 1, currentSection).longValue();
            if(ret == 0) {
                delete();
                return null;
            } else if(ret < 0) {
                throw new AudioDecoderException("An error ocurred: " + ret);
            } else {
                pcm = MemoryUtils.createByteBuffer((int) ret);
                pcm.put(buff, 0, (int) ret);
                return new AudioPCM(format, pcm);
            }
        }
    }

    @Override
    public void delete() {
        if(vf != null) {
            LibVorbisFile.ov_clear(vf);
            vf = null;
        }
    }

    @Override
    protected void setFile(File file) throws FileNotFoundException {
        this.file = file;
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("ogg", VorbisDecoder.class);
    }
}
