package colours;
import java.awt.Color;

import objects.Triangle;
import objects.Vertex;

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