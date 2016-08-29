package org.melchor629.engine.loaders.audio.wav;

import org.melchor629.engine.loaders.audio.AudioFormat;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.loaders.audio.AudioPCM;
import org.melchor629.engine.utils.BufferUtils;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

/**
 * Load wav files using riff format
 */
public class WavDecoder extends AudioDecoder {
    private DataInputStream fis;
    private long sizeOfWavData;

    @Override
    public AudioFormat readHeader() throws IOException {
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
        format.setBitDepth(AudioFormat.bitDepthFromNumber(bitDepth));

        read = toNativeOrder(fis.readShort());
        if(read != 1) throw new AudioDecoderException("Wave format is not PCM. Unsupported form " + read);

        format.setChannels(toNativeOrder(fis.readUnsignedShort()) >> 16);
        format.setSampleRate(toNativeOrder(fis.readInt()));
        fis.readInt();
        fis.readInt();

        throwIfEOF(fis.read(buff));
        if(!new String(buff, Charset.defaultCharset()).equals("data")) throw new AudioDecoderException("No data section");

        sizeOfWavData = toNativeOrder(fis.readInt());
        format.setSamples(sizeOfWavData / format.getChannels() / bitDepth * 8);
        return format;
    }

    @Override
    public AudioPCM decodeAll() throws IOException {
        if(fis == null) return null;
        ByteBuffer buff = BufferUtils.createByteBuffer((int) sizeOfWavData);

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
        fis = null;
        return new AudioPCM(format, buff);
    }

    @Override
    public AudioPCM decodeOne() throws IOException {
        if(fis == null) return null;
        int bytesToRead = format.getSampleRate() / 10 * format.getChannels() * format.getBitDepth().bitDepth;
        if(fis.available() < bytesToRead) bytesToRead = fis.available();
        ByteBuffer buff = BufferUtils.createByteBuffer(bytesToRead);
        byte[] tmp = new byte[bytesToRead];

        bytesToRead = fis.read(tmp);
        buff.put(tmp);

        if(fis.available() == 0 || bytesToRead == -1) {
            fis.close();
            fis = null;
        }

        return new AudioPCM(format, buff);
    }

    @Override
    public void delete() {
        if(fis != null) {
            try {
                fis.close();
            } catch (IOException ignore) {}
            fis = null;
        }
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
