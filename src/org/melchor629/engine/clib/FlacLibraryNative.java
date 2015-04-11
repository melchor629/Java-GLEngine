package org.melchor629.engine.clib;

import com.sun.jna.*;

import java.util.Arrays;
import java.util.List;

/**
 * Native bindings to Engine Library
 */
public interface FlacLibraryNative extends Library {
    FlacLibraryNative instance = (FlacLibraryNative) Native.loadLibrary("Engine", FlacLibraryNative.class);

    /*
     * Functions
     */

    /**
     * Decodes a FLAC file to PCM
     * @param file FLAC file
     * @param m Callback called when metadata is read
     * @param d Callback called when FLAC is decoded
     * @param e Callback called when there's an error
     * @param forceMono Forces the audio to be Mono
     * @return true if could decode correctly the FLAC file
     */
    boolean engine_flac_decoder(String file, OnMetadataEventCallback m, OnDataEventCallback d, OnErrorEventCallback e,
                                boolean forceMono);

    /**
     * Encodes PCM to a FLAC file (buggy)
     * @param file FLAC file
     * @param attr Metadata about the PCM format
     * @param buffer PCM data
     * @param e Callback called when there's an error
     * @return true if could encode correctly PCM data
     */
    boolean engine_flac_encoder(String file, PCMAttr attr, ShortBuffer buffer, OnErrorEventCallback e);


    /*
     * Structures
     */
    class ShortBuffer extends Structure {
        public Pointer data;
        public int pos, size;
        public _clear_func clear;

        protected List getFieldOrder() {
            return Arrays.asList(
                    "data", "pos", "size", "clear"
            );
        }
    }

    class PCMAttr extends Structure {
        public NativeLong total_samples;
        public int sample_rate, channels, bps;

        protected List getFieldOrder() {
            return Arrays.asList(
                    "total_samples", "sample_rate", "channels", "bps"
            );
        }
    }


    /*
     * Callbacks
     */
    interface OnErrorEventCallback extends Callback {
        void invoke(int errCode, String message);
    }

    interface OnMetadataEventCallback extends Callback {
        void invoke(PCMAttr attr);
    }

    interface OnDataEventCallback extends Callback {
        void invoke(ShortBuffer buff);
    }

    interface _clear_func extends Callback {
        void invoke(ShortBuffer b);
    }
}
