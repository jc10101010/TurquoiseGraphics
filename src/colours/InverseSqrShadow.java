package colours;
import core.Scene;
import java.awt.Color;
import objects.Triangle;
import objects.Vertex;

//The InverseSqrShadow class hades an object based on it's distance from the center of the scene.

public class InverseSqrShadow extends ColourShader{
    private Color colour; 
    private float shaderFactor;
    private Scene scene;
    
    public InverseSqrShadow (Color colour, Scene scene) {
        this.colour = colour;
        this.shaderFactor = 0.03f;//0.02f;
        this.scene = scene;
    }

    public Color shadeBasedOnTriangle(Triangle triangle) {
        Vertex tV = averageTriangleAsVertex(triangle);
        Vertex diff = Vertex.difference(tV, scene.getCamPos());
        int red = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor) * colour.getRed());
        int green = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor)* colour.getGreen());
        int blue = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor)* colour.getBlue());
        Color finalColour = new Color(red, green, blue);
        return finalColour;
    }
}
