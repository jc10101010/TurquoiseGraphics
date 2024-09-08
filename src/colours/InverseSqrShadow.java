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
    /**
    * Returns the colour this triangle should be. 
    * Based on the position of the triangle, the rest of the scene.
    * 
    * In particular this function reduces the brightness of triangles
    * based on their proximity to the camera
    * 
    * @param  triangle  the triangle to be shaded
    * @return  finalColour the colour the triangle should be
    */
    public Color shadeBasedOnTriangle(Triangle triangle) {
        //Averages out triangle so it can be treated as one point
        Vertex tV = averageTriangleAsVertex(triangle);

        //Find distance to camera
        Vertex diff = Vertex.difference(tV, scene.getCamPos());
        
        //Scale the rgb values by the inverse square distance to the camera and the shaderFactor
        int red = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor) * colour.getRed());
        int green = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor)* colour.getGreen());
        int blue = (int) Math.round(inverseSquare(diff.magnitude() * shaderFactor)* colour.getBlue());

        Color finalColour = new Color(red, green, blue);
        return finalColour;
    }

}
