package core;

import colours.ColourShader;
import events.CameraEvent;
import java.awt.Color;
import java.util.ArrayList;
import objects.Triangle;
import objects.Triangle2D;
import objects.Vertex;
import objects.Vertex2D;

/**
 * The Scene class represents a 3D scene that contains RenderObjects. 
 * It handles rendering, camera movement, and sorting objects for correct rendering order.
 */
public class Scene {

    //Parallel arrays to store triangles, their colours, and object names
    private Triangle[] triangles; //The array of all triangles in the scene
    private ColourShader[] colours; //The array of colours of each triangle
    private String[] objectNames; //The array of object names corresponding to each triangle

    //Arrays to store the rendered 2D triangles and their final colours
    private Triangle2D[] triangles2DRendered; //The array of rendered (2D) triangles
    private Color[] finalColours; //The array of final colours for rendered triangles

    private int triangleCount; //The total number of triangles in the scene

    //Lists to hold objects and camera events in the scene
    private ArrayList<RenderObject> objects = new ArrayList<>(); //List of all objects in the scene
    private ArrayList<CameraEvent> cameraEvents = new ArrayList<>(); //List of camera events in the scene

    //Camera properties
    private Vertex camPos = new Vertex(0, 0, 0); //Camera position
    private Vertex screenPosRel = new Vertex(0, 0, 1.2f); //Position of the screen relative to the camera
    private Vertex camRotation = new Vertex(0, 0, 0); //Camera rotation (x, y, z)
    
    //Precomputed sine and cosine of the camera's rotation
    private Vertex c = new Vertex(0, 0, 0); //Cosine of camera rotation
    private Vertex s = new Vertex(0, 0, 0); //Sine of camera rotation

    /**
     * Constructs the Scene object with a list of RenderObjects.
     * 
     * @param objects The list of objects in the scene.
     */
    public Scene(ArrayList<RenderObject> objects) {
        this.objects = objects;
        recreateArrays(); //Initialize arrays for triangles, colours, and object names
    }

    /**
     * Renders the current state of the scene by following camera events, generating camera rotation,
     * sorting triangles by distance from the camera, and rendering them.
     */
    public void renderScene() {
        followCameraEvents(); //Follow camera events to update camera position and rotation
        generateCameraRot(); //Generate sine and cosine values for the current camera rotation
        sortTrianglesForRendering(); //Sort triangles by their distance from the camera
        renderTriangles(); //Render the triangles
    }

    /**
     * Follows the current camera events and updates the camera's position and rotation.
     * Removes completed events from the list.
     */
    private void followCameraEvents() {
        if (cameraEvents.size() != 0) {
            CameraEvent currentEvent = cameraEvents.get(0);

            //If the event hasn't started, initialize it
            if (!currentEvent.getStarted()) {
                currentEvent.startTimer(camPos, camRotation);
            }

            //Update camera position and rotation based on the event
            camPos = currentEvent.calcCamPosition();
            camRotation = currentEvent.calcCamRotation();
            
            //Remove the event if it is done
            if (currentEvent.getDone()) {
                cameraEvents.remove(0);
            }
        }
    }

    /**
     * Precomputes the sine and cosine of the camera's rotation for use in rendering.
     */
    private void generateCameraRot() {
        s.x = (float) Math.sin(camRotation.x);
        s.y = (float) Math.sin(camRotation.y);
        s.z = (float) Math.sin(camRotation.z);
        c.x = (float) Math.cos(camRotation.x);
        c.y = (float) Math.cos(camRotation.y);
        c.z = (float) Math.cos(camRotation.z);
    }

    /**
     * Sorts the triangles in the scene based on their distance from the camera for proper rendering order.
     */
    private void sortTrianglesForRendering() {
        float[] valArray = new float[triangleCount]; //Array to store distances of each triangle from the camera
        
        //Calculate the distance of each triangle from the camera
        for (int index = 0; index < triangleCount; index++) {
            float val = triangleValue(triangles[index]);
            valArray[index] = val;
        }

        //Perform a simple bubble sort on triangles based on their distance from the camera
        int n = triangleCount;
        boolean sorted = false;
        while (!sorted) {
            sorted = true;
            for (int i = 0; i < n - 1; i++) {
                if (valArray[i] < valArray[i + 1]) {
                    //Swap the distance values
                    float temp = valArray[i];
                    valArray[i] = valArray[i + 1];
                    valArray[i + 1] = temp;

                    //Swap the corresponding triangles, colours, and object names
                    Triangle tempT = triangles[i];
                    triangles[i] = triangles[i + 1];
                    triangles[i + 1] = tempT;

                    ColourShader col = colours[i];
                    colours[i] = colours[i + 1];
                    colours[i + 1] = col;

                    String name = objectNames[i];
                    objectNames[i] = objectNames[i + 1];
                    objectNames[i + 1] = name;

                    sorted = false;
                }
            }
            n--;
        }
    }

