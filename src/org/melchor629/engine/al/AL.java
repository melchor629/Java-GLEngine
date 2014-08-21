package org.melchor629.engine.al;

/**
 * OpenAL interface for implement in bindings.
 * http://grva.lamce.coppe.ufrj.br/labcogsdk/download/extras/openal/OpenAL_Programmers_Guide.pdf
 * @author melchor9000
 */
public interface AL {
    enum Listener {
        GAIN (0x100A),
        POSITION (0x1004),
        VELOCITY (0x1006),
        ORIENTATION (0x100F);

        final int e;
        Listener(int t) { e = t; }
    }

    enum Buffer {
        FREQUENCY (0x2001),
        BITS (0x2002),
        CHANNELS (0x2003),
        SIZE (0x2004),
        @Deprecated
        DATA (0x2005);

        final int e;
        Buffer(int t) { e = t; }
    }

    enum Source {
        PITCH (0x1003),
        GAIN (0x100A),
        MAX_DISTANCE (0x1023),
        ROLLOFF_FACTOR (0x1021),
        REFERENCE_DISTANCE (0x1020),
        MIN_GAIN (0x100D),
        MAX_GAIN(0x100E),
        CONE_OUTER_GAIN (0x1022),
        CONE_INNER_ANGLE (0x1001),
        CONE_OUTER_ANGLE (0x1002),
        POSITION (0x1004),
        VELOCITY (0x1006),
        DIRECTION (0x1005),
        SOURCE_ABSOLUTE (0x201),
        SOURCE_RELATIVE (0x202),
        SOURCE_TYPE (0x1027),
        SOURCE_STATE (0x1010),
        LOOPING (0x1007),
        BUFFER (0x1009),
        BUFFERS_QUEUED (0x1015),
        BUFFERS_PROCESSED (0x1016),
        SEC_OFFSET (0x1024),
        SAMPLE_OFFSET (0x1025),
        BYTE_OFFSET (0x1026);

        final int e;
        Source(int t) { e = t; }
    }

    enum Error {
        NO_ERROR (0x0),
        INVALID_NAME (0xA001),
        INVALID_ENUM (0xA002),
        INVALID_VALUE (0xA003),
        INVALID_OPERATION (0xA004),
        OUT_OF_MEMORY (0xA005);

        final int e;
        Error(int t) { e = t; }
    }

    enum Get {
        DOPPLER_FACTOR (0xC000),
        DOPPLER_SPEED (0xC001),
        SPEED_OF_SOUND (0xC003),
        DISTANCE_MODEL (0xD000);

        final int e;
        Get(int t) { e = t; }
    }

    enum Format {
        MONO8 (0x0),
        MONO16 (0x0),
        STEREO8 (0x0),
        STEREO16 (0x0);

        final int e;
        Format(int t) { e = t; }
    }

    void createContext();
    //TODO Dispositivos

    /**
     * This function fills a buffer with audio data. All the pre-defined formats are
     * PCM data, but this function may be used by extensions to load other data
     * types as well.
     * @param buffer Buffer to be filled
     * @param format Format of the data
     * @param data Array with the data
     * @param freq Frequency of the sound
     */
    void alBufferData(int buffer, Format format, byte[] data, int freq);

    /**
     * This function fills a buffer with audio data. All the pre-defined formats are
     * PCM data, but this function may be used by extensions to load other data
     * types as well.
     * @param buffer Buffer to be filled
     * @param format Format of the data
     * @param data Array with the data
     * @param freq Frequency of the sound
     */
    void alBufferData(int buffer, Format format, short[] data, int freq);

    /**
     * This function fills a buffer with audio data. All the pre-defined formats are
     * PCM data, but this function may be used by extensions to load other data
     * types as well.
     * @param buffer Buffer to be filled
     * @param format Format of the data
     * @param data Array with the data
     * @param freq Frequency of the sound
     */
    void alBufferData(int buffer, Format format, int[] data, int freq);
    
