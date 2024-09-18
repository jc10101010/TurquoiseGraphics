package objects;

/**
 * The Vertex class represents a point in 3D space with x, y, and z coordinates.
 * It also provides various static methods for vector operations like addition, subtraction,
 * magnitude calculation, multiplication, division, and rotation.
 */
public class Vertex {
    //Public fields for the x, y, and z coordinates of the vertex
    public float x;
    public float y;
    public float z;

    /**
     * Constructor to initialize a vertex with given x, y, and z coordinates.
     * 
     * @param x The x-coordinate of the vertex.
     * @param y The y-coordinate of the vertex.
     * @param z The z-coordinate of the vertex.
     */
    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Static method to calculate the difference between two vertices (v1 - v2).
     * 
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     * @return A new Vertex representing the difference (v1 - v2).
     */
    public static Vertex difference(Vertex v1, Vertex v2) {
        return new Vertex(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    /**
     * Static method to add two vertices (v1 + v2).
     * 
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     * @return A new Vertex representing the sum (v1 + v2).
     */
    public static Vertex add(Vertex v1, Vertex v2) {
        return new Vertex(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    /**
     * Method to calculate the magnitude (length) of the vertex vector.
     * 
     * @return The magnitude of the vector.
     */
    public float magnitude() {
        return (float) Math.pow(x * x + y * y + z * z, 0.5); //Euclidean distance formula
    }

    /**
     * Method to calculate the squared magnitude of the vertex vector.
     * Useful when you want to avoid the performance cost of square root calculation.
     * 
     * @return The squared magnitude of the vector.
     */
    public float magnitudeSqrd() {
        return x * x + y * y + z * z;
    }

    /**
     * Static method to multiply two vertices element-wise (v1 * v2).
     * 
     * @param v1 The first vertex.
     * @param v2 The second vertex.
     * @return A new Vertex representing the element-wise multiplication of v1 and v2.
     */
    public static Vertex multiply(Vertex v1, Vertex v2) {
        return new Vertex(v1.x * v2.x, v1.y * v2.y, v1.z * v2.z);
    }

    /**
     * Static method to divide a vertex by a scalar value.
     * 
     * @param v The vertex to be divided.
     * @param n The scalar divisor.
     * @return A new Vertex with each component of v divided by n.
     */
    public static Vertex divide(Vertex v, float n) {
        return new Vertex(v.x / n, v.y / n, v.z / n);
    }

    /**
     * Static method to rotate a vertex around the axes by another vertex (v2) that represents the rotation angles.
     * It uses trigonometric sine and cosine functions to perform the rotation.
     * 
     * @param v1 The vertex to rotate.
     * @param v2 The rotation vertex, where each component represents a rotation around the respective axis.
     * @return A new Vertex representing the rotated coordinates of v1.
     */
    public static Vertex rotate(Vertex v1, Vertex v2) {
        //Calculate sine and cosine values for each rotation angle (negated)
        Vertex c = new Vertex(0, 0, 0);
        Vertex s = new Vertex(0, 0, 0);
        s.x = (float) Math.sin(-v2.x);
        s.y = (float) Math.sin(-v2.y);
        s.z = (float) Math.sin(-v2.z);
        c.x = (float) Math.cos(-v2.x);
        c.y = (float) Math.cos(-v2.y);
        c.z = (float) Math.cos(-v2.z);
        
        //Rotate the vertex using precomputed sine and cosine values
        return rotateWithSinCos(v1, s, c);
    }

    /**
     * Static method to rotate a vertex using precomputed sine and cosine values.
     * This method applies the standard 3D rotation formulas along the x, y, and z axes.
     * 
     * @param v1 The vertex to rotate.
     * @param s The precomputed sine values for each axis.
     * @param c The precomputed cosine values for each axis.
     * @return A new Vertex representing the rotated coordinates of v1.
     */
    public static Vertex rotateWithSinCos(Vertex v1, Vertex s, Vertex c) {
        float x = v1.x;
        float y = v1.y;
        float z = v1.z;
        
        //Perform 3D rotation calculations for each axis
        float dX = c.y * (s.z * y + c.z * x) - s.y * z;
        float dY = s.x * (c.y * z + s.y * (s.z * y + c.z * x)) + c.x * (c.z * y - s.z * x);
        float dZ = c.x * (c.y * z + s.y * (s.z * y + c.z * x)) - s.x * (c.z * y - s.z * x);
        
        //Return the new rotated vertex
        return new Vertex(dX, dY, dZ);
    }
}
