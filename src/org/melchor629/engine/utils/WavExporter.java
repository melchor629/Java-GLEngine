package org.melchor629.engine.utils;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ShortBuffer;

/**
 * Exports any PCM to Wav (RIFF) sound
 */
public class WavExporter {
    private static void write_uint32(FileOutputStream fos, long x) throws IOException {
        fos.write((byte) (x));
        fos.write((byte) (x >> 8));
        fos.write((byte) (x >> 16));
        fos.write((byte) (x >> 24));
    }

    private static void write_uint16(FileOutputStream fos, int x) throws IOException {
        fos.write((byte) (x));
        fos.write((byte) (x >> 8));
    }

    private static void write_string(FileOutputStream fos, String s) throws IOException {
        fos.write(s.getBytes());
    }

    public static void export(String file, ShortBuffer data, long total_samples, int sample_rate, int channels) throws IOException {
        FileOutputStream fos = new FileOutputStream(new File(file));
        DataOutputStream dos = new DataOutputStream(fos);
        long total_size = total_samples * channels * 16 / 8;
        write_string(fos, "RIFF");
        write_uint32(fos, total_size + 36);
        write_string(fos, "WAVEfmt ");
        write_uint32(fos, 16);
        write_uint16(fos, 1);
        write_uint16(fos, channels);
        write_uint32(fos, sample_rate);
        write_uint32(fos, sample_rate * channels * 16 / 8);
        write_uint16(fos, channels * 16 / 8); //BLOCK_ALIGN (?)
        write_uint16(fos, 16);
        write_string(fos, "data");
        write_uint32(fos, total_size);

        byte[] littleBuff = new byte[8196];
        short[] bigBuff = new short[4098];
        int left = data.capacity(), toWrite = 4098;
        while(left > 8196) {
            data.get(bigBuff, 0, bigBuff.length);
            shortToByte(bigBuff, littleBuff);
            dos.write(littleBuff);
            left -= toWrite;
        }
        fos.close();
    }

    private static void shortToByte(short[] a, byte[] b) {
        for(int i = 0; i < a.length; i++) {
            b[2 * i] = (byte) (a[i]);
            b[2*i+1] = (byte) (a[i] >> 8);
        }

        if(a.length % 2 == 1) {
            b[b.length-2] = (byte) a[a.length];
            b[b.length-1] = (byte) (a[a.length] >> 8);
        }
    }
}
