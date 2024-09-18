package colours;

import core.Scene;
import java.awt.Color;
import objects.Triangle;
import objects.Vertex;

/**
 * The InverseSqrShadow class applies a shading effect based on the inverse square distance 
 * from the camera. This simulates a basic shadow effect where the further an object is 
 * from the camera, the darker it appears.
 */
public class InverseSqrShadow extends ColourShader {
    
    //The base colour of the object
    private Color colour;
    
    //Factor used to control the shading intensity based on distance
    private float shaderFactor;
    
    //Reference to the scene to get the camera's position
    private Scene scene;

    /**
     * Constructor to initialize the InverseSqrShadow shader with a base colour and reference to the scene.
     * 
     * @param colour The base colour of the object.
     * @param scene The scene reference used to get the camera's position.
     */
    public InverseSqrShadow(Color colour, Scene scene) {
        this.colour = colour; 
        this.shaderFactor = 0.03f; //Controls the intensity of the shading effect
        this.scene = scene; //Store the reference to the scene for camera position access
    }

    /**
     * Shades the triangle based on its distance from the camera.
     * The further the triangle is from the camera, the darker its final colour will be.
     * The shading is based on the inverse square law, reducing brightness with distance.
     * 
     * @param triangle The triangle to be shaded.
     * @return The final shaded colour of the triangle.
     */
    @Override
    public Color shadeBasedOnTriangle(Triangle triangle) {
        //Average the triangle's vertices into a single point for shading calculation
        Vertex tV = averageTriangleAsVertex(triangle);

        //Calculate the distance from the triangle to the camera
        Vertex diff = Vertex.difference(tV, scene.getCamPos());

        //Adjust the RGB values based on the inverse square of the distance and shaderFactor
        int red = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor) * colour.getRed());
        int green = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor) * colour.getGreen());
        int blue = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor) * colour.getBlue());

        //Return the final shaded colour
        Color finalColour = new Color(red, green, blue);
        return finalColour;
    }
}
