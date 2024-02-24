package events;

import objects.Vertex;

//An event represents an interpolation of an object's position between one starting position and an ending position.

public abstract class Event {
    protected float startTime;
    protected float endTime;
    protected Vertex startPosition;
    protected Vertex endPosition;
    protected boolean done = false;
    protected boolean started = false;
    protected float duration;

    public Event(Vertex posStart, Vertex posEnd, float duration) {
        startPosition = posStart;
        endPosition = posEnd;
        this.duration = duration;
    }

    public void startTimer(Vertex position) {
        started = true;
        if (startPosition == null) {
            startPosition = position;
        }
        startTime = System.currentTimeMillis();
        endTime = startTime + (duration * 1000);
    }
    protected abstract float onCurve();
    public Vertex calcCamPosition() {
        float progress = onCurve();
        Vertex goal = Vertex.difference(endPosition, startPosition);
        Vertex goalScaled = Vertex.multiply(goal, new Vertex(progress, progress, progress));
        Vertex finalProgress = Vertex.add(startPosition, goalScaled);
        return finalProgress;
    }
    protected float timeProgress() {
        float progress = (System.currentTimeMillis() - startTime) / (endTime - startTime);

        if (progress > 1) {
            done = true;
            return 1;
        } else if (progress < 0) {
            return 0;
        } else { 
            return progress;
        }
    }

    public boolean getDone() {
        return done;
    }

    public boolean getStarted() {
        return started;
    }
}
