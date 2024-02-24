



import javax.swing.JPanel;

import colours.*;
import core.RenderObject;
import core.Scene;
import events.CameraEvent;
import events.LinearCameraEvent;

import java.awt.event.MouseEvent;

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

    private float camDistance = 3.5f; //This is the distance from the camera to a cave/mini-dodecahedron/cavegraphic
    
    private Font miniDodFont = new Font( "SansSerif", Font.PLAIN, 23 ); //This is the font that swing will write the numbers of the mini-dodecahedron text
    private Game mult;

    //This constructor sets up the dodecahedron panel for rendering
    public GPanel() {  
        scene = new Scene(new ArrayList<RenderObject> (Arrays.asList()));
        
        mult = new Game(scene);
        scene.setObjects(mult.getObjects());
        scene.setCamPos(new Vertex(0,2,-8));
        scene.setCamRot(new Vertex(0, 0, 0));
    }

    //This method moves the camera to look at a given vertex
    protected void panCamToVertex(Vertex dest) {
        //This calculates the destination at camDistance * vertex position. This works as all the vertices are based around zero
        Vertex camDestination = Vertex.multiply(dest, new Vertex(camDistance, camDistance, camDistance));

        //Reference [7] https://bukkit.org/threads/how-do-i-get-yaw-and-pitch-from-a-vector.50317/
        float deltaX = dest.x;
        float deltaY = dest.y;
        float deltaZ = dest.z;

        
        float distance = (float) Math.sqrt(deltaZ * deltaZ + deltaX * deltaX);
        float pitch = (float) Math.atan2(deltaY, distance);
        float yaw = (float) Math.atan2(deltaX, deltaZ);
        //Reference end

        //We then use the CameraEvent framework we created to queue up a camera event for the scene at the correct destination and rotation
        //This moves the camera to the correct position over the space of one second
        scene.addCameraEvent(new LinearCameraEvent(
        null,
        camDestination,
        null,
        new Vertex(-pitch,yaw,0),
        1));
    }

    //This method is called every "frame" by the GameFrame class when it refreshes and repaints
    //This updates the graphics position on the screen
    protected void paintComponent(Graphics g)
    {
        //Fills background with black
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
        
        //Draw the current state of the scene to the JPanel screen
        drawSceneToScreen(g);
    }

    //This method draws the scene to the screen 
    private void drawSceneToScreen(Graphics g) {
        mult.refreshObjects();
        g.setFont(miniDodFont);
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
        //Scales axis to screen and then draws
        g.drawPolygon(fitAxisToScreen(xPoints, GraphicsRatio), fitAxisToScreen(yPoints, verticalGraphicsRatio), 3);
    }

    //This method draws an filled of the triangle2d as scaled to the screen
    private void fillTriangle(Triangle2D triangle2d, Color colour,  Graphics g) {
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();
        g.setColor(colour);
        //Scales axis to screen and then draws
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

    //This method fits some value to the screen. The value is less than one
    private int valFromOneToScreen(float value, float ratio) {
        //Scales based on screen width and height
        return (int) ((value/2) * ratio * Math.min(SCREEN_HEIGHT, SCREEN_WIDTH) + (Math.min(SCREEN_HEIGHT, SCREEN_WIDTH)/2));
    }
}
