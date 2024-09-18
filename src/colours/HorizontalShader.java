package colours;
import java.awt.Color;
import objects.Triangle;
import objects.Vertex;

/**
 * HorizontalShader is a ColourShader that applies a shadow effect based on the x-axis.
 * The shader darkens parts of the object on the negative x-axis, simulating a light source from the positive x-axis.
 */
public class HorizontalShader extends ColourShader {
    
    //The base colour of the object
    private Color colour; 

    //Parameters to adjust the shading effect
    private float axisAdjust = 0.4f; //Shifts the point where shading begins on the x-axis
    private float inputMult = 0.5f; //Multiplier to control the intensity of the shadow effect
    
    /**
     * Constructor to initialize the HorizontalShader with a base colour.
     * 
     * @param colour The base colour of the object.
     */
    public HorizontalShader(Color colour) {
        this.colour = colour; //Store the base colour of the object
    }

    /**
     * Shades the triangle by adding a shadow effect based on its position on the negative x-axis.
     * The further the triangle is on the negative x-axis, the darker it becomes.
     * 
     * @param triangle The triangle to be shaded.
     * @return The final shaded colour of the triangle.
     */
    @Override
    public Color shadeBasedOnTriangle(Triangle triangle) {
        //Average the triangle's vertices into a single point to apply the shading
        Vertex tV = averageTriangleAsVertex(triangle);

        //Calculate a value based on the x and z position to determine the shading effect
        float axisValue = (tV.x + (-tV.z / 2)) / 1.5f;

        //Apply the shading to the RGB components based on the axis value
        int red = capRGB((int) Math.round(newShadow(inputMult * (axisValue - axisAdjust)) * colour.getRed()));
        int green = capRGB((int) Math.round(newShadow(inputMult * (axisValue - axisAdjust)) * colour.getGreen()));
        int blue = capRGB((int) Math.round(newShadow(inputMult * (axisValue - axisAdjust)) * colour.getBlue()));

        //If the triangle is not on the negative x-axis, return the base colour without shading
        if (axisValue > axisAdjust) {
            return colour;
        }

        //Return the final shaded colour based on the calculated RGB values
        Color finalColour = new Color(red, green, blue);
        return finalColour;
    }
}
