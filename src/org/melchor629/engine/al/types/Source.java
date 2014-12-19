package org.melchor629.engine.al.types;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.objects.PCMData;
import org.melchor629.engine.utils.math.vec3;
import org.melchor629.engine.utils.math.GLM;

import static org.melchor629.engine.Game.al;

/**
 * Source of a sound, with its position, gain, speed, and so...
 * @author melchor9000
 */
//TODO Clamp values within their intervals
public class Source {
    protected Buffer buffer;
    protected int source;
    protected boolean relative = false;
    protected boolean looping = false;
    protected float reference_distance = 1.0f, rolloff_factor = 1.0f, max_distance = Float.MAX_VALUE;
    protected float pitch = 1.0f;
    protected vec3 direction;
    protected float cone_inner_angle = 360.0f, cone_outer_angle = 360.0f, cone_outer_gain = 0.0f;
    protected vec3 position, velocity;
    protected float gain;
    
    public float min_gain, max_gain;

    public Source(Buffer buffer0) {
        if(buffer0 == null || !buffer0.isComplete())
            throw new IllegalArgumentException("Cannot pass a null or incomplete buffer");
        buffer = buffer0;
        position = new vec3();
        velocity = new vec3();
        source = al.genSource();
        al.sourcei(source, AL.Source.BUFFER, buffer.getBuffer());
    }
    
    public Source(PCMData data) {
    	this(new Buffer(data));
    }
    
    public void setPosition(vec3 pos) {
        position = pos;
        al.source3f(source, AL.Source.POSITION, pos.x, pos.y, pos.z);
    }
    
    public vec3 getPosition() {
        return position;
    }
    
    public void setVelocity(vec3 vel) {
        velocity = vel;
        al.source3f(source, AL.Source.VELOCITY, vel.x, vel.y, vel.z);
    }
    
    public vec3 getVelocity() {
        return velocity;
    }
    
    public void setGain(float gain) {
        this.gain = Math.abs(gain);
        al.sourcef(source, AL.Source.GAIN, this.gain);
    }
    
    public float getGain() {
        return gain;
    }

    public void setRelative(boolean relativ) {
        relative = relativ;
        al.sourcei(source, AL.Source.SOURCE_RELATIVE, relativ ? 1 : 0);
    }

    public boolean isRelative() {
        return relative;
    }
    
    //TODO pasar el int a un enum en AL
    public int getSourceState() {
        return al.getSourcei(source, AL.Source.SOURCE_STATE);
    }
    
    public void setLoop(boolean loop) {
        looping = loop;
        al.sourcei(source, AL.Source.LOOPING, loop ? 1 : 0);
    }
    
    public boolean isLooping() {
        return looping;
    }
    
    public void setGainBounds(float min, float max) {
        this.min_gain = GLM.belongsToInterval(min, 0.0f, 1.0f) ? min : 0.0f;
        this.max_gain = GLM.belongsToInterval(max, 0.0f, 1.0f) ? max : 1.0f;
        al.sourcef(source, AL.Source.MIN_GAIN, min_gain);
        al.sourcef(source, AL.Source.MAX_GAIN, max_gain);
    }
    
    public void setReferenceDistance(float dist) {
        reference_distance = Math.abs(dist);
        al.sourcef(source, AL.Source.REFERENCE_DISTANCE, reference_distance);
    }
    
    public float getReferenceDistance() {
        return reference_distance;
    }
    
    public void setRolloffFactor(float factor) {
        rolloff_factor = Math.abs(factor);
        al.sourcef(source, AL.Source.ROLLOFF_FACTOR, Math.abs(factor));
    }
    
    public float getRolloffFactor() {
        return rolloff_factor;
    }
    
    public void setMaxDistance(float dist) {
        max_distance = Math.abs(dist);
        al.sourcef(source, AL.Source.MAX_DISTANCE, max_distance);
    }
    
    public void setPitch(float pitch) {
        if(pitch <= 0.0f)
            pitch = 1.f;
        this.pitch = pitch;
        al.sourcef(source, AL.Source.PITCH, pitch);
    }
    
    public float getPitch() {
        return pitch;
    }
    
    public void setDirection(vec3 dir) {
        direction = dir;
        al.source3f(source, AL.Source.DIRECTION, dir.x, dir.y, dir.z);
    }
    
    public vec3 getDirection() {
        return direction;
    }
    
    public void setConeAngles(float inner, float outer) {
        cone_inner_angle = inner;
        cone_outer_angle = outer;
        al.sourcef(source, AL.Source.CONE_INNER_ANGLE, inner);
        al.sourcef(source, AL.Source.CONE_OUTER_ANGLE, outer);
    }
    
    public void setConeInnerAngle(float inner) {
        setConeAngles(inner, cone_outer_angle);
    }
    
    public void setConeOuterAngle(float outer) {
        setConeAngles(cone_inner_angle, outer);
    }
    
    public float getConeInnerAngle() {
        return cone_inner_angle;
    }
    
    public float getConeOuterAngle() {
        return cone_outer_angle;
    }
    
    public void setConeOuterGain(float gain) {
        cone_outer_gain = GLM.belongsToInterval(gain, 0.0f, 1.0f) ? gain : 0.0f;
        al.sourcef(source, AL.Source.CONE_OUTER_GAIN, cone_outer_gain);
    }
    
    public float getConeOuterGain() {
        return gain;
    }
    
    public void setPlaybackPosition(float pos) {
        al.sourcef(source, AL.Source.SEC_OFFSET, Math.abs(pos));
    }
    
    public float getPlaybackPosition() {
        return al.getSourcef(source, AL.Source.SEC_OFFSET);
    }
    
    public void setPlaybackSample(float pos) {
        al.sourcef(source, AL.Source.SAMPLE_OFFSET, Math.abs(pos));
    }
    
    public float getPlaybackSample() {
        return al.getSourcef(source, AL.Source.SAMPLE_OFFSET);
    }
    
    public void setPlaybackBytes(float pos) {
        al.sourcef(source, AL.Source.BYTE_OFFSET, Math.abs(pos));
    }
    
    public float getPlaybackBytes() {
        return al.getSourcef(source, AL.Source.BYTE_OFFSET);
    }
    
    //Buffers (Un)Queue functions go here
    
    public void play() {
        al.sourcePlay(source);
    }
    
    public void pause() {
        al.sourcePause(source);
    }
    
    public void stop() {
        al.sourceStop(source);
    }
    
    public void rewind() {
        al.sourceRewind(source);
    }
    
    public static void playAll(Source... sources) {
        for(Source source : sources)
            source.play();
    }
    
    public static void pauseAll(Source... sources) {
        for(Source source : sources)
            source.pause();
    }
    
    public static void stopAll(Source... sources) {
        for(Source source : sources)
            source.stop();
    }
    
    public static void rewindAll(Source... sources) {
        for(Source source : sources)
            source.rewind();
    }

    public void destroy() {
        if(source != 0)
            al.deleteSource(source);
        if(buffer != null)
        	buffer.destroy();
        source = 0;
        buffer = null;
    }
}
