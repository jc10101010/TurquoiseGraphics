package graphics.colour;
import java.awt.Color;

import graphics.objects.Triangle;
import graphics.objects.Vertex;

//The InverseSqrShadow class hades an object based on it's distance from the center of the scene.

public class InverseSqrShadow extends ColourShader{
    private Color colour; 
    private float shaderFactor;
    
    public InverseSqrShadow (Color colour) {
        this.colour = colour;
        this.shaderFactor = 0.2f;
    }

    public Color shadeBasedOnTriangle(Triangle triangle) {
        Vertex tV = averageTriangleAsVertex(triangle);
        int red = (int) Math.round(inverseSquare(tV.magnitude() * shaderFactor) * colour.getRed());
        int green = (int) Math.round(inverseSquare(tV.magnitude() * shaderFactor)* colour.getGreen());
        int blue = (int) Math.round(inverseSquare(tV.magnitude() * shaderFactor)* colour.getBlue());
        Color finalColour = new Color(red, green, blue);
        return finalColour;
    }
}
