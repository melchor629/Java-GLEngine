package org.melchor629.engine.al.types;

import org.melchor629.engine.al.AL;
import org.melchor629.engine.utils.math.vec3;

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
    
    public float min_gain, max_gain;

    public Source(Buffer buffer0) {
        buffer = buffer0;
        source = al.genSources();
        al.sourcei(source, AL.Source.BUFFER, buffer.getBuffer());
    }

    public void setRelative(boolean relativ) {
        relative = relativ;
        al.sourcei(source, AL.Source.SOURCE_RELATIVE, relativ ? 1 : 0);
    }

    public boolean isRelative() {
        return AL.relative;
    }
    
    //TODO pasar el int a un enum en AL
    public int getSourceState() {
        return al.getSourcei(source, AL.Source.SOURCE_TYPE);
    }
    
    public void setLoop(boolean loop) {
        looping = loop;
        al.sourcei(source, AL.Source.LOOPING, loop ? 1 : 0);
    }
    
    public boolean isLooping() {
        return looping;
    }
    
    public void setGainBounds(float min, float max) {
        al.sourcef(source, AL.Source.MIN_GAIN, min);
        al.sourcef(source, AL.Source.MAX_GAIN, max);
        this.min_gain = min;
        this.max_gain = max;
    }
    
    public void setReferenceDistance(float dist) {
        reference_distance = dist;
        al.sourcef(source, AL.Source.REFERENCE_DISTANCE, dist);
    }
    
    public float getReferenceDistance() {
        return reference_distance;
    }
    
    public void setRolloffFactor(float factor) {
        rolloff_factor = factor;
        al.sourcef(source, AL.Source.ROLLOFF_FACTOR, factor);
    }
    
    public float getRolloffFactor() {
        return rolloff_factor;
    }
    
    public void setMaxDistance(float dist) {
        max_distance = dist;
        al.sourcef(source, AL.Source.MAX_DISTANCE, dist);
    }
    
    public void setPitch(float pitch) {
        if(pitch =< 0.0f)
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
        cone_outer_gain = gain;
        al.sourcef(source, AL.Source.CONE_OUTER_GAIN, gain);
    }
    
    public float getConeOuterGain() {
        return gain;
    }
    
    public void setPlaybackPosition(float pos) {
        al.sourcef(source, AL.Source.SEC_OFFSET, pos);
    }
    
    public float getPlaybackPosition() {
        return al.getSourcef(source, AL.Source.SEC_OFFSET);
    }
    
    public void setPlaybackSample(float pos) {
        al.sourcef(source, AL.Source.SAMPLE_OFFSET, pos);
    }
    
    public float getPlaybackSample() {
        return al.getSourcef(source, AL.Source.SAMPLE_OFFSET);
    }
    
    public void setPlaybackBytes(float pos) {
        al.sourcef(source, AL.Source.BYTE_OFFSET, pos);
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
            al.destroySources(source);
        if(buffer != null)
            buffer.destroy();
        source = 0;
        buffer = null;
    }
}
