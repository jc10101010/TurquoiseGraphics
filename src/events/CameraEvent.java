package events;

import objects.Vertex;

/**
 * CameraEvent is an abstract class that represents an interpolation of a camera's position and rotation 
 * between a starting and ending position/rotation over a specified duration. It provides functionality 
 * to calculate the camera's rotation at a given time based on a progress curve.
 */
public abstract class CameraEvent extends Event {

    //Fields to store the start and end rotations of the camera
    private Vertex startRotation;
    private Vertex endRotation;
    private String description; //Optional description of the camera event

    /**
     * Constructor to initialize a camera event with start and end positions and rotations.
     * 
     * @param posStart The starting position of the camera.
     * @param posEnd The ending position of the camera.
     * @param rotStart The starting rotation of the camera.
     * @param rotEnd The ending rotation of the camera.
     * @param duration The duration of the event in seconds.
     */
    public CameraEvent(Vertex posStart, Vertex posEnd, Vertex rotStart, Vertex rotEnd, float duration) {
        //Initialize the base Event class with position and duration
        super(posStart, posEnd, duration);
        this.startRotation = rotStart;
        this.endRotation = rotEnd;
    }

    /**
     * Calculates the interpolated camera rotation based on the current progress of the event.
     * This takes into account the shortest rotational path between start and end rotations.
     * 
     * @return The interpolated rotation as a Vertex.
     */
    public Vertex calcCamRotation() {
        float progress = onCurve(); //Get the current progress on the interpolation curve (0 to 1)
        float xDifference = startRotation.x - endRotation.x;
        float yDifference = startRotation.y - endRotation.y;

        //Adjust for wrap-around (2π) in the x-rotation to ensure the shortest rotational path
        float xBelow = startRotation.x - (endRotation.x + (float) Math.PI * 2);
        float xAbove = startRotation.x - (endRotation.x - (float) Math.PI * 2);

        //If rotating clockwise or counterclockwise is a shorter path, adjust the x-rotation
        if (Math.abs(xBelow) < Math.abs(xDifference)) {
            endRotation.x += Math.PI * 2;
        }
        if (Math.abs(xAbove) < Math.abs(xDifference)) {
            endRotation.x -= Math.PI * 2;
        }

        //Adjust for wrap-around (2π) in the y-rotation to ensure the shortest rotational path
        float yBelow = startRotation.y - (endRotation.y + (float) Math.PI * 2);
        float yAbove = startRotation.y - (endRotation.y - (float) Math.PI * 2);

        //If rotating clockwise or counterclockwise is a shorter path, adjust the y-rotation
        if (Math.abs(yBelow) < Math.abs(yDifference)) {
            endRotation.y += Math.PI * 2;
        }
        if (Math.abs(yAbove) < Math.abs(yDifference)) {
            endRotation.y -= Math.PI * 2;
        }

        //Calculate the difference between the start and end rotations
        Vertex goal = Vertex.difference(endRotation, startRotation);

        //Scale the difference by the current progress to get partial movement towards the goal
        Vertex goalScaled = Vertex.multiply(goal, new Vertex(progress, progress, progress));

        //Add the scaled difference to the start rotation to get the final interpolated rotation
        Vertex finalProgress = Vertex.add(startRotation, goalScaled);

        return finalProgress;
    }

    /**
     * Starts the timer for the camera event. If the start or end positions/rotations are not provided,
     * they are initialized to the current camera state.
     * 
     * @param position The current position of the camera when the event starts.
     * @param rotation The current rotation of the camera when the event starts.
     */
    public void startTimer(Vertex position, Vertex rotation) {
        started = true; //Mark the event as started

        //Initialize the start position if not already set
        if (startPosition == null) {
            startPosition = position;
        } 
        //Initialize the end position if not already set
        if (endPosition == null) {
            endPosition = position;
        } 
        //Initialize the start rotation if not already set
        if (startRotation == null) {
            startRotation = rotation;
        }
        //Initialize the end rotation if not already set
        if (endRotation == null) {
            endRotation = rotation;
        }

        //Set the start time to the current system time
        startTime = System.currentTimeMillis();
        //Calculate the end time based on the duration of the event
        endTime = startTime + (duration * 1000); //Convert duration from seconds to milliseconds
    }

    /**
     * Retrieves the description of the camera event, if any.
     * 
     * @return A string description of the event.
     */
    public String getDescription() {
        return this.description;
    }
}
