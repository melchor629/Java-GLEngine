package org.melchor629.engine.loaders.audio.flac;

import com.sun.jna.Pointer;
import org.melchor629.engine.loaders.audio.AudioDecoderException;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.loaders.audio.AudioDecoder;
import org.melchor629.engine.nativeBridge.LibFlac;
import org.melchor629.engine.utils.BufferUtils;
import org.melchor629.engine.utils.logger.Logger;

import java.io.File;
import java.nio.BufferOverflowException;
import java.nio.ShortBuffer;

/**
 * FLAC decoding class
 */
public class FlacDecoder extends AudioDecoder {
    private static final Logger log = Logger.getLogger(FlacDecoder.class);

    private String file;
    private Pointer decoder;
    private LibFlac.FlacStreamDecoderMetadataCallback metadataCallback;
    private LibFlac.FlacStreamDecoderErrorCallback errorCallback;
    private LibFlac.FlacStreamDecoderWriteStatusCallback writeStatus;
    private int pcmSize;
    private ShortBuffer buffer;
    private int flacError = 0;

    public FlacDecoder() {
        super();

        writeStatus = (Pointer _d, LibFlac.FlacFrame frame, Pointer frameBuffer, Pointer c) -> {
            if(frameBuffer.getPointer(0) == null) {
                return LibFlac.FlacStreamDecoderWriteStatus.ABORT;
            }

            try {
                for(long i = 0; i < frame.header.blockSize; i++) {
                    for(long ch = 0; ch < container.getChannels(); ch++) {
                        Pointer channelBuffer = frameBuffer.getPointer(ch * Pointer.SIZE);
                        this.buffer.put((short) (channelBuffer.getInt(i * 4L) & 0xFFFF));
                    }
                }
            } catch(BufferOverflowException e) {
                log.debugThrowable("That exception should not occurr", e);
                return LibFlac.FlacStreamDecoderWriteStatus.ABORT;
            }

            return LibFlac.FlacStreamDecoderWriteStatus.CONTINUE;
        };
        metadataCallback = (Pointer _d, LibFlac.FlacStreamMetadata m, Pointer c) -> {
            if(m.type == LibFlac.FlacMetadataType.STREAMINFO) {
                container.setBitDepth(AudioContainer.bitDepthFromNumber(m.data.bitsPerSample));
                container.setChannels(m.data.channels);
                container.setSampleRate(m.data.sampleRate);
                container.setSamples(m.data.totalSamples.longValue());
                pcmSize = m.data.bitsPerSample * container.getChannels() * (int) container.getNumberOfSamples();
            }
        };
        errorCallback = (Pointer _d, int error, Pointer c) -> flacError = error;
    }

    /**
     * Decodes FLAC file
     */
    @Override
    public void decode() {
        buffer = BufferUtils.createShortBuffer(pcmSize);
        if(!LibFlac.FLAC__stream_decoder_process_until_end_of_stream(decoder) && flacError != 0) {
            throw new AudioDecoderException("FLAC decoder failed: errno " + flacError);
        }
        container.setBuffer(buffer);
    }

    /**
     * Clear all memory stuff
     */
    private void clear() {
        LibFlac.FLAC__stream_decoder_delete(decoder);
        decoder = null;
        metadataCallback = null;
        errorCallback = null;
        writeStatus = null;
    }

    @Override
    public void readHeader() {
        decoder = LibFlac.FLAC__stream_decoder_new();
        if(decoder == null)
            throw new NullPointerException("Could not create FLAC decoder");

        LibFlac.FLAC__stream_decoder_set_md5_checking(decoder, true);
        LibFlac.FLAC__stream_decoder_init_file(decoder,
            file,
            writeStatus,
            metadataCallback,
            errorCallback,
            null);
        LibFlac.FLAC__stream_decoder_process_until_end_of_metadata(decoder);
        setCleanUpFunction((self) -> clear());
    }

    @Override
    protected void setFile(File file) {
        this.file = file.getAbsolutePath();
    }

    @Override
    protected void addItselfToDecoders() {
        AudioDecoder.addDecoder("flac", FlacDecoder.class);
    }
}
