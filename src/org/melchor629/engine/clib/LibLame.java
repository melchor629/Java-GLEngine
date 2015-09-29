package org.melchor629.engine.clib;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;
import java.nio.ShortBuffer;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.NativeLong;
import com.sun.jna.Platform;
import com.sun.jna.Pointer;
import com.sun.jna.PointerType;
import com.sun.jna.Structure;
import com.sun.jna.ptr.FloatByReference;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;
import com.sun.jna.ptr.PointerByReference;
import com.sun.jna.ptr.ShortByReference;

/**
 * Java Binding to lame encoder & decoder
 */
public interface LibLame extends Library {
    LibLame INSTANCE = (LibLame) Native.loadLibrary("mp3lame", LibLame.class);

    /** <i>native declaration : line 2</i> */
    class buf extends Structure {
        /** C type : unsigned char* */
        public Pointer pnt;
        public NativeLong size;
        public NativeLong pos;
        /** C type : buf* */
        public buf.ByReference next;
        /** C type : buf* */
        public buf.ByReference prev;
        public buf() {
            super();
        }
        protected List<? > getFieldOrder() {
            return Arrays.asList("pnt", "size", "pos", "next", "prev");
        }
        /**
         * @param pnt C type : unsigned char*<br>
         * @param next C type : buf*<br>
         * @param prev C type : buf*
         */
        public buf(Pointer pnt, NativeLong size, NativeLong pos, buf.ByReference next, buf.ByReference prev) {
            super();
            this.pnt = pnt;
            this.size = size;
            this.pos = pos;
            this.next = next;
            this.prev = prev;
        }
        public static class ByReference extends buf implements Structure.ByReference {

        }

        public static class ByValue extends buf implements Structure.ByValue {

        }
    }

    /** <i>native declaration : line 10</i> */
    class framebuf extends Structure {
        /** C type : buf* */
        public buf.ByReference buf;
        public NativeLong pos;
        /** C type : frame* */
        public frame next;
        /** C type : frame* */
        public frame prev;
        public framebuf() {
            super();
        }
        protected List<? > getFieldOrder() {
            return Arrays.asList("buf", "pos", "next", "prev");
        }
        /**
         * @param buf C type : buf*<br>
         * @param next C type : frame*<br>
         * @param prev C type : frame*
         */
        public framebuf(buf.ByReference buf, NativeLong pos, frame next, frame prev) {
            super();
            this.buf = buf;
            this.pos = pos;
            this.next = next;
            this.prev = prev;
        }
        public static class ByReference extends framebuf implements Structure.ByReference {

        }

        public static class ByValue extends framebuf implements Structure.ByValue {

        }
    }

    class mpstr_tag extends Structure {
        /**
         * buffer linked list pointers, tail points to oldest buffer<br>
         * C type : buf*
         */
        public buf.ByReference head;
        /**
         * buffer linked list pointers, tail points to oldest buffer<br>
         * C type : buf*
         */
        public buf.ByReference tail;
        /** 1 if valid Xing vbr header detected */
        public int vbr_header;
        /** set if vbr header present */
        public int num_frames;
        /** set if vbr header present */
        public int enc_delay;
        /** set if vbr header present */
        public int enc_padding;
        /**
         * header_parsed, side_parsed and data_parsed must be all set 1<br>
         * before the full frame has been parsed<br>
         * 1 = header of current frame has been parsed
         */
        public int header_parsed;
        /** 1 = header of sideinfo of current frame has been parsed */
        public int side_parsed;
        public int data_parsed;
        /** 1 = free format frame */
        public int free_format;
        /** 1 = last frame was free format */
        public int old_free_format;
        public int bsize;
        public int framesize;
        /** number of bytes used for side information, including 2 bytes for CRC-16 if present */
        public int ssize;
        public int dsize;
        /** size of previous frame, -1 for first */
        public int fsizeold;
        public int fsizeold_nopadding;
        /**
         * holds the parameters decoded from the header<br>
         * C type : frame
         */
        public frame fr;
        /**
         * bit stream space used ????<br>
         * C type : unsigned char[2][2880 + 1024]
         */
        public byte[] bsspace = new byte[((2) * (2880 + 1024))];
        /**
         * MAXFRAMESIZE<br>
         * C type : real[2][2][32 * 18]
         */
        public real[] hybrid_block = new real[((2) * ((2) * (32 * 18)))];
        /** C type : int[2] */
        public int[] hybrid_blc = new int[2];
        public NativeLong header;
        public int bsnum;
        /** C type : real[2][2][0x110] */
        public real[] synth_buffs = new real[((2) * ((2) * (0x110)))];
        public int synth_bo;
        /** 1 = bitstream is yet to be synchronized */
        public int sync_bitstream;
        public int bitindex;
        /** C type : unsigned char* */
        public Pointer wordpointer;
        /** C type : plotting_data* */
        public plotting_data pinfo;
        public mpstr_tag() {
            super();
        }
        protected List<? > getFieldOrder() {
            return Arrays.asList("head", "tail", "vbr_header", "num_frames", "enc_delay", "enc_padding", "header_parsed", "side_parsed", "data_parsed", "free_format", "old_free_format", "bsize", "framesize", "ssize", "dsize", "fsizeold", "fsizeold_nopadding", "fr", "bsspace", "hybrid_block", "hybrid_blc", "header", "bsnum", "synth_buffs", "synth_bo", "sync_bitstream", "bitindex", "wordpointer", "pinfo");
        }
        public static class ByReference extends mpstr_tag implements Structure.ByReference {

        }

        public static class ByValue extends mpstr_tag implements Structure.ByValue {

        }
    }

    class real extends PointerType {
        public real(Pointer address) {
            super(address);
        }
        public real() {
            super();
        }
    }

    class frame extends PointerType {
        public frame(Pointer address) {
            super(address);
        }
        public frame() {
            super();
        }
    }

    class plotting_data extends PointerType {
        public plotting_data(Pointer address) {
            super(address);
        }
        public plotting_data() {
            super();
        }
    }

    interface vbr_mode_e {
        int vbr_off = 0;
        /**
         * obsolete, same as vbr_mtrh
         */
        int vbr_mt = 1;
        int vbr_rh = 2;
        int vbr_abr = 3;
        int vbr_mtrh = 4;
        /**
         * Don't use this! It's used for sanity checks.
         */
        int vbr_max_indicator = 5;
        /**
         * change this to change the default VBR mode of LAME
         */
        int vbr_default = (int) vbr_mode_e.vbr_rh;
    }

    interface MPEG_mode_e {
        int STEREO = 0;
        int JOINT_STEREO = 1;
        /**
         * LAME doesn't supports this!
         */
        int DUAL_CHANNEL = 2;
        int MONO = 3;
        int NOT_SET = 4;
        /**
         * Don't use this! It's used for sanity checks.
         */
        int MAX_INDICATOR = 5;
    }

    interface Padding_type_e {
        int PAD_NO = 0;
        int PAD_ALL = 1;
        int PAD_ADJUST = 2;
        /**
         * Don't use this! It's used for sanity checks.
         */
        int PAD_MAX_INDICATOR = 3;
    }

    interface preset_mode_e {
        /**
         * for abr I'd suggest to directly use the targeted bitrate as a value
         */
        int ABR_8 = 8;
        int ABR_320 = 320;
        /**
         * Vx to match Lame and VBR_xx to match FhG
         */
        int V9 = 410;
        int VBR_10 = 410;
        int V8 = 420;
        int VBR_20 = 420;
        int V7 = 430;
        int VBR_30 = 430;
        int V6 = 440;
        int VBR_40 = 440;
        int V5 = 450;
        int VBR_50 = 450;
        int V4 = 460;
        int VBR_60 = 460;
        int V3 = 470;
        int VBR_70 = 470;
        int V2 = 480;
        int VBR_80 = 480;
        int V1 = 490;
        int VBR_90 = 490;
        int V0 = 500;
        int VBR_100 = 500;

        /**
         * still there for compatibility
         */
        int R3MIX = 1000;
        int STANDARD = 1001;
        int EXTREME = 1002;
        int INSANE = 1003;
        int STANDARD_FAST = 1004;
        int EXTREME_FAST = 1005;
        int MEDIUM = 1006;
        int MEDIUM_FAST = 1007;
    }

    interface asm_optimizations_e {
        int MMX = 1;
        int AMD_3DNOW = 2;
        int SSE = 3;
    }

    interface Psy_model_e {
        int PSY_GPSYCHO = 1;
        int PSY_NSPSYTUNE = 2;
    }

    interface lame_errorcodes_t {
        int LAME_OKAY = 0;
        int LAME_NOERROR = 0;
        int LAME_GENERICERROR = -1;
        int LAME_NOMEM = -10;
        int LAME_BADBITRATE = -11;
        int LAME_BADSAMPFREQ = -12;
        int LAME_INTERNALERROR = -13;
        int FRONTEND_READERROR = -80;
        int FRONTEND_WRITEERROR = -81;
        int FRONTEND_FILETOOLARGE = -82;
    }

    class lame_global_flags extends Structure {
        /* (non-Javadoc)
         * @see com.sun.jna.Structure#getFieldOrder()
         */
        @Override
        protected List<Object> getFieldOrder() {
            return null;
        }
    }

    /**
     * REQUIRED:
     * initialize the encoder.  sets default for all encoder parameters,
     * returns NULL if some malloc()'s failed
     * otherwise returns pointer to structure needed for all future
     * API calls.
     */
    PointerByReference lame_init();



    /** <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h</i> */
    class lame_version_t extends Structure {
        /** generic LAME version */
        public int major;
        public int minor;
        /** 0 if not an alpha version */
        public int alpha;
        /** 0 if not a beta version */
        public int beta;
        /** version of the psy model */
        public int psy_major;
        public int psy_minor;
        /** 0 if not an alpha version */
        public int psy_alpha;
        /** 0 if not a beta version */
        public int psy_beta;
        /**
         * compile time features<br>
         * Don't make assumptions about the contents!<br>
         * C type : const char*
         */
        public Pointer features;
        public lame_version_t() {
            super();
        }
        protected List<? > getFieldOrder() {
            return Arrays.asList("major", "minor", "alpha", "beta", "psy_major", "psy_minor", "psy_alpha", "psy_beta", "features");
        }
        /**
         * @param major generic LAME version<br>
         * @param alpha 0 if not an alpha version<br>
         * @param beta 0 if not a beta version<br>
         * @param psy_major version of the psy model<br>
         * @param psy_alpha 0 if not an alpha version<br>
         * @param psy_beta 0 if not a beta version<br>
         * @param features compile time features<br>
         * Don't make assumptions about the contents!<br>
         * C type : const char*
         */
        public lame_version_t(int major, int minor, int alpha, int beta, int psy_major, int psy_minor, int psy_alpha, int psy_beta, Pointer features) {
            super();
            this.major = major;
            this.minor = minor;
            this.alpha = alpha;
            this.beta = beta;
            this.psy_major = psy_major;
            this.psy_minor = psy_minor;
            this.psy_alpha = psy_alpha;
            this.psy_beta = psy_beta;
            this.features = features;
        }
        public static class ByReference extends lame_version_t implements Structure.ByReference {

        }

        public static class ByValue extends lame_version_t implements Structure.ByValue {

        }
    }

    /** <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:309</i> */
    interface lame_set_errorf_func_callback extends Callback {
        void apply(Pointer charPtr1, Pointer va_list1);
    }

    /** <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:311</i> */
    interface lame_set_debugf_func_callback extends Callback {
        void apply(Pointer charPtr1, Pointer va_list1);
    }

    /** <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:313</i> */
    interface lame_set_msgf_func_callback extends Callback {
        void apply(Pointer charPtr1, Pointer va_list1);
    }

    /** <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1016</i> */
    interface id3tag_genre_list_handler_callback extends Callback {
        void apply(int int1, Pointer charPtr1, Pointer voidPtr1);
    }

    class mp3data_struct extends Structure {
        /**
         * 1 if header was parsed and following data was<br>
         * computed
         */
        public int header_parsed;
        /** number of channels */
        public int stereo;
        /** sample rate */
        public int samplerate;
        /** bitrate */
        public int bitrate;
        /** mp3 frame type */
        public int mode;
        /** mp3 frame type */
        public int mode_ext;
        /** number of samples per mp3 frame */
        public int framesize;
        /**
         * this data is only computed if mpglib detects a Xing VBR header<br>
         * number of samples in mp3 file.
         */
        public NativeLong nsamp;
        /** total number of frames in mp3 file */
        public int totalframes;
        /**
         * this data is not currently computed by the mpglib routines<br>
         * frames decoded counter
         */
        public int framenum;
        public mp3data_struct() {
            super();
        }
        protected List<? > getFieldOrder() {
            return Arrays.asList("header_parsed", "stereo", "samplerate", "bitrate", "mode", "mode_ext", "framesize", "nsamp", "totalframes", "framenum");
        }
        public static class ByReference extends mp3data_struct implements Structure.ByReference {

        }

        public static class ByValue extends mp3data_struct implements Structure.ByValue {

        }
    }

    /**
     * number of samples.  default = 2^32-1<br>
     * Original signature : <code>int lame_set_num_samples(lame_global_flags*, unsigned long)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:153</i>
     */
    int lame_set_num_samples(PointerByReference lame_global_flagsPtr1, NativeLong u1);

    /**
     * Original signature : <code>long lame_get_num_samples(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:154</i>
     */
    NativeLong lame_get_num_samples(PointerByReference lame_global_flagsPtr1);

    /**
     * input sample rate in Hz.  default = 44100hz<br>
     * Original signature : <code>int lame_set_in_samplerate(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:157</i>
     */
    int lame_set_in_samplerate(PointerByReference lame_global_flagsPtr1, int int1);

    /**
     * Original signature : <code>int lame_get_in_samplerate(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:158</i>
     */
    int lame_get_in_samplerate(PointerByReference lame_global_flagsPtr1);

    /**
     * number of channels in input stream. default=2<br>
     * Original signature : <code>int lame_set_num_channels(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:161</i>
     */
    int lame_set_num_channels(PointerByReference lame_global_flagsPtr1, int int1);

    /**
     * Original signature : <code>int lame_get_num_channels(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:162</i>
     */
    int lame_get_num_channels(PointerByReference lame_global_flagsPtr1);

    /**
     * scale the input by this amount before encoding.  default=0 (disabled)<br>
     * (not used by decoding routines)<br>
     * Original signature : <code>int lame_set_scale(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:168</i>
     */
    int lame_set_scale(PointerByReference lame_global_flagsPtr1, float float1);

    /**
     * Original signature : <code>float lame_get_scale(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:169</i>
     */
    float lame_get_scale(PointerByReference lame_global_flagsPtr1);

    /**
     * scale the channel 0 (left) input by this amount before encoding.<br>
     * default=0 (disabled)<br>
     * (not used by decoding routines)<br>
     * Original signature : <code>int lame_set_scale_left(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:176</i>
     */
    int lame_set_scale_left(PointerByReference lame_global_flagsPtr1, float float1);

    /**
     * Original signature : <code>float lame_get_scale_left(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:177</i>
     */
    float lame_get_scale_left(PointerByReference lame_global_flagsPtr1);

    /**
     * scale the channel 1 (right) input by this amount before encoding.<br>
     * default=0 (disabled)<br>
     * (not used by decoding routines)<br>
     * Original signature : <code>int lame_set_scale_right(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:184</i>
     */
    int lame_set_scale_right(PointerByReference lame_global_flagsPtr1, float float1);

