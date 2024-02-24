package events;

import objects.Vertex;

//A LinearCameraEvent represents a CameraEvemt that has a linear animation curve.

public class LinearCameraEvent extends CameraEvent{
    public LinearCameraEvent(Vertex posStart, Vertex posEnd, Vertex rotStart, Vertex rotEnd, float duration) {
        super(posStart, posEnd, rotStart, rotEnd, duration);
    }

    protected float onCurve() {
        return timeProgress();
    }

    
}

