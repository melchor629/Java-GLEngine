package org.melchor629.engine.al;

import org.melchor629.engine.Erasable;
import org.melchor629.engine.utils.math.Vector3;
import org.melchor629.engine.utils.math.GLM;

/**
 * Source of a sound, with its position, gain, speed, and so...
 * <p>
 *     Source is an OpenAL Object that represents a Source of a sound. Its
 *     attributes defines how the source acts. The audio comes from one
 *     {@link Buffer} or a stream of {@link Buffer}s.
 * </p>
 * Base class for OpenAL Sources
 * @author melchor9000
 */
public abstract class Source implements Erasable {
    protected int source;
    private boolean relative = false;
    private boolean looping = false;
    private float reference_distance = 1.0f, rolloff_factor = 1.0f, max_distance = Float.MAX_VALUE;
    private float pitch = 1.0f;
    private Vector3 direction;
    private float cone_inner_angle = 360.0f, cone_outer_angle = 360.0f, cone_outer_gain = 0.0f;
    private Vector3 position, velocity;
    private float gain;
    protected final AL al;
    
    private float min_gain = 0.f, max_gain = 1.f;

    protected Source(AL al) {
        this.al = al;
        position = new Vector3();
        velocity = new Vector3();
        source = al.genSource();
        al.addErasable(this);
    }
    
    public void setPosition(Vector3 pos) {
        position = pos;
        al.source3f(source, AL.Source.POSITION, pos.x(), pos.y(), pos.z());
    }
    
    public Vector3 getPosition() {
        return position;
    }
    
    public void setVelocity(Vector3 vel) {
        velocity = vel;
        al.source3f(source, AL.Source.VELOCITY, vel.x(), vel.y(), vel.z());
    }
    
    public Vector3 getVelocity() {
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
        setMaximumGain(max);
        setMinimumGain(min);
    }
    
    public void setReferenceDistance(float dist) {
        assert(0.f <= dist);
        reference_distance = Math.abs(dist);
        al.sourcef(source, AL.Source.REFERENCE_DISTANCE, reference_distance);
    }
    
    public float getReferenceDistance() {
        return reference_distance;
    }
    
    public void setRolloffFactor(float factor) {
        assert(0.f <= factor);
        rolloff_factor = Math.abs(factor);
        al.sourcef(source, AL.Source.ROLLOFF_FACTOR, Math.abs(factor));
    }
    
    public float getRolloffFactor() {
        return rolloff_factor;
    }
    
    public void setMaxDistance(float dist) {
        assert(0.f <= dist);
        max_distance = Math.abs(dist);
        al.sourcef(source, AL.Source.MAX_DISTANCE, max_distance);
    }

    public float getMaxDistance(float dist) {
        return max_distance;
    }
    
    public void setPitch(float pitch) {
        assert(0.f < pitch);
        this.pitch = pitch;
        al.sourcef(source, AL.Source.PITCH, pitch);
    }
    
    public float getPitch() {
        return pitch;
    }
    
    public void setDirection(Vector3 dir) {
        direction = dir;
        al.source3f(source, AL.Source.DIRECTION, dir.x(), dir.y(), dir.z());
    }
    
    public Vector3 getDirection() {
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
        assert(GLM.between(gain, 0.f, 1.f));
        cone_outer_gain = gain;
        al.sourcef(source, AL.Source.CONE_OUTER_GAIN, cone_outer_gain);
    }
    
    public float getConeOuterGain() {
        return cone_outer_gain;
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

    public void setMinimumGain(float min) {
        if(GLM.between(min, 0.f, 1.f)) {
            min_gain = min;
            al.sourcef(source, AL.Source.MIN_GAIN, min_gain);
        }
    }

    public float getMinimumGain() {
        return min_gain;
    }

    public void setMaximumGain(float min) {
        if(GLM.between(min, 0.f, 1.f)) {
            max_gain = min;
            al.sourcef(source, AL.Source.MAX_GAIN, min_gain);
        }
    }

    public float getMaximumGain() {
        return max_gain;
    }
    
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

    public void delete() {
        if(source != 0)
            al.deleteSource(source);
        source = 0;
    }
}
