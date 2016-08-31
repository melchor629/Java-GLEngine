package org.melchor629.engine.loaders.audio.lame;

import com.sun.jna.NativeLong;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.loaders.audio.AudioFormat;
import org.melchor629.engine.loaders.audio.AudioPCM;
import org.melchor629.engine.utils.MemoryUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;

import static org.melchor629.engine.nativeBridge.LibMpg123.*;
import static org.melchor629.engine.nativeBridge.LibMpg123.mpg123_errors.*;

/**
 * Decodes an mp3 audio file using libmpg123
 */
public class Mpg123Decoder extends AudioDecoder {
    private mpg123_handle h;
    private int safeBuffer;
    private ByteBuffer tmpBuff;

    public Mpg123Decoder() {}

    @Override
    public AudioFormat readHeader() throws IOException {
        NativeLongByReference sr = new NativeLongByReference();
        IntByReference ch = new IntByReference(),
                en = new IntByReference();
        throwOnError(mpg123_getformat(h, sr, ch, en));
        safeBuffer = mpg123_safe_buffer();

        format.setBitDepth(AudioFormat.bitDepthFromNumber(mpg123_samplesize(en.getValue()) * 8));
        format.setChannels(ch.getValue());
        format.setSampleRate(sr.getValue().intValue());

        long ret2 = mpg123_length(h).longValue();
        if(ret2 != MPG123_ERR)
            format.setSamples(ret2);
        tmpBuff = MemoryUtils.createByteBuffer(safeBuffer);

        return format;
    }

    @Override
    public AudioPCM decodeAll() throws IOException {
        NativeLongByReference decodedBytes = new NativeLongByReference();
        ByteBuffer pcm = null;
        if(format.getNumberOfSamples() != 0)
            pcm = MemoryUtils.createByteBuffer((int) format.getNumberOfSamples() * format.getChannels() * format.getBitDepth().bitDepth / 8);
        int ret = MPG123_OK;
        int bytesRead = 0;

        while(ret != MPG123_DONE) {
            ret = mpg123_read(h, tmpBuff, new NativeLong(tmpBuff.capacity()), decodedBytes);

            if(ret == MPG123_OK || ret == MPG123_NEW_FORMAT) {
                bytesRead += decodedBytes.getValue().intValue();

                if(ret == MPG123_NEW_FORMAT) {
                    readHeader();
                    if(pcm != null) {
                        pcm = MemoryUtils.realloc(pcm, (int) format.getNumberOfSamples() * format.getChannels() * format.getBitDepth().bitDepth / 8);
                    }
                }

                if(format.getNumberOfSamples() != 0) {
                    pcm.position(bytesRead);
                    MemoryUtils.copy(tmpBuff, pcm, decodedBytes.getValue().intValue());
                } else {
                    pcm = MemoryUtils.realloc(pcm, (pcm != null ? pcm.capacity() : 0) + decodedBytes.getValue().intValue());
                    pcm.position(bytesRead);
                    MemoryUtils.copy(tmpBuff, pcm, decodedBytes.getValue().intValue());
                }
            } else if(ret != MPG123_DONE) {
                MemoryUtils.free(pcm);
                delete();
                throw new AudioDecoderException(mpg123_plain_strerror(ret));
            }
        }

        delete();
        return new AudioPCM(format, pcm);
    }

    @Override
    public AudioPCM decodeOne() throws IOException {
        NativeLongByReference decodedBytes = new NativeLongByReference();
        int ret = mpg123_read(h, tmpBuff, new NativeLong(tmpBuff.capacity()), decodedBytes);

        if(ret == MPG123_OK) {
            ByteBuffer p = MemoryUtils.createByteBuffer(decodedBytes.getValue().intValue());
            MemoryUtils.copy(tmpBuff, p, decodedBytes.getValue().intValue());
            return new AudioPCM(format, p);
        } else if(ret == MPG123_DONE) {
            //No more sound
            delete();
            return null;
        } else if(ret == MPG123_NEW_FORMAT) {
            //The format have been changed, so must recall this...
            readHeader();
            //...an then return the sound
            ByteBuffer p = MemoryUtils.createByteBuffer(decodedBytes.getValue().intValue());
            MemoryUtils.copy(tmpBuff, p, decodedBytes.getValue().intValue());
            return new AudioPCM(format, p);
        } else {
            delete();
            throw new AudioDecoderException(mpg123_plain_strerror(ret));
        }
    }

    @Override
    public void delete() {
        if(h != null) {
            mpg123_close(h);
            mpg123_delete(h);
            h = null;
        }
        if(tmpBuff != null) {
            MemoryUtils.free(tmpBuff);
            tmpBuff = null;
        }
    }

    @Override
    protected void setFile(File file) throws FileNotFoundException {
        IntByReference err = new IntByReference();
        h = mpg123_new(null, err);
        if(err.getValue() != MPG123_OK) {
            throw new AudioDecoderException(mpg123_plain_strerror(err.getValue()));
        }

        int ret = mpg123_open(h, file.getAbsolutePath());
        throwOnError(ret);
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("mp3", Mpg123Decoder.class);
    }

    private void throwOnError(int ret) {
        if(ret != MPG123_OK) {
            throw new AudioDecoderException(mpg123_plain_strerror(ret));
        }
    }
}
