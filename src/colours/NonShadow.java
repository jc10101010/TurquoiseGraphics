package colours;

import java.awt.Color;
import objects.Triangle;

/**
 * The NonShadow class is a simple ColourShader implementation that returns 
 * the base colour of an object without applying any shadow effects.
 */
public class NonShadow extends ColourShader {
    
    //The base colour of the object
    private Color colour; 
    
    /**
     * Constructor to initialize the NonShadow shader with a base colour.
     * 
     * @param colour The base colour to apply to the object.
     */
    public NonShadow(Color colour) {
        this.colour = colour; //Store the provided base colour
    }

    /**
     * Returns the base colour of the object, ignoring any shadow effects.
     * This method overrides the shading behavior and simply returns the specified colour.
     * 
     * @param triangle The triangle being shaded (unused in this implementation).
     * @return The base colour of the object.
     */
    @Override
    public Color shadeBasedOnTriangle(Triangle triangle) {
        //Return the base colour without applying any shading logic
        return colour;
    }
}
