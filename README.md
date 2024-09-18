# Turquoise Graphics - A Lightweight 3D Graphics Engine in Java

Turquoise Graphics is a versatile, GUI-independent 3D rendering engine written in Java. It enables users to import 3D models, apply custom shaders, and perform various transformations such as positioning, scaling, and rotating objects. This engine seamlessly integrates with any GUI framework and can even be used in console-based applications, offering maximum flexibility for designing and presenting 3D scenes. A sample implementation using Java Swing is included for demonstration purposes.

## Features

- **OBJ File Importing:** Easily import 3D models in the OBJ format for smooth integration of external assets.
- **Customizable Shaders:** Define custom shading logic with `ColourShader` to control visual rendering, including shadow effects and lighting simulations.
- **3D Transformations:** Effortlessly manipulate 3D objects with support for positioning, scaling, and rotating in 3D space.
- **Flexible Rendering:** The engine is independent of any specific GUI framework, making it adaptable for both graphical interfaces (like Swing) and console environments.
- **Scene Management:** Manage multiple objects dynamically, control camera movements, and apply transformations seamlessly.
- **Swing Example:** A basic Swing-based implementation is included to illustrate how to use the engine in a desktop GUI environment.

## Getting Started

### Prerequisites

Before using Turquoise Graphics, ensure you have the following set up on your system:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) version 8 or later.
- A terminal or command-line interface to compile and run Java programs.

### Running the Swing Implementation

To compile and run the Swing demo, follow these steps:

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

## Usage

Turquoise Graphics enables users to render 3D models, manipulate their properties, and apply shaders for custom visual effects. While the provided implementation demonstrates usage within Swing, the engine is designed to be modular and adaptable to different graphical frameworks.

### Key Components

- **`GPanel`**: The main panel responsible for rendering the scene and handling graphical updates. It acts as a canvas for displaying 3D objects.
- **`GFrame`**: The window container for `GPanel`, managing the overall Swing-based window.
- **`Game`**: Handles game logic, input processing, and interactions with the scene and camera.
- **`RenderObject`**: Represents individual 3D objects loaded from OBJ files, managing transformations and applying shading during rendering.
- **`Scene`**: A container for managing multiple `RenderObject`s, handling rendering, object management, and camera interactions.
- **`CameraEvent`**: Manages camera movement and rotation, allowing smooth transitions and animations.

## Customization and Extensions

Turquoise Graphics is highly extensible, enabling you to customize various engine aspects:

- **Custom Shaders:** Extend `ColourShader` to create unique shading effects, controlling how objects are visually rendered.
- **Camera and Event Handling:** Use `CameraEvent` for custom animations or smooth transitions, enhancing the camera experience.
- **Dynamic Scene Composition:** Add or remove objects in real-time, adjusting transformations based on user input or game logic.

### Example: Adding and Rendering a 3D Object

Here’s a simple example of loading a 3D object from an OBJ file, applying a custom shader, and adding it to the scene:

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

### Shader Customization Example

To create a custom shader, extend `ColourShader` and implement your shading logic:

```java
public class MyCustomShader extends ColourShader {
    
    private Color baseColour;

    public MyCustomShader(Color baseColour) {
        this.baseColour = baseColour;
    }

    @Override
    public Color shadeBasedOnTriangle(Triangle triangle) {
        Vertex avgVertex = averageTriangleAsVertex(triangle);
        float distance = avgVertex.magnitude();
        float shadingFactor = inverseSquare(distance);

        // Modify the base colour based on the shading factor
        return new Color(capRGB((int)(baseColour.getRed() * shadingFactor)),
                         capRGB((int)(baseColour.getGreen() * shadingFactor)),
                         capRGB((int)(baseColour.getBlue() * shadingFactor)));
    }
}
```

This shader simulates light falloff based on the distance from the camera.

---

### GUI-Agnostic Rendering

**Turquoise Graphics** boasts a GUI-agnostic design, allowing the 3D rendering engine to function across various front-end interfaces, whether graphical or text-based. This adaptability enables you to integrate the engine into diverse applications, from desktop GUIs to terminal-based environments, without rewriting the core 3D rendering logic.

#### Console Version

The console version illustrates this flexibility. The `ConsolePanel` class renders 3D objects as ASCII characters directly in the terminal. Characters like `*` and `#` are used to draw outlines and fill objects, respectively, showcasing the engine's capability in environments with limited rendering options.

#### Adapting to a New GUI

If you wish to adapt the engine to a new GUI framework (e.g., JavaFX, OpenGL, or web-based systems), you will need to modify some rendering methods. The core engine manages 3D transformations, scene composition, and shading consistently across any environment.

##### Key Methods to Modify

1. **`drawSceneToScreen`**

Responsible for converting 3D coordinates to 2D and rendering them to the screen. Adapt this method to fit your target GUI's rendering functions.

**Example:**
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

2. **`fillTriangle`**

This method fills a triangle with a specific color. Modify it to use the appropriate methods in your new GUI framework.

**Example:**
```java
private void fillTriangle(Triangle2D triangle, Color colour, NewGUIRenderer renderer) {
    float[] xPoints = triangle.xValues();
    float[] yPoints = triangle.yValues();
    
    renderer.setFillColour(colour);
    renderer.fillPolygon(convertToScreenCoords(xPoints), convertToScreenCoords(yPoints));
}
```

3. **`outlineTriangle`**

Reimplement this method to use the drawing functions of your new GUI system.

**Example:**
```java
private void outlineTriangle(Triangle2D triangle, Color colour, NewGUIRenderer renderer) {
    float[] xPoints = triangle.xValues();
    float[] yPoints = triangle.yValues();
    
    renderer.setOutlineColour(colour);
    renderer.drawPolygon(convertToScreenCoords(xPoints), convertToScreenCoords(yPoints));
}
```

### Console Version Example

The console version demonstrates the flexibility of Turquoise Graphics. It outputs the rendering to a terminal using characters, maintaining the same logic but changing the output medium to suit a text-based environment.

For instance, the `fillTriangle` method uses characters like `#`:
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

By adapting the key methods (`drawSceneToScreen`, `fillTriangle`, and `outlineTriangle`), you can seamlessly integrate **Turquoise Graphics** into virtually any rendering system. Whether targeting a modern GUI framework or a simple text-based terminal, the core engine remains consistent, allowing maximum flexibility and reuse of rendering logic.