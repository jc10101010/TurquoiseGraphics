# Turquoise Graphics - A Lightweight 3D Graphics Engine in Java

Turquoise Graphics is a versatile, GUI-independent 3D rendering engine written in Java. This engine allows users to import 3D models, apply custom shaders, and perform a variety of transformations like positioning, scaling, and rotating objects. It can be easily integrated with any GUI framework or even used for console-based applications, providing flexibility in how you design and present 3D scenes. This repository includes a sample implementation using Java Swing for demonstration purposes.

## Features

- **OBJ File Importing:** Supports importing 3D models in the OBJ format for easy integration of external models into your scenes.
- **Customizable Shaders:** Define custom shading logic using `ColourShader` to control how objects are rendered visually, including shadow effects, lighting simulations, and colour adjustments.
- **3D Transformations:** Easily manipulate 3D objects with support for positioning, scaling, and rotating in the 3D space.
- **Flexible Rendering:** The engine is designed to be independent of any specific GUI framework, making it adaptable for both graphical user interfaces (like Swing) or non-GUI environments (like the console).
- **Scene Management:** Manage multiple objects, apply transformations dynamically, and control camera movements.
- **Swing Example:** The repository includes a basic Swing-based implementation to demonstrate how to use the engine in a desktop GUI environment.

## Getting Started

### Prerequisites

Before using Turquoise Graphics, ensure you have the following set up on your system:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) version 8 or later
- A terminal or command-line interface to compile and run Java programs

### Running the Swing Implementation

This repository provides a sample implementation of the engine using Java Swing. Follow these steps to compile and run the Swing demo:

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd turquoise-graphics
   ```

2. Compile and run the Swing example:
   ```bash
   bash crGame.sh
   ```

   This script will compile and execute the demo, showcasing a basic 3D scene rendered using Swing.

### Usage

Turquoise Graphics allows users to render 3D models, manipulate their positions, rotations, and scales, and apply shaders for custom visual effects. While the provided implementation demonstrates usage within Swing, the engine is designed to be modular and can be adapted to different graphical frameworks.

#### Key Components:

- **`GPanel`**: The primary panel responsible for rendering the scene and handling graphical updates. It acts as a canvas where the 3D objects are projected and displayed.
- **`GFrame`**: The window container for `GPanel`, managing the overall Swing-based window.
- **`Game`**: The game logic, including input handling, updates, and interactions with the scene and camera.
- **`RenderObject`**: Represents an individual 3D object loaded from an OBJ file. It manages the object's transformations (position, rotation, scale) and applies shading during rendering.
- **`Scene`**: A container for managing multiple `RenderObject`s. It handles rendering, object management, and interactions between objects and the camera.
- **`CameraEvent`**: Manages the camera's movement and rotation over time, allowing smooth transitions between camera positions or animations.

### Customization and Extensions

Turquoise Graphics is designed to be extensible, enabling you to customize various aspects of the engine:

- **Custom Shaders:** Implement custom shaders by extending `ColourShader`. These shaders allow you to define how objects in the scene are visually rendered, enabling effects like shadows, light falloff, and surface reflection.
- **Camera and Event Handling:** Use `CameraEvent` to create custom animations or smooth transitions for the camera, allowing complex camera behavior like panning, zooming, or rotating around objects.
- **Dynamic Scene Composition:** Add or remove objects from the scene in real-time. You can also adjust the transformations (position, rotation, scale) of objects based on user input or game logic.

## Example: Adding and Rendering a 3D Object

Below is a simple example of how to load a 3D object from an OBJ file, apply a custom shader, and add it to the scene for rendering:

```java
// Load a 3D object from an OBJ file
RenderObject obj = RenderObject.loadObject("path/to/model.obj", "MyObject", new NonShadow(Color.BLUE), new Vertex(0, 0, -10));

// Set transformations for the object
obj.setScale(new Vertex(1.5f, 1.5f, 1.5f));  // Scale the object
obj.setRotation(new Vertex(45, 30, 0));      // Rotate the object
obj.setPosition(new Vertex(0, 0, -10));      // Position the object

// Add the object to the scene
scene.addObject(obj);
```

## Shader Customization Example

To create a custom shader, you can extend `ColourShader` and define how the triangle's data (such as its vertices) and its base colour should be processed into a final colour:

```java
public class MyCustomShader extends ColourShader {
    
    private Color baseColour;

    public MyCustomShader(Color baseColour) {
        this.baseColour = baseColour;
    }

