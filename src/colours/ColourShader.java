package colours;
import java.awt.Color;

import objects.Triangle;
import objects.Vertex;

//A ColourShader can take in a triangles data, and it's own colour and converts this into a final processed colour. This is similiar to how pixel shaders work.

public abstract class ColourShader {

    public abstract Color shadeBasedOnTriangle(Triangle triangle);
    public static float sigmoid(float x) {
        float absX = Math.abs(x);
        return (float) (1/(1 + Math.exp(-1 * absX))) * -2 + 2;
    }
    public static float inverseSquare(float x) {
        float absX = Math.abs(x);
        return 1/((absX+1)*(absX+1));
    }

    public static float newShadow(float x) {
        float res = 1/(-x+1.4f);
        return Math.abs(res);
    }

    public static int capRGB(int inp){
        if (inp > 255) {
            return 255;
        }
        else if (inp < 0) { 
            return 0;
        }
        else {
            return inp;
        }
    }

    public static Vertex averageTriangleAsVertex(Triangle triangle) {
        Vertex avgVertex =  Vertex.divide(Vertex.add(Vertex.add(triangle.v1, triangle.v2), triangle.v3), 3.0f);
        return avgVertex;
    }

}
