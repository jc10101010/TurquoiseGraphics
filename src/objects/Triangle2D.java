package objects;
/*
* The triangle2d is an object with three 2D vertices
*/
public class Triangle2D {
    public Vertex2D v1;
    public Vertex2D v2;
    public Vertex2D v3;

    public Triangle2D(Vertex2D v1, Vertex2D v2, Vertex2D v3) {
        this.v1 = v1;
        this.v2 = v2;
        this.v3 = v3;
    }

    public float[] xValues() {
        return new float[] {v1.x, v2.x, v3.x};
    }

    public float[] yValues() {
        return new float[] {v1.y, v2.y, v3.y};
    }
}
