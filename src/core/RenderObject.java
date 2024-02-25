package core;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import colours.ColourShader;
import objects.Triangle;
import objects.Vertex;
/*
 * The RenderObject class represents an object in a scene.
 * It has a position, scale, name and colourShader and 
 * can be moved and scaled.
 */
public class RenderObject {
    
    private String name;
    private Vertex scale;
    private Vertex position;
    private ColourShader colourShader;

    private Triangle[] triangles;
    private Triangle[] adjustedTriangles;
    private Vertex[] vertices;
    
    private int tCount;
    
    //Constructor that initializes the object
    public RenderObject(String name, Triangle[] triangles, Vertex[] vertices, Vertex position, Vertex scale, ColourShader colourShader) {
        this.name = name;
        this.triangles = triangles;
        this.vertices = vertices;
        this.scale = scale;
        this.adjustedTriangles = new Triangle[triangles.length];
        this.colourShader = colourShader;
        this.tCount = triangles.length;
        this.position = position;
    }

    //This function returns the adjusted & scaled positions of all triangles
    public Triangle[] loadTriangles() {
        for (int i = 0; i < tCount; i++) {
            adjustedTriangles[i] = new Triangle(new Vertex(0, 0, 0), new Vertex(0, 0, 0), new Vertex(0, 0, 0));
        }
        adjustTriangles();
        return adjustedTriangles;
    }
    
    //This method calculates the adjusted & scaled positions of all triangles
    private void adjustTriangles() {
        for (int index = 0; index < tCount; index++){
            RenderObject.scale(adjustedTriangles[index], triangles[index], scale);
            RenderObject.adjust(adjustedTriangles[index], triangles[index] , position);
        }
    }

    //Normal getters
    public String getName() {
        return this.name;
    }

    public Vertex getPosition() {
        return position;
    }

    public Vertex getScale() {
        return scale;
    }

    public int getTCount() {
        return tCount;
    }

    public ColourShader getColour() {
        return colourShader;
    }


    //Normal setters
    public void setPosition(Vertex newPosition) {
        adjustTriangles();
        this.position = newPosition;
    }

    public void alterPosition(Vertex alteration) {
        adjustTriangles();
        this.position = Vertex.add(position, alteration);
    }

    public void setScale(Vertex newScale) {
        adjustTriangles();
        this.scale = newScale;
    }

    public void alterScale(Vertex alteration) {
        adjustTriangles();
        this.scale = Vertex.add(scale, alteration);
    }

    public void setColour(ColourShader colourShader) {
        this.colourShader = colourShader;
    }

    //This static method scales a triangle by some vertex 
    public static void scale(Triangle tAfter, Triangle tBefore, Vertex scale) {
        Vertex v1new = Vertex.multiply(tBefore.v1, scale);
        Vertex v2new = Vertex.multiply(tBefore.v2, scale);
        Vertex v3new = Vertex.multiply(tBefore.v3, scale);
        tAfter.v1 = v1new;
        tAfter.v2 = v2new;
        tAfter.v3 = v3new;
    }

    //This static method adjusts a triangle by some vertex 
    public static void adjust(Triangle tAfter, Triangle tBefore, Vertex adjustment) {
        Vertex v1new = Vertex.add(tBefore.v1, adjustment);
        Vertex v2new = Vertex.add(tBefore.v2, adjustment);
        Vertex v3new = Vertex.add(tBefore.v3, adjustment);
        tAfter.v1 = v1new;
        tAfter.v2 = v2new;
        tAfter.v3 = v3new;
    }

    //This static method loads in a obj file and parses it into a series of triangles
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
        
        try
        {
            reader = new BufferedReader(new FileReader(objPath));
            line = "";
            lineIndex = 1;
			while ((line = reader.readLine()) != null) {
                String[] lineS = line.split(" ");
                if (lineS[0].equals("v")) {
                    if (firstLineVertex == lastLineVertex) {
                        firstLineVertex = lineIndex;
                        lastLineVertex = lineIndex+1;
                    } else {
                        lastLineVertex = lineIndex;
                    }
                    
                } else if (lineS[0].equals("f")) {
                    if (firstLineFace == lastLineFace) {
                        firstLineFace = lineIndex;
                        lastLineFace = lineIndex+1;
                    } else {
                        lastLineFace = lineIndex;
                    }
                    
                }
                lineIndex += 1;
			}
            reader.close();

            vertices = new Vertex[lastLineVertex-firstLineVertex+1];
            triangles = new Triangle[lastLineFace-firstLineFace+1];

            reader = new BufferedReader(new FileReader(objPath));
			line = "";
            lineIndex = 1;
			while ((line = reader.readLine()) != null) {
                
                String[] lineS = line.split(" ");
                if (firstLineVertex <= lineIndex && lineIndex <= lastLineVertex) {
                    float valueOne = Float.parseFloat(lineS[1]);
                    float valueTwo = Float.parseFloat(lineS[2]);
                    float valueThree = Float.parseFloat(lineS[3]);
                    vertices[lineIndex-firstLineVertex] = new Vertex(valueOne, valueTwo, valueThree);
                } else if (firstLineFace <= lineIndex && lineIndex <= lastLineFace) {
                    int valueOne = Integer.parseInt(lineS[1].split("/")[0]);
                    int valueTwo = Integer.parseInt(lineS[2].split("/")[0]);
                    int valueThree = Integer.parseInt(lineS[3].split("/")[0]);
                    triangles[lineIndex-firstLineFace] = new Triangle(vertices[valueOne-1], vertices[valueTwo-1], vertices[valueThree-1]);
                }
                lineIndex += 1;
			}
            reader.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not found: " + e.getMessage());
		} catch (IOException e) {
			System.out.println("IO Exception: " + e.getMessage());
		}
        return new RenderObject(name, triangles, vertices, position, new Vertex(1, 1, 1), colourShader);
    }
}
