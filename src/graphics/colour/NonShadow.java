package graphics.colour;
import java.awt.Color;

import graphics.objects.Triangle;
import graphics.objects.Vertex;

//The NonShadow class just returns the base colour of the object with no shadow.

public class NonShadow extends ColourShader{
    private Color colour; 
    
    public NonShadow (Color colour) {
        this.colour = colour;
    }

    public Color shadeBasedOnTriangle(Triangle triangle) {
        return colour;
    }
}