package org.melchor629.engine.loaders.audio.opus;

import com.sun.jna.ptr.IntByReference;
import org.melchor629.engine.loaders.audio.AudioFormat;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.loaders.audio.AudioPCM;
import org.melchor629.engine.nativeBridge.LibOpusFile;
import org.melchor629.engine.utils.MemoryUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ShortBuffer;

/**
 * Decodes an ogg/opus file
 */
public class OpusDecoder extends AudioDecoder {
    private LibOpusFile.OggOpusFile of;
    private File file;

    @Override
    public AudioFormat readHeader() throws IOException {
        IntByReference n = new IntByReference();
        of = LibOpusFile.op_open_file(file.getAbsolutePath(), n);

        if(n.getValue() == LibOpusFile.OP_EREAD) {
            throw new AudioDecoderException("Cannot read opus file");
        } else if(n.getValue() == LibOpusFile.OP_ENOTFORMAT) {
            throw new AudioDecoderException("File is not in opus codec");
        } else if(n.getValue() < 0) {
            throw new AudioDecoderException("Internal error in opus decoder: " + n.getValue());
        }

        format.setChannels(LibOpusFile.op_channel_count(of, -1));
        format.setBitDepth(AudioFormat.BitDepth.INT16); //Opus solo soporta short* o float*, nosotros solo short*
        format.setSamples(LibOpusFile.op_pcm_total(of, -1).longValue());
        format.setSampleRate(48000); //Opus solo descodifica a 48000Hz
        return format;
    }

    @Override
    public AudioPCM decodeAll() throws IOException {
        if(of == null) return null;
        boolean eof = false;
        short[] buff = new short[5760 * format.getChannels()];
        ShortBuffer pcm = MemoryUtils.createShortBuffer((int) format.getNumberOfSamples() * format.getChannels());

        while(!eof) {
            int r = LibOpusFile.op_read(of, buff, buff.length, null);
            if(r == 0) {
                eof = true;
            } else if(r < 0) {
                throw new AudioDecoderException("Error while decoding opus: " + r);
            } else {
                pcm.put(buff, 0, r * format.getChannels());
            }
        }

        delete();
        return new AudioPCM(format, pcm);
    }

    @Override
    public AudioPCM decodeOne() throws IOException {
        if(of == null) return null;
        short[] buff = new short[5760 * format.getChannels()];
        int r = LibOpusFile.op_read(of, buff, buff.length, null);
        if(r == 0) {
            delete();
            return null;
        } else if(r < 0) {
            throw new AudioDecoderException("Error while decoding opus: " + r);
        } else {
            ShortBuffer pcm = MemoryUtils.createShortBuffer(r * format.getChannels());
            pcm.put(buff, 0, r * format.getChannels());
            return new AudioPCM(format, pcm);
        }
    }

    @Override
    public void delete() {
        if(of != null) {
            LibOpusFile.op_free(of);
            of = null;
        }
    }

    @Override
    protected void setFile(File file) throws FileNotFoundException {
        this.file = file;
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("opus", OpusDecoder.class);
    }
}
