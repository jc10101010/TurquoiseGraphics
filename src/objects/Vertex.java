package objects;

/*
* The vertex is an object with an x,y,z position
*/

public class Vertex {
    public float x;
    public float y;
    public float z;

    public Vertex(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public static Vertex difference(Vertex v1, Vertex v2) {
        return new Vertex(v1.x - v2.x, v1.y - v2.y, v1.z - v2.z);
    }

    public static Vertex add(Vertex v1, Vertex v2) {
        return new Vertex(v1.x + v2.x, v1.y + v2.y, v1.z + v2.z);
    }

    public float magnitude() {
        return (float) Math.pow(x * x + y * y + z * z, 0.5);
    }

    public float magnitudeSqrd() {
        return x * x + y * y + z * z;
    } 

    public static Vertex multiply(Vertex v1, Vertex v2) {
        return new Vertex(v1.x*v2.x, v1.y*v2.y, v1.z*v2.z);
    }
    
    public static Vertex divide(Vertex v, float n) {
        return new Vertex(v.x/n, v.y/n, v.z/n);
    }
}
