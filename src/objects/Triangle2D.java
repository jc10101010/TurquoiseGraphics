package objects;

/**
 * The Triangle2D class represents a triangle in 2D space.
 * It consists of three 2D vertices (v1, v2, and v3), each represented by a Vertex2D object.
 */
public class Triangle2D {
    //Public fields representing the three vertices of the triangle in 2D space
    public Vertex2D v1;
    public Vertex2D v2;
    public Vertex2D v3;

    /**
     * Constructor to create a 2D triangle with three vertices.
     * 
     * @param v1 The first vertex of the triangle.
     * @param v2 The second vertex of the triangle.
     * @param v3 The third vertex of the triangle.
     */
    public Triangle2D(Vertex2D v1, Vertex2D v2, Vertex2D v3) {
        //Initialize the triangle with the provided 2D vertices
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    /**
     * Returns an array containing the x-coordinates of the triangle's vertices.
     * 
     * @return An array of floats representing the x-coordinates of v1, v2, and v3.
     */
    public float[] xValues() {
        //Return an array of x-coordinates from the vertices
        return new float[] { v1.x, v2.x, v3.x };
    }

    /**
     * Returns an array containing the y-coordinates of the triangle's vertices.
     * 
     * @return An array of floats representing the y-coordinates of v1, v2, and v3.
     */
    public float[] yValues() {
        //Return an array of y-coordinates from the vertices
        return new float[] { v1.y, v2.y, v3.y };
    }
}