    @Override
    public Color shadeBasedOnTriangle(Triangle triangle) {
        // Implement custom shading logic
        Vertex avgVertex = averageTriangleAsVertex(triangle);
        float distance = avgVertex.magnitude();
        float shadingFactor = inverseSquare(distance);

        // Modify the base colour based on the shading factor
        int red = (int)(baseColour.getRed() * shadingFactor);
        int green = (int)(baseColour.getGreen() * shadingFactor);
        int blue = (int)(baseColour.getBlue() * shadingFactor);

        return new Color(capRGB(red), capRGB(green), capRGB(blue));
    }
}
```

This shader darkens the object based on its distance from the camera, simulating a light falloff effect.

Sure! Below is an updated section of the README that explains the console version and how you can adapt the GUI-agnostic approach for a new GUI by rewriting key methods (`drawSceneToScreen`, `fillTriangle`, and `outlineTriangle`).

---

### GUI-Agnostic Rendering

One of the core strengths of **Turquoise Graphics** is its **GUI-agnostic design**, allowing the 3D rendering engine to be used across different front-end interfaces, whether graphical user interfaces (GUI) or text-based consoles. This flexibility means you can plug the engine into various types of applications, from desktop GUIs to terminal-based environments, without rewriting the core 3D rendering logic.

#### Console Version
A key example of this adaptability is the **console version** provided in this repository. The `ConsolePanel` class renders 3D objects as ASCII characters directly to the terminal, instead of using a graphical window. It uses characters like `*` to draw outlines and `#` to fill objects, demonstrating how the engine can function even in environments that don't support pixel-based rendering.

The console version highlights how the rendering logic (e.g., converting 3D triangles into 2D projections, handling transformations) remains the same, but the output medium changes to fit a text-based environment.

#### Adapting to a New GUI

If you want to adapt the engine to a new GUI framework (e.g., JavaFX, OpenGL, or web-based systems), you need to rewrite some of the methods responsible for rendering the scene. The core engine manages 3D transformations, scene composition, and shading consistently across any environment. Below are key methods you would modify when integrating Turquoise Graphics into a new GUI.

##### 1. `drawSceneToScreen`

This method is responsible for iterating through the objects in the scene, converting their 3D coordinates into 2D, and rendering them onto the screen. In a new GUI, this method will need to replace the specific drawing functions (e.g., Swing's `g.drawPolygon()` or terminal output in the console version) with the rendering functions of your target GUI.

**Example for a New GUI:**
```java
private void drawSceneToScreen(NewGUIRenderer renderer) {
    Triangle2D[] trianglesToDisplay = scene.getRenderedTriangles();
    Color[] colours = scene.getColours();
    
    for (int index = 0; index < scene.getCount(); index++) {
        if (trianglesToDisplay[index] != null) {
            outlineTriangle(trianglesToDisplay[index], colours[index], renderer);
            fillTriangle(trianglesToDisplay[index], colours[index], renderer);
        }
    }
}
```
The `NewGUIRenderer` would handle the actual rendering calls, such as filling and outlining polygons in the new graphical system.

##### 2. `fillTriangle`

This method fills a triangle with a specific color. When adapting it to a new GUI, you replace the way the triangle is filled in the target environment, such as with a `GraphicsContext` for JavaFX or with vertex shaders for OpenGL.

**Example for a New GUI:**
```java
private void fillTriangle(Triangle2D triangle, Color colour, NewGUIRenderer renderer) {
    float[] xPoints = triangle.xValues();
    float[] yPoints = triangle.yValues();
    
    renderer.setFillColour(colour);
    renderer.fillPolygon(convertToScreenCoords(xPoints), convertToScreenCoords(yPoints));
}
```
Here, `NewGUIRenderer` would handle the polygon filling, depending on the rendering system.

##### 3. `outlineTriangle`

This method draws the outline of a triangle. You would rewrite it to use the appropriate drawing functions in the new GUI framework.

**Example for a New GUI:**
```java
private void outlineTriangle(Triangle2D triangle, Color colour, NewGUIRenderer renderer) {
    float[] xPoints = triangle.xValues();
    float[] yPoints = triangle.yValues();
    
    renderer.setOutlineColour(colour);
    renderer.drawPolygon(convertToScreenCoords(xPoints), convertToScreenCoords(yPoints));
}
```
This would replace Swing's `g.drawPolygon()` with the equivalent rendering call in the new GUI system.

#### Customization for Different GUIs

Depending on the target GUI system, you will need to adapt these methods accordingly:

- **JavaFX**: Use `GraphicsContext.fillPolygon()` and `GraphicsContext.strokePolygon()` for filling and outlining triangles.
- **OpenGL (JOGL)**: Set up vertex arrays and shaders for drawing triangles.
- **Web (e.g., WebGL)**: Use web-based APIs to handle rendering and transformations.

#### Console Version Example

The console version demonstrates the flexibility of this design. Instead of using a GUI framework like Swing, it outputs the rendering to a terminal using characters. The same `drawSceneToScreen`, `fillTriangle`, and `outlineTriangle` methods are adapted to use ASCII characters in a `screenBuffer`, proving that the engine can work in environments with limited rendering capabilities.

For example, the `fillTriangle` method in the console version draws the triangle using characters like `#`:
```java
private void fillTriangle(Triangle2D triangle, char ch) {
    float[] xPoints = triangle.xValues();
    float[] yPoints = triangle.yValues();

    int[] xScreen = fitAxisToScreen(xPoints, false);
    int[] yScreen = fitAxisToScreen(yPoints, true);
    
    rasterizeTriangle(xScreen, yScreen, ch);
}
```

---

By adapting these key methods (`drawSceneToScreen`, `fillTriangle`, and `outlineTriangle`), you can integrate **Turquoise Graphics** into virtually any rendering system. Whether you're targeting a modern GUI framework or a simple text-based terminal, the core engine remains the same, allowing maximum flexibility and reuse of rendering logic.
