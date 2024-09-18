import core.RenderObject;
import core.Scene;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.JPanel;
import objects.*;

public class ConsolePanel {

    //Constants for screen dimensions in characters
    private static final int SCREEN_WIDTH = 120;
    private static final int SCREEN_HEIGHT = 60;
    
    //Buffer to hold characters that will be displayed on the console
    private char[][] screenBuffer = new char[SCREEN_HEIGHT][SCREEN_WIDTH];

    //Scaling factors for the graphics. These control the size of rendered objects on the screen
    private float graphicsRatio = 0.5f; //Ratio for scaling dodecahedron
    private float verticalGraphicsRatio = graphicsRatio * -1; //Inverts the dodecahedron vertically

    //Scene and rendering details
    private Scene scene;
    private Color outline = Color.BLACK; //Outline color for objects
    private Font font = new Font( "SansSerif", Font.PLAIN, 23 ); //Font settings for rendering

    private Game game; //The game logic object

    /**
     * Constructor sets up the scene and initializes the game.
     * 
     * @param w Width of the panel (not used directly for console rendering).
     * @param h Height of the panel (not used directly for console rendering).
     */
    public ConsolePanel(int w, int h) { 
        //Initialize the scene with an empty list of render objects
        scene = new Scene(new ArrayList<RenderObject> (Arrays.asList()));
        
        //Create a new game instance, passing the scene and this console panel
        game = new Game(scene, this);
    }

