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

    private final int SCREEN_WIDTH = 700;
    private final int SCREEN_HEIGHT = 700;

    private float GraphicsRatio = 0.5f; //Ratio that dodecahedrom takes up on the screen
    private float verticalGraphicsRatio = GraphicsRatio * -1; //Flips the dodecahedron vertically as it is mapped upside down

    private Scene scene;
    private Color outline = Color.BLACK; //This is the colour that swing draws the outline of all objects
    
    private Font font = new Font( "SansSerif", Font.PLAIN, 23 ); 
    private Game game;

    //This constructor sets up game for rendering
    public GPanel() {  
        scene = new Scene(new ArrayList<RenderObject> (Arrays.asList()));
        game = new Game(scene);
    }

    //This method is called every "frame" by the GFrame class when it refreshes and repaints
    protected void paintComponent(Graphics g)
    {
        //Fills background with black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);

        //Tick the game
        game.tick();
        
        //Draw the current state of the scene to the GPanel screen
        drawSceneToScreen(g);
    }

    //This method draws the scene to the screen 
    private void drawSceneToScreen(Graphics g) {

        Triangle2D[] trianglesToDisplay = scene.renderScene();
        Color[] colours = scene.getColours();
        String[] names = scene.getNames();

        for (int index = 0; index < scene.getCount(); index++) {
            if (trianglesToDisplay[index] != null) {
                outlineTriangle(trianglesToDisplay[index], outline, g);
                fillTriangle(trianglesToDisplay[index], colours[index], g);
            }
        }
    }

    //This method draws an outline of the triangle2d as scaled to the screen
    private void outlineTriangle(Triangle2D triangle2d, Color colour,  Graphics g) {
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();

        g.setColor(colour);
        g.drawPolygon(fitAxisToScreen(xPoints, GraphicsRatio), fitAxisToScreen(yPoints, verticalGraphicsRatio), 3);
    }

    //This method draws an filled of the triangle2d as scaled to the screen
    private void fillTriangle(Triangle2D triangle2d, Color colour,  Graphics g) {
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();

        g.setColor(colour);
        g.fillPolygon(fitAxisToScreen(xPoints, GraphicsRatio), fitAxisToScreen(yPoints, verticalGraphicsRatio), 3);
    }

    //This method fits all values in array to the screen
    private int[] fitAxisToScreen(float[] axisVal, float axisRatio) {
        int[] fittedAxisVal = new int[3];
        for (int i = 0; i < 3; i++) {
            fittedAxisVal[i] = valFromOneToScreen(axisVal[i], axisRatio);
        }
        return fittedAxisVal;
    }

    //This method fits some value to the screen. The value is betwwen zero and one
    private int valFromOneToScreen(float value, float ratio) {
        //Scales based on screen width and height
        return (int) ((value/2) * ratio * Math.min(SCREEN_HEIGHT, SCREEN_WIDTH) + (Math.min(SCREEN_HEIGHT, SCREEN_WIDTH)/2));
    }
}