    /**
     * This function deletes one buffer, freeing the resources used
     * by the buffer. Buffers which are attached to a source can not be deleted.
     * See {@link #alSourcei(int, Source, int)} and {@link #alSourceUnqueueBuffers(int, int[])}
     * for information on how to detach a buffer from a source.
     * @param buffer Buffer to be deleted
     */
    void alDeleteBuffer(int buffer);
    
    /**
     * This function deletes one or more buffers, freeing the resources used
     * by the buffer. Buffers which are attached to a source can not be deleted.
     * See {@link #alSourcei(int, Source, int)} and {@link #alSourceUnqueueBuffers(int, int[])}
     * for information on how to detach a buffer from a source.
     * @param buffers Array with references to buffers to be deleted
     */
    void alDeleteBuffers(int[] buffers);

    /**
     * This function deletes one source.
     * @param source Source reference to be deleted
     */
    void alDeleteSource(int source);

    /**
     * This function deletes one or more sources.
     * @param sources array with all source references to be deleted
     */
    void alDeleteSources(int[] sources);
    void alDisable(int cap);
    void alDistanceModel(int value);
    void alDopplerFactor(float factor);
    void alDopplerVelocity(float speed);
    void alEnable(int cap);

    /**
     * This function generates one buffer, which contain audio data.
     * @return Reference to the buffer
     */
    int alGenBuffer();

    /**
     * This function generates one or more buffers, which contain audio data.
     * @param buffers Int array where will be references to buffers
     */
    void alGenBuffers(int[] buffers);

    /**
     * This function generates one source
     * @return Reference to the source
     */
    int alGenSource();

    /**
     * This function generates one or more sources
     * @param sources array which will store the references of new sources
     */
    void alGenSources(int[] sources);
    boolean alGetBoolean(Get g);

    /**
     * This function retrieves a floating point property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being retrived
     * @param pname The attribute to retrieve
     * @return value for the attribute
     */
    float alGetBufferf(int buffer, Buffer pname);

    /**
     * This function retrieves a integer property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being retrived
     * @param pname The attribute to retrieve
     * @return value for attribute
     */
    int alGetBufferi(int buffer, Buffer pname);

    double alGetDouble(Get cap);
    void alGetDouble(Get cap, double[] doubles);
    void alGetEnumValue(String ename);
    Error alGetError();
    float alGetFloat(Get pname);
    void alGetFloat(Get pname, float[] floats);
    int alGetInteger(Get pname);
    void alGetInteger(Get pname, int[] integers);

    /**
     * Retrieves a floating point-vector property of the listener. Can retrieve
     * values of params POSITION, VELOCITY and ORIENTATION.
     * @param pname Parameter to retrieve
     * @param floats array where the values will be set
     */
    void alGetListener(Get pname, float[] floats);

    /**
     * Retrieves a integer-vector property of the listener. Can retrieve
     * values of params POSITION, VELOCITY and ORIENTATION.
     * @param pname Parameter to retrieve
     * @param ints array where the values will be set
     */
    void alGetListener(Get pname, int[] ints);

    /**
     * Retrieves a floating point property of the listener. Only can
     * retrieve value of parameter GAIN.
     * @param pname Parameter to retrieve
     * @return value of the parameter
     */
    float alGetListenerf(Listener pname);

    /**
     * Retrieves an integer property of the listener. Actually, nothing can
     * be retrieved.
     * @param pname Parameter to retrieve
     * @return value of the parameter
     */
    int alGetListeneri(Listener pname);

    /**
     * Retrieves some values into a floating point array.  Valid
     * attributes for this function are:<br>
     * &nbsp;&nbsp;- POSITION<br>
     * &nbsp;&nbsp;- VELOCITY<br>
     * &nbsp;&nbsp;- DIRECTION<br>
     * @param source Source whose attribute is being retrieved
     * @param pname The attribute to retrieve
     * @param floats array where the values will be stored
     */
    void alGetSource(int source, Source pname, float[] floats);

