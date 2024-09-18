package events;

import objects.Vertex;

/**
 * The Event class represents an interpolation (smooth transition) of an object's position
 * between a starting and an ending position over a specified duration.
 * It serves as a base class for more specific events (e.g., camera events) 
 * and provides key methods for managing time-based transitions.
 */
public abstract class Event {

    //Fields for event timing and position
    protected float startTime; //The time when the event started
    protected float endTime; //The time when the event is expected to end
    protected Vertex startPosition; //The starting position of the object
    protected Vertex endPosition; //The ending position of the object
    protected boolean done = false; //Indicates if the event is finished
    protected boolean started = false; //Indicates if the event has started
    protected float duration; //The duration of the event in seconds

    /**
     * Constructor to initialize an event with starting and ending positions and a duration.
     * 
     * @param posStart The starting position of the object.
     * @param posEnd The ending position of the object.
     * @param duration The duration of the event in seconds.
     */
    public Event(Vertex posStart, Vertex posEnd, float duration) {
        this.startPosition = posStart;
        this.endPosition = posEnd;
        this.duration = duration;
    }

    /**
     * Starts the timer for the event. If the start position is not provided, 
     * it will be initialized to the given position. The current system time 
     * is recorded as the start time.
     * 
     * @param position The position from which the event starts (if startPosition is null).
     */
    public void startTimer(Vertex position) {
        started = true; //Mark the event as started

        //If no start position is provided, use the given position as the starting point
        if (startPosition == null) {
            startPosition = position;
        }

        //Record the current system time as the start time and calculate the end time
        startTime = System.currentTimeMillis();
        endTime = startTime + (duration * 1000); //Convert duration from seconds to milliseconds
    }

    /**
     * Abstract method to define the interpolation curve (e.g., linear, ease-in, etc.).
     * Subclasses must implement this method to define how the progress over time behaves.
     * 
     * @return The progress value (between 0 and 1) based on the curve.
     */
    protected abstract float onCurve();

    /**
     * Calculates the current position of the object based on the progress of the event.
     * Interpolates between the start and end positions using the progress value from the curve.
     * 
     * @return The current position as a Vertex based on the event's progress.
     */
    public Vertex calcCamPosition() {
        float progress = onCurve(); //Get the current progress on the curve (0 to 1)
        
        //Calculate the difference between the end and start positions
        Vertex goal = Vertex.difference(endPosition, startPosition);
        
        //Scale the difference by the progress value to determine how far along the transition we are
        Vertex goalScaled = Vertex.multiply(goal, new Vertex(progress, progress, progress));
        
        //Add the scaled difference to the start position to get the current position
        Vertex finalProgress = Vertex.add(startPosition, goalScaled);

        return finalProgress;
    }

    /**
     * Calculates the progress of the event based on the time elapsed since the start.
     * The value is between 0 (just started) and 1 (completed). If the event is done, 
     * the progress is capped at 1.
     * 
     * @return A float value between 0 and 1 indicating the time-based progress of the event.
     */
    protected float timeProgress() {
        //Calculate the time progress by dividing elapsed time by total event duration
        float progress = (System.currentTimeMillis() - startTime) / (endTime - startTime);

        //If progress exceeds 1, mark the event as done and cap the progress at 1
        if (progress > 1) {
            done = true;
            return 1;
        } else if (progress < 0) {
            return 0;
        } else {
            return progress;
        }
    }

    /**
     * Checks if the event has finished.
     * 
     * @return true if the event is done, otherwise false.
     */
    public boolean getDone() {
        return done;
    }

    /**
     * Checks if the event has started.
     * 
     * @return true if the event has started, otherwise false.
     */
    public boolean getStarted() {
        return started;
    }
}
