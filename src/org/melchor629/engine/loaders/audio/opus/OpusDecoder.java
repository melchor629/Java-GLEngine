package org.melchor629.engine.loaders.audio.opus;

import com.sun.jna.ptr.IntByReference;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.nativeBridge.LibOpusFile;
import org.melchor629.engine.utils.BufferUtils;

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
    public void readHeader() throws IOException {
        synchronized (this) {
            IntByReference n = new IntByReference();
            of = LibOpusFile.op_open_file(file.getAbsolutePath(), n);

            if(n.getValue() == LibOpusFile.OP_EREAD) {
                throw new AudioDecoderException("Cannot read opus file");
            } else if(n.getValue() == LibOpusFile.OP_ENOTFORMAT) {
                throw new AudioDecoderException("File is not in opus codec");
            } else if(n.getValue() < 0) {
                throw new AudioDecoderException("Internal error in opus decoder: " + n.getValue());
            }

            container.setChannels(LibOpusFile.op_channel_count(of, -1));
            container.setBitDepth(AudioContainer.BitDepth.INT16); //Opus solo soporta short* o float*, nosotros solo short*
            container.setSamples(LibOpusFile.op_pcm_total(of, -1).longValue());
            container.setSampleRate(48000); //Opus solo descodifica a 48000Hz
        }
    }

    @Override
    public void decode() throws IOException {
        synchronized (this) {
            boolean eof = false;
            short[] buff = new short[5760 * container.getChannels()];
            ShortBuffer pcm = BufferUtils.createShortBuffer((int) container.getNumberOfSamples() * container.getChannels());

            while(!eof) {
                int r = LibOpusFile.op_read(of, buff, buff.length, null);
                if(r == 0) {
                    eof = true;
                } else if(r < 0) {
                    LibOpusFile.op_free(of);
                    throw new AudioDecoderException("Error while decoding opus: " + r);
                } else {
                    pcm.put(buff, 0, r);
                }
            }

            container.setBuffer(pcm);
            LibOpusFile.op_free(of);
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