    /**
     * Retrieves some values into a integer array.  Valid
     * attributes for this function are:<br>
     * &nbsp;&nbsp;- POSITION<br>
     * &nbsp;&nbsp;- VELOCITY<br>
     * &nbsp;&nbsp;- DIRECTION<br>
     * @param source Source whose attribute is being retrieved
     * @param pname The attribute to retrieve
     * @param integers array where the values will be stored
     */
    void alGetSource(int source, Source pname, int[] integers);

    /**
     * Retrieves a floating point property of a source. Valid attributes for this
     * function are:<br>
     * &nbsp;&nbsp;- PITCH <br>
     * &nbsp;&nbsp;- GAIN <br>
     * &nbsp;&nbsp;- MIN_GAIN <br>
     * &nbsp;&nbsp;- MAX_GAIN <br>
     * &nbsp;&nbsp;- MAX_DISTANCE <br>
     * &nbsp;&nbsp;- ROLLOFF_FACTOR <br>
     * &nbsp;&nbsp;- CONE_OUTER_GAIN <br>
     * &nbsp;&nbsp;- CONE_INNER_ANGLE <br>
     * &nbsp;&nbsp;- CONE_OUTER_ANGLE <br>
     * &nbsp;&nbsp;- REFERENCE_DISTANCE <br>
     * @param source Source whose attribute is being retrieved
     * @param pname The attribute to retrieve
     * @return value for attribute
     */
    float alGetSourcef(int source, Source pname);

    /**
     * Retrieves a integer value of a source. Valid attributes for this function
     * are:<br>
     * &nbsp;&nbsp;- SOURCE_RELATIVE<br>
     * &nbsp;&nbsp;- CONE_INNER_ANGLE<br>
     * &nbsp;&nbsp;- CONE_OUTER_ANGLE<br>
     * &nbsp;&nbsp;- LOOPING<br>
     * &nbsp;&nbsp;- BUFFER<br>
     * &nbsp;&nbsp;- SOURCE_STATE<br>
     * @param source Source whose attribute is being retrieved
     * @param pname The attribute to retrieve
     * @return value for the attribute
     */
    int alGetSourcei(int source, Source pname);
    String alGetString(int pname);

    /**
     * This function tests if the object is a buffer and is a
     * valid one
     * @param obj Reference to something
     * @return True if is a buffer, false otherwise
     */
    boolean alIsBuffer(int obj);
    boolean alIsEnabled(int obj);
    boolean alIsExtensionPresent(String obj);

    /**
     * This function tests if an object is a source and is a
     * valid one.
     * @param obj Reference to something
     * @return True if is a source, false otherwise
     */
    boolean alIsSource(int obj);

    /**
     * Sets a floating point-vector property of the listener. Valid
     * params are POSITION, VELOCITY and ORIENTATION.
     * @param pname Property to change
     * @param data Values to be set
     */
    void alListener(Listener pname, float[] data);

    /**
     * Sets a integer-vector property of the listener. Valid
     * params are POSITION, VELOCITY and ORIENTATION.
     * @param pname Property to change
     * @param data Values to be set
     */
    void alListener(Listener pname, int[] data);

    /**
     * Sets a floating point property for the listener. Valid Params
     * are VELOCITY and POSITION
     * @param pname Property to change
     * @param x Value
     * @param y Value
     * @param z Value
     */
    void alListener3f(Listener pname, float x, float y, float z);

    /**
     * Sets a floating point property for the listener. Only can be changed
     * GAIN.
     * @param pname Param to change
     * @param v Value to be set
     */
    void alListenerf(Listener pname, float v);

    /**
     * Sets an intefer property for the listener. Actually there's no
     * params to be set with ints.
     * @param pname Param to change
     * @param v Value to be set
     */
    void alListeneri(Listener pname, int v);

