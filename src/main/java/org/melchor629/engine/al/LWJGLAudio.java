package org.melchor629.engine.al;

import org.lwjgl.openal.AL11;
import org.lwjgl.openal.ALContext;
import org.melchor629.engine.loaders.audio.AudioContainer;
import org.melchor629.engine.utils.BufferUtils;

import java.nio.*;

import static org.lwjgl.openal.AL10.*;

/**
 * @author melchor9000
 */
public class LWJGLAudio implements AL {
	ALContext context;
	private org.melchor629.engine.al.Listener listener;

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#createContext()
	 */
	@Override
	public void createContext() throws ALError {
		try {
			context = ALContext.create();
            context.makeCurrent();
            listener = new org.melchor629.engine.al.Listener(this);
		} catch(Exception e) {
			ALError err = new ALError("Error creating context");
			err.initCause(e);
			throw err;
		}
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#deleteContext()
	 */
	@Override
	public void deleteContext() {
		if(context != null) {
            context.destroy();
            context.getDevice().close();
            context = null;
        }
	}

	@Override
	public org.melchor629.engine.al.Listener getListener() {
		return listener;
	}

    @Override
    public org.melchor629.engine.al.Buffer createBuffer(AudioContainer ac) {
        return new org.melchor629.engine.al.Buffer(this, ac);
    }

    @Override
    public org.melchor629.engine.al.Buffer createBuffer(short[] data, Format f, int freq) {
        return new org.melchor629.engine.al.Buffer(this, data, f, freq);
    }

    @Override
    public org.melchor629.engine.al.Buffer createBuffer(ShortBuffer data, Format f, int freq) {
        return new org.melchor629.engine.al.Buffer(this, data, f, freq);
    }

    @Override
    public org.melchor629.engine.al.Source createSource(AudioContainer ac) {
        return new org.melchor629.engine.al.Source(this, ac);
    }

    @Override
    public org.melchor629.engine.al.Source createSource(org.melchor629.engine.al.Buffer b) {
        return new org.melchor629.engine.al.Source(this, b);
    }

    /* (non-Javadoc)
     * @see org.melchor629.engine.al.AL#bufferData(int, org.melchor629.engine.al.AL.Format, byte[], int)
     */
	@Override
	public void bufferData(int buffer, Format format, byte[] data, int freq) {
		if(!isBuffer(buffer))
			throw new ALError("alBufferData", "Argument passed as buffer is not a buffer");
		if(data == null || data.length == 0)
			throw new ALError("alBufferData", "Data cannot be null or empty");
		
		alBufferData(buffer, format.e, BufferUtils.toBuffer(data), freq);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#bufferData(int, org.melchor629.engine.al.AL.Format, short[], int)
	 */
	@Override
	public void bufferData(int buffer, Format format, short[] data, int freq) {
		if(!isBuffer(buffer))
			throw new ALError("alBufferData", "Argument passed as buffer is not a buffer");
		if(data == null || data.length == 0)
			throw new ALError("alBufferData", "Data cannot be null or empty");
		
		alBufferData(buffer, format.e, BufferUtils.toBuffer(data), freq);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#bufferData(int, org.melchor629.engine.al.AL.Format, int[], int)
	 */
	@Override
	public void bufferData(int buffer, Format format, int[] data, int freq) {
		if(!isBuffer(buffer))
			throw new ALError("alBufferData", "Argument passed as buffer is not a buffer");
		if(data == null || data.length == 0)
			throw new ALError("alBufferData", "Data cannot be null or empty");
		
		alBufferData(buffer, format.e, BufferUtils.toBuffer(data), freq);
	}
	
	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#bufferData(int, org.melchor629.engine.al.AL.Format, ByteBuffer, int)
	 */
	@Override
	public void bufferData(int buffer, Format format, ByteBuffer data, int freq) {
		if(!isBuffer(buffer))
			throw new ALError("alBufferData", "Argument passed as buffer is not a buffer");
		if(data == null || data.capacity() == 0)
			throw new ALError("alBufferData", "Data cannot be null or empty");
		if(!data.isDirect())
			throw new ALError("alBufferData", "Data buffer is not direct, cannot be used by OpenAL");
		
		alBufferData(buffer, format.e, data, freq);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#bufferData(int, org.melchor629.engine.al.AL.Format, ShortBuffer, int)
	 */
	@Override
	public void bufferData(int buffer, Format format, ShortBuffer data, int freq) {
		if(!isBuffer(buffer))
			throw new ALError("alBufferData", "Argument passed as buffer is not a buffer");
		if(data == null || data.capacity() == 0)
			throw new ALError("alBufferData", "Data cannot be null or empty");
		if(!data.isDirect())
			throw new ALError("alBufferData", "Data buffer is not direct, cannot be used by OpenAL");

		alBufferData(buffer, format.e, data, freq);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#deleteBuffer(int)
	 */
	@Override
	public void deleteBuffer(int buffer) {
		if(!isBuffer(buffer))
			throw new ALError("alDeleteBuffer", "Buffer is not a buffer");
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#deleteBuffers(int[])
	 */
	@Override
	public void deleteBuffers(int[] buffers) {
		for(int buffer : buffers)
			deleteBuffer(buffer);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#deleteSource(int)
	 */
	@Override
	public void deleteSource(int source) {
		if(!isSource(source))
			throw new ALError("alDeleteSource", "Source is not a source");
		alDeleteSources(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#deleteSources(int[])
	 */
	@Override
	public void deleteSources(int[] sources) {
		for(int source : sources)
			deleteSource(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#disable(int)
	 */
	@Override
	public void disable(int cap) {
		alDisable(cap);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#distanceModel(org.melchor629.engine.al.AL.DistanceModel)
	 */
	@Override
	public void distanceModel(DistanceModel value) {
		alDistanceModel(value.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#dopplerFactor(float)
	 */
	@Override
	public void dopplerFactor(float factor) {
		alDopplerFactor(factor);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#enable(int)
	 */
	@Override
	public void enable(int cap) {
		alEnable(cap);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#genBuffer()
	 */
	@Override
	public int genBuffer() {
		return alGenBuffers();
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#genBuffers(int[])
	 */
	@Override
	public void genBuffers(int[] buffers) {
		IntBuffer buff = BufferUtils.createIntBuffer(buffers.length);
		alGenBuffers(buff);
		buff.position(0);
		buff.get(buffers);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#genSource()
	 */
	@Override
	public int genSource() {
		return alGenSources();
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#genSources(int[])
	 */
	@Override
	public void genSources(int[] sources) {
		IntBuffer buff = BufferUtils.createIntBuffer(sources.length);
		alGenSources(buff);
		buff.position(0);
		buff.get(sources);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getBoolean(org.melchor629.engine.al.AL.Get)
	 */
	@Override
	public boolean getBoolean(Get g) {
		return alGetBoolean(g.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getBufferf(int, org.melchor629.engine.al.AL.Buffer)
	 */
	@Override
	public float getBufferf(int buffer, Buffer pname) {
		return alGetBufferf(buffer, pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getBufferi(int, org.melchor629.engine.al.AL.Buffer)
	 */
	@Override
	public int getBufferi(int buffer, Buffer pname) {
		return alGetBufferi(buffer, pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getDouble(org.melchor629.engine.al.AL.Get)
	 */
	@Override
	public double getDouble(Get cap) {
		return alGetDouble(cap.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getDouble(org.melchor629.engine.al.AL.Get, double[])
	 */
	@Override
	public void getDouble(Get cap, double[] doubles) {
		DoubleBuffer buff = BufferUtils.createDoubleBuffer(doubles.length);
		alGetDoublev(cap.e, buff);
		buff.position(0);
		buff.get(doubles);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getEnumValue(java.lang.String)
	 */
	@Override
	public int getEnumValue(String ename) {
		return alGetEnumValue(ename);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getError()
	 */
	@Override
	public Error getError() {
		switch(alGetError()) {
			case 0x0:
				return Error.NO_ERROR;
			case 0xA001:
				return Error.INVALID_NAME;
			case 0xA002:
				return Error.INVALID_ENUM;
			case 0xA003:
				return Error.INVALID_VALUE;
			case 0xA004:
				return Error.INVALID_OPERATION;
			case 0xA005:
				return Error.OUT_OF_MEMORY;
			default:
				return Error.UNKNOWN;
		}
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getFloat(org.melchor629.engine.al.AL.Get)
	 */
	@Override
	public float getFloat(Get pname) {
		return alGetFloat(pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getFloat(org.melchor629.engine.al.AL.Get, float[])
	 */
	@Override
	public void getFloat(Get pname, float[] floats) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(floats.length);
		alGetFloatv(pname.e, fb);
		fb.position(0);
		fb.get(floats);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getInteger(org.melchor629.engine.al.AL.Get)
	 */
	@Override
	public int getInteger(Get pname) {
		return alGetInteger(pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getInteger(org.melchor629.engine.al.AL.Get, int[])
	 */
	@Override
	public void getInteger(Get pname, int[] integers) {
		IntBuffer fb = BufferUtils.createIntBuffer(integers.length);
		alGetIntegerv(pname.e, fb);
		fb.position(0);
		fb.get(integers);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getListener(org.melchor629.engine.al.AL.Get, float[])
	 */
	@Override
	public void getListener(Get pname, float[] floats) {
		FloatBuffer fb = BufferUtils.createFloatBuffer(floats.length);
		alGetListenerfv(pname.e, fb);
		fb.position(0);
		fb.get(floats);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getListener(org.melchor629.engine.al.AL.Get, int[])
	 */
	@Override
	public void getListener(Get pname, int[] ints) {
		IntBuffer fb = BufferUtils.createIntBuffer(ints.length);
		alGetListeneri(pname.e, fb);
		fb.position(0);
		fb.get(ints);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getListenerf(org.melchor629.engine.al.AL.Listener)
	 */
	@Override
	public float getListenerf(Listener pname) {
		return alGetListenerf(pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getListeneri(org.melchor629.engine.al.AL.Listener)
	 */
	@Override
	public int getListeneri(Listener pname) {
		return alGetListeneri(pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getSource(int, org.melchor629.engine.al.AL.Source, float[])
	 */
	@Override
	public void getSource(int source, Source pname, float[] floats) {
        FloatBuffer buffer = BufferUtils.createFloatBuffer(3);
        alGetSourcefv(source, pname.e, buffer);
        buffer.get(floats);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getSource(int, org.melchor629.engine.al.AL.Source, int[])
	 */
	@Override
	public void getSource(int source, Source pname, int[] integers) {
        IntBuffer buffer = BufferUtils.createIntBuffer(3);
        alGetSourceiv(source, pname.e, buffer);
        buffer.get(integers);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getSourcef(int, org.melchor629.engine.al.AL.Source)
	 */
	@Override
	public float getSourcef(int source, Source pname) {
		return alGetSourcef(source, pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getSourcei(int, org.melchor629.engine.al.AL.Source)
	 */
	@Override
	public int getSourcei(int source, Source pname) {
		return alGetSourcei(source, pname.e);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getString(int)
	 */
	@Override
	public String getString(int pname) {
		return alGetString(pname);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#isBuffer(int)
	 */
	@Override
	public boolean isBuffer(int obj) {
		return alIsBuffer(obj);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#isEnabled(int)
	 */
	@Override
	public boolean isEnabled(int obj) {
		return alIsEnabled(obj);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#isExtensionPresent(java.lang.String)
	 */
	@Override
	public boolean isExtensionPresent(String obj) {
		return alIsExtensionPresent(obj);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#isSource(int)
	 */
	@Override
	public boolean isSource(int obj) {
		return alIsSource(obj);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#listener(org.melchor629.engine.al.AL.Listener, float[])
	 */
	@Override
	public void listener(Listener pname, float[] data) {
		if(data == null || data.length == 0)
			throw new ALError("alListener", "Data cannot be null or empty");
		
		alListenerfv(pname.e, BufferUtils.toBuffer(data));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#listener(org.melchor629.engine.al.AL.Listener, int[])
	 */
	@Override
	public void listener(Listener pname, int[] data) {
		if(data == null || data.length == 0)
			throw new ALError("alListener", "Data cannot be null or empty");
		
		//TODO alListener(pname.e, BufferUtils.toBuffer(data));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#listener3f(org.melchor629.engine.al.AL.Listener, float, float, float)
	 */
	@Override
	public void listener3f(Listener pname, float x, float y, float z) {
		alListener3f(pname.e, x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#listenerf(org.melchor629.engine.al.AL.Listener, float)
	 */
	@Override
	public void listenerf(Listener pname, float v) {
		alListenerf(pname.e, v);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#listeneri(org.melchor629.engine.al.AL.Listener, int)
	 */
	@Override
	public void listeneri(Listener pname, int v) {
		alListeneri(pname.e, v);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#source(int, org.melchor629.engine.al.AL.Source, float[])
	 */
	@Override
	public void source(int source, Source pname, float[] data) {
		if(data == null || data.length == 0)
			throw new ALError("alSource", "Data cannot be null or empty");
		if(!isSource(source))
			throw new ALError("alSource", "Source is not a source");
		
		alSourcefv(source, pname.e, BufferUtils.toBuffer(data));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#source3f(int, org.melchor629.engine.al.AL.Source, float, float, float)
	 */
	@Override
	public void source3f(int source, Source pname, float x, float y, float z) {
		if(!isSource(source))
			throw new ALError("alSource3f", "Source is not a source");
		
		alSource3f(source, pname.e, x, y, z);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourcef(int, org.melchor629.engine.al.AL.Source, float)
	 */
	@Override
	public void sourcef(int source, Source pname, float v) {
		if(!isSource(source))
			throw new ALError("alSourcef", "Source is not a source");
		
		alSourcef(source, pname.e, v);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourcei(int, org.melchor629.engine.al.AL.Source, int)
	 */
	@Override
	public void sourcei(int source, Source pname, int v) {
		if(!isSource(source))
			throw new ALError("alSourcei", "Source is not a source");
		
		alSourcei(source, pname.e, v);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourcePause(int)
	 */
	@Override
	public void sourcePause(int source) {
		if(!isSource(source))
			throw new ALError("alSourcePause", "Source is not a source");
		
		alSourcePause(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourcePause(int[])
	 */
	@Override
	public void sourcePause(int[] sources) {
		for(int source : sources)
			sourcePause(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourcePlay(int)
	 */
	@Override
	public void sourcePlay(int source) {
		if(!isSource(source))
			throw new ALError("alSourcePlay", "Source is not a source");
		
		alSourcePlay(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourcePlay(int[])
	 */
	@Override
	public void sourcePlay(int[] sources) {
		for(int source : sources)
			sourcePlay(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceQueueBuffers(int, int)
	 */
	@Override
	public void sourceQueueBuffers(int source, int buffer) {
		if(!isSource(source))
			throw new ALError("alSourceQueueBuffers", "Source is not a source");
		if(!isBuffer(buffer))
			throw new ALError("alSourceQueueBuffers", "Buffer is not a buffer");
		
		alSourceQueueBuffers(source, buffer);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceQueueBuffers(int, int[])
	 */
	@Override
	public void sourceQueueBuffers(int source, int[] buffers) {
		for(int buffer : buffers)
			sourceQueueBuffers(source, buffer);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceRewind(int)
	 */
	@Override
	public void sourceRewind(int source) {
		if(!isSource(source))
			throw new ALError("alSourceRewind", "Source is not a source");
		
		alSourceRewind(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceRewind(int[])
	 */
	@Override
	public void sourceRewind(int[] sources) {
		for(int source : sources)
			sourceRewind(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceStop(int)
	 */
	@Override
	public void sourceStop(int source) {
		if(!isSource(source))
			throw new ALError("alSourceStop", "Source is not a source");
		
		alSourceStop(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceStop(int[])
	 */
	@Override
	public void sourceStop(int[] sources) {
		for(int source : sources)
			sourceStop(source);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceUnqueueBuffers(int, int)
	 */
	@Override
	public void sourceUnqueueBuffers(int source, int buffer) {
		sourceUnqueueBuffers(source, new int[] { buffer });
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#sourceUnqueueBuffers(int, int[])
	 */
	@Override
	public void sourceUnqueueBuffers(int source, int[] buffers) {
		if(!isSource(source))
			throw new ALError("alSourceUnqueueBuffers", "Source is not a source");
		for(int buffer : buffers)
			if(!isBuffer(buffer))
				throw new ALError("alSourceUnqueueBuffers", 
						"One of the buffers is not a buffer");
		
		alSourceUnqueueBuffers(source, BufferUtils.toBuffer(buffers));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#buffer(int, org.melchor629.engine.al.AL.Buffer, float[])
	 */
	@Override
	public void buffer(int buffer, Buffer pname, float[] floats) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");
		
		AL11.alBufferfv(buffer, pname.e, BufferUtils.toBuffer(floats));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#buffer(int, org.melchor629.engine.al.AL.Buffer, int[])
	 */
	@Override
	public void buffer(int buffer, Buffer pname, int[] integers) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");
		
		AL11.alBufferiv(buffer, pname.e, BufferUtils.toBuffer(integers));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#buffer3f(int, org.melchor629.engine.al.AL.Buffer, float, float, float)
	 */
	@Override
	public void buffer3f(int buffer, Buffer pname, float v0, float v1, float v2) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");
		
		AL11.alBuffer3f(buffer, pname.e, v0, v1, v2);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#buffer3i(int, org.melchor629.engine.al.AL.Buffer, int, int, int)
	 */
	@Override
	public void buffer3i(int buffer, Buffer pname, int v0, int v1, int v2) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");
		
		AL11.alBuffer3i(buffer, pname.e, v0, v1, v2);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#bufferf(int, org.melchor629.engine.al.AL.Buffer, float)
	 */
	@Override
	public void bufferf(int buffer, Buffer pname, float v) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");
		
		AL11.alBufferf(buffer, pname.e, v);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#bufferi(int, org.melchor629.engine.al.AL.Buffer, int)
	 */
	@Override
	public void bufferi(int buffer, Buffer pname, int v) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");
		
		AL11.alBufferi(buffer, pname.e, v);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getBuffer(int, org.melchor629.engine.al.AL.Buffer, float[])
	 */
	@Override
	public void getBuffer(int buffer, Buffer pname, float[] floats) {
		if(!isBuffer(buffer))
			throw new ALError("alBuffer", "Buffer is not a buffer");

		//TODO AL11.
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#getBuffer(int, org.melchor629.engine.al.AL.Buffer, int[])
	 */
	@Override
	public void getBuffer(int buffer, Buffer pname, int[] integers) {
		// TODO Auto-generated method stub

	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#listener3i(org.melchor629.engine.al.AL.Listener, int, int, int)
	 */
	@Override
	public void listener3i(Listener listener, int v0, int v1, int v2) {
		AL11.alListener3i(listener.e, v0, v1, v2);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#source(int, org.melchor629.engine.al.AL.Source, int[])
	 */
	@Override
	public void source(int source, Source pname, int[] integers) {
		if(!isSource(source))
			throw new ALError("alSource", "Source is not a source");
		
		AL11.alSourceiv(source, pname.e, BufferUtils.toBuffer(integers));
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#source3i(int, org.melchor629.engine.al.AL.Source, int, int, int)
	 */
	@Override
	public void source3i(int source, Source pname, int v0, int v1, int v2) {
		if(!isSource(source))
			throw new ALError("alSource3i", "Source is not a source");
		
		AL11.alSource3i(source, pname.e, v0, v1, v2);
	}

	/* (non-Javadoc)
	 * @see org.melchor629.engine.al.AL#speedOfSound(float)
	 */
	@Override
	public void speedOfSound(float speed) {
		AL11.alSpeedOfSound(speed);
	}

}
