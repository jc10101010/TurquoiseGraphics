import javax.swing.JPanel;

import core.RenderObject;
import core.Scene;
import events.LinearCameraEvent;

import objects.*;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.awt.Color;
import java.awt.Font;

public class GPanel extends JPanel{

    //Screen width and height for the panel
    private final int SCREEN_WIDTH;
    private final int SCREEN_HEIGHT;

    //Graphics ratio determines the size of the dodecahedron on the screen
    private float graphicsRatio = 0.5f; //Ratio that the dodecahedron takes up on the screen
    private float verticalGraphicsRatio = graphicsRatio * -1; //Inverts the dodecahedron vertically for correct orientation

    //Scene containing objects to be rendered
    private Scene scene;
    //Outline color for all objects (drawn with Swing)
    private Color outline = Color.BLACK;

    //Font used for rendering text
    private Font font = new Font( "SansSerif", Font.PLAIN, 23 );

    //Game instance to handle game logic
    private Game game;

    /**
     * Constructor to initialize the game panel with given dimensions.
     * Initializes the scene and sets up the game for rendering.
     * @param w The width of the screen.
     * @param h The height of the screen.
     */
    public GPanel(int w, int h) {  
        SCREEN_WIDTH = w;
        SCREEN_HEIGHT = h;
        //Initializes an empty scene with no renderable objects
        scene = new Scene(new ArrayList<RenderObject> (Arrays.asList()));
        
        //Initializes the game logic with the current scene and this GPanel instance
        game = new Game(scene, this);
    }

    /**
     * Called every "frame" by the GFrame class when it refreshes and repaints.
     * It handles updating the game state and rendering the new scene.
     * @param g The Graphics object used for drawing.
     */
    @Override
    protected void paintComponent(Graphics g) {
        //Fills background with black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //Updates the game state (e.g., physics, logic)
        game.tick();
        
        //Renders the updated scene to the screen
        drawSceneToScreen(g);
    }

    /**
     * Performs all projection calculations and draws the scene to the screen.
     * Projects 3D objects to 2D and displays the rendered triangles.
     * @param g The Graphics object used for drawing.
     */
    private void drawSceneToScreen(Graphics g) {
        //Set the font for rendering text
        g.setFont(font);
        
        //Renders the scene with the current camera view and objects' state
        scene.renderScene(); 
        
        //Get the triangles, colors, and names of objects to display
        Triangle2D[] trianglesToDisplay = scene.getRenderedTriangles();
        Color[] colours = scene.getColours();
        String[] names = scene.getNames();

        //Loop through each triangle and draw it on the screen
        for (int index = 0; index < scene.getCount(); index++) {
            if (trianglesToDisplay[index] != null) {
                //Draw the outline of the triangle
                outlineTriangle(trianglesToDisplay[index], outline, g);
                //Fill the triangle with the appropriate color
                fillTriangle(trianglesToDisplay[index], colours[index], g);
            }
        }
    }

    /**
     * Draws an outline of the given Triangle2D, scaled to the screen size.
     * @param triangle2d The triangle to outline.
     * @param colour The color of the outline.
     * @param g The Graphics object used for drawing.
     */
    private void outlineTriangle(Triangle2D triangle2d, Color colour, Graphics g) {
        //Get the x and y coordinates of the triangle vertices
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();

        //Set the outline color and draw the triangle
        g.setColor(colour);
        g.drawPolygon(fitAxisToScreen(xPoints, false), fitAxisToScreen(yPoints, true), 3);
    }

    /**
     * Fills the given Triangle2D with the specified color, scaled to the screen size.
     * @param triangle2d The triangle to fill.
     * @param colour The fill color.
     * @param g The Graphics object used for drawing.
     */
    private void fillTriangle(Triangle2D triangle2d, Color colour, Graphics g) {
        //Get the x and y coordinates of the triangle vertices
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();

        //Set the fill color and draw the triangle
        g.setColor(colour);
        g.fillPolygon(fitAxisToScreen(xPoints, false), fitAxisToScreen(yPoints, true), 3);
    }

    /**
     * Scales the given array of axis values to fit within the screen dimensions.
     * Converts the 3D coordinates to 2D screen coordinates.
     * @param axisVal The array of values to scale.
     * @param vertical True if scaling for the vertical axis, false for horizontal.
     * @return An array of screen coordinates.
     */
    private int[] fitAxisToScreen(float[] axisVal, boolean vertical) {
        //Create an array for the scaled axis values
        int[] fittedAxisVal = new int[3];
        for (int i = 0; i < 3; i++) {
            //Scale each value to fit the screen
            fittedAxisVal[i] = valFromOneToScreen(axisVal[i], vertical);
        }
        return fittedAxisVal;
    }

    /**
     * Scales a single value between 0 and 1 to fit within the screen dimensions.
     * @param value The value to scale.
     * @param vertical True if scaling for the vertical axis, false for horizontal.
     * @return The corresponding screen coordinate.
     */
    private int valFromOneToScreen(float value, boolean vertical) {
        //Get the larger dimension (width or height) for scaling
        int bigAxis = Math.max(SCREEN_WIDTH, SCREEN_HEIGHT);

        //If scaling vertically, apply verticalGraphicsRatio, else use graphicsRatio
        if (vertical) {
            return (int) ((value / 2) * verticalGraphicsRatio * bigAxis + (SCREEN_HEIGHT / 2));
        } else {
            return (int) ((value / 2) * graphicsRatio * bigAxis + (SCREEN_WIDTH / 2));
        }
    }
}