    /**
     * Sets a source property requiring an array of floating point values.
     * Valid attributes for this function are:<br>
     * &nbsp;&nbsp;- POSITION<br>
     * &nbsp;&nbsp;- VELOCITY<br>
     * &nbsp;&nbsp;- DIRECTION<br>
     * @param source Source reference whose attribute is being set
     * @param pname Attribute to change
     * @param data array with values
     * @see AL.Source Source attributes enum
     */
    void alSource(int source, Source pname, float[] data);

    /**
     * Sets a source property requiring three floating point values. Valid
     * attributes for this function are:<br>
     * &nbsp;&nbsp;- POSITION<br>
     * &nbsp;&nbsp;- VELOCITY<br>
     * &nbsp;&nbsp;- DIRECTION<br>
     * @param source Source reference whose attribute is being set
     * @param pname Attribute to change
     * @param x Value
     * @param y Value
     * @param z Value
     * @see AL.Source Source attributes enum
     */
    void alSource3f(int source, Source pname, float x, float y, float z);

    /**
     * Sets a floating point property of a source. Valid attributes for this
     * function are:<br>
     * &nbsp;&nbsp;- PITCH <br>
     * &nbsp;&nbsp;- GAIN <br>
     * &nbsp;&nbsp;- MIN_GAIN <br>
     * &nbsp;&nbsp;- MAX_GAIN <br>
     * &nbsp;&nbsp;- MAX_DISTANCE <br>
     * &nbsp;&nbsp;- ROLLOFF_FACTOR <br>
     * &nbsp;&nbsp;- CONE_OUTER_GAIN <br>
     * &nbsp;&nbsp;- CONE_INNER_ANGLE <br>
     * &nbsp;&nbsp;- CONE_OUTER_ANGLE <br>
     * &nbsp;&nbsp;- REFERENCE_DISTANCE <br>
     * @param source Source reference whose attribute is being set
     * @param pname Attribute to change
     * @param v Value to be set
     * @see AL.Source Source attributes enum
     */
    void alSourcef(int source, Source pname, float v);

    /**
     * Sets an integer property of a source. Valid attributes for this function
     * are:<br>
     * &nbsp;&nbsp;- SOURCE_RELATIVE<br>
     * &nbsp;&nbsp;- CONE_INNER_ANGLE<br>
     * &nbsp;&nbsp;- CONE_OUTER_ANGLE<br>
     * &nbsp;&nbsp;- LOOPING<br>
     * &nbsp;&nbsp;- BUFFER<br>
     * &nbsp;&nbsp;- SOURCE_STATE<br>
     * @param source Source reference whose attribute is being set
     * @param pname Attribute to change
     * @param v Value to be set
     * @see AL.source Source attributes enum
     */
    void alSourcei(int source, Source pname, int v);

    /**
     * Puases the source
     * @param source Reference to the source
     */
    void alSourcePause(int source);

    /**
     * Pauses the sources
     * @param sources array with the resources
     */
    void alSourcePause(int[] sources);

    /**
     * Plays the source
     * @param source Reference to the source
     */
    void alSourcePlay(int source);

    /**
     * Plays the sources
     * @param sources array with the sources to play
     */
    void alSourcePlay(int[] sources);

    /**
     * This function queues one buffers on a source. All buffers
     * attached to a source will be played in sequence, and the number
     * of processed buffers can be detected using an
     * {@link #alGetSourcei(int, Source)} call to retrieve AL_BUFFERS_PROCESSED.
     * @param source Reference to the source
     * @param buffer buffer to attach
     */
    void alSourceQueueBuffers(int source, int buffer);

    /**
     * This function queues a set of buffers on a source. All buffers
     * attached to a source will be played in sequence, and the number
     * of processed buffers can be detected using an
     * {@link #alGetSourcei(int, Source)} call to retrieve AL_BUFFERS_PROCESSED.
     * @param source Reference to the source
     * @param buffer array of buffers to attach
     */
    void alSourceQueueBuffers(int source, int[] buffers);

    /**
     * Stops the source and sets the position to initial
     * @param source Source
     */
    void alSourceRewind(int source);

