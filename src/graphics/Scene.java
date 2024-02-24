package graphics;

import graphics.colour.ColourShader;
import graphics.events.CameraEvent;
import graphics.objects.Triangle;
import graphics.objects.Triangle2D;
import graphics.objects.Vertex;
import graphics.objects.Vertex2D;
import java.awt.Color;
import java.util.ArrayList;

/*
 * The Scene object has RenderObjects in it.
 * A cameraPos and cameraRot and 
 * cameraEvents. When rendered
 * the triangles of the objects are translated 
 * based on the cameraPosition and 
 * cameraRotation when renderScene is called.
 * These triangles are sorted based on their 
 * distance from the camera so that the 
 * furthert ones can be drawn first. And the 
 * closest ones overwrite the further ones.
 */

public class Scene {
    private Triangle[] triangles; //The array of all triangles in the scene
    private ColourShader[] colours; //The array of the colours of each triangle in the scene
    private String[] objectNames; //The array of the object names of each triangle in the scene
    private Triangle2D[] triangles2DRendered; //The array of all rendered triangles in the scene
    private int triangleCount; //The number of triangles in the scene
    private ArrayList<RenderObject> objects = new ArrayList<>(); //The arraylist of objects in the scene. Easy to add more.
    private ArrayList<CameraEvent> cameraEvents = new ArrayList<>(); //The arraylist of cameraEvents. Easy to add more.

    private Vertex camPos = new Vertex(-3, -3, 3); //Camera position
    private Vertex screenPosRel = new Vertex(0, 0, 3.8f); //Screen relative to camera
    private Vertex camRotation = new Vertex(0, 0, 0); //Camera rotation
    private Vertex c = new Vertex(0, 0, 0); //The vertex of cos of camera rotation
    private Vertex s = new Vertex(0, 0, 0); //The vertex of sin of camera rotation

    //Constructs the Scene object
    public Scene(ArrayList<RenderObject> objects) {
        this.objects = objects;
        //Sets up all the arrays
        createArrays();
    }

    //Renders the scene as it currently is into triangle2d
    public Triangle2D[] renderScene() {
        followCameraEvents();
        reloadObjectsTriangles();
        sortTrianglesForRendering();
        generateCameraRot();
        int index = 0;
        for (Triangle triangle : triangles) {
            triangles2DRendered[index] = renderTriangle(triangle);
            index += 1;
        }
        return triangles2DRendered;
    }

    //This method follows along with the list of camera events
    private void followCameraEvents() {
        if (cameraEvents.size() != 0) {
            CameraEvent currentEvent = cameraEvents.get(0);
            if (currentEvent.getStarted() == false) {
                currentEvent.startTimer(camPos, camRotation);
            }
            camPos = currentEvent.calcCamPosition();
            camRotation = currentEvent.calcCamRotation();
            
            if (cameraEvents.get(0).getDone()) {
                cameraEvents.remove(0);
            }
        }
    }

    //This method reloads the triangles of the objects as they may have moved
    private void reloadObjectsTriangles() {
        int tIndex = 0;
        for (RenderObject object : objects) {
            for (Triangle triangle : object.getAdjustedTriangles()) {
                triangles[tIndex] = triangle;
                colours[tIndex] = object.getColour();
                objectNames[tIndex] = object.getName();
                tIndex += 1;
            }
        }
    }

