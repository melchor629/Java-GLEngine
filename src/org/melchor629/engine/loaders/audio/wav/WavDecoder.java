package org.melchor629.engine.loaders.audio.wav;

import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.utils.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Load wav files using riff container
 */
public class WavDecoder extends AudioDecoder {
    private DataInputStream fis;
    private long sizeOfWavData;

    @Override
    public void readHeader() throws IOException {
        byte[] buff = {0,0,0,0};
        int read, bitDepth;
        throwIfEOF(fis.read(buff));
        if(!new String(buff, Charset.defaultCharset()).equals("RIFF")) throw new AudioDecoderException("Not RIFF header");

        fis.readInt();
        throwIfEOF(fis.read(buff));
        if(!new String(buff, Charset.defaultCharset()).equals("WAVE"))
            throw new AudioDecoderException("File is not Wave");

        throwIfEOF(fis.read(buff));
        if(!new String(buff, Charset.defaultCharset()).equals("fmt "))
            throw new AudioDecoderException("Wav Format Header not found");

        bitDepth = toNativeOrder(fis.readInt());
        container.setBitDepth(AudioContainer.bitDepthFromNumber(bitDepth));

        read = toNativeOrder(fis.readShort());
        if(read != 1) throw new AudioDecoderException("Wave format is not PCM. Unsupported form " + read);

        container.setChannels(toNativeOrder(fis.readUnsignedShort()) >> 16);
        container.setSampleRate(toNativeOrder(fis.readInt()));
        fis.readInt();
        fis.readInt();

        throwIfEOF(fis.read(buff));
        if(!new String(buff, Charset.defaultCharset()).equals("data")) throw new AudioDecoderException("No data section");

        sizeOfWavData = toNativeOrder(fis.readInt());
        container.setSamples(sizeOfWavData / container.getChannels() / bitDepth * 8);
    }

    @Override
    public void decode() throws IOException {
        ByteBuffer buff = BufferUtils.createByteBuffer((int) sizeOfWavData);
        container.setBuffer(buff);

        int size = (int) sizeOfWavData;
        int read;
        byte[] tmpBuff = new byte[4096];
        do {
            read = fis.read(tmpBuff);
            buff.put(tmpBuff);
            size -= read;
        } while(size >= 4096 && read != -1);
        throwIfEOF(read);
        if(size != 0) {
            throwIfEOF(fis.read(tmpBuff, 0, size));
            buff.put(tmpBuff, 0, size);
        }

        fis.close();
    }

    @Override
    protected void setFile(File file) throws FileNotFoundException {
        fis = new DataInputStream(new FileInputStream(file));
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("wav", WavDecoder.class);
    }

    private int toNativeOrder(int value) {
        if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            int b1 = (value      ) & 0xff;
            int b2 = (value >>  8) & 0xff;
            int b3 = (value >> 16) & 0xff;
            int b4 = (value >> 24) & 0xff;

            return b1 << 24 | b2 << 16 | b3 << 8 | b4;
        }
        return value;
    }

    private short toNativeOrder(short value) {
        if(ByteOrder.nativeOrder() == ByteOrder.LITTLE_ENDIAN) {
            int b1 = value & 0xff;
            int b2 = (value >> 8) & 0xff;

            return (short) (b1 << 8 | b2);
        }
        return value;
    }
}