    /**
     * Renders the triangles by projecting them into 2D space and shading them based on their distance and colour.
     */
    private void renderTriangles() {
        triangles2DRendered = new Triangle2D[triangleCount]; //Array for rendered 2D triangles
        finalColours = new Color[triangleCount]; //Array for final triangle colours

        int index = 0;
        for (Triangle triangle : triangles) {
            if (triangle != null && colours[index] != null) {
                //Project the triangle into 2D space and calculate its final colour
                triangles2DRendered[index] = renderTriangle(triangle);
                finalColours[index] = colours[index].shadeBasedOnTriangle(triangles[index]);
                index++;
            }
        }
    }

    /**
     * Projects a 3D triangle into 2D space.
     * 
     * @param t The triangle to project.
     * @return The corresponding 2D triangle, or null if it can't be projected.
     */
    public Triangle2D renderTriangle(Triangle t) {
        Vertex2D v1 = renderVertex(t.v1);
        if (v1 == null) return null;

        Vertex2D v2 = renderVertex(t.v2);
        if (v2 == null) return null;

        Vertex2D v3 = renderVertex(t.v3);
        if (v3 == null) return null;

        return new Triangle2D(v1, v2, v3);
    }

    /**
     * Projects a 3D vertex into 2D space.
     * 
     * @param t The vertex to project.
     * @return The corresponding 2D vertex, or null if it can't be projected.
     */
    public Vertex2D renderVertex(Vertex t) {
        Vertex dif = Vertex.difference(t, camPos); //Calculate the vector from the camera to the vertex
        Vertex d = Vertex.rotateWithSinCos(dif, s, c); //Rotate the vertex around the camera's position

        //If the vertex is behind the camera, don't render it
        if (d.z <= 0) return null;

        //Perspective projection calculation to convert 3D coordinates into 2D screen space
        float bX = (screenPosRel.z / d.z) * d.x + screenPosRel.x;
        float bY = (screenPosRel.z / d.z) * d.y + screenPosRel.y;
        return new Vertex2D(bX, bY);
    }

    /**
     * Calculates the average squared distance of a triangle's vertices from the camera.
     * 
     * @param triangle The triangle to calculate the distance for.
     * @return The average squared distance of the triangle's vertices from the camera.
     */
    private float triangleValue(Triangle triangle) {
        if (triangle == null) return 0;
        
        //Calculate squared distances of the triangle's vertices from the camera
        float v1d = Vertex.difference(triangle.v1, camPos).magnitudeSqrd();
        float v2d = Vertex.difference(triangle.v2, camPos).magnitudeSqrd();
        float v3d = Vertex.difference(triangle.v3, camPos).magnitudeSqrd();
        return (v1d + v2d + v3d) / 3.0f; //Return the average squared distance
    }

    //Getter methods for triangles, colours, object names, and triangle count
    public Triangle2D[] getRenderedTriangles() {
        return triangles2DRendered;
    }

    public Color[] getColours() {
        return finalColours;
    }

    public String[] getNames() {
        return objectNames;
    }

    public int getCount() {
        return triangleCount;
    }

    /**
     * Adds a RenderObject to the scene and updates the arrays of triangles, colours, and names.
     * 
     * @param objectToAdd The object to add to the scene.
     */
    public void addObject(RenderObject objectToAdd) {
        objects.add(objectToAdd);
        recreateArrays();
        reloadObjectsTriangles();
    }

    /**
     * Sets a new list of objects in the scene and reloads their triangles.
     * 
     * @param objectsToSet The new list of objects to set in the scene.
     */
    public void setObjects(ArrayList<RenderObject> objectsToSet) {
        objects = objectsToSet;
        recreateArrays();
        reloadObjectsTriangles();
    }

    /**
     * Recreates the arrays to store triangle data, colours, and object names when new objects are added.
     */
    private void recreateArrays() {
        int totalTCount = 0;
        
        //Calculate the total number of triangles from all objects
        for (RenderObject object : objects) {
            totalTCount += object.getTCount();
        }

        //Allocate memory for the arrays based on the total triangle count
        triangles = new Triangle[totalTCount];
        triangles2DRendered = new Triangle2D[totalTCount];
        colours = new ColourShader[totalTCount];
        objectNames = new String[totalTCount];
        triangleCount = totalTCount;
    }

    /**
     * Reloads the triangles, colours, and object names of all objects in the scene.
     */
    public void reloadObjectsTriangles() {
        int tIndex = 0;
        for (RenderObject object : objects) {
            for (Triangle triangle : object.loadTriangles()) {
                //Load the triangles of the object into the scene arrays
                triangles[tIndex] = triangle;
                colours[tIndex] = object.getColour();
                objectNames[tIndex] = object.getName();
                tIndex++;
            }
        }
    }

    //Methods to add camera events and set camera position and rotation
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
