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

    public static Vertex rotate(Vertex v1, Vertex v2) {
        Vertex c = new Vertex(0, 0, 0);
        Vertex s = new Vertex(0, 0, 0);
        s.x = (float) Math.sin(-v2.x);
        s.y = (float) Math.sin(-v2.y);
        s.z = (float) Math.sin(-v2.z);
        c.x = (float) Math.cos(-v2.x);
        c.y = (float) Math.cos(-v2.y);
        c.z = (float) Math.cos(-v2.z);
        return rotateWithSinCos(v1, s, c);
    }

    public static Vertex rotateWithSinCos(Vertex v1, Vertex s, Vertex c) {
        float x = v1.x;
        float y = v1.y;
        float z = v1.z;
        float dX = c.y * (s.z * y + c.z * x) - s.y * z;
        float dY = s.x * (c.y * z + s.y * (s.z * y + c.z * x)) + c.x * (c.z * y - s.z * x);
        float dZ = c.x * (c.y * z + s.y * (s.z * y + c.z * x)) - s.x * (c.z * y - s.z * x);
        return new Vertex(dX, dY, dZ);
    }
}
