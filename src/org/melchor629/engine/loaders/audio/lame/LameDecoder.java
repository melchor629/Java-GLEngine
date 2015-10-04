package org.melchor629.engine.loaders.audio.lame;

import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.PointerByReference;
import org.melchor629.engine.clib.LibLame;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.utils.BufferUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

/**
 * Decodes mp3 audio
 */
public class LameDecoder extends AudioDecoder {
    PointerByReference decoder;
    LibLame.mp3data_struct data;
    FileInputStream input;
    int delay, padding;

    @Override
    public void readHeader() throws IOException {
        if(!configureDecoder()) throw new AudioDecoderException("Cannot parse mp3 header (may be invalid)");
        if(data.totalframes == 0) {
            //Calcular una aproximación de lo que hay
            double bytesPerSec = (double) input.available() / (double) data.bitrate * 8.0 / 1000d;
            data.nsamp.setValue(Math.round(bytesPerSec * data.samplerate) + data.framesize);
            data.totalframes = data.nsamp.intValue() / data.framesize + 1;
        }

        container.setBitDepth(AudioContainer.bitDepthFromNumber(16));
        container.setSampleRate(data.samplerate);
        container.setChannels(data.stereo);
        container.setSamples(data.nsamp.longValue());
    }

    @Override
    public void decode() throws IOException {
        int samplesRead, offset, skip_start = 0, skip_end = 0,
                frameSize = data.framesize, totalFrames = data.totalframes,
                frame = 0;
        short left[] = new short[1152], right[] = new short[1152];
        boolean doneEncoding = false;
        ShortBuffer pcm = BufferUtils.createShortBuffer(data.nsamp.intValue() * data.stereo);
        container.setBuffer(pcm);

        if(delay > -1 || padding > -1) {
            if(delay > -1) {
                skip_start = delay + 529;
            }
            if(padding > -1) {
                skip_end = padding - 529;
            }
        } else {
            skip_start = 576 + 529;
        }

        while(!doneEncoding) {
            samplesRead = decodeFrame(left, right);
            offset = skip_start < samplesRead ? skip_start : samplesRead;
            skip_start -= offset;
            frame += samplesRead / frameSize;

            if(samplesRead >= 0) {
                if(skip_end > 1152 && frame + 2 > totalFrames) {
                    samplesRead -= skip_end - 1152;
                    skip_end = 1152;
                } else if(frame == totalFrames && samplesRead == 0) {
                    samplesRead -= skip_end;
                }

                if(data.stereo == 2) {
                    for(int o = offset; o < samplesRead; o++) {
                        pcm.put(left[o]);
                        pcm.put(right[o]);
                    }
                } else {
                    for(int o = offset; o < samplesRead; o++) {
                        pcm.put(left[o]);
                    }
                }
            } else {
                doneEncoding = true;
            }
        }

        input.close();
        LibLame.INSTANCE.hip_decode_exit(decoder);
    }

    @Override
    protected void setFile(File file) throws FileNotFoundException {
        input = new FileInputStream(file);
        decoder = LibLame.INSTANCE.hip_decode_init();
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("mp3", LameDecoder.class);
    }

    private boolean configureDecoder() throws IOException {
        int size = 100;
        int id3Length, aidLength;
        byte[] buf = new byte[size];

        if (input.read(buf, 0, 4) != 4) {
            return false;
        }
        if (isId3Header(buf)) {
            // ID3 header found, skip past it
            if (input.read(buf, 0, 6) != 6) {
                return false;
            }
            buf[2] &= 0x7F;
            buf[3] &= 0x7F;
            buf[4] &= 0x7F;
            buf[5] &= 0x7F;
            id3Length = (((((buf[2] << 7) + buf[3]) << 7) + buf[4]) << 7) + buf[5];
            input.skip(id3Length);
            if (input.read(buf, 0, 4) != 4) {
                return false;
            }
        }
        if (isAidHeader(buf)) {
            // AID header found, skip past it too
            if (input.read(buf, 0, 2) != 2) {
                return false;
            }
            aidLength = buf[0] + 256 * buf[1];
            input.skip(aidLength);
            if (input.read(buf, 0, 4) != 4) {
                return false;
            }
        }
        while (!isMp123SyncWord(buf)) {
            // search for MP3 syncword one byte at a time
            for (int i = 0; i < 3; i++) {
                buf[i] = buf[i + 1];
            }
            int val = input.read();
            if (val == -1) {
                return false;
            }
            buf[3] = (byte) val;
        }

        do {
            size = input.read(buf);
            short left[] = new short[1152], right[] = new short[1152];
            data = new LibLame.mp3data_struct();
            IntByReference delay = new IntByReference(-1), padding = new IntByReference(-1);
            LibLame.INSTANCE.hip_decode1_headersB(decoder, ByteBuffer.wrap(buf), size, left, right, data, delay, padding);
            this.delay = delay.getValue();
            this.padding = delay.getValue();
            if(data.header_parsed != 0) {
                data.totalframes = data.nsamp.intValue() / data.framesize;
                return true;
            }
        } while(size > 0);
        return false;
    }

    private boolean isId3Header(byte[] buf) {
        return (buf[0] == 'I' &&
                buf[1] == 'D' &&
                buf[2] == '3');
    }

    private boolean isAidHeader(byte[] buf) {
        return (buf[0] == 'A' &&
                buf[1] == 'i' &&
                buf[2] == 'D' &&
                buf[3] == '\1');
    }

    private boolean isMp123SyncWord(byte[] buf) {
        // function taken from LAME to identify MP3 syncword
        char[] abl2 = new char[] { 0, 7, 7, 7, 0, 7, 0, 0, 0, 0, 0, 8, 8, 8, 8, 8 };
        if ((buf[0] & 0xFF) != 0xFF) {
            return false;
        }
        if ((buf[1] & 0xE0) != 0xE0) {
            return false;
        }
        if ((buf[1] & 0x18) == 0x08) {
            return false;
        }
        if ((buf[1] & 0x06) == 0x00) {
            // not layer I/II/III
            return false;
        }
        if ((buf[2] & 0xF0) == 0xF0) {
            // bad bitrate
            return false;
        }
        if ((buf[2] & 0x0C) == 0x0C) {
            // bad sample frequency
            return false;
        }
        if ((buf[1] & 0x18) == 0x18 &&
                (buf[1] & 0x06) == 0x04 &&
                (abl2[buf[2] >> 4] & (1 << (buf[3] >> 6))) != 0) {
            return false;
        }
        // reserved emphasis mode (?)
        return !((buf[3] & 0x03) == 2);
    }

    private int decodeFrame(short[] left, short[] right) throws IOException {
        int length = 0, samplesRead;
        byte[] buf = new byte[1024];
        boolean decodedSomething = false;

        samplesRead = _decodeFrame(buf, length, left, right);
        if(samplesRead == 0) {
            while(!decodedSomething) {
                length = input.read(buf);
                if(length == -1) {
                    samplesRead = _decodeFrame(buf, length, left, right);
                    decodedSomething = true;
                } else {
                    samplesRead = _decodeFrame(buf, length, left, right);
                    decodedSomething = samplesRead > 0;
                }
            }
        }

        return samplesRead;
    }

    private int _decodeFrame(byte[] inBuff, int length, short[] left, short[] right) {
        ByteBuffer buff = ByteBuffer.wrap(inBuff);
        int samples_read = LibLame.INSTANCE.hip_decode1_headers(decoder, buff, length, left, right, data);
        buff.clear();
        return samples_read;
    }
}
