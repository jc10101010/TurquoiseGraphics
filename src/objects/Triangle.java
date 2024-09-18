package objects;

/**
 * The Triangle class represents a geometric triangle in 3D space.
 * It consists of three vertices (v1, v2, and v3), which define the corners of the triangle.
 */
public class Triangle {
    //Public fields representing the three vertices of the triangle
    public Vertex v1;
    public Vertex v2;
    public Vertex v3;

    /**
     * Constructor to create a triangle with three vertices.
     * 
     * @param v1 The first vertex of the triangle.
     * @param v2 The second vertex of the triangle.
     * @param v3 The third vertex of the triangle.
     */
    public Triangle(Vertex v1, Vertex v2, Vertex v3) {
        //Initialize the triangle with the provided vertices
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }
}
