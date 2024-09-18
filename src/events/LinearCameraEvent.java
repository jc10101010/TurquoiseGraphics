package events;

import objects.Vertex;

/**
 * The LinearCameraEvent class represents a CameraEvent where the interpolation
 * follows a linear animation curve. This means the camera will move and rotate
 * at a constant speed between the start and end positions/rotations.
 */
public class LinearCameraEvent extends CameraEvent {

    /**
     * Constructor to initialize a linear camera event with specified start and end positions, 
     * rotations, and duration.
     * 
     * @param posStart The starting position of the camera.
     * @param posEnd The ending position of the camera.
     * @param rotStart The starting rotation of the camera.
     * @param rotEnd The ending rotation of the camera.
     * @param duration The duration of the event in seconds.
     */
    public LinearCameraEvent(Vertex posStart, Vertex posEnd, Vertex rotStart, Vertex rotEnd, float duration) {
        //Pass the parameters to the parent CameraEvent class
        super(posStart, posEnd, rotStart, rotEnd, duration);
    }

    /**
     * Defines the animation curve for the linear camera event.
     * In this case, the curve is linear, so the progress over time is simply the
     * proportion of time that has passed.
     * 
     * @return The current progress of the event, a float between 0 (start) and 1 (end).
     */
    @Override
    protected float onCurve() {
        //Linear interpolation is represented by time progress directly
        return timeProgress();
    }
}
