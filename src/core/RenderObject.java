package core;

import colours.ColourShader;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import objects.Triangle;
import objects.Vertex;

/**
 * The RenderObject class represents an object in a 3D scene that can be transformed
 * (moved, rotated, scaled) and rendered as a collection of triangles. It supports loading 
 * objects from .obj files and transforming their geometry.
 */
public class RenderObject {

    //Basic properties of the render object
    private String name; //Name of the object
    private Vertex scale; //Scale of the object
    private Vertex rotation; //Rotation of the object (x, y, z angles)
    private Vertex position; //Position of the object in the scene
    private ColourShader colourShader; //Colour shader to apply for rendering

    //Triangles that make up the object
    private Triangle[] triangles; //Original triangles of the object
    private Triangle[] adjustedTriangles; //Transformed triangles after scaling, rotation, and translation

    private int tCount; //Number of triangles in the object

    //Precomputed sine and cosine values for the object's rotation
    private Vertex c = new Vertex(0, 0, 0); //The vertex storing cosine of the rotation angles
    private Vertex s = new Vertex(0, 0, 0); //The vertex storing sine of the rotation angles

    /**
     * Constructor to initialize the render object with a name, triangles, position, scale, rotation, and colour shader.
     * 
     * @param name The name of the object.
     * @param triangles The triangles that form the object.
     * @param position The position of the object in 3D space.
     * @param scale The scale of the object.
     * @param rotation The rotation of the object.
     * @param colourShader The shader used to apply colours to the object.
     */
    public RenderObject(String name, Triangle[] triangles, Vertex position, Vertex scale, Vertex rotation, ColourShader colourShader) {
        this.name = name;
        this.triangles = triangles;
        this.scale = scale;
        this.adjustedTriangles = new Triangle[triangles.length]; //Create array for transformed triangles
        this.colourShader = colourShader;
        this.tCount = triangles.length;
        this.position = position;
        this.rotation = rotation;

        //Initialize adjusted triangles with placeholder triangles
        for (int i = 0; i < tCount; i++) {
            adjustedTriangles[i] = new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, 0), new Vertex(0, 0, 0));
        }
    }

    /**
     * Returns the adjusted (transformed) triangles of the object.
     * 
     * @return An array of transformed triangles.
     */
    public Triangle[] loadTriangles() {
        adjustTriangles(); //Recalculate positions, scales, and rotations of the triangles
        return adjustedTriangles;
    }

    /**
     * Adjusts the positions, scales, and rotations of all triangles in the object.
     */
    private void adjustTriangles() {
        generateObjectRotation(); //Precompute the sine and cosine for rotation
        //Apply scaling, rotation, and translation to all triangles
        for (int index = 0; index < tCount; index++) {
            scale(adjustedTriangles[index], triangles[index]); //Scale the triangle
            rotate(adjustedTriangles[index], adjustedTriangles[index]); //Rotate the triangle
            adjust(adjustedTriangles[index], adjustedTriangles[index]); //Translate the triangle
        }
    }

    //Getters for object properties
    public String getName() {
        return this.name;
    }

    public Vertex getPosition() {
        return position;
    }

    public Vertex getScale() {
        return scale;
    }

    public Vertex getRotation() {
        return rotation;
    }

    public int getTCount() {
        return tCount;
    }

    public ColourShader getColour() {
        return colourShader;
    }

    //Setters for object properties with automatic adjustment of triangles
    public void setPosition(Vertex newPosition) {
        this.position = newPosition;
        adjustTriangles(); //Recalculate adjusted triangles based on new position
    }

    public void alterPosition(Vertex alteration) {
        this.position = Vertex.add(position, alteration);
        adjustTriangles(); //Recalculate adjusted triangles based on updated position
    }

    public void setScale(Vertex newScale) {
        this.scale = newScale;
        adjustTriangles(); //Recalculate adjusted triangles based on new scale
    }

    public void alterScale(Vertex alteration) {
        this.scale = Vertex.add(scale, alteration);
        adjustTriangles(); //Recalculate adjusted triangles based on updated scale
    }

    public void setRotation(Vertex newRotation) {
        this.rotation = newRotation;
        adjustTriangles(); //Recalculate adjusted triangles based on new rotation
    }

    public void alterRotation(Vertex alteration) {
        this.rotation = Vertex.add(rotation, alteration);
        adjustTriangles(); //Recalculate adjusted triangles based on updated rotation
    }

    public void setColour(ColourShader colourShader) {
        this.colourShader = colourShader;
    }

    /**
     * Scales a triangle by the object's scale factor.
     * 
     * @param tAfter The triangle after scaling.
     * @param tBefore The original triangle before scaling.
     */
    public void scale(Triangle tAfter, Triangle tBefore) {
        Vertex v1new = Vertex.multiply(tBefore.v1, scale);
        Vertex v2new = Vertex.multiply(tBefore.v2, scale);
        Vertex v3new = Vertex.multiply(tBefore.v3, scale);
        tAfter.v1 = v1new;
        tAfter.v2 = v2new;
        tAfter.v3 = v3new;
    }

    /**
     * Translates (adjusts) a triangle by the object's position.
     * 
     * @param tAfter The triangle after translation.
     * @param tBefore The original triangle before translation.
     */
    public void adjust(Triangle tAfter, Triangle tBefore) {
        Vertex v1new = Vertex.add(tBefore.v1, position);
        Vertex v2new = Vertex.add(tBefore.v2, position);
        Vertex v3new = Vertex.add(tBefore.v3, position);
        tAfter.v1 = v1new;
        tAfter.v2 = v2new;
        tAfter.v3 = v3new;
    }

    /**
     * Precomputes the sine and cosine values of the object's rotation for efficient rotation calculations.
     */
    private void generateObjectRotation() {
        s.x = (float) Math.sin(-rotation.x);
        s.y = (float) Math.sin(-rotation.y);
        s.z = (float) Math.sin(-rotation.z);
        c.x = (float) Math.cos(-rotation.x);
        c.y = (float) Math.cos(-rotation.y);
        c.z = (float) Math.cos(-rotation.z);
    }

    /**
     * Rotates a triangle using the precomputed sine and cosine values.
     * 
     * @param tAfter The triangle after rotation.
     * @param tBefore The original triangle before rotation.
     */
    public void rotate(Triangle tAfter, Triangle tBefore) {
        Vertex v1new = Vertex.rotateWithSinCos(tBefore.v1, s, c);
        Vertex v2new = Vertex.rotateWithSinCos(tBefore.v2, s, c);
        Vertex v3new = Vertex.rotateWithSinCos(tBefore.v3, s, c);
        tAfter.v1 = v1new;
        tAfter.v2 = v2new;
        tAfter.v3 = v3new;
    }

    /**
     * Loads a 3D object from an .obj file and parses it into a series of triangles.
     * 
     * @param objPath The path to the .obj file.
     * @param name The name of the object.
     * @param colourShader The shader to apply to the object.
     * @param position The initial position of the object.
     * @return A new RenderObject loaded from the file.
     */
    public static RenderObject loadObject(String objPath, String name, ColourShader colourShader, Vertex position) {
        int firstLineVertex = 1;
        int lastLineVertex = 1;
        int firstLineFace = 1;
        int lastLineFace = 1;
        int lineIndex = 1;
        Vertex[] vertices = null;
        Triangle[] triangles = null;
        BufferedReader reader = null;
        String line;

        try {
            reader = new BufferedReader(new FileReader(objPath));
            line = "";

            //Parse the .obj file to find vertices and faces
            while ((line = reader.readLine()) != null) {
                String[] lineS = line.split(" ");
                if (lineS[0].equals("v")) { //Vertex line
                    if (firstLineVertex == lastLineVertex) {
                        firstLineVertex = lineIndex;
                        lastLineVertex = lineIndex + 1;
                    } else {
                        lastLineVertex = lineIndex;
                    }
                } else if (lineS[0].equals("f")) { //Face line
                    if (firstLineFace == lastLineFace) {
                        firstLineFace = lineIndex;
                        lastLineFace = lineIndex + 1;
                    } else {
                        lastLineFace = lineIndex;
                    }
                }
                lineIndex += 1;
            }
            reader.close();

            //Allocate memory for vertices and triangles
            vertices = new Vertex[lastLineVertex - firstLineVertex + 1];
            triangles = new Triangle[lastLineFace - firstLineFace + 1];

            reader = new BufferedReader(new FileReader(objPath));
            line = "";
            lineIndex = 1;

            //Parse the .obj file and extract vertex and face data
            while ((line = reader.readLine()) != null) {
                String[] lineS = line.split(" ");
                if (firstLineVertex <= lineIndex && lineIndex <= lastLineVertex) {
                    //Parse vertex positions
                    float valueOne = Float.parseFloat(lineS[1]);
                    float valueTwo = Float.parseFloat(lineS[2]);
                    float valueThree = Float.parseFloat(lineS[3]);
                    vertices[lineIndex - firstLineVertex] = new Vertex(valueOne, valueTwo, valueThree);
                } else if (firstLineFace <= lineIndex && lineIndex <= lastLineFace) {
                    //Parse face (triangle) indices
                    int valueOne = Integer.parseInt(lineS[1].split("/")[0]);
                    int valueTwo = Integer.parseInt(lineS[2].split("/")[0]);
                    int valueThree = Integer.parseInt(lineS[3].split("/")[0]);
                    triangles[lineIndex - firstLineFace] = new Triangle(vertices[valueOne - 1], vertices[valueTwo - 1], vertices[valueThree - 1]);
                }
                lineIndex += 1;
            }
            reader.close();

        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
            e.printStackTrace();
        } catch (IOException e) {
            System.out.println("IO Exception: " + e.getMessage());
            e.printStackTrace();
        }

        //Create and return the RenderObject with loaded triangles
        return new RenderObject(name, triangles, position, new Vertex(1, 1, 1), new Vertex(0, 0, 0), colourShader);
    }
}
