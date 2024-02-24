package colours;
import java.awt.Color;

import objects.Triangle;
import objects.Vertex;

//A HorizontalShader is a ColourShader that adds a shadow on the nagtive x-axis.

public class HorizontalShader extends ColourShader{
    private Color colour; 
    private float axisAdjust = 0.4f;
    private float inputMult = 0.5f;
    
    public HorizontalShader (Color colour) {
        this.colour = colour;
    }

    public Color shadeBasedOnTriangle(Triangle triangle) {
        Vertex tV = averageTriangleAsVertex(triangle);
        float axisValue = (tV.x + -tV.z/2)/1.5f;
        int red = capRGB((int) Math.round(newShadow(inputMult *( axisValue - axisAdjust)) * colour.getRed()));
        int green = capRGB((int) Math.round(newShadow(inputMult *( axisValue - axisAdjust)) * colour.getGreen()));
        int blue = capRGB((int) Math.round(newShadow(inputMult *( axisValue - axisAdjust)) * colour.getBlue()));
        if (axisValue > axisAdjust) {
            return colour;
        }
        
        Color finalColour = new Color(red, green, blue);
        return finalColour;
    }
}
