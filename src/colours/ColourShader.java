package colours;

import java.awt.Color;
import objects.Triangle;
import objects.Vertex;

/**
 * The ColourShader class is an abstract base class for shaders that process
 * a triangle's data and its base colour to produce a final shaded colour.
 * This simulates how pixel shaders work in rendering pipelines.
 */
public abstract class ColourShader {

    /**
     * Abstract method that must be implemented by subclasses to apply specific
     * shading logic based on the triangle's data.
     * 
     * @param triangle The triangle to be shaded.
     * @return The final shaded colour of the triangle.
     */
    public abstract Color shadeBasedOnTriangle(Triangle triangle);

    /**
     * Sigmoid function to normalize the input value `x` for shading purposes.
     * This is used to smoothly transition values, commonly in shading and lighting.
     * 
     * @param x The input value to process.
     * @return A float value between 0 and 2, transformed by a sigmoid function.
     */
    public static float sigmoid(float x) {
        float absX = Math.abs(x); //Take the absolute value of x
        return (float) (1 / (1 + Math.exp(-1 * absX))) * -2 + 2; //Return sigmoid-transformed value
    }
    
    /**
     * Calculates the inverse square of a given value.
     * This simulates the inverse square law, often used in physics and lighting, where light intensity decreases
     * exponentially as the distance from the source increases.
     * 
     * @param x The value to be inverse-squared.
     * @return The result of 1 / ( (abs(x) + 1) ^ 2 ), used for light falloff.
     */
    public static float inverseSquare(float x) {
        float absX = Math.abs(x); //Take the absolute value of x
        return 1 / ((absX + 1) * (absX + 1)); //Apply inverse square formula
    }

    /**
     * A shadow function that adjusts the input value `x` to simulate a light falloff.
     * This function is customized to create shadow effects based on the triangle's position.
     * 
     * @param x The input value.
     * @return A transformed value representing the shadow intensity.
     */
    public static float newShadow(float x) {
        float res = 1 / (-x + 1.4f); //Custom shadow calculation based on input value
        return Math.abs(res); //Return the absolute value to ensure positive results
    }

    /**
     * Caps the RGB values to ensure they remain within the valid range of 0 to 255.
     * This is crucial to avoid rendering colours with invalid values.
     * 
     * @param inp The input RGB value.
     * @return A capped value within the range [0, 255].
     */
    public static int capRGB(int inp) {
        if (inp > 255) {
            return 255; //Cap the value at 255 if it's too high
        } else if (inp < 0) {
            return 0; //Cap the value at 0 if it's negative
        } else {
            return inp; //Return the input value if it's already within the valid range
        }
    }

    /**
     * Calculates the average position of a triangle by averaging the three vertices.
     * This is often used in shading to treat the triangle as a single point for lighting calculations.
     * 
     * @param triangle The triangle whose average position is calculated.
     * @return A Vertex representing the average position of the triangle.
     */
    public static Vertex averageTriangleAsVertex(Triangle triangle) {
        //Add the three vertices and divide by 3 to get the average position
        Vertex avgVertex = Vertex.divide(Vertex.add(Vertex.add(triangle.v1, triangle.v2), triangle.v3), 3.0f);
        return avgVertex;
    }
}
