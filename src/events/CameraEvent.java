package events;

import objects.Vertex;

//A camera event represents an interpolation of a camera position between one starting position/rotation and an ending position/rotation.

public abstract class CameraEvent extends Event {

    private Vertex startRotation;
    private Vertex endRotation;
    private String description;

    public CameraEvent(Vertex posStart, Vertex posEnd, Vertex rotStart, Vertex rotEnd, float duration) {
        super(posStart, posEnd, duration);
        this.startRotation = rotStart;
        this.endRotation = rotEnd;
    }

    public Vertex calcCamRotation() {
        float progress = onCurve();
        float xDifference =  startRotation.x - endRotation.x;
        float yDifference =  startRotation.y - endRotation.y;

        float xBelow = startRotation.x - (endRotation.x + (float) Math.PI * 2);
        float xAbove = startRotation.x - (endRotation.x - (float) Math.PI * 2);

        if (Math.abs(xBelow) < Math.abs(xDifference)) {
            endRotation.x += Math.PI * 2;
        }
        if (Math.abs(xAbove) < Math.abs(xDifference)) {
            endRotation.x -= Math.PI * 2;
        }


        float yBelow = startRotation.y - (endRotation.y + (float) Math.PI * 2);
        float yAbove = startRotation.y - (endRotation.y - (float) Math.PI * 2);

        if (Math.abs(yBelow) < Math.abs(yDifference)) {
            endRotation.y += Math.PI * 2;
        }
        if (Math.abs(yAbove) < Math.abs(yDifference)) {
            endRotation.y -= Math.PI * 2;
        }

        Vertex goal = Vertex.difference(endRotation, startRotation);
        Vertex goalScaled = Vertex.multiply(goal, new Vertex(progress, progress, progress));
        Vertex finalProgress = Vertex.add(startRotation, goalScaled);
        return finalProgress;
    }

    public void startTimer(Vertex position, Vertex rotation) {
        started = true;
        if (startPosition == null) {
            startPosition = position;
        } 
        if (endPosition == null) {
            endPosition = position;
        } 
        if (startRotation == null) {
            startRotation = rotation;
        }
        if (endRotation == null) {
            endRotation = rotation;
        }
        startTime = System.currentTimeMillis();
        endTime = startTime + (duration * 1000);
    }

    public String getDescription() {
        return this.description;
    }
}