    /**
     * Clears the screen buffer and prints a terminal command to clear the console.
     */
    private void clearScreenBuffer() {
        //Clear the console screen using terminal escape codes
        System.out.print("\033[H\033[2J");   
        System.out.flush();   
        
        //Fill the screen buffer with spaces (empty characters)
        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            Arrays.fill(screenBuffer[y], ' ');
        }
    }
    
    /**
     * This method is called every "frame" by the GFrame class when it refreshes and repaints.
     * It updates the game state and draws the current scene.
     * 
     * @param g Graphics object (not used for console-based rendering).
     */
    protected void paintComponent(Graphics g) {
        //Clear the screen buffer before rendering the next frame
        clearScreenBuffer();
        
        //Update the game state
        game.tick();
        
        //Draw the current scene
        drawSceneToScreen();
    }

    /**
     * Draws the outline of a triangle using a character to represent edges.
     * 
     * @param triangle2d The triangle to be outlined.
     * @param ch The character used for drawing the outline.
     */
    private void outlineTriangle(Triangle2D triangle2d, char ch) {
        //Get the x and y coordinates of the triangle's vertices
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();
    
        //Convert the triangle coordinates to screen coordinates
        int[] xScreen = fitAxisToScreen(xPoints, false);
        int[] yScreen = fitAxisToScreen(yPoints, true);
    
        //Draw the triangle's three edges
        drawLine(xScreen[0], yScreen[0], xScreen[1], yScreen[1], ch);
        drawLine(xScreen[1], yScreen[1], xScreen[2], yScreen[2], ch);
        drawLine(xScreen[2], yScreen[2], xScreen[0], yScreen[0], ch);
    }

    /**
     * Fills the interior of a triangle using a specified character.
     * 
     * @param triangle2d The triangle to be filled.
     * @param ch The character used to fill the triangle.
     */
    private void fillTriangle(Triangle2D triangle2d, char ch) {
        //Get the x and y coordinates of the triangle's vertices
        float[] xPoints = triangle2d.xValues();
        float[] yPoints = triangle2d.yValues();
    
        //Convert the triangle coordinates to screen coordinates
        int[] xScreen = fitAxisToScreen(xPoints, false);
        int[] yScreen = fitAxisToScreen(yPoints, true);
    
        //Rasterize the triangle (fill it in with the character)
        rasterizeTriangle(xScreen, yScreen, ch);
    }

    /**
     * Outputs the screen buffer to the console by printing each character to the terminal.
     */
    private void outputScreenBufferToConsole() {
        for (int y = 0; y < SCREEN_HEIGHT; y++) {
            for (int x = 0; x < SCREEN_WIDTH; x++) {
                //Print each character in the buffer
                System.out.print(screenBuffer[y][x]);
            }
            //Move to the next line after printing each row
            System.out.println();
        }
    }

    /**
     * Draws the entire scene to the screen buffer.
     * Clears the buffer, renders the scene, and outputs it to the console.
     */
    private void drawSceneToScreen() {
        //Clear the buffer for the next frame
        clearScreenBuffer();
    
        //Render the current scene
        scene.renderScene();
    
        //Retrieve the rendered triangles and other details
        Triangle2D[] trianglesToDisplay = scene.getRenderedTriangles();
        Color[] colours = scene.getColours();
        String[] names = scene.getNames();
        
        //Loop through all triangles in the scene and draw them
        for (int index = 0; index < scene.getCount(); index++) {
            if (trianglesToDisplay[index] != null) {
                //Outline the triangle with '*' and fill it with '#'
                outlineTriangle(trianglesToDisplay[index], '*');
                fillTriangle(trianglesToDisplay[index], '#');
            }
        }
    
        //Output the final rendered screen buffer to the console
        outputScreenBufferToConsole();
    }
    
    /**
     * Plots a character at a specific (x, y) position on the screen buffer.
     * 
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     * @param ch The character to plot.
     */
    private void plotPixel(int x, int y, char ch) {
        //Only plot the pixel if it's within the bounds of the screen
        if (x >= 0 && x < SCREEN_WIDTH && y >= 0 && y < SCREEN_HEIGHT) {
            screenBuffer[y][x] = ch;
        }
    }

    /**
     * Rasterizes (fills) a triangle using horizontal lines.
     * 
     * @param x The x-coordinates of the triangle vertices.
     * @param y The y-coordinates of the triangle vertices.
     * @param ch The character used to fill the triangle.
     */
    private void rasterizeTriangle(int[] x, int[] y, char ch) {
        //Sort the vertices by their y-coordinate
        int[] sortedIndices = sortIndicesByY(y);
        int x0 = x[sortedIndices[0]], y0 = y[sortedIndices[0]];
        int x1 = x[sortedIndices[1]], y1 = y[sortedIndices[1]];
        int x2 = x[sortedIndices[2]], y2 = y[sortedIndices[2]];
    
        //Compute the slopes of the triangle sides
        float invSlope1 = (y1 - y0 != 0) ? (float)(x1 - x0) / (y1 - y0) : 0;
        float invSlope2 = (y2 - y0 != 0) ? (float)(x2 - x0) / (y2 - y0) : 0;
    
        //Rasterize the lower part of the triangle (from y0 to y1)
        for (int scanY = y0; scanY <= y1; scanY++) {
            int xStart = (int)(x0 + (scanY - y0) * invSlope1);
            int xEnd = (int)(x0 + (scanY - y0) * invSlope2);
            drawHorizontalLine(xStart, xEnd, scanY, ch);
        }
    
        //Compute the slope for the upper part of the triangle (from y1 to y2)
        invSlope1 = (y2 - y1 != 0) ? (float)(x2 - x1) / (y2 - y1) : 0;
    
        //Rasterize the upper part of the triangle (from y1 to y2)
        for (int scanY = y1; scanY <= y2; scanY++) {
            int xStart = (int)(x1 + (scanY - y1) * invSlope1);
            int xEnd = (int)(x0 + (scanY - y0) * invSlope2);
            drawHorizontalLine(xStart, xEnd, scanY, ch);
        }
    }

    /**
     * Sorts the triangle vertices by their y-coordinate (ascending order).
     * 
     * @param y The y-coordinates of the vertices.
     * @return An array of indices sorted by their corresponding y-values.
     */
    private int[] sortIndicesByY(int[] y) {
        Integer[] indices = {0, 1, 2};
        //Sort indices by comparing the y-values
        Arrays.sort(indices, (i1, i2) -> Integer.compare(y[i1], y[i2]));
        return Arrays.stream(indices).mapToInt(i -> i).toArray();
    }

    /**
     * Draws a horizontal line between two x-coordinates at a specific y-coordinate.
     * 
     * @param xStart The starting x-coordinate.
     * @param xEnd The ending x-coordinate.
     * @param y The y-coordinate of the line.
     * @param ch The character used to draw the line.
     */
    private void drawHorizontalLine(int xStart, int xEnd, int y, char ch) {
        //Make sure the y-coordinate is within the screen bounds
        if (y < 0 || y >= SCREEN_HEIGHT) return;
        
        //Swap xStart and xEnd if xStart is greater than xEnd
        if (xStart > xEnd) {
            int temp = xStart;
            xStart = xEnd;
            xEnd = temp;
        }

        //Draw the line by plotting each character between xStart and xEnd
        for (int x = xStart; x <= xEnd; x++) {
            plotPixel(x, y, ch);
        }
    }

    /**
     * Draws a line between two points using Bresenham's line algorithm.
     * 
     * @param x0 Starting x-coordinate.
     * @param y0 Starting y-coordinate.
     * @param x1 Ending x-coordinate.
     * @param y1 Ending y-coordinate.
     * @param ch The character used to draw the line.
     */
    private void drawLine(int x0, int y0, int x1, int y1, char ch) {
        int dx = Math.abs(x1 - x0);
        int dy = -Math.abs(y1 - y0);
        int sx = x0 < x1 ? 1 : -1;
        int sy = y0 < y1 ? 1 : -1;
        int err = dx + dy;

        while (true) {
            //Plot the current pixel
            plotPixel(x0, y0, ch);

            //If the current point is the end point, stop drawing
            if (x0 == x1 && y0 == y1) break;

            //Adjust error and move in x or y direction
            int e2 = 2 * err;
            if (e2 >= dy) {
                err += dy;
                x0 += sx;
            }
            if (e2 <= dx) {
                err += dx;
                y0 += sy;
            }
        }
    }

    /**
     * Fits axis values to the screen size, converting normalized coordinates to screen coordinates.
     * 
     * @param axisValues The axis values to be scaled.
     * @param isYAxis Whether the values are for the Y-axis (true) or X-axis (false).
     * @return An array of screen coordinates.
     */
    private int[] fitAxisToScreen(float[] axisValues, boolean isYAxis) {
        int[] screenValues = new int[axisValues.length];
        for (int i = 0; i < axisValues.length; i++) {
            if (isYAxis) {
                //Convert normalized y-coordinate to screen y-coordinate
                screenValues[i] = (int)((1 - axisValues[i]) * (SCREEN_HEIGHT - 1) / 2);
            } else {
                //Convert normalized x-coordinate to screen x-coordinate
                screenValues[i] = (int)((axisValues[i] + 1) * (SCREEN_WIDTH - 1) / 2);
            }
        }
        return screenValues;
    }

    /**
     * Converts a value between 0 and 1 to the screen coordinate system.
     * 
     * @param value The value to be converted.
     * @param vertical Whether to convert for the vertical axis (true) or horizontal axis (false).
     * @return The corresponding screen coordinate.
     */
    private int valFromOneToScreen(float value, boolean vertical) {
        int bigAxis = Math.max(SCREEN_WIDTH, SCREEN_HEIGHT);
        //Scale the value based on whether it's for the vertical or horizontal axis
        if (vertical) {
            return (int) ((value / 2) * verticalGraphicsRatio * bigAxis + (SCREEN_HEIGHT / 2));
        } else {
            return (int) ((value / 2) * graphicsRatio * bigAxis + (SCREEN_WIDTH / 2));
        }
    }
}