    /**
     * Stops the sources and sets the position to initial
     * @param sources array with sources
     */
    void alSourceRewind(int[] sources);

    /**
     * Stops the source
     * @param source Reference to the source
     */
    void alSourceStop(int source);

    /**
     * Stops the resources
     * @param sources arry with all sources
     */
    void alSourceStop(int[] sources);

    /**
     * Unqueues one buffer attached to a source.
     * @param source Reference to the source
     * @param buffer buffer to deattach
     */
    void alSourceUnqueueBuffers(int source, int buffer);

    /**
     * Unqueues a set of buffers attached to a source.
     * @param source Reference to the source
     * @param buffers buffers to deattach
     */
    void alSourceUnqueueBuffers(int source, int[] buffers);

    /**
     * This function sets floating points property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being set
     * @param pname the attribute to set
     * @param floats values
     */
    void alBuffer(int buffer, Buffer pname, float[] floats);

    /**
     * This function sets integers property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being set
     * @param pname the attribute to set
     * @param integers values
     */
    void alBuffer(int buffer, Buffer pname, int[] integers);

    /**
     * This function sets a floating point property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being set
     * @param pname the attribute to set
     * @param v0 value to be set
     * @param v1 value to be set
     * @param v2 value to be set
     */
    void alBuffer3f(int buffer, Buffer pname, float v0, float v1, float v2);

    /**
     * This function sets a integer property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being set
     * @param pname the attribute to set
     * @param v0 value to be set
     * @param v1 value to be set
     * @param v2 value to be set
     */
    void alBuffer3i(int buffer, Buffer pname, int v0, int v1, int v2);

    /**
     * This function sets a floating point property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being set
     * @param pname the attribute to set
     * @param v value to be set
     */
    void alBufferf(int buffer, Buffer pname, float v);

    /**
     * This function sets a floating point property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being set
     * @param pname the attribute to set
     * @param v value to be set
     */
    void alBufferi(int buffer, Buffer pname, int v);

    /**
     * This function retrieves a floating point property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being retrived
     * @param pname The attribute to retrieve
     * @param floats values in an array
     */
    void alGetBuffer(int buffer, Buffer pname, float[] floats);

    /**
     * This function retrieves a integer property of a buffer.<br>
     * There are no relevant buffer properties defined in OpenAL 1.1 which
     * can be affected by this call, but this function may be used by OpenAL extensions.
     * @param buffer Buffer whose attribute is being retrived
     * @param pname The attribute to retrieve
     * @param floats values in an array
     */
    void alGetBuffer(int buffer, Buffer pname, int[] integers);
    void alGetListeneri(int listener, int[] integers);

    /**
     * Sets a integer property for the listener. Valid Params
     * are VELOCITY and POSITION
     * @param pname Property to change
     * @param x Value
     * @param y Value
     * @param z Value
     */
    void alListener3i(int listener, int v0, int v1, int v2);

    /**
     * Sets a source property requiring an array of integers values. Valid
     * attributes for this function are:<br>
     * &nbsp;&nbsp;- POSITION<br>
     * &nbsp;&nbsp;- VELOCITY<br>
     * &nbsp;&nbsp;- DIRECTION<br>
     * @param source Source reference whose attribute is being set
     * @param pname Attribute to change
     * @param integers Array with the values
     * @see AL.Source Source attributes enum
     */
    void alSource(int source, Source pname, int[] integers);

    /**
     * Sets a source property requiring three integers values. Valid
     * attributes for this function are:<br>
     * &nbsp;&nbsp;- POSITION<br>
     * &nbsp;&nbsp;- VELOCITY<br>
     * &nbsp;&nbsp;- DIRECTION<br>
     * @param source Source reference whose attribute is being set
     * @param pname Attribute to change
     * @param x Value
     * @param y Value
     * @param z Value
     * @see AL.Source Source attributes enum
     */
    void alSource3i(int source, Source pname, int v0, int v1, int v2);
    void alSpeedOfSound(float speed);
}