    //This method sorts all the triangles in the scene based on their distance from the camera
    private void sortTrianglesForRendering() {
        float[] valArray = new float[triangleCount];
        for (int index = 0; index < triangleCount; index++) {
            float val = triangleValue(triangles[index]);
            valArray[index] = val;
        }

        int n = triangleCount;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < n-1; i++) {
                if (valArray[i] < valArray[i+1]) {
                    float temp = valArray[i];
                    valArray[i] = valArray[i+1];
                    valArray[i+1] = temp;

                    Triangle tempT = triangles[i];
                    triangles[i] = triangles[i+1];
                    triangles[i+1] = tempT;

                    ColourShader col = colours[i];
                    colours[i] = colours[i+1];
                    colours[i+1] = col;

                    String name = objectNames[i];
                    objectNames[i] = objectNames[i+1];
                    objectNames[i+1] = name;
                    sorted = false;
                } 
            }
            n--;
        }
    }

    //This method generates the rotation vertices for math later
    private void generateCameraRot() {
        s.x = (float) Math.sin(camRotation.x);
        s.y = (float) Math.sin(camRotation.y);
        s.z = (float) Math.sin(camRotation.z);
        c.x = (float) Math.cos(camRotation.x);
        c.y = (float) Math.cos(camRotation.y);
        c.z = (float) Math.cos(camRotation.z);
    }
    
    //This function takes a triangle and turns it into 2D
    public Triangle2D renderTriangle(Triangle t){
        Vertex2D v1 = renderVertex(t.v1);
        if (v1 == null) {
            return null;
        }
        Vertex2D v2 = renderVertex(t.v2);
        if (v2 == null) {
            return null;
        }
        Vertex2D v3 = renderVertex(t.v3);
        if (v3 == null) {
            return null;
        }
        
        return new Triangle2D(v1, v2, v3);
    }

    //This function renders a single vertex onto 2D
    public Vertex2D renderVertex(Vertex t){
        Vertex dif = Vertex.difference(t, camPos);
        float x = dif.x;
        float y = dif.y;
        float z = dif.z;
        //Math learned from [11] https://en.wikipedia.org/wiki/3D_projection
        float dX = c.y * (s.z * y + c.z * x) - s.y * z;
        float dY = s.x * (c.y * z + s.y * (s.z * y + c.z * x)) + c.x * (c.z * y - s.z * x);
        float dZ = c.x * (c.y * z + s.y * (s.z * y + c.z * x)) - s.x * (c.z * y - s.z * x);
        //Math ended

        if (dZ <= 0) { 
            return null;
        }

        float bX = (screenPosRel.z / dZ) * dX + screenPosRel.x;
        float bY = (screenPosRel.z / dZ) * dY + screenPosRel.y;
        return new Vertex2D(bX,bY);
    }

    // This function finds the average magnitude sqrd of a triangle in relation to the camera
    private float triangleValue(Triangle triangle) {
        float v1d = Vertex.difference(triangle.v1, camPos).magnitudeSqrd();
        float v2d = Vertex.difference(triangle.v2, camPos).magnitudeSqrd();
        float v3d = Vertex.difference(triangle.v3, camPos).magnitudeSqrd();
        return (v1d + v2d + v3d) / 3.0f;
    }

    //This function generates all the colours of the triangles based on the shader 
    public Color[] getColours() {
        Color[] newColours = new Color[triangleCount];
        for (int index = 0; index < triangleCount; index++) {
            newColours[index] = colours[index].shadeBasedOnTriangle(triangles[index]);
        }
        return newColours;
    }

    //This function returns an array of all the names of the objects
    public String[] getNames() {
        return objectNames;
    }

    //This function returns the number of triangles in the scene currently
    public int getCount() {
        return triangleCount;
    }


    //This method adds an object to the objects in the scene
    public void addObject(RenderObject objectToAdd) {
        objects.add(objectToAdd);
        createArrays();
    }

    //This method sets the list of objects in the scene
    public void setObjects(ArrayList<RenderObject> objectsToSet) {
        objects = objectsToSet;
        createArrays();
    }

    //This method resets the arrays when a new object is added
    private void createArrays() {
        int totalTCount = 0;
        for (RenderObject object : objects) {
            totalTCount += object.getTCount();
        }

        this.triangles = new Triangle[totalTCount];
        this.triangles2DRendered = new Triangle2D[totalTCount];
        this.colours = new ColourShader[totalTCount];
        this.objectNames = new String[totalTCount];
        this.triangleCount = totalTCount;
    }

    //This method adds a new cameraEvent to the list of cameraEvents
    public void addCameraEvent(CameraEvent cameraEvent) {
        cameraEvents.add(cameraEvent);
    }

    public void setCamPos(Vertex newPos) {
        camPos = newPos;
    }

    public Vertex getCamPos() {
        return camPos;
    }
    
    public void setCamRot(Vertex newRotation) {
        camRotation = newRotation;
    }

    public Vertex getCamRotation() {
        return camRotation;
    }
    
}
