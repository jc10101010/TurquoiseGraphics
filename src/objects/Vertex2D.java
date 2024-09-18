package objects;

/**
 * The Vertex2D class represents a point in 2D space with x and y coordinates.
 * It is commonly used for 2D graphics, UI elements, or screen space representations.
 */
public class Vertex2D {
    //Public fields for the x and y coordinates of the vertex
    public float x;
    public float y;

    /**
     * Constructor to initialize a 2D vertex with given x and y coordinates.
     * 
     * @param x The x-coordinate of the vertex.
     * @param y The y-coordinate of the vertex.
     */
    public Vertex2D(float x, float y) {
        this.x = x;
        this.y = y;
    }
}
