package org.melchor629.engine.loaders.audio.vorbis;

import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import org.melchor629.engine.clib.LibVorbisFile;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.utils.BufferUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Decodes an ogg vorbis file
 */
public class VorbisDecoder extends AudioDecoder {
    private Pointer vf;
    private File file;

    @Override
    public void readHeader() throws IOException {
        synchronized (this) {
            vf = LibVorbisFile.createOggVorbis_File();
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
            container.setChannels(vinfo.channels);
            container.setSampleRate(vinfo.rate.intValue());
            container.setSamples(LibVorbisFile.ov_pcm_total(vf, -1).longValue());
            container.setBitDepth(AudioContainer.BitDepth.INT16); //Solo soportaremos 16 (vorbis solo soporta 8 y 16)
        }
    }

    @Override
    public void decode() throws IOException {
        boolean eof = false;
        ByteBuffer pcm = BufferUtils.createByteBuffer((int) container.getNumberOfSamples() * container.getChannels() * 2);
        byte[] buff = new byte[4096];
        IntByReference currentSection = new IntByReference();

        while(!eof && pcm.hasRemaining()) {
            long ret = LibVorbisFile.ov_read(vf, buff, 4096, 0, 2, 1, currentSection).longValue();
            if(ret == 0) {
                eof = true;
            } else if(ret < 0) {
                System.err.println("Error in VorbisDecoder");
            } else {
                pcm.put(buff, 0, (int) ret);
            }
        }

        container.setBuffer(pcm);
        LibVorbisFile.ov_clear(vf);
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