    /**
     * Original signature : <code>float lame_get_scale_right(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:185</i>
     */
    float lame_get_scale_right(PointerByReference lame_global_flagsPtr1);
    /**
     * output sample rate in Hz.  default = 0, which means LAME picks best value<br>
     * based on the amount of compression.  MPEG only allows:<br>
     * MPEG1    32, 44.1,   48khz<br>
     * MPEG2    16, 22.05,  24<br>
     * MPEG2.5   8, 11.025, 12<br>
     * (not used by decoding routines)<br>
     * Original signature : <code>int lame_set_out_samplerate(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:195</i><br>
     * @deprecated use the safer method {@link #lame_set_out_samplerate(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_out_samplerate(Pointer lame_global_flagsPtr1, int int1);
    /**
     * output sample rate in Hz.  default = 0, which means LAME picks best value<br>
     * based on the amount of compression.  MPEG only allows:<br>
     * MPEG1    32, 44.1,   48khz<br>
     * MPEG2    16, 22.05,  24<br>
     * MPEG2.5   8, 11.025, 12<br>
     * (not used by decoding routines)<br>
     * Original signature : <code>int lame_set_out_samplerate(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:195</i>
     */
    int lame_set_out_samplerate(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_out_samplerate(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:196</i><br>
     * @deprecated use the safer method {@link #lame_get_out_samplerate(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_out_samplerate(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_out_samplerate(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:196</i>
     */
    int lame_get_out_samplerate(PointerByReference lame_global_flagsPtr1);
    /**
     * 1=cause LAME to collect data for an MP3 frame analyzer. default=0<br>
     * Original signature : <code>int lame_set_analysis(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:203</i><br>
     * @deprecated use the safer method {@link #lame_set_analysis(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_analysis(Pointer lame_global_flagsPtr1, int int1);
    /**
     * 1=cause LAME to collect data for an MP3 frame analyzer. default=0<br>
     * Original signature : <code>int lame_set_analysis(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:203</i>
     */
    int lame_set_analysis(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_analysis(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:204</i><br>
     * @deprecated use the safer method {@link #lame_get_analysis(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_analysis(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_analysis(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:204</i>
     */
    int lame_get_analysis(PointerByReference lame_global_flagsPtr1);
    /**
     * 1 = write a Xing VBR header frame.<br>
     * default = 1<br>
     * this variable must have been added by a Hungarian notation Windows programmer :-)<br>
     * Original signature : <code>int lame_set_bWriteVbrTag(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:211</i><br>
     * @deprecated use the safer method {@link #lame_set_bWriteVbrTag(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_bWriteVbrTag(Pointer lame_global_flagsPtr1, int int1);
    /**
     * 1 = write a Xing VBR header frame.<br>
     * default = 1<br>
     * this variable must have been added by a Hungarian notation Windows programmer :-)<br>
     * Original signature : <code>int lame_set_bWriteVbrTag(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:211</i>
     */
    int lame_set_bWriteVbrTag(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_bWriteVbrTag(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:212</i><br>
     * @deprecated use the safer method {@link #lame_get_bWriteVbrTag(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_bWriteVbrTag(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_bWriteVbrTag(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:212</i>
     */
    int lame_get_bWriteVbrTag(PointerByReference lame_global_flagsPtr1);
    /**
     * 1=decode only.  use lame/mpglib to convert mp3/ogg to wav.  default=0<br>
     * Original signature : <code>int lame_set_decode_only(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:215</i><br>
     * @deprecated use the safer method {@link #lame_set_decode_only(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_decode_only(Pointer lame_global_flagsPtr1, int int1);
    /**
     * 1=decode only.  use lame/mpglib to convert mp3/ogg to wav.  default=0<br>
     * Original signature : <code>int lame_set_decode_only(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:215</i>
     */
    int lame_set_decode_only(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_decode_only(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:216</i><br>
     * @deprecated use the safer method {@link #lame_get_decode_only(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_decode_only(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_decode_only(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:216</i>
     */
    int lame_get_decode_only(PointerByReference lame_global_flagsPtr1);
    /**
     * DEPRECATED<br>
     * Original signature : <code>int lame_set_ogg(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:220</i><br>
     * @deprecated use the safer method {@link #lame_set_ogg(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_ogg(Pointer lame_global_flagsPtr1, int int1);
    /**
     * DEPRECATED<br>
     * Original signature : <code>int lame_set_ogg(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:220</i>
     */
    int lame_set_ogg(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_ogg(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:221</i><br>
     * @deprecated use the safer method {@link #lame_get_ogg(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_ogg(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_ogg(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:221</i>
     */
    int lame_get_ogg(PointerByReference lame_global_flagsPtr1);
    /**
     * internal algorithm selection.  True quality is determined by the bitrate<br>
     * but this variable will effect quality by selecting expensive or cheap algorithms.<br>
     * quality=0..9.  0=best (very slow).  9=worst.<br>
     * recommended:  2     near-best quality, not too slow<br>
     * 5     good quality, fast<br>
     * 7     ok quality, really fast<br>
     * Original signature : <code>int lame_set_quality(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:231</i><br>
     * @deprecated use the safer method {@link #lame_set_quality(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_quality(Pointer lame_global_flagsPtr1, int int1);
    /**
     * internal algorithm selection.  True quality is determined by the bitrate<br>
     * but this variable will effect quality by selecting expensive or cheap algorithms.<br>
     * quality=0..9.  0=best (very slow).  9=worst.<br>
     * recommended:  2     near-best quality, not too slow<br>
     * 5     good quality, fast<br>
     * 7     ok quality, really fast<br>
     * Original signature : <code>int lame_set_quality(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:231</i>
     */
    int lame_set_quality(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_quality(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:232</i><br>
     * @deprecated use the safer method {@link #lame_get_quality(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_quality(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_quality(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:232</i>
     */
    int lame_get_quality(PointerByReference lame_global_flagsPtr1);
    /**
     * mode = 0,1,2,3 = stereo, jstereo, dual channel (not supported), mono<br>
     * default: lame picks based on compression ration and input channels<br>
     * Original signature : <code>int lame_set_mode(lame_global_flags*, MPEG_mode)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:238</i><br>
     * @deprecated use the safer method {@link #lame_set_mode(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_mode(Pointer lame_global_flagsPtr1, int MPEG_mode1);
    /**
     * mode = 0,1,2,3 = stereo, jstereo, dual channel (not supported), mono<br>
     * default: lame picks based on compression ration and input channels<br>
     * Original signature : <code>int lame_set_mode(lame_global_flags*, MPEG_mode)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:238</i>
     */
    int lame_set_mode(PointerByReference lame_global_flagsPtr1, int MPEG_mode1);
    /**
     * Original signature : <code>MPEG_mode lame_get_mode(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:239</i><br>
     * @deprecated use the safer method {@link #lame_get_mode(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_mode(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>MPEG_mode lame_get_mode(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:239</i>
     */
    int lame_get_mode(PointerByReference lame_global_flagsPtr1);
    /**
     * mode_automs.  Use a M/S mode with a switching threshold based on<br>
     * compression ratio<br>
     * DEPRECATED<br>
     * Original signature : <code>int lame_set_mode_automs(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:246</i><br>
     * @deprecated use the safer method {@link #lame_set_mode_automs(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_mode_automs(Pointer lame_global_flagsPtr1, int int1);
    /**
     * mode_automs.  Use a M/S mode with a switching threshold based on<br>
     * compression ratio<br>
     * DEPRECATED<br>
     * Original signature : <code>int lame_set_mode_automs(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:246</i>
     */
    int lame_set_mode_automs(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_mode_automs(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:247</i><br>
     * @deprecated use the safer method {@link #lame_get_mode_automs(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_mode_automs(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_mode_automs(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:247</i>
     */
    int lame_get_mode_automs(PointerByReference lame_global_flagsPtr1);
    /**
     * force_ms.  Force M/S for all frames.  For testing only.<br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_force_ms(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:253</i><br>
     * @deprecated use the safer method {@link #lame_set_force_ms(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_force_ms(Pointer lame_global_flagsPtr1, int int1);
    /**
     * force_ms.  Force M/S for all frames.  For testing only.<br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_force_ms(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:253</i>
     */
    int lame_set_force_ms(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_force_ms(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:254</i><br>
     * @deprecated use the safer method {@link #lame_get_force_ms(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_force_ms(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_force_ms(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:254</i>
     */
    int lame_get_force_ms(PointerByReference lame_global_flagsPtr1);
    /**
     * use free_format?  default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_free_format(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:257</i><br>
     * @deprecated use the safer method {@link #lame_set_free_format(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_free_format(Pointer lame_global_flagsPtr1, int int1);
    /**
     * use free_format?  default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_free_format(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:257</i>
     */
    int lame_set_free_format(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_free_format(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:258</i><br>
     * @deprecated use the safer method {@link #lame_get_free_format(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_free_format(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_free_format(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:258</i>
     */
    int lame_get_free_format(PointerByReference lame_global_flagsPtr1);
    /**
     * perform ReplayGain analysis?  default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_findReplayGain(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:261</i><br>
     * @deprecated use the safer method {@link #lame_set_findReplayGain(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_findReplayGain(Pointer lame_global_flagsPtr1, int int1);
    /**
     * perform ReplayGain analysis?  default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_findReplayGain(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:261</i>
     */
    int lame_set_findReplayGain(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_findReplayGain(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:262</i><br>
     * @deprecated use the safer method {@link #lame_get_findReplayGain(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_findReplayGain(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_findReplayGain(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:262</i>
     */
    int lame_get_findReplayGain(PointerByReference lame_global_flagsPtr1);
    /**
     * decode on the fly. Search for the peak sample. If the ReplayGain<br>
     * analysis is enabled then perform the analysis on the decoded data<br>
     * stream. default = 0 (disabled) <br>
     * NOTE: if this option is set the build-in decoder should not be used<br>
     * Original signature : <code>int lame_set_decode_on_the_fly(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:268</i><br>
     * @deprecated use the safer method {@link #lame_set_decode_on_the_fly(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_decode_on_the_fly(Pointer lame_global_flagsPtr1, int int1);
    /**
     * decode on the fly. Search for the peak sample. If the ReplayGain<br>
     * analysis is enabled then perform the analysis on the decoded data<br>
     * stream. default = 0 (disabled) <br>
     * NOTE: if this option is set the build-in decoder should not be used<br>
     * Original signature : <code>int lame_set_decode_on_the_fly(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:268</i>
     */
    int lame_set_decode_on_the_fly(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_decode_on_the_fly(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:269</i><br>
     * @deprecated use the safer method {@link #lame_get_decode_on_the_fly(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_decode_on_the_fly(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_decode_on_the_fly(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:269</i>
     */
    int lame_get_decode_on_the_fly(PointerByReference lame_global_flagsPtr1);
    /**
     * DEPRECATED: now does the same as lame_set_findReplayGain() <br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_ReplayGain_input(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:273</i><br>
     * @deprecated use the safer method {@link #lame_set_ReplayGain_input(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_ReplayGain_input(Pointer lame_global_flagsPtr1, int int1);
    /**
     * DEPRECATED: now does the same as lame_set_findReplayGain() <br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_ReplayGain_input(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:273</i>
     */
    int lame_set_ReplayGain_input(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_ReplayGain_input(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:274</i><br>
     * @deprecated use the safer method {@link #lame_get_ReplayGain_input(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_ReplayGain_input(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_ReplayGain_input(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:274</i>
     */
    int lame_get_ReplayGain_input(PointerByReference lame_global_flagsPtr1);
    /**
     * DEPRECATED: now does the same as <br>
     * lame_set_decode_on_the_fly() && lame_set_findReplayGain()<br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_ReplayGain_decode(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:279</i><br>
     * @deprecated use the safer method {@link #lame_set_ReplayGain_decode(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_ReplayGain_decode(Pointer lame_global_flagsPtr1, int int1);
    /**
     * DEPRECATED: now does the same as <br>
     * lame_set_decode_on_the_fly() && lame_set_findReplayGain()<br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_ReplayGain_decode(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:279</i>
     */
    int lame_set_ReplayGain_decode(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_ReplayGain_decode(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:280</i><br>
     * @deprecated use the safer method {@link #lame_get_ReplayGain_decode(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_ReplayGain_decode(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_ReplayGain_decode(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:280</i>
     */
    int lame_get_ReplayGain_decode(PointerByReference lame_global_flagsPtr1);
    /**
     * DEPRECATED: now does the same as lame_set_decode_on_the_fly() <br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_findPeakSample(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:284</i><br>
     * @deprecated use the safer method {@link #lame_set_findPeakSample(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_findPeakSample(Pointer lame_global_flagsPtr1, int int1);
    /**
     * DEPRECATED: now does the same as lame_set_decode_on_the_fly() <br>
     * default = 0 (disabled)<br>
     * Original signature : <code>int lame_set_findPeakSample(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:284</i>
     */
    int lame_set_findPeakSample(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_findPeakSample(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:285</i><br>
     * @deprecated use the safer method {@link #lame_get_findPeakSample(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_findPeakSample(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_findPeakSample(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:285</i>
     */
    int lame_get_findPeakSample(PointerByReference lame_global_flagsPtr1);
    /**
     * counters for gapless encoding<br>
     * Original signature : <code>int lame_set_nogap_total(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:288</i><br>
     * @deprecated use the safer method {@link #lame_set_nogap_total(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_nogap_total(Pointer lame_global_flagsPtr1, int int1);
    /**
     * counters for gapless encoding<br>
     * Original signature : <code>int lame_set_nogap_total(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:288</i>
     */
    int lame_set_nogap_total(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_nogap_total(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:289</i><br>
     * @deprecated use the safer method {@link #lame_get_nogap_total(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_nogap_total(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_nogap_total(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:289</i>
     */
    int lame_get_nogap_total(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_nogap_currentindex(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:291</i><br>
     * @deprecated use the safer method {@link #lame_set_nogap_currentindex(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_nogap_currentindex(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_set_nogap_currentindex(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:291</i>
     */
    int lame_set_nogap_currentindex(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_nogap_currentindex(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:292</i><br>
     * @deprecated use the safer method {@link #lame_get_nogap_currentindex(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_nogap_currentindex(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_nogap_currentindex(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:292</i>
     */
    int lame_get_nogap_currentindex(PointerByReference lame_global_flagsPtr1);
    /**
     * OPTIONAL:<br>
     * Set printf like error/debug/message reporting functions.<br>
     * The second argument has to be a pointer to a function which looks like<br>
     *   void my_debugf(const char *format, va_list ap)<br>
     *   {<br>
     *       (void) vfprintf(stdout, format, ap);<br>
     *   }<br>
     * If you use NULL as the value of the pointer in the set function, the<br>
     * lame buildin function will be used (prints to stderr).<br>
     * To quiet any output you have to replace the body of the example function<br>
     * with just "return;" and use it in the set function.<br>
     * Original signature : <code>int lame_set_errorf(lame_global_flags*, lame_set_errorf_func_callback*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:308</i><br>
     * @deprecated use the safer method {@link #lame_set_errorf(com.sun.jna.ptr.PointerByReference, LibLame.lame_set_errorf_func_callback)} instead
     */
    @Deprecated
    int lame_set_errorf(Pointer lame_global_flagsPtr1, LibLame.lame_set_errorf_func_callback func);
    /**
     * OPTIONAL:<br>
     * Set printf like error/debug/message reporting functions.<br>
     * The second argument has to be a pointer to a function which looks like<br>
     *   void my_debugf(const char *format, va_list ap)<br>
     *   {<br>
     *       (void) vfprintf(stdout, format, ap);<br>
     *   }<br>
     * If you use NULL as the value of the pointer in the set function, the<br>
     * lame buildin function will be used (prints to stderr).<br>
     * To quiet any output you have to replace the body of the example function<br>
     * with just "return;" and use it in the set function.<br>
     * Original signature : <code>int lame_set_errorf(lame_global_flags*, lame_set_errorf_func_callback*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:308</i>
     */
    int lame_set_errorf(PointerByReference lame_global_flagsPtr1, LibLame.lame_set_errorf_func_callback func);
    /**
     * Original signature : <code>int lame_set_debugf(lame_global_flags*, lame_set_debugf_func_callback*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:310</i><br>
     * @deprecated use the safer method {@link #lame_set_debugf(com.sun.jna.ptr.PointerByReference, mp3lame.LibLame.lame_set_debugf_func_callback)} instead
     */
    @Deprecated
    int lame_set_debugf(Pointer lame_global_flagsPtr1, LibLame.lame_set_debugf_func_callback func);
    /**
     * Original signature : <code>int lame_set_debugf(lame_global_flags*, lame_set_debugf_func_callback*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:310</i>
     */
    int lame_set_debugf(PointerByReference lame_global_flagsPtr1, LibLame.lame_set_debugf_func_callback func);
    /**
     * Original signature : <code>int lame_set_msgf(lame_global_flags*, lame_set_msgf_func_callback*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:312</i><br>
     * @deprecated use the safer method {@link #lame_set_msgf(com.sun.jna.ptr.PointerByReference, mp3lame.LibLame.lame_set_msgf_func_callback)} instead
     */
    @Deprecated
    int lame_set_msgf(Pointer lame_global_flagsPtr1, LibLame.lame_set_msgf_func_callback func);
    /**
     * Original signature : <code>int lame_set_msgf(lame_global_flags*, lame_set_msgf_func_callback*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:312</i>
     */
    int lame_set_msgf(PointerByReference lame_global_flagsPtr1, LibLame.lame_set_msgf_func_callback func);
    /**
     * set one of brate compression ratio.  default is compression ratio of 11.<br>
     * Original signature : <code>int lame_set_brate(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:318</i><br>
     * @deprecated use the safer method {@link #lame_set_brate(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_brate(Pointer lame_global_flagsPtr1, int int1);
    /**
     * set one of brate compression ratio.  default is compression ratio of 11.<br>
     * Original signature : <code>int lame_set_brate(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:318</i>
     */
    int lame_set_brate(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_brate(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:319</i><br>
     * @deprecated use the safer method {@link #lame_get_brate(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_brate(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_brate(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:319</i>
     */
    int lame_get_brate(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_compression_ratio(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:320</i><br>
     * @deprecated use the safer method {@link #lame_set_compression_ratio(com.sun.jna.ptr.PointerByReference, float)} instead
     */
    @Deprecated
    int lame_set_compression_ratio(Pointer lame_global_flagsPtr1, float float1);
    /**
     * Original signature : <code>int lame_set_compression_ratio(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:320</i>
     */
    int lame_set_compression_ratio(PointerByReference lame_global_flagsPtr1, float float1);
    /**
     * Original signature : <code>float lame_get_compression_ratio(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:321</i><br>
     * @deprecated use the safer method {@link #lame_get_compression_ratio(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_compression_ratio(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>float lame_get_compression_ratio(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:321</i>
     */
    float lame_get_compression_ratio(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_preset(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:324</i><br>
     * @deprecated use the safer method {@link #lame_set_preset(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_preset(Pointer gfp, int int1);
    /**
     * Original signature : <code>int lame_set_preset(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:324</i>
     */
    int lame_set_preset(PointerByReference gfp, int int1);
    /**
     * Original signature : <code>int lame_set_asm_optimizations(lame_global_flags*, int, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:325</i><br>
     * @deprecated use the safer method {@link #lame_set_asm_optimizations(com.sun.jna.ptr.PointerByReference, int, int)} instead
     */
    @Deprecated
    int lame_set_asm_optimizations(Pointer gfp, int int1, int int2);
    /**
     * Original signature : <code>int lame_set_asm_optimizations(lame_global_flags*, int, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:325</i>
     */
    int lame_set_asm_optimizations(PointerByReference gfp, int int1, int int2);
    /**
     * mark as copyright.  default=0<br>
     * Original signature : <code>int lame_set_copyright(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:333</i><br>
     * @deprecated use the safer method {@link #lame_set_copyright(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_copyright(Pointer lame_global_flagsPtr1, int int1);
    /**
     * mark as copyright.  default=0<br>
     * Original signature : <code>int lame_set_copyright(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:333</i>
     */
    int lame_set_copyright(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_copyright(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:334</i><br>
     * @deprecated use the safer method {@link #lame_get_copyright(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_copyright(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_copyright(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:334</i>
     */
    int lame_get_copyright(PointerByReference lame_global_flagsPtr1);
    /**
     * mark as original.  default=1<br>
     * Original signature : <code>int lame_set_original(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:337</i><br>
     * @deprecated use the safer method {@link #lame_set_original(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_original(Pointer lame_global_flagsPtr1, int int1);
    /**
     * mark as original.  default=1<br>
     * Original signature : <code>int lame_set_original(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:337</i>
     */
    int lame_set_original(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_original(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:338</i><br>
     * @deprecated use the safer method {@link #lame_get_original(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_original(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_original(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:338</i>
     */
    int lame_get_original(PointerByReference lame_global_flagsPtr1);
    /**
     * error_protection.  Use 2 bytes from each frame for CRC checksum. default=0<br>
     * Original signature : <code>int lame_set_error_protection(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:341</i><br>
     * @deprecated use the safer method {@link #lame_set_error_protection(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_error_protection(Pointer lame_global_flagsPtr1, int int1);
    /**
     * error_protection.  Use 2 bytes from each frame for CRC checksum. default=0<br>
     * Original signature : <code>int lame_set_error_protection(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:341</i>
     */
    int lame_set_error_protection(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_error_protection(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:342</i><br>
     * @deprecated use the safer method {@link #lame_get_error_protection(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_error_protection(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_error_protection(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:342</i>
     */
    int lame_get_error_protection(PointerByReference lame_global_flagsPtr1);
    /**
     * padding_type. 0=pad no frames  1=pad all frames 2=adjust padding(default)<br>
     * Original signature : <code>int lame_set_padding_type(lame_global_flags*, Padding_type)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:345</i><br>
     * @deprecated use the safer method {@link #lame_set_padding_type(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_padding_type(Pointer lame_global_flagsPtr1, int Padding_type1);
    /**
     * padding_type. 0=pad no frames  1=pad all frames 2=adjust padding(default)<br>
     * Original signature : <code>int lame_set_padding_type(lame_global_flags*, Padding_type)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:345</i>
     */
    int lame_set_padding_type(PointerByReference lame_global_flagsPtr1, int Padding_type1);
    /**
     * Original signature : <code>Padding_type lame_get_padding_type(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:346</i><br>
     * @deprecated use the safer method {@link #lame_get_padding_type(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_padding_type(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>Padding_type lame_get_padding_type(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:346</i>
     */
    int lame_get_padding_type(PointerByReference lame_global_flagsPtr1);
    /**
     * MP3 'private extension' bit  Meaningless.  default=0<br>
     * Original signature : <code>int lame_set_extension(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:349</i><br>
     * @deprecated use the safer method {@link #lame_set_extension(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_extension(Pointer lame_global_flagsPtr1, int int1);
    /**
     * MP3 'private extension' bit  Meaningless.  default=0<br>
     * Original signature : <code>int lame_set_extension(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:349</i>
     */
    int lame_set_extension(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_extension(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:350</i><br>
     * @deprecated use the safer method {@link #lame_get_extension(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_extension(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_extension(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:350</i>
     */
    int lame_get_extension(PointerByReference lame_global_flagsPtr1);
    /**
     * enforce strict ISO compliance.  default=0<br>
     * Original signature : <code>int lame_set_strict_ISO(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:353</i><br>
     * @deprecated use the safer method {@link #lame_set_strict_ISO(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_strict_ISO(Pointer lame_global_flagsPtr1, int int1);
    /**
     * enforce strict ISO compliance.  default=0<br>
     * Original signature : <code>int lame_set_strict_ISO(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:353</i>
     */
    int lame_set_strict_ISO(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_strict_ISO(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:354</i><br>
     * @deprecated use the safer method {@link #lame_get_strict_ISO(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_strict_ISO(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_strict_ISO(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:354</i>
     */
    int lame_get_strict_ISO(PointerByReference lame_global_flagsPtr1);
    /**
     * disable the bit reservoir. For testing only. default=0<br>
     * Original signature : <code>int lame_set_disable_reservoir(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:362</i><br>
     * @deprecated use the safer method {@link #lame_set_disable_reservoir(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_disable_reservoir(Pointer lame_global_flagsPtr1, int int1);
    /**
     * disable the bit reservoir. For testing only. default=0<br>
     * Original signature : <code>int lame_set_disable_reservoir(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:362</i>
     */
    int lame_set_disable_reservoir(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_disable_reservoir(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:363</i><br>
     * @deprecated use the safer method {@link #lame_get_disable_reservoir(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_disable_reservoir(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_disable_reservoir(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:363</i>
     */
    int lame_get_disable_reservoir(PointerByReference lame_global_flagsPtr1);
    /**
     * select a different "best quantization" function. default=0<br>
     * Original signature : <code>int lame_set_quant_comp(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:366</i><br>
     * @deprecated use the safer method {@link #lame_set_quant_comp(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_quant_comp(Pointer lame_global_flagsPtr1, int int1);
    /**
     * select a different "best quantization" function. default=0<br>
     * Original signature : <code>int lame_set_quant_comp(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:366</i>
     */
    int lame_set_quant_comp(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_quant_comp(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:367</i><br>
     * @deprecated use the safer method {@link #lame_get_quant_comp(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_quant_comp(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_quant_comp(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:367</i>
     */
    int lame_get_quant_comp(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_quant_comp_short(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:368</i><br>
     * @deprecated use the safer method {@link #lame_set_quant_comp_short(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_quant_comp_short(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_set_quant_comp_short(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:368</i>
     */
    int lame_set_quant_comp_short(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_quant_comp_short(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:369</i><br>
     * @deprecated use the safer method {@link #lame_get_quant_comp_short(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_quant_comp_short(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_quant_comp_short(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:369</i>
     */
    int lame_get_quant_comp_short(PointerByReference lame_global_flagsPtr1);
    /**
     * compatibility<br>
     * Original signature : <code>int lame_set_experimentalX(lame_global_flags*, int)</code><br>
     * @param int1 compatibility<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:371</i><br>
     * @deprecated use the safer method {@link #lame_set_experimentalX(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_experimentalX(Pointer lame_global_flagsPtr1, int int1);
    /**
     * compatibility<br>
     * Original signature : <code>int lame_set_experimentalX(lame_global_flags*, int)</code><br>
     * @param int1 compatibility<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:371</i>
     */
    int lame_set_experimentalX(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_experimentalX(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:372</i><br>
     * @deprecated use the safer method {@link #lame_get_experimentalX(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_experimentalX(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_experimentalX(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:372</i>
     */
    int lame_get_experimentalX(PointerByReference lame_global_flagsPtr1);
    /**
     * another experimental option.  for testing only<br>
     * Original signature : <code>int lame_set_experimentalY(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:375</i><br>
     * @deprecated use the safer method {@link #lame_set_experimentalY(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_experimentalY(Pointer lame_global_flagsPtr1, int int1);
    /**
     * another experimental option.  for testing only<br>
     * Original signature : <code>int lame_set_experimentalY(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:375</i>
     */
    int lame_set_experimentalY(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_experimentalY(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:376</i><br>
     * @deprecated use the safer method {@link #lame_get_experimentalY(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_experimentalY(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_experimentalY(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:376</i>
     */
    int lame_get_experimentalY(PointerByReference lame_global_flagsPtr1);
    /**
     * another experimental option.  for testing only<br>
     * Original signature : <code>int lame_set_experimentalZ(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:379</i><br>
     * @deprecated use the safer method {@link #lame_set_experimentalZ(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_experimentalZ(Pointer lame_global_flagsPtr1, int int1);
    /**
     * another experimental option.  for testing only<br>
     * Original signature : <code>int lame_set_experimentalZ(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:379</i>
     */
    int lame_set_experimentalZ(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_experimentalZ(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:380</i><br>
     * @deprecated use the safer method {@link #lame_get_experimentalZ(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_experimentalZ(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_experimentalZ(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:380</i>
     */
    int lame_get_experimentalZ(PointerByReference lame_global_flagsPtr1);
    /**
     * Naoki's psycho acoustic model.  default=0<br>
     * Original signature : <code>int lame_set_exp_nspsytune(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:383</i><br>
     * @deprecated use the safer method {@link #lame_set_exp_nspsytune(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_exp_nspsytune(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Naoki's psycho acoustic model.  default=0<br>
     * Original signature : <code>int lame_set_exp_nspsytune(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:383</i>
     */
    int lame_set_exp_nspsytune(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_exp_nspsytune(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:384</i><br>
     * @deprecated use the safer method {@link #lame_get_exp_nspsytune(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_exp_nspsytune(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_exp_nspsytune(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:384</i>
     */
    int lame_get_exp_nspsytune(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>void lame_set_msfix(lame_global_flags*, double)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:386</i><br>
     * @deprecated use the safer method {@link #lame_set_msfix(com.sun.jna.ptr.PointerByReference, double)} instead
     */
    @Deprecated
    void lame_set_msfix(Pointer lame_global_flagsPtr1, double double1);
    /**
     * Original signature : <code>void lame_set_msfix(lame_global_flags*, double)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:386</i>
     */
    void lame_set_msfix(PointerByReference lame_global_flagsPtr1, double double1);
    /**
     * Original signature : <code>float lame_get_msfix(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:387</i><br>
     * @deprecated use the safer method {@link #lame_get_msfix(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_msfix(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>float lame_get_msfix(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:387</i>
     */
    float lame_get_msfix(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_exp_nspsytune2_int(lame_global_flags*, int, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:389</i><br>
     * @deprecated use the safer method {@link #lame_set_exp_nspsytune2_int(com.sun.jna.ptr.PointerByReference, int, int)} instead
     */
    @Deprecated
    int lame_set_exp_nspsytune2_int(Pointer lame_global_flagsPtr1, int int1, int int2);
    /**
     * Original signature : <code>int lame_set_exp_nspsytune2_int(lame_global_flags*, int, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:389</i>
     */
    int lame_set_exp_nspsytune2_int(PointerByReference lame_global_flagsPtr1, int int1, int int2);
    /**
     * Original signature : <code>float lame_set_exp_nspsytune2_real(lame_global_flags*, int, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:390</i><br>
     * @deprecated use the safer method {@link #lame_set_exp_nspsytune2_real(com.sun.jna.ptr.PointerByReference, int, float)} instead
     */
    @Deprecated
    float lame_set_exp_nspsytune2_real(Pointer lame_global_flagsPtr1, int int1, float float1);
    /**
     * Original signature : <code>float lame_set_exp_nspsytune2_real(lame_global_flags*, int, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:390</i>
     */
    float lame_set_exp_nspsytune2_real(PointerByReference lame_global_flagsPtr1, int int1, float float1);
    /**
     * Original signature : <code>void* lame_set_exp_nspsytune2_pointer(lame_global_flags*, int, void*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:391</i><br>
     * @deprecated use the safer method {@link #lame_set_exp_nspsytune2_pointer(com.sun.jna.ptr.PointerByReference, int, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    Pointer lame_set_exp_nspsytune2_pointer(Pointer lame_global_flagsPtr1, int int1, Pointer voidPtr1);
    /**
     * Original signature : <code>void* lame_set_exp_nspsytune2_pointer(lame_global_flags*, int, void*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:391</i>
     */
    Pointer lame_set_exp_nspsytune2_pointer(PointerByReference lame_global_flagsPtr1, int int1, Pointer voidPtr1);
    /**
     * Types of VBR.  default = vbr_off = CBR<br>
     * Original signature : <code>int lame_set_VBR(lame_global_flags*, vbr_mode)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:397</i><br>
     * @deprecated use the safer method {@link #lame_set_VBR(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_VBR(Pointer lame_global_flagsPtr1, int vbr_mode1);
    /**
     * Types of VBR.  default = vbr_off = CBR<br>
     * Original signature : <code>int lame_set_VBR(lame_global_flags*, vbr_mode)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:397</i>
     */
    int lame_set_VBR(PointerByReference lame_global_flagsPtr1, int vbr_mode1);
    /**
     * Original signature : <code>vbr_mode lame_get_VBR(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:398</i><br>
     * @deprecated use the safer method {@link #lame_get_VBR(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_VBR(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>vbr_mode lame_get_VBR(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:398</i>
     */
    int lame_get_VBR(PointerByReference lame_global_flagsPtr1);
    /**
     * VBR quality level.  0=highest  9=lowest<br>
     * Original signature : <code>int lame_set_VBR_q(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:401</i><br>
     * @deprecated use the safer method {@link #lame_set_VBR_q(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_VBR_q(Pointer lame_global_flagsPtr1, int int1);
    /**
     * VBR quality level.  0=highest  9=lowest<br>
     * Original signature : <code>int lame_set_VBR_q(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:401</i>
     */
    int lame_set_VBR_q(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_VBR_q(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:402</i><br>
     * @deprecated use the safer method {@link #lame_get_VBR_q(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_VBR_q(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_VBR_q(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:402</i>
     */
    int lame_get_VBR_q(PointerByReference lame_global_flagsPtr1);
    /**
     * Ignored except for VBR=vbr_abr (ABR mode)<br>
     * Original signature : <code>int lame_set_VBR_mean_bitrate_kbps(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:405</i><br>
     * @deprecated use the safer method {@link #lame_set_VBR_mean_bitrate_kbps(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_VBR_mean_bitrate_kbps(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Ignored except for VBR=vbr_abr (ABR mode)<br>
     * Original signature : <code>int lame_set_VBR_mean_bitrate_kbps(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:405</i>
     */
    int lame_set_VBR_mean_bitrate_kbps(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_VBR_mean_bitrate_kbps(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:406</i><br>
     * @deprecated use the safer method {@link #lame_get_VBR_mean_bitrate_kbps(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_VBR_mean_bitrate_kbps(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_VBR_mean_bitrate_kbps(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:406</i>
     */
    int lame_get_VBR_mean_bitrate_kbps(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_VBR_min_bitrate_kbps(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:408</i><br>
     * @deprecated use the safer method {@link #lame_set_VBR_min_bitrate_kbps(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_VBR_min_bitrate_kbps(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_set_VBR_min_bitrate_kbps(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:408</i>
     */
    int lame_set_VBR_min_bitrate_kbps(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_VBR_min_bitrate_kbps(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:409</i><br>
     * @deprecated use the safer method {@link #lame_get_VBR_min_bitrate_kbps(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_VBR_min_bitrate_kbps(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_VBR_min_bitrate_kbps(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:409</i>
     */
    int lame_get_VBR_min_bitrate_kbps(PointerByReference lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_set_VBR_max_bitrate_kbps(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:411</i><br>
     * @deprecated use the safer method {@link #lame_set_VBR_max_bitrate_kbps(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_VBR_max_bitrate_kbps(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_set_VBR_max_bitrate_kbps(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:411</i>
     */
    int lame_set_VBR_max_bitrate_kbps(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_VBR_max_bitrate_kbps(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:412</i><br>
     * @deprecated use the safer method {@link #lame_get_VBR_max_bitrate_kbps(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_VBR_max_bitrate_kbps(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_VBR_max_bitrate_kbps(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:412</i>
     */
    int lame_get_VBR_max_bitrate_kbps(PointerByReference lame_global_flagsPtr1);
    /**
     * 1=strictly enforce VBR_min_bitrate.  Normally it will be violated for<br>
     * analog silence<br>
     * Original signature : <code>int lame_set_VBR_hard_min(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:418</i><br>
     * @deprecated use the safer method {@link #lame_set_VBR_hard_min(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_VBR_hard_min(Pointer lame_global_flagsPtr1, int int1);
    /**
     * 1=strictly enforce VBR_min_bitrate.  Normally it will be violated for<br>
     * analog silence<br>
     * Original signature : <code>int lame_set_VBR_hard_min(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:418</i>
     */
    int lame_set_VBR_hard_min(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_VBR_hard_min(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:419</i><br>
     * @deprecated use the safer method {@link #lame_get_VBR_hard_min(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_VBR_hard_min(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_VBR_hard_min(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:419</i>
     */
    int lame_get_VBR_hard_min(PointerByReference lame_global_flagsPtr1);
    /**
     * for preset<br>
     * Original signature : <code>int lame_set_preset_expopts(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:422</i><br>
     * @deprecated use the safer method {@link #lame_set_preset_expopts(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_preset_expopts(Pointer lame_global_flagsPtr1, int int1);
    /**
     * for preset<br>
     * Original signature : <code>int lame_set_preset_expopts(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:422</i>
     */
    int lame_set_preset_expopts(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * freq in Hz to apply lowpass. Default = 0 = lame chooses.  -1 = disabled<br>
     * Original signature : <code>int lame_set_lowpassfreq(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:429</i><br>
     * @deprecated use the safer method {@link #lame_set_lowpassfreq(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_lowpassfreq(Pointer lame_global_flagsPtr1, int int1);
    /**
     * freq in Hz to apply lowpass. Default = 0 = lame chooses.  -1 = disabled<br>
     * Original signature : <code>int lame_set_lowpassfreq(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:429</i>
     */
    int lame_set_lowpassfreq(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_lowpassfreq(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:430</i><br>
     * @deprecated use the safer method {@link #lame_get_lowpassfreq(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_lowpassfreq(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_lowpassfreq(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:430</i>
     */
    int lame_get_lowpassfreq(PointerByReference lame_global_flagsPtr1);
    /**
     * width of transition band, in Hz.  Default = one polyphase filter band<br>
     * Original signature : <code>int lame_set_lowpasswidth(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:432</i><br>
     * @deprecated use the safer method {@link #lame_set_lowpasswidth(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_lowpasswidth(Pointer lame_global_flagsPtr1, int int1);
    /**
     * width of transition band, in Hz.  Default = one polyphase filter band<br>
     * Original signature : <code>int lame_set_lowpasswidth(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:432</i>
     */
    int lame_set_lowpasswidth(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_lowpasswidth(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:433</i><br>
     * @deprecated use the safer method {@link #lame_get_lowpasswidth(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_lowpasswidth(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_lowpasswidth(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:433</i>
     */
    int lame_get_lowpasswidth(PointerByReference lame_global_flagsPtr1);
    /**
     * freq in Hz to apply highpass. Default = 0 = lame chooses.  -1 = disabled<br>
     * Original signature : <code>int lame_set_highpassfreq(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:436</i><br>
     * @deprecated use the safer method {@link #lame_set_highpassfreq(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_highpassfreq(Pointer lame_global_flagsPtr1, int int1);
    /**
     * freq in Hz to apply highpass. Default = 0 = lame chooses.  -1 = disabled<br>
     * Original signature : <code>int lame_set_highpassfreq(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:436</i>
     */
    int lame_set_highpassfreq(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_highpassfreq(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:437</i><br>
     * @deprecated use the safer method {@link #lame_get_highpassfreq(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_highpassfreq(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_highpassfreq(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:437</i>
     */
    int lame_get_highpassfreq(PointerByReference lame_global_flagsPtr1);
    /**
     * width of transition band, in Hz.  Default = one polyphase filter band<br>
     * Original signature : <code>int lame_set_highpasswidth(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:439</i><br>
     * @deprecated use the safer method {@link #lame_set_highpasswidth(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_highpasswidth(Pointer lame_global_flagsPtr1, int int1);
    /**
     * width of transition band, in Hz.  Default = one polyphase filter band<br>
     * Original signature : <code>int lame_set_highpasswidth(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:439</i>
     */
    int lame_set_highpasswidth(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_highpasswidth(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:440</i><br>
     * @deprecated use the safer method {@link #lame_get_highpasswidth(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_highpasswidth(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_highpasswidth(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:440</i>
     */
    int lame_get_highpasswidth(PointerByReference lame_global_flagsPtr1);
    /**
     * only use ATH for masking<br>
     * Original signature : <code>int lame_set_ATHonly(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:449</i><br>
     * @deprecated use the safer method {@link #lame_set_ATHonly(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_ATHonly(Pointer lame_global_flagsPtr1, int int1);
    /**
     * only use ATH for masking<br>
     * Original signature : <code>int lame_set_ATHonly(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:449</i>
     */
    int lame_set_ATHonly(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_ATHonly(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:450</i><br>
     * @deprecated use the safer method {@link #lame_get_ATHonly(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_ATHonly(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_ATHonly(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:450</i>
     */
    int lame_get_ATHonly(PointerByReference lame_global_flagsPtr1);
    /**
     * only use ATH for short blocks<br>
     * Original signature : <code>int lame_set_ATHshort(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:453</i><br>
     * @deprecated use the safer method {@link #lame_set_ATHshort(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_ATHshort(Pointer lame_global_flagsPtr1, int int1);
    /**
     * only use ATH for short blocks<br>
     * Original signature : <code>int lame_set_ATHshort(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:453</i>
     */
    int lame_set_ATHshort(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_ATHshort(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:454</i><br>
     * @deprecated use the safer method {@link #lame_get_ATHshort(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_ATHshort(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_ATHshort(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:454</i>
     */
    int lame_get_ATHshort(PointerByReference lame_global_flagsPtr1);
    /**
     * disable ATH<br>
     * Original signature : <code>int lame_set_noATH(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:457</i><br>
     * @deprecated use the safer method {@link #lame_set_noATH(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_noATH(Pointer lame_global_flagsPtr1, int int1);
    /**
     * disable ATH<br>
     * Original signature : <code>int lame_set_noATH(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:457</i>
     */
    int lame_set_noATH(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_noATH(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:458</i><br>
     * @deprecated use the safer method {@link #lame_get_noATH(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_noATH(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_noATH(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:458</i>
     */
    int lame_get_noATH(PointerByReference lame_global_flagsPtr1);
    /**
     * select ATH formula<br>
     * Original signature : <code>int lame_set_ATHtype(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:461</i><br>
     * @deprecated use the safer method {@link #lame_set_ATHtype(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_ATHtype(Pointer lame_global_flagsPtr1, int int1);
    /**
     * select ATH formula<br>
     * Original signature : <code>int lame_set_ATHtype(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:461</i>
     */
    int lame_set_ATHtype(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_ATHtype(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:462</i><br>
     * @deprecated use the safer method {@link #lame_get_ATHtype(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_ATHtype(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_ATHtype(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:462</i>
     */
    int lame_get_ATHtype(PointerByReference lame_global_flagsPtr1);
    /**
     * lower ATH by this many db<br>
     * Original signature : <code>int lame_set_ATHlower(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:465</i><br>
     * @deprecated use the safer method {@link #lame_set_ATHlower(com.sun.jna.ptr.PointerByReference, float)} instead
     */
    @Deprecated
    int lame_set_ATHlower(Pointer lame_global_flagsPtr1, float float1);
    /**
     * lower ATH by this many db<br>
     * Original signature : <code>int lame_set_ATHlower(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:465</i>
     */
    int lame_set_ATHlower(PointerByReference lame_global_flagsPtr1, float float1);
    /**
     * Original signature : <code>float lame_get_ATHlower(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:466</i><br>
     * @deprecated use the safer method {@link #lame_get_ATHlower(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_ATHlower(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>float lame_get_ATHlower(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:466</i>
     */
    float lame_get_ATHlower(PointerByReference lame_global_flagsPtr1);
    /**
     * select ATH adaptive adjustment type<br>
     * Original signature : <code>int lame_set_athaa_type(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:469</i><br>
     * @deprecated use the safer method {@link #lame_set_athaa_type(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_athaa_type(Pointer lame_global_flagsPtr1, int int1);
    /**
     * select ATH adaptive adjustment type<br>
     * Original signature : <code>int lame_set_athaa_type(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:469</i>
     */
    int lame_set_athaa_type(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_athaa_type(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:470</i><br>
     * @deprecated use the safer method {@link #lame_get_athaa_type(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_athaa_type(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_athaa_type(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:470</i>
     */
    int lame_get_athaa_type(PointerByReference lame_global_flagsPtr1);
    /**
     * select the loudness approximation used by the ATH adaptive auto-leveling<br>
     * Original signature : <code>int lame_set_athaa_loudapprox(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:473</i><br>
     * @deprecated use the safer method {@link #lame_set_athaa_loudapprox(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_athaa_loudapprox(Pointer lame_global_flagsPtr1, int int1);
    /**
     * select the loudness approximation used by the ATH adaptive auto-leveling<br>
     * Original signature : <code>int lame_set_athaa_loudapprox(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:473</i>
     */
    int lame_set_athaa_loudapprox(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_athaa_loudapprox(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:474</i><br>
     * @deprecated use the safer method {@link #lame_get_athaa_loudapprox(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_athaa_loudapprox(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_athaa_loudapprox(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:474</i>
     */
    int lame_get_athaa_loudapprox(PointerByReference lame_global_flagsPtr1);
    /**
     * adjust (in dB) the point below which adaptive ATH level adjustment occurs<br>
     * Original signature : <code>int lame_set_athaa_sensitivity(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:477</i><br>
     * @deprecated use the safer method {@link #lame_set_athaa_sensitivity(com.sun.jna.ptr.PointerByReference, float)} instead
     */
    @Deprecated
    int lame_set_athaa_sensitivity(Pointer lame_global_flagsPtr1, float float1);
    /**
     * adjust (in dB) the point below which adaptive ATH level adjustment occurs<br>
     * Original signature : <code>int lame_set_athaa_sensitivity(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:477</i>
     */
    int lame_set_athaa_sensitivity(PointerByReference lame_global_flagsPtr1, float float1);
    /**
     * Original signature : <code>float lame_get_athaa_sensitivity(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:478</i><br>
     * @deprecated use the safer method {@link #lame_get_athaa_sensitivity(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_athaa_sensitivity(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>float lame_get_athaa_sensitivity(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:478</i>
     */
    float lame_get_athaa_sensitivity(PointerByReference lame_global_flagsPtr1);
    /**
     * predictability limit (ISO tonality formula)<br>
     * Original signature : <code>int lame_set_cwlimit(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:481</i><br>
     * @deprecated use the safer method {@link #lame_set_cwlimit(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_cwlimit(Pointer lame_global_flagsPtr1, int int1);
    /**
     * predictability limit (ISO tonality formula)<br>
     * Original signature : <code>int lame_set_cwlimit(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:481</i>
     */
    int lame_set_cwlimit(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_cwlimit(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:482</i><br>
     * @deprecated use the safer method {@link #lame_get_cwlimit(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_cwlimit(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_cwlimit(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:482</i>
     */
    int lame_get_cwlimit(PointerByReference lame_global_flagsPtr1);
    /**
     * allow blocktypes to differ between channels?<br>
     * default: 0 for jstereo, 1 for stereo<br>
     * Original signature : <code>int lame_set_allow_diff_short(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:488</i><br>
     * @deprecated use the safer method {@link #lame_set_allow_diff_short(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_allow_diff_short(Pointer lame_global_flagsPtr1, int int1);
    /**
     * allow blocktypes to differ between channels?<br>
     * default: 0 for jstereo, 1 for stereo<br>
     * Original signature : <code>int lame_set_allow_diff_short(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:488</i>
     */
    int lame_set_allow_diff_short(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_allow_diff_short(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:489</i><br>
     * @deprecated use the safer method {@link #lame_get_allow_diff_short(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_allow_diff_short(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_allow_diff_short(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:489</i>
     */
    int lame_get_allow_diff_short(PointerByReference lame_global_flagsPtr1);
    /**
     * use temporal masking effect (default = 1)<br>
     * Original signature : <code>int lame_set_useTemporal(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:492</i><br>
     * @deprecated use the safer method {@link #lame_set_useTemporal(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_useTemporal(Pointer lame_global_flagsPtr1, int int1);
    /**
     * use temporal masking effect (default = 1)<br>
     * Original signature : <code>int lame_set_useTemporal(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:492</i>
     */
    int lame_set_useTemporal(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_useTemporal(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:493</i><br>
     * @deprecated use the safer method {@link #lame_get_useTemporal(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_useTemporal(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_useTemporal(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:493</i>
     */
    int lame_get_useTemporal(PointerByReference lame_global_flagsPtr1);
    /**
     * use temporal masking effect (default = 1)<br>
     * Original signature : <code>int lame_set_interChRatio(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:496</i><br>
     * @deprecated use the safer method {@link #lame_set_interChRatio(com.sun.jna.ptr.PointerByReference, float)} instead
     */
    @Deprecated
    int lame_set_interChRatio(Pointer lame_global_flagsPtr1, float float1);
    /**
     * use temporal masking effect (default = 1)<br>
     * Original signature : <code>int lame_set_interChRatio(lame_global_flags*, float)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:496</i>
     */
    int lame_set_interChRatio(PointerByReference lame_global_flagsPtr1, float float1);
    /**
     * Original signature : <code>float lame_get_interChRatio(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:497</i><br>
     * @deprecated use the safer method {@link #lame_get_interChRatio(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_interChRatio(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>float lame_get_interChRatio(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:497</i>
     */
    float lame_get_interChRatio(PointerByReference lame_global_flagsPtr1);
    /**
     * disable short blocks<br>
     * Original signature : <code>int lame_set_no_short_blocks(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:500</i><br>
     * @deprecated use the safer method {@link #lame_set_no_short_blocks(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_no_short_blocks(Pointer lame_global_flagsPtr1, int int1);
    /**
     * disable short blocks<br>
     * Original signature : <code>int lame_set_no_short_blocks(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:500</i>
     */
    int lame_set_no_short_blocks(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_no_short_blocks(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:501</i><br>
     * @deprecated use the safer method {@link #lame_get_no_short_blocks(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_no_short_blocks(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_no_short_blocks(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:501</i>
     */
    int lame_get_no_short_blocks(PointerByReference lame_global_flagsPtr1);
    /**
     * force short blocks<br>
     * Original signature : <code>int lame_set_force_short_blocks(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:504</i><br>
     * @deprecated use the safer method {@link #lame_set_force_short_blocks(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_force_short_blocks(Pointer lame_global_flagsPtr1, int int1);
    /**
     * force short blocks<br>
     * Original signature : <code>int lame_set_force_short_blocks(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:504</i>
     */
    int lame_set_force_short_blocks(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_force_short_blocks(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:505</i><br>
     * @deprecated use the safer method {@link #lame_get_force_short_blocks(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_force_short_blocks(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_force_short_blocks(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:505</i>
     */
    int lame_get_force_short_blocks(PointerByReference lame_global_flagsPtr1);
    /**
     * Input PCM is emphased PCM (for instance from one of the rarely<br>
     * emphased CDs), it is STRONGLY not recommended to use this, because<br>
     * psycho does not take it into account, and last but not least many decoders<br>
     * ignore these bits<br>
     * Original signature : <code>int lame_set_emphasis(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:511</i><br>
     * @deprecated use the safer method {@link #lame_set_emphasis(com.sun.jna.ptr.PointerByReference, int)} instead
     */
    @Deprecated
    int lame_set_emphasis(Pointer lame_global_flagsPtr1, int int1);
    /**
     * Input PCM is emphased PCM (for instance from one of the rarely<br>
     * emphased CDs), it is STRONGLY not recommended to use this, because<br>
     * psycho does not take it into account, and last but not least many decoders<br>
     * ignore these bits<br>
     * Original signature : <code>int lame_set_emphasis(lame_global_flags*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:511</i>
     */
    int lame_set_emphasis(PointerByReference lame_global_flagsPtr1, int int1);
    /**
     * Original signature : <code>int lame_get_emphasis(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:512</i><br>
     * @deprecated use the safer method {@link #lame_get_emphasis(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_emphasis(Pointer lame_global_flagsPtr1);
    /**
     * Original signature : <code>int lame_get_emphasis(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:512</i>
     */
    int lame_get_emphasis(PointerByReference lame_global_flagsPtr1);
    /**
     * version  0=MPEG-2  1=MPEG-1  (2=MPEG-2.5)<br>
     * Original signature : <code>int lame_get_version(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:521</i><br>
     * @deprecated use the safer method {@link #lame_get_version(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_version(Pointer lame_global_flagsPtr1);
    /**
     * version  0=MPEG-2  1=MPEG-1  (2=MPEG-2.5)<br>
     * Original signature : <code>int lame_get_version(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:521</i>
     */
    int lame_get_version(PointerByReference lame_global_flagsPtr1);
    /**
     * encoder delay<br>
     * Original signature : <code>int lame_get_encoder_delay(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:524</i><br>
     * @deprecated use the safer method {@link #lame_get_encoder_delay(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_encoder_delay(Pointer lame_global_flagsPtr1);
    /**
     * encoder delay<br>
     * Original signature : <code>int lame_get_encoder_delay(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:524</i>
     */
    int lame_get_encoder_delay(PointerByReference lame_global_flagsPtr1);
    /**
     * padding appended to the input to make sure decoder can fully decode<br>
     * all input.  Note that this value can only be calculated during the<br>
     * call to lame_encoder_flush().  Before lame_encoder_flush() has<br>
     * been called, the value of encoder_padding = 0.<br>
     * Original signature : <code>int lame_get_encoder_padding(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:532</i><br>
     * @deprecated use the safer method {@link #lame_get_encoder_padding(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_encoder_padding(Pointer lame_global_flagsPtr1);
    /**
     * padding appended to the input to make sure decoder can fully decode<br>
     * all input.  Note that this value can only be calculated during the<br>
     * call to lame_encoder_flush().  Before lame_encoder_flush() has<br>
     * been called, the value of encoder_padding = 0.<br>
     * Original signature : <code>int lame_get_encoder_padding(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:532</i>
     */
    int lame_get_encoder_padding(PointerByReference lame_global_flagsPtr1);
    /**
     * size of MPEG frame<br>
     * Original signature : <code>int lame_get_framesize(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:535</i><br>
     * @deprecated use the safer method {@link #lame_get_framesize(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_framesize(Pointer lame_global_flagsPtr1);
    /**
     * size of MPEG frame<br>
     * Original signature : <code>int lame_get_framesize(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:535</i>
     */
    int lame_get_framesize(PointerByReference lame_global_flagsPtr1);
    /**
     * number of PCM samples buffered, but not yet encoded to mp3 data.<br>
     * Original signature : <code>int lame_get_mf_samples_to_encode(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:538</i><br>
     * @deprecated use the safer method {@link #lame_get_mf_samples_to_encode(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_mf_samples_to_encode(Pointer gfp);
    /**
     * number of PCM samples buffered, but not yet encoded to mp3 data.<br>
     * Original signature : <code>int lame_get_mf_samples_to_encode(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:538</i>
     */
    int lame_get_mf_samples_to_encode(PointerByReference gfp);
    /**
     * size (bytes) of mp3 data buffered, but not yet encoded.<br>
     * this is the number of bytes which would be output by a call to <br>
     * lame_encode_flush_nogap.  NOTE: lame_encode_flush() will return<br>
     * more bytes than this because it will encode the reamining buffered<br>
     * PCM samples before flushing the mp3 buffers.<br>
     * Original signature : <code>int lame_get_size_mp3buffer(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:547</i><br>
     * @deprecated use the safer method {@link #lame_get_size_mp3buffer(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_size_mp3buffer(Pointer gfp);
    /**
     * size (bytes) of mp3 data buffered, but not yet encoded.<br>
     * this is the number of bytes which would be output by a call to <br>
     * lame_encode_flush_nogap.  NOTE: lame_encode_flush() will return<br>
     * more bytes than this because it will encode the reamining buffered<br>
     * PCM samples before flushing the mp3 buffers.<br>
     * Original signature : <code>int lame_get_size_mp3buffer(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:547</i>
     */
    int lame_get_size_mp3buffer(PointerByReference gfp);
    /**
     * number of frames encoded so far<br>
     * Original signature : <code>int lame_get_frameNum(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:550</i><br>
     * @deprecated use the safer method {@link #lame_get_frameNum(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_frameNum(Pointer lame_global_flagsPtr1);
    /**
     * number of frames encoded so far<br>
     * Original signature : <code>int lame_get_frameNum(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:550</i>
     */
    int lame_get_frameNum(PointerByReference lame_global_flagsPtr1);
    /**
     * lame's estimate of the total number of frames to be encoded<br>
     * only valid if calling program set num_samples<br>
     * Original signature : <code>int lame_get_totalframes(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:556</i><br>
     * @deprecated use the safer method {@link #lame_get_totalframes(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_totalframes(Pointer lame_global_flagsPtr1);
    /**
     * lame's estimate of the total number of frames to be encoded<br>
     * only valid if calling program set num_samples<br>
     * Original signature : <code>int lame_get_totalframes(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:556</i>
     */
    int lame_get_totalframes(PointerByReference lame_global_flagsPtr1);
    /**
     * RadioGain value. Multiplied by 10 and rounded to the nearest.<br>
     * Original signature : <code>int lame_get_RadioGain(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:559</i><br>
     * @deprecated use the safer method {@link #lame_get_RadioGain(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_RadioGain(Pointer lame_global_flagsPtr1);
    /**
     * RadioGain value. Multiplied by 10 and rounded to the nearest.<br>
     * Original signature : <code>int lame_get_RadioGain(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:559</i>
     */
    int lame_get_RadioGain(PointerByReference lame_global_flagsPtr1);
    /**
     * AudiophileGain value. Multipled by 10 and rounded to the nearest.<br>
     * Original signature : <code>int lame_get_AudiophileGain(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:562</i><br>
     * @deprecated use the safer method {@link #lame_get_AudiophileGain(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_AudiophileGain(Pointer lame_global_flagsPtr1);
    /**
     * AudiophileGain value. Multipled by 10 and rounded to the nearest.<br>
     * Original signature : <code>int lame_get_AudiophileGain(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:562</i>
     */
    int lame_get_AudiophileGain(PointerByReference lame_global_flagsPtr1);
    /**
     * the peak sample<br>
     * Original signature : <code>float lame_get_PeakSample(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:565</i><br>
     * @deprecated use the safer method {@link #lame_get_PeakSample(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_PeakSample(Pointer lame_global_flagsPtr1);
    /**
     * the peak sample<br>
     * Original signature : <code>float lame_get_PeakSample(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:565</i>
     */
    float lame_get_PeakSample(PointerByReference lame_global_flagsPtr1);
    /**
     * Gain change required for preventing clipping. The value is correct only if <br>
     * peak sample searching was enabled. If negative then the waveform <br>
     * already does not clip. The value is multiplied by 10 and rounded up.<br>
     * Original signature : <code>int lame_get_noclipGainChange(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:576</i><br>
     * @deprecated use the safer method {@link #lame_get_noclipGainChange(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_get_noclipGainChange(Pointer lame_global_flagsPtr1);
    /**
     * Gain change required for preventing clipping. The value is correct only if <br>
     * peak sample searching was enabled. If negative then the waveform <br>
     * already does not clip. The value is multiplied by 10 and rounded up.<br>
     * Original signature : <code>int lame_get_noclipGainChange(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:576</i>
     */
    int lame_get_noclipGainChange(PointerByReference lame_global_flagsPtr1);
    /**
     * user-specified scale factor required for preventing clipping. Value is <br>
     * correct only if peak sample searching was enabled and no user-specified<br>
     * scaling was performed. If negative then either the waveform already does<br>
     * not clip or the value cannot be determined<br>
     * Original signature : <code>float lame_get_noclipScale(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:582</i><br>
     * @deprecated use the safer method {@link #lame_get_noclipScale(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    float lame_get_noclipScale(Pointer lame_global_flagsPtr1);
    /**
     * user-specified scale factor required for preventing clipping. Value is <br>
     * correct only if peak sample searching was enabled and no user-specified<br>
     * scaling was performed. If negative then either the waveform already does<br>
     * not clip or the value cannot be determined<br>
     * Original signature : <code>float lame_get_noclipScale(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:582</i>
     */
    float lame_get_noclipScale(PointerByReference lame_global_flagsPtr1);
    /**
     * REQUIRED:<br>
     * sets more internal configuration based on data provided above.<br>
     * returns -1 if something failed.<br>
     * Original signature : <code>int lame_init_params(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:595</i><br>
     * @deprecated use the safer method {@link #lame_init_params(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_init_params(Pointer lame_global_flagsPtr1);
    /**
     * REQUIRED:<br>
     * sets more internal configuration based on data provided above.<br>
     * returns -1 if something failed.<br>
     * Original signature : <code>int lame_init_params(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:595</i>
     */
    int lame_init_params(PointerByReference lame_global_flagsPtr1);
    /**
     * OPTIONAL:<br>
     * get the version number, in a string. of the form:  <br>
     * "3.63 (beta)" or just "3.63".<br>
     * Original signature : <code>char* get_lame_version()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:603</i>
     */
    Pointer get_lame_version();
    /**
     * Original signature : <code>char* get_lame_short_version()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:604</i>
     */
    Pointer get_lame_short_version();
    /**
     * Original signature : <code>char* get_lame_very_short_version()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:605</i>
     */
    Pointer get_lame_very_short_version();
    /**
     * Original signature : <code>char* get_psy_version()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:606</i>
     */
    Pointer get_psy_version();
    /**
     * Original signature : <code>char* get_lame_url()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:607</i>
     */
    Pointer get_lame_url();
    /**
     * Original signature : <code>void get_lame_version_numerical(const lame_version_t*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:629</i>
     */
    void get_lame_version_numerical(LibLame.lame_version_t lame_version_tPtr1);
    /**
     * OPTIONAL:<br>
     * print internal lame configuration to message handler<br>
     * Original signature : <code>void lame_print_config(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:636</i><br>
     * @deprecated use the safer method {@link #lame_print_config(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void lame_print_config(Pointer gfp);
    /**
     * OPTIONAL:<br>
     * print internal lame configuration to message handler<br>
     * Original signature : <code>void lame_print_config(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:636</i>
     */
    void lame_print_config(PointerByReference gfp);
    /**
     * Original signature : <code>void lame_print_internals(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:638</i><br>
     * @deprecated use the safer method {@link #lame_print_internals(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void lame_print_internals(Pointer gfp);
    /**
     * Original signature : <code>void lame_print_internals(const lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:638</i>
     */
    void lame_print_internals(PointerByReference gfp);
    /**
     * input pcm data, output (maybe) mp3 frames.<br>
     * This routine handles all buffering, resampling and filtering for you.<br>
     * <br>
     * return code     number of bytes output in mp3buf. Can be 0 <br>
     *                 -1:  mp3buf was too small<br>
     *                 -2:  malloc() problem<br>
     *                 -3:  lame_init_params() not called<br>
     *                 -4:  psycho acoustic problems <br>
     * * The required mp3buf_size can be computed from num_samples, <br>
     * samplerate and encoding rate, but here is a worst case estimate:<br>
     * * mp3buf_size in bytes = 1.25*num_samples + 7200<br>
     * * I think a tighter bound could be:  (mt, March 2000)<br>
     * MPEG1:<br>
     *    num_samples*(bitrate/8)/samplerate + 4*1152*(bitrate/8)/samplerate + 512<br>
     * MPEG2:<br>
     *    num_samples*(bitrate/8)/samplerate + 4*576*(bitrate/8)/samplerate + 256<br>
     * * but test first if you use that!<br>
     * * set mp3buf_size = 0 and LAME will not check if mp3buf_size is<br>
     * large enough.<br>
     * * NOTE:<br>
     * if gfp->num_channels=2, but gfp->mode = 3 (mono), the L & R channels<br>
     * will be averaged into the L channel before encoding only the L channel<br>
     * This will overwrite the data in buffer_l[] and buffer_r[].<br>
     * Original signature : <code>int lame_encode_buffer(lame_global_flags*, const short[], const short[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:673</i><br>
     * @deprecated use the safer methods {@link #lame_encode_buffer(com.sun.jna.ptr.PointerByReference, short[], short[], int, java.nio.ByteBuffer, int)} and {@link #lame_encode_buffer(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.ShortByReference, com.sun.jna.ptr.ShortByReference, int, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_buffer(Pointer gfp, ShortByReference buffer_l, ShortByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * input pcm data, output (maybe) mp3 frames.<br>
     * This routine handles all buffering, resampling and filtering for you.<br>
     * <br>
     * return code     number of bytes output in mp3buf. Can be 0 <br>
     *                 -1:  mp3buf was too small<br>
     *                 -2:  malloc() problem<br>
     *                 -3:  lame_init_params() not called<br>
     *                 -4:  psycho acoustic problems <br>
     * * The required mp3buf_size can be computed from num_samples, <br>
     * samplerate and encoding rate, but here is a worst case estimate:<br>
     * * mp3buf_size in bytes = 1.25*num_samples + 7200<br>
     * * I think a tighter bound could be:  (mt, March 2000)<br>
     * MPEG1:<br>
     *    num_samples*(bitrate/8)/samplerate + 4*1152*(bitrate/8)/samplerate + 512<br>
     * MPEG2:<br>
     *    num_samples*(bitrate/8)/samplerate + 4*576*(bitrate/8)/samplerate + 256<br>
     * * but test first if you use that!<br>
     * * set mp3buf_size = 0 and LAME will not check if mp3buf_size is<br>
     * large enough.<br>
     * * NOTE:<br>
     * if gfp->num_channels=2, but gfp->mode = 3 (mono), the L & R channels<br>
     * will be averaged into the L channel before encoding only the L channel<br>
     * This will overwrite the data in buffer_l[] and buffer_r[].<br>
     * Original signature : <code>int lame_encode_buffer(lame_global_flags*, const short[], const short[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:673</i>
     */
    int lame_encode_buffer(PointerByReference gfp, short buffer_l[], short buffer_r[], int nsamples, ByteBuffer mp3buf, int mp3buf_size);
    /**
     * input pcm data, output (maybe) mp3 frames.<br>
     * This routine handles all buffering, resampling and filtering for you.<br>
     * <br>
     * return code     number of bytes output in mp3buf. Can be 0 <br>
     *                 -1:  mp3buf was too small<br>
     *                 -2:  malloc() problem<br>
     *                 -3:  lame_init_params() not called<br>
     *                 -4:  psycho acoustic problems <br>
     * * The required mp3buf_size can be computed from num_samples, <br>
     * samplerate and encoding rate, but here is a worst case estimate:<br>
     * * mp3buf_size in bytes = 1.25*num_samples + 7200<br>
     * * I think a tighter bound could be:  (mt, March 2000)<br>
     * MPEG1:<br>
     *    num_samples*(bitrate/8)/samplerate + 4*1152*(bitrate/8)/samplerate + 512<br>
     * MPEG2:<br>
     *    num_samples*(bitrate/8)/samplerate + 4*576*(bitrate/8)/samplerate + 256<br>
     * * but test first if you use that!<br>
     * * set mp3buf_size = 0 and LAME will not check if mp3buf_size is<br>
     * large enough.<br>
     * * NOTE:<br>
     * if gfp->num_channels=2, but gfp->mode = 3 (mono), the L & R channels<br>
     * will be averaged into the L channel before encoding only the L channel<br>
     * This will overwrite the data in buffer_l[] and buffer_r[].<br>
     * Original signature : <code>int lame_encode_buffer(lame_global_flags*, const short[], const short[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:673</i>
     */
    int lame_encode_buffer(PointerByReference gfp, ShortByReference buffer_l, ShortByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * as above, but input has L & R channel data interleaved.<br>
     * NOTE: <br>
     * num_samples = number of samples in the L (or R)<br>
     * channel, not the total number of samples in pcm[]<br>
     * Original signature : <code>int lame_encode_buffer_interleaved(lame_global_flags*, short[], int, unsigned char*, int)</code><br>
     * @param gfp global context handlei<br>
     * @param pcm PCM data for left and right<br>channel, interleaved<br>
     * @param num_samples number of samples per channel,<br>_not_ number of samples in<br>pcm[]<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:688</i><br>
     * @deprecated use the safer methods {@link #lame_encode_buffer_interleaved(com.sun.jna.ptr.PointerByReference, java.nio.ShortBuffer, int, java.nio.ByteBuffer, int)} and {@link #lame_encode_buffer_interleaved(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.ShortByReference, int, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_buffer_interleaved(Pointer gfp, ShortByReference pcm, int num_samples, Pointer mp3buf, int mp3buf_size);
    /**
     * as above, but input has L & R channel data interleaved.<br>
     * NOTE: <br>
     * num_samples = number of samples in the L (or R)<br>
     * channel, not the total number of samples in pcm[]<br>
     * Original signature : <code>int lame_encode_buffer_interleaved(lame_global_flags*, short[], int, unsigned char*, int)</code><br>
     * @param gfp global context handlei<br>
     * @param pcm PCM data for left and right<br>channel, interleaved<br>
     * @param num_samples number of samples per channel,<br>_not_ number of samples in<br>pcm[]<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:688</i>
     */
    int lame_encode_buffer_interleaved(PointerByReference gfp, ShortBuffer pcm, int num_samples, ByteBuffer mp3buf, int mp3buf_size);
    /**
     * as above, but input has L & R channel data interleaved.<br>
     * NOTE: <br>
     * num_samples = number of samples in the L (or R)<br>
     * channel, not the total number of samples in pcm[]<br>
     * Original signature : <code>int lame_encode_buffer_interleaved(lame_global_flags*, short[], int, unsigned char*, int)</code><br>
     * @param gfp global context handlei<br>
     * @param pcm PCM data for left and right<br>channel, interleaved<br>
     * @param num_samples number of samples per channel,<br>_not_ number of samples in<br>pcm[]<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:688</i>
     */
    int lame_encode_buffer_interleaved(PointerByReference gfp, ShortByReference pcm, int num_samples, Pointer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for 'float's.<br>
     * !! NOTE: !! data must still be scaled to be in the same range as <br>
     * short int, +/- 32768<br>
     * Original signature : <code>int lame_encode_buffer_float(lame_global_flags*, const float[], const float[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:704</i><br>
     * @deprecated use the safer methods {@link #lame_encode_buffer_float(com.sun.jna.ptr.PointerByReference, float[], float[], int, java.nio.ByteBuffer, int)} and {@link #lame_encode_buffer_float(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.FloatByReference, com.sun.jna.ptr.FloatByReference, int, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_buffer_float(Pointer gfp, FloatByReference buffer_l, FloatByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for 'float's.<br>
     * !! NOTE: !! data must still be scaled to be in the same range as <br>
     * short int, +/- 32768<br>
     * Original signature : <code>int lame_encode_buffer_float(lame_global_flags*, const float[], const float[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:704</i>
     */
    int lame_encode_buffer_float(PointerByReference gfp, float buffer_l[], float buffer_r[], int nsamples, ByteBuffer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for 'float's.<br>
     * !! NOTE: !! data must still be scaled to be in the same range as <br>
     * short int, +/- 32768<br>
     * Original signature : <code>int lame_encode_buffer_float(lame_global_flags*, const float[], const float[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:704</i>
     */
    int lame_encode_buffer_float(PointerByReference gfp, FloatByReference buffer_l, FloatByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for long's <br>
     * !! NOTE: !! data must still be scaled to be in the same range as <br>
     * short int, +/- 32768  <br>
     * * This scaling was a mistake (doesn't allow one to exploit full<br>
     * precision of type 'long'.  Use lame_encode_buffer_long2() instead.<br>
     * Original signature : <code>int lame_encode_buffer_long(lame_global_flags*, const long[], const long[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:722</i><br>
     * @deprecated use the safer methods {@link #lame_encode_buffer_long(com.sun.jna.ptr.PointerByReference, com.sun.jna.NativeLong[], com.sun.jna.NativeLong[], int, java.nio.ByteBuffer, int)} and {@link #lame_encode_buffer_long(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.NativeLongByReference, com.sun.jna.ptr.NativeLongByReference, int, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_buffer_long(Pointer gfp, NativeLongByReference buffer_l, NativeLongByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for long's <br>
     * !! NOTE: !! data must still be scaled to be in the same range as <br>
     * short int, +/- 32768  <br>
     * * This scaling was a mistake (doesn't allow one to exploit full<br>
     * precision of type 'long'.  Use lame_encode_buffer_long2() instead.<br>
     * Original signature : <code>int lame_encode_buffer_long(lame_global_flags*, const long[], const long[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:722</i>
     */
    int lame_encode_buffer_long(PointerByReference gfp, NativeLong buffer_l[], NativeLong buffer_r[], int nsamples, ByteBuffer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for long's <br>
     * !! NOTE: !! data must still be scaled to be in the same range as <br>
     * short int, +/- 32768  <br>
     * * This scaling was a mistake (doesn't allow one to exploit full<br>
     * precision of type 'long'.  Use lame_encode_buffer_long2() instead.<br>
     * Original signature : <code>int lame_encode_buffer_long(lame_global_flags*, const long[], const long[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:722</i>
     */
    int lame_encode_buffer_long(PointerByReference gfp, NativeLongByReference buffer_l, NativeLongByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * Same as lame_encode_buffer_long(), but with correct scaling. <br>
     * !! NOTE: !! data must still be scaled to be in the same range as  <br>
     * type 'long'.   Data should be in the range:  +/- 2^(8*size(long)-1)<br>
     * Original signature : <code>int lame_encode_buffer_long2(lame_global_flags*, const long[], const long[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:736</i><br>
     * @deprecated use the safer methods {@link #lame_encode_buffer_long2(com.sun.jna.ptr.PointerByReference, com.sun.jna.NativeLong[], com.sun.jna.NativeLong[], int, java.nio.ByteBuffer, int)} and {@link #lame_encode_buffer_long2(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.NativeLongByReference, com.sun.jna.ptr.NativeLongByReference, int, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_buffer_long2(Pointer gfp, NativeLongByReference buffer_l, NativeLongByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * Same as lame_encode_buffer_long(), but with correct scaling. <br>
     * !! NOTE: !! data must still be scaled to be in the same range as  <br>
     * type 'long'.   Data should be in the range:  +/- 2^(8*size(long)-1)<br>
     * Original signature : <code>int lame_encode_buffer_long2(lame_global_flags*, const long[], const long[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:736</i>
     */
    int lame_encode_buffer_long2(PointerByReference gfp, NativeLong buffer_l[], NativeLong buffer_r[], int nsamples, ByteBuffer mp3buf, int mp3buf_size);
    /**
     * Same as lame_encode_buffer_long(), but with correct scaling. <br>
     * !! NOTE: !! data must still be scaled to be in the same range as  <br>
     * type 'long'.   Data should be in the range:  +/- 2^(8*size(long)-1)<br>
     * Original signature : <code>int lame_encode_buffer_long2(lame_global_flags*, const long[], const long[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:736</i>
     */
    int lame_encode_buffer_long2(PointerByReference gfp, NativeLongByReference buffer_l, NativeLongByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for int's <br>
     * !! NOTE: !! input should be scaled to the maximum range of 'int'<br>
     * If int is 4 bytes, then the values should range from<br>
     * +/- 2147483648.  <br>
     * * This routine does not (and cannot, without loosing precision) use<br>
     * the same scaling as the rest of the lame_encode_buffer() routines.<br>
     * Original signature : <code>int lame_encode_buffer_int(lame_global_flags*, const int[], const int[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:754</i><br>
     * @deprecated use the safer methods {@link #lame_encode_buffer_int(com.sun.jna.ptr.PointerByReference, int[], int[], int, java.nio.ByteBuffer, int)} and {@link #lame_encode_buffer_int(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference, com.sun.jna.ptr.IntByReference, int, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_buffer_int(Pointer gfp, IntByReference buffer_l, IntByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for int's <br>
     * !! NOTE: !! input should be scaled to the maximum range of 'int'<br>
     * If int is 4 bytes, then the values should range from<br>
     * +/- 2147483648.  <br>
     * * This routine does not (and cannot, without loosing precision) use<br>
     * the same scaling as the rest of the lame_encode_buffer() routines.<br>
     * Original signature : <code>int lame_encode_buffer_int(lame_global_flags*, const int[], const int[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:754</i>
     */
    int lame_encode_buffer_int(PointerByReference gfp, int buffer_l[], int buffer_r[], int nsamples, ByteBuffer mp3buf, int mp3buf_size);
    /**
     * as lame_encode_buffer, but for int's <br>
     * !! NOTE: !! input should be scaled to the maximum range of 'int'<br>
     * If int is 4 bytes, then the values should range from<br>
     * +/- 2147483648.  <br>
     * * This routine does not (and cannot, without loosing precision) use<br>
     * the same scaling as the rest of the lame_encode_buffer() routines.<br>
     * Original signature : <code>int lame_encode_buffer_int(lame_global_flags*, const int[], const int[], const int, unsigned char*, const int)</code><br>
     * @param gfp global context handle<br>
     * @param buffer_l PCM data for left channel<br>
     * @param buffer_r PCM data for right channel<br>
     * @param nsamples number of samples per channel<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:754</i>
     */
    int lame_encode_buffer_int(PointerByReference gfp, IntByReference buffer_l, IntByReference buffer_r, int nsamples, Pointer mp3buf, int mp3buf_size);
    /**
     * REQUIRED:<br>
     * lame_encode_flush will flush the intenal PCM buffers, padding with <br>
     * 0's to make sure the final frame is complete, and then flush<br>
     * the internal MP3 buffers, and thus may return a <br>
     * final few mp3 frames.  'mp3buf' should be at least 7200 bytes long<br>
     * to hold all possible emitted data.<br>
     * * will also write id3v1 tags (if any) into the bitstream       <br>
     * * return code = number of bytes output to mp3buf. Can be 0<br>
     * Original signature : <code>int lame_encode_flush(lame_global_flags*, unsigned char*, int)</code><br>
     * @param gfp global context handle<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:779</i><br>
     * @deprecated use the safer methods {@link #lame_encode_flush(com.sun.jna.ptr.PointerByReference, java.nio.ByteBuffer, int)} and {@link #lame_encode_flush(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_flush(Pointer gfp, Pointer mp3buf, int size);
    /**
     * REQUIRED:<br>
     * lame_encode_flush will flush the intenal PCM buffers, padding with <br>
     * 0's to make sure the final frame is complete, and then flush<br>
     * the internal MP3 buffers, and thus may return a <br>
     * final few mp3 frames.  'mp3buf' should be at least 7200 bytes long<br>
     * to hold all possible emitted data.<br>
     * * will also write id3v1 tags (if any) into the bitstream       <br>
     * * return code = number of bytes output to mp3buf. Can be 0<br>
     * Original signature : <code>int lame_encode_flush(lame_global_flags*, unsigned char*, int)</code><br>
     * @param gfp global context handle<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:779</i>
     */
    int lame_encode_flush(PointerByReference gfp, ByteBuffer mp3buf, int size);
    /**
     * REQUIRED:<br>
     * lame_encode_flush will flush the intenal PCM buffers, padding with <br>
     * 0's to make sure the final frame is complete, and then flush<br>
     * the internal MP3 buffers, and thus may return a <br>
     * final few mp3 frames.  'mp3buf' should be at least 7200 bytes long<br>
     * to hold all possible emitted data.<br>
     * * will also write id3v1 tags (if any) into the bitstream       <br>
     * * return code = number of bytes output to mp3buf. Can be 0<br>
     * Original signature : <code>int lame_encode_flush(lame_global_flags*, unsigned char*, int)</code><br>
     * @param gfp global context handle<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:779</i>
     */
    int lame_encode_flush(PointerByReference gfp, Pointer mp3buf, int size);
    /**
     * OPTIONAL:<br>
     * lame_encode_flush_nogap will flush the internal mp3 buffers and pad<br>
     * the last frame with ancillary data so it is a complete mp3 frame.<br>
     * <br>
     * 'mp3buf' should be at least 7200 bytes long<br>
     * to hold all possible emitted data.<br>
     * * After a call to this routine, the outputed mp3 data is complete, but<br>
     * you may continue to encode new PCM samples and write future mp3 data<br>
     * to a different file.  The two mp3 files will play back with no gaps<br>
     * if they are concatenated together.<br>
     * * This routine will NOT write id3v1 tags into the bitstream.<br>
     * * return code = number of bytes output to mp3buf. Can be 0<br>
     * Original signature : <code>int lame_encode_flush_nogap(lame_global_flags*, unsigned char*, int)</code><br>
     * @param gfp global context handle<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:801</i><br>
     * @deprecated use the safer methods {@link #lame_encode_flush_nogap(com.sun.jna.ptr.PointerByReference, java.nio.ByteBuffer, int)} and {@link #lame_encode_flush_nogap(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_flush_nogap(Pointer gfp, Pointer mp3buf, int size);
    /**
     * OPTIONAL:<br>
     * lame_encode_flush_nogap will flush the internal mp3 buffers and pad<br>
     * the last frame with ancillary data so it is a complete mp3 frame.<br>
     * <br>
     * 'mp3buf' should be at least 7200 bytes long<br>
     * to hold all possible emitted data.<br>
     * * After a call to this routine, the outputed mp3 data is complete, but<br>
     * you may continue to encode new PCM samples and write future mp3 data<br>
     * to a different file.  The two mp3 files will play back with no gaps<br>
     * if they are concatenated together.<br>
     * * This routine will NOT write id3v1 tags into the bitstream.<br>
     * * return code = number of bytes output to mp3buf. Can be 0<br>
     * Original signature : <code>int lame_encode_flush_nogap(lame_global_flags*, unsigned char*, int)</code><br>
     * @param gfp global context handle<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:801</i>
     */
    int lame_encode_flush_nogap(PointerByReference gfp, ByteBuffer mp3buf, int size);
    /**
     * OPTIONAL:<br>
     * lame_encode_flush_nogap will flush the internal mp3 buffers and pad<br>
     * the last frame with ancillary data so it is a complete mp3 frame.<br>
     * <br>
     * 'mp3buf' should be at least 7200 bytes long<br>
     * to hold all possible emitted data.<br>
     * * After a call to this routine, the outputed mp3 data is complete, but<br>
     * you may continue to encode new PCM samples and write future mp3 data<br>
     * to a different file.  The two mp3 files will play back with no gaps<br>
     * if they are concatenated together.<br>
     * * This routine will NOT write id3v1 tags into the bitstream.<br>
     * * return code = number of bytes output to mp3buf. Can be 0<br>
     * Original signature : <code>int lame_encode_flush_nogap(lame_global_flags*, unsigned char*, int)</code><br>
     * @param gfp global context handle<br>
     * @param mp3buf pointer to encoded MP3 stream<br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:801</i>
     */
    int lame_encode_flush_nogap(PointerByReference gfp, Pointer mp3buf, int size);
    /**
     * OPTIONAL:<br>
     * Normally, this is called by lame_init_params().  It writes id3v2 and<br>
     * Xing headers into the front of the bitstream, and sets frame counters<br>
     * and bitrate histogram data to 0.  You can also call this after <br>
     * lame_encode_flush_nogap().<br>
     * Original signature : <code>int lame_init_bitstream(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:813</i><br>
     * @deprecated use the safer method {@link #lame_init_bitstream(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_init_bitstream(Pointer gfp);
    /**
     * OPTIONAL:<br>
     * Normally, this is called by lame_init_params().  It writes id3v2 and<br>
     * Xing headers into the front of the bitstream, and sets frame counters<br>
     * and bitrate histogram data to 0.  You can also call this after <br>
     * lame_encode_flush_nogap().<br>
     * Original signature : <code>int lame_init_bitstream(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:813</i>
     */
    int lame_init_bitstream(PointerByReference gfp);
    /**
     * Original signature : <code>void lame_bitrate_hist(const const lame_global_flags*, int[14])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:832</i><br>
     * @deprecated use the safer methods {@link #lame_bitrate_hist(com.sun.jna.ptr.PointerByReference, java.nio.IntBuffer)} and {@link #lame_bitrate_hist(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    void lame_bitrate_hist(Pointer gfp, IntByReference bitrate_count);
    /**
     * Original signature : <code>void lame_bitrate_hist(const const lame_global_flags*, int[14])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:832</i>
     */
    void lame_bitrate_hist(PointerByReference gfp, IntBuffer bitrate_count);
    /**
     * Original signature : <code>void lame_bitrate_hist(const const lame_global_flags*, int[14])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:832</i>
     */
    void lame_bitrate_hist(PointerByReference gfp, IntByReference bitrate_count);
    /**
     * Original signature : <code>void lame_bitrate_kbps(const const lame_global_flags*, int[14])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:835</i><br>
     * @deprecated use the safer methods {@link #lame_bitrate_kbps(com.sun.jna.ptr.PointerByReference, java.nio.IntBuffer)} and {@link #lame_bitrate_kbps(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    void lame_bitrate_kbps(Pointer gfp, IntByReference bitrate_kbps);
    /**
     * Original signature : <code>void lame_bitrate_kbps(const const lame_global_flags*, int[14])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:835</i>
     */
    void lame_bitrate_kbps(PointerByReference gfp, IntBuffer bitrate_kbps);
    /**
     * Original signature : <code>void lame_bitrate_kbps(const const lame_global_flags*, int[14])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:835</i>
     */
    void lame_bitrate_kbps(PointerByReference gfp, IntByReference bitrate_kbps);
    /**
     * Original signature : <code>void lame_stereo_mode_hist(const const lame_global_flags*, int[4])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:838</i><br>
     * @deprecated use the safer methods {@link #lame_stereo_mode_hist(com.sun.jna.ptr.PointerByReference, java.nio.IntBuffer)} and {@link #lame_stereo_mode_hist(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    void lame_stereo_mode_hist(Pointer gfp, IntByReference stereo_mode_count);
    /**
     * Original signature : <code>void lame_stereo_mode_hist(const const lame_global_flags*, int[4])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:838</i>
     */
    void lame_stereo_mode_hist(PointerByReference gfp, IntBuffer stereo_mode_count);
    /**
     * Original signature : <code>void lame_stereo_mode_hist(const const lame_global_flags*, int[4])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:838</i>
     */
    void lame_stereo_mode_hist(PointerByReference gfp, IntByReference stereo_mode_count);
    /**
     * Original signature : <code>void lame_bitrate_stereo_mode_hist(const const lame_global_flags*, int[14][4])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:842</i><br>
     * @deprecated use the safer methods {@link #lame_bitrate_stereo_mode_hist(com.sun.jna.ptr.PointerByReference, java.nio.IntBuffer)} and {@link #lame_bitrate_stereo_mode_hist(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    void lame_bitrate_stereo_mode_hist(Pointer gfp, IntByReference bitrate_stmode_count);
    /**
     * Original signature : <code>void lame_bitrate_stereo_mode_hist(const const lame_global_flags*, int[14][4])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:842</i>
     */
    void lame_bitrate_stereo_mode_hist(PointerByReference gfp, IntBuffer bitrate_stmode_count);
    /**
     * Original signature : <code>void lame_bitrate_stereo_mode_hist(const const lame_global_flags*, int[14][4])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:842</i>
     */
    void lame_bitrate_stereo_mode_hist(PointerByReference gfp, IntByReference bitrate_stmode_count);
    /**
     * Original signature : <code>void lame_block_type_hist(const const lame_global_flags*, int[6])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:846</i><br>
     * @deprecated use the safer methods {@link #lame_block_type_hist(com.sun.jna.ptr.PointerByReference, java.nio.IntBuffer)} and {@link #lame_block_type_hist(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    void lame_block_type_hist(Pointer gfp, IntByReference btype_count);
    /**
     * Original signature : <code>void lame_block_type_hist(const const lame_global_flags*, int[6])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:846</i>
     */
    void lame_block_type_hist(PointerByReference gfp, IntBuffer btype_count);
    /**
     * Original signature : <code>void lame_block_type_hist(const const lame_global_flags*, int[6])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:846</i>
     */
    void lame_block_type_hist(PointerByReference gfp, IntByReference btype_count);
    /**
     * Original signature : <code>void lame_bitrate_block_type_hist(const const lame_global_flags*, int[14][6])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:850</i><br>
     * @deprecated use the safer methods {@link #lame_bitrate_block_type_hist(com.sun.jna.ptr.PointerByReference, java.nio.IntBuffer)} and {@link #lame_bitrate_block_type_hist(com.sun.jna.ptr.PointerByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    void lame_bitrate_block_type_hist(Pointer gfp, IntByReference bitrate_btype_count);
    /**
     * Original signature : <code>void lame_bitrate_block_type_hist(const const lame_global_flags*, int[14][6])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:850</i>
     */
    void lame_bitrate_block_type_hist(PointerByReference gfp, IntBuffer bitrate_btype_count);
    /**
     * Original signature : <code>void lame_bitrate_block_type_hist(const const lame_global_flags*, int[14][6])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:850</i>
     */
    void lame_bitrate_block_type_hist(PointerByReference gfp, IntByReference bitrate_btype_count);
    /**
     * OPTIONAL:<br>
     * lame_mp3_tags_fid will append a Xing VBR tag to the mp3 file with file<br>
     * pointer fid.  These calls perform forward and backwards seeks, so make<br>
     * sure fid is a real file.  Make sure lame_encode_flush has been called,<br>
     * and all mp3 data has been written to the file before calling this<br>
     * function.<br>
     * NOTE:<br>
     * if VBR  tags are turned off by the user, or turned off by LAME because<br>
     * the output is not a regular file, this call does nothing<br>
     * Original signature : <code>void lame_mp3_tags_fid(lame_global_flags*, FILE*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:866</i><br>
     * @deprecated use the safer method {@link #lame_mp3_tags_fid(com.sun.jna.ptr.PointerByReference, mp3lame.LibLame.FILE)} instead
     */
    //@Deprecated
    //void lame_mp3_tags_fid(Pointer lame_global_flagsPtr1, stdio.FILE fid);
    /**
     * OPTIONAL:<br>
     * lame_mp3_tags_fid will append a Xing VBR tag to the mp3 file with file<br>
     * pointer fid.  These calls perform forward and backwards seeks, so make<br>
     * sure fid is a real file.  Make sure lame_encode_flush has been called,<br>
     * and all mp3 data has been written to the file before calling this<br>
     * function.<br>
     * NOTE:<br>
     * if VBR  tags are turned off by the user, or turned off by LAME because<br>
     * the output is not a regular file, this call does nothing<br>
     * Original signature : <code>void lame_mp3_tags_fid(lame_global_flags*, FILE*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:866</i>
     */
    //void lame_mp3_tags_fid(PointerByReference lame_global_flagsPtr1, stdio.FILE fid);
    /**
     * REQUIRED:<br>
     * final call to free all remaining buffers<br>
     * Original signature : <code>int lame_close(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:873</i><br>
     * @deprecated use the safer method {@link #lame_close(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    int lame_close(Pointer lame_global_flagsPtr1);
    /**
     * REQUIRED:<br>
     * final call to free all remaining buffers<br>
     * Original signature : <code>int lame_close(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:873</i>
     */
    int lame_close(PointerByReference lame_global_flagsPtr1);
    /**
     * OBSOLETE:<br>
     * lame_encode_finish combines lame_encode_flush() and lame_close() in<br>
     * one call.  However, once this call is made, the statistics routines<br>
     * will no longer work because the data will have been cleared, and<br>
     * lame_mp3_tags_fid() cannot be called to add data to the VBR header<br>
     * Original signature : <code>int lame_encode_finish(lame_global_flags*, unsigned char*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:882</i><br>
     * @deprecated use the safer methods {@link #lame_encode_finish(com.sun.jna.ptr.PointerByReference, java.nio.ByteBuffer, int)} and {@link #lame_encode_finish(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer, int)} instead
     */
    @Deprecated
    int lame_encode_finish(Pointer gfp, Pointer mp3buf, int size);
    /**
     * OBSOLETE:<br>
     * lame_encode_finish combines lame_encode_flush() and lame_close() in<br>
     * one call.  However, once this call is made, the statistics routines<br>
     * will no longer work because the data will have been cleared, and<br>
     * lame_mp3_tags_fid() cannot be called to add data to the VBR header<br>
     * Original signature : <code>int lame_encode_finish(lame_global_flags*, unsigned char*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:882</i>
     */
    int lame_encode_finish(PointerByReference gfp, ByteBuffer mp3buf, int size);
    /**
     * OBSOLETE:<br>
     * lame_encode_finish combines lame_encode_flush() and lame_close() in<br>
     * one call.  However, once this call is made, the statistics routines<br>
     * will no longer work because the data will have been cleared, and<br>
     * lame_mp3_tags_fid() cannot be called to add data to the VBR header<br>
     * Original signature : <code>int lame_encode_finish(lame_global_flags*, unsigned char*, int)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:882</i>
     */
    int lame_encode_finish(PointerByReference gfp, Pointer mp3buf, int size);
    /**
     * required call to initialize decoder <br>
     * NOTE: the decoder should not be used when encoding is performed<br>
     * with decoding on the fly<br>
     * Original signature : <code>int lame_decode_init()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:923</i>
     */
    int lame_decode_init();
    /**
     * input 1 mp3 frame, output (maybe) pcm data.  <br>
     * *  nout = lame_decode(mp3buf,len,pcm_l,pcm_r);<br>
     * * input:  <br>
     *    len          :  number of bytes of mp3 data in mp3buf<br>
     *    mp3buf[len]  :  mp3 data to be decoded<br>
     * * output:<br>
     *    nout:  -1    : decoding error<br>
     *            0    : need more data before we can complete the decode <br>
     *           >0    : returned 'nout' samples worth of data in pcm_l,pcm_r<br>
     *    pcm_l[nout]  : left channel data<br>
     *    pcm_r[nout]  : right channel data <br>
     *    <br>
     * *******************************************************************<br>
     * Original signature : <code>int lame_decode(unsigned char*, int, short[], short[])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:942</i><br>
     * @deprecated use the safer methods {@link #lame_decode(java.nio.ByteBuffer, int, java.nio.ShortBuffer, java.nio.ShortBuffer)} and {@link #lame_decode(com.sun.jna.Pointer, int, com.sun.jna.ptr.ShortByReference, com.sun.jna.ptr.ShortByReference)} instead
     */
    @Deprecated
    int lame_decode(Pointer mp3buf, int len, ShortByReference pcm_l, ShortByReference pcm_r);
    /**
     * input 1 mp3 frame, output (maybe) pcm data.  <br>
     * *  nout = lame_decode(mp3buf,len,pcm_l,pcm_r);<br>
     * * input:  <br>
     *    len          :  number of bytes of mp3 data in mp3buf<br>
     *    mp3buf[len]  :  mp3 data to be decoded<br>
     * * output:<br>
     *    nout:  -1    : decoding error<br>
     *            0    : need more data before we can complete the decode <br>
     *           >0    : returned 'nout' samples worth of data in pcm_l,pcm_r<br>
     *    pcm_l[nout]  : left channel data<br>
     *    pcm_r[nout]  : right channel data <br>
     *    <br>
     * *******************************************************************<br>
     * Original signature : <code>int lame_decode(unsigned char*, int, short[], short[])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:942</i>
     */
    int lame_decode(ByteBuffer mp3buf, int len, ShortBuffer pcm_l, ShortBuffer pcm_r);
    /**
     * same as lame_decode, and also returns mp3 header data<br>
     * Original signature : <code>int lame_decode_headers(unsigned char*, int, short[], short[], mp3data_struct*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:949</i><br>
     * @deprecated use the safer methods {@link #lame_decode_headers(java.nio.ByteBuffer, int, java.nio.ShortBuffer, java.nio.ShortBuffer, mp3lame.LibLame.mp3data_struct)} and {@link #lame_decode_headers(com.sun.jna.Pointer, int, com.sun.jna.ptr.ShortByReference, com.sun.jna.ptr.ShortByReference, mp3lame.LibLame.mp3data_struct)} instead
     */
    @Deprecated
    int lame_decode_headers(Pointer mp3buf, int len, ShortByReference pcm_l, ShortByReference pcm_r, LibLame.mp3data_struct mp3data);
    /**
     * same as lame_decode, and also returns mp3 header data<br>
     * Original signature : <code>int lame_decode_headers(unsigned char*, int, short[], short[], mp3data_struct*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:949</i>
     */
    int lame_decode_headers(ByteBuffer mp3buf, int len, ShortBuffer pcm_l, ShortBuffer pcm_r, LibLame.mp3data_struct mp3data);
    /**
     * same as lame_decode, but returns at most one frame<br>
     * Original signature : <code>int lame_decode1(unsigned char*, int, short[], short[])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:957</i><br>
     * @deprecated use the safer methods {@link #lame_decode1(java.nio.ByteBuffer, int, java.nio.ShortBuffer, java.nio.ShortBuffer)} and {@link #lame_decode1(com.sun.jna.Pointer, int, com.sun.jna.ptr.ShortByReference, com.sun.jna.ptr.ShortByReference)} instead
     */
    @Deprecated
    int lame_decode1(Pointer mp3buf, int len, ShortByReference pcm_l, ShortByReference pcm_r);
    /**
     * same as lame_decode, but returns at most one frame<br>
     * Original signature : <code>int lame_decode1(unsigned char*, int, short[], short[])</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:957</i>
     */
    int lame_decode1(ByteBuffer mp3buf, int len, ShortBuffer pcm_l, ShortBuffer pcm_r);
    /**
     * same as lame_decode1, but returns at most one frame and mp3 header data<br>
     * Original signature : <code>int lame_decode1_headers(unsigned char*, int, short[], short[], mp3data_struct*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:964</i><br>
     * @deprecated use the safer methods {@link #lame_decode1_headers(java.nio.ByteBuffer, int, java.nio.ShortBuffer, java.nio.ShortBuffer, mp3lame.LibLame.mp3data_struct)} and {@link #lame_decode1_headers(com.sun.jna.Pointer, int, com.sun.jna.ptr.ShortByReference, com.sun.jna.ptr.ShortByReference, mp3lame.LibLame.mp3data_struct)} instead
     */
    @Deprecated
    int lame_decode1_headers(Pointer mp3buf, int len, ShortByReference pcm_l, ShortByReference pcm_r, LibLame.mp3data_struct mp3data);
    /**
     * same as lame_decode1, but returns at most one frame and mp3 header data<br>
     * Original signature : <code>int lame_decode1_headers(unsigned char*, int, short[], short[], mp3data_struct*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:964</i>
     */
    int lame_decode1_headers(ByteBuffer mp3buf, int len, ShortBuffer pcm_l, ShortBuffer pcm_r, LibLame.mp3data_struct mp3data);
    /**
     * same as lame_decode1_headers, but also returns enc_delay and enc_padding<br>
     * from VBR Info tag, (-1 if no info tag was found)<br>
     * Original signature : <code>int lame_decode1_headersB(unsigned char*, int, short[], short[], mp3data_struct*, int*, int*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:973</i><br>
     * @deprecated use the safer methods {@link #lame_decode1_headersB(java.nio.ByteBuffer, int, java.nio.ShortBuffer, java.nio.ShortBuffer, mp3lame.LibLame.mp3data_struct, java.nio.IntBuffer, java.nio.IntBuffer)} and {@link #lame_decode1_headersB(com.sun.jna.Pointer, int, com.sun.jna.ptr.ShortByReference, com.sun.jna.ptr.ShortByReference, mp3lame.LibLame.mp3data_struct, com.sun.jna.ptr.IntByReference, com.sun.jna.ptr.IntByReference)} instead
     */
    @Deprecated
    int lame_decode1_headersB(Pointer mp3buf, int len, ShortByReference pcm_l, ShortByReference pcm_r, LibLame.mp3data_struct mp3data, IntByReference enc_delay, IntByReference enc_padding);
    /**
     * same as lame_decode1_headers, but also returns enc_delay and enc_padding<br>
     * from VBR Info tag, (-1 if no info tag was found)<br>
     * Original signature : <code>int lame_decode1_headersB(unsigned char*, int, short[], short[], mp3data_struct*, int*, int*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:973</i>
     */
    int lame_decode1_headersB(ByteBuffer mp3buf, int len, ShortBuffer pcm_l, ShortBuffer pcm_r, LibLame.mp3data_struct mp3data, IntBuffer enc_delay, IntBuffer enc_padding);
    /**
     * cleanup call to exit decoder<br>
     * Original signature : <code>int lame_decode_exit()</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:984</i>
     */
    int lame_decode_exit();
    /**
     * utility to obtain alphabetically sorted list of genre names with numbers<br>
     * Original signature : <code>void id3tag_genre_list(id3tag_genre_list_handler_callback*, void*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1015</i>
     */
    void id3tag_genre_list(LibLame.id3tag_genre_list_handler_callback handler, Pointer cookie);
    /**
     * Original signature : <code>void id3tag_init(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1019</i><br>
     * @deprecated use the safer method {@link #id3tag_init(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void id3tag_init(Pointer gfp);
    /**
     * Original signature : <code>void id3tag_init(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1019</i>
     */
    void id3tag_init(PointerByReference gfp);
    /**
     * force addition of version 2 tag<br>
     * Original signature : <code>void id3tag_add_v2(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1022</i><br>
     * @deprecated use the safer method {@link #id3tag_add_v2(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void id3tag_add_v2(Pointer gfp);
    /**
     * force addition of version 2 tag<br>
     * Original signature : <code>void id3tag_add_v2(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1022</i>
     */
    void id3tag_add_v2(PointerByReference gfp);
    /**
     * add only a version 1 tag<br>
     * Original signature : <code>void id3tag_v1_only(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1025</i><br>
     * @deprecated use the safer method {@link #id3tag_v1_only(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void id3tag_v1_only(Pointer gfp);
    /**
     * add only a version 1 tag<br>
     * Original signature : <code>void id3tag_v1_only(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1025</i>
     */
    void id3tag_v1_only(PointerByReference gfp);
    /**
     * add only a version 2 tag<br>
     * Original signature : <code>void id3tag_v2_only(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1028</i><br>
     * @deprecated use the safer method {@link #id3tag_v2_only(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void id3tag_v2_only(Pointer gfp);
    /**
     * add only a version 2 tag<br>
     * Original signature : <code>void id3tag_v2_only(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1028</i>
     */
    void id3tag_v2_only(PointerByReference gfp);
    /**
     * pad version 1 tag with spaces instead of nulls<br>
     * Original signature : <code>void id3tag_space_v1(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1031</i><br>
     * @deprecated use the safer method {@link #id3tag_space_v1(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void id3tag_space_v1(Pointer gfp);
    /**
     * pad version 1 tag with spaces instead of nulls<br>
     * Original signature : <code>void id3tag_space_v1(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1031</i>
     */
    void id3tag_space_v1(PointerByReference gfp);
    /**
     * pad version 2 tag with extra 128 bytes<br>
     * Original signature : <code>void id3tag_pad_v2(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1034</i><br>
     * @deprecated use the safer method {@link #id3tag_pad_v2(com.sun.jna.ptr.PointerByReference)} instead
     */
    @Deprecated
    void id3tag_pad_v2(Pointer gfp);
    /**
     * pad version 2 tag with extra 128 bytes<br>
     * Original signature : <code>void id3tag_pad_v2(lame_global_flags*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1034</i>
     */
    void id3tag_pad_v2(PointerByReference gfp);
    /**
     * Original signature : <code>void id3tag_set_title(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1036</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_title(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_title(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    void id3tag_set_title(Pointer gfp, Pointer title);
    /**
     * Original signature : <code>void id3tag_set_title(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1036</i>
     */
    void id3tag_set_title(PointerByReference gfp, String title);
    /**
     * Original signature : <code>void id3tag_set_title(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1036</i>
     */
    void id3tag_set_title(PointerByReference gfp, Pointer title);
    /**
     * Original signature : <code>void id3tag_set_artist(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1039</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_artist(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_artist(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    void id3tag_set_artist(Pointer gfp, Pointer artist);
    /**
     * Original signature : <code>void id3tag_set_artist(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1039</i>
     */
    void id3tag_set_artist(PointerByReference gfp, String artist);
    /**
     * Original signature : <code>void id3tag_set_artist(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1039</i>
     */
    void id3tag_set_artist(PointerByReference gfp, Pointer artist);
    /**
     * Original signature : <code>void id3tag_set_album(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1042</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_album(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_album(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    void id3tag_set_album(Pointer gfp, Pointer album);
    /**
     * Original signature : <code>void id3tag_set_album(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1042</i>
     */
    void id3tag_set_album(PointerByReference gfp, String album);
    /**
     * Original signature : <code>void id3tag_set_album(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1042</i>
     */
    void id3tag_set_album(PointerByReference gfp, Pointer album);
    /**
     * Original signature : <code>void id3tag_set_year(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1045</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_year(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_year(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    void id3tag_set_year(Pointer gfp, Pointer year);
    /**
     * Original signature : <code>void id3tag_set_year(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1045</i>
     */
    void id3tag_set_year(PointerByReference gfp, String year);
    /**
     * Original signature : <code>void id3tag_set_year(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1045</i>
     */
    void id3tag_set_year(PointerByReference gfp, Pointer year);
    /**
     * Original signature : <code>void id3tag_set_comment(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1048</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_comment(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_comment(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    void id3tag_set_comment(Pointer gfp, Pointer comment);
    /**
     * Original signature : <code>void id3tag_set_comment(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1048</i>
     */
    void id3tag_set_comment(PointerByReference gfp, String comment);
    /**
     * Original signature : <code>void id3tag_set_comment(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1048</i>
     */
    void id3tag_set_comment(PointerByReference gfp, Pointer comment);
    /**
     * Original signature : <code>void id3tag_set_track(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1051</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_track(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_track(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    void id3tag_set_track(Pointer gfp, Pointer track);
    /**
     * Original signature : <code>void id3tag_set_track(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1051</i>
     */
    void id3tag_set_track(PointerByReference gfp, String track);
    /**
     * Original signature : <code>void id3tag_set_track(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1051</i>
     */
    void id3tag_set_track(PointerByReference gfp, Pointer track);
    /**
     * return non-zero result if genre name or number is invalid<br>
     * Original signature : <code>int id3tag_set_genre(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1056</i><br>
     * @deprecated use the safer methods {@link #id3tag_set_genre(com.sun.jna.ptr.PointerByReference, java.lang.String)} and {@link #id3tag_set_genre(com.sun.jna.ptr.PointerByReference, com.sun.jna.Pointer)} instead
     */
    @Deprecated
    int id3tag_set_genre(Pointer gfp, Pointer genre);
    /**
     * return non-zero result if genre name or number is invalid<br>
     * Original signature : <code>int id3tag_set_genre(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1056</i>
     */
    int id3tag_set_genre(PointerByReference gfp, String genre);
    /**
     * return non-zero result if genre name or number is invalid<br>
     * Original signature : <code>int id3tag_set_genre(lame_global_flags*, const char*)</code><br>
     * <i>native declaration : /usr/include/c++/4.2.1/tr1/stdarg.h:1056</i>
     */
    int id3tag_set_genre(PointerByReference gfp, Pointer genre);

    PointerByReference hip_decode_init();
    int hip_decode_exit(PointerByReference tag);
    int hip_decode_frame(PointerByReference ctx, ByteBuffer buf, int bufferSize, short[] left_buf, short[] right_buf, mp3data_struct mp3data);
    int hip_decode1_headersB(PointerByReference ctx, ByteBuffer mp3_buf, int bufferSize, short[] left_buf, short[] right_buf, mp3data_struct mp3data, IntByReference enc_delay, IntByReference enc_padding);
    int hip_decode1_headers(PointerByReference ctx, ByteBuffer mp3_buf, int bufferSize, short[] left_buf, short[] right_buf, mp3data_struct mp3data);

    int lame_get_bitrate(int mpeg_version, int table_index); //line 1290
    int lame_get_samplerate(int mpeg_version, int table_index); //line 1291
}
