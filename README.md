# Turquoise Graphics - A GUI-Agnostic 3D Graphics Engine in Java

Turquoise Graphics is a flexible, GUI-agnostic 3D rendering engine designed in Java. It allows users to import 3D models, apply custom shaders, and perform transformations such as positioning, scaling, and rotating objects. This engine can be integrated with any GUI framework (or even console-based applications) while keeping rendering logic independent of the UI implementation. This repository includes a sample implementation using Swing.

## Features

- **OBJ File Importing:** Quickly import 3D models in the OBJ format.
- **Customizable Shaders:** Use `ColourShaders` to fully customize graphical outputs.
- **3D Transformations:** Position, scale, and rotate objects in a 3D scene.
- **GUI Agnostic:** Can be integrated with any GUI system, including console applications.
- **Swing Example:** A sample implementation using Java Swing is included.

## Getting Started

### Prerequisites

To use Turquoise Graphics, ensure you have the following installed:

- [Java Development Kit (JDK)](https://www.oracle.com/java/technologies/javase-downloads.html) 8 or later
- A terminal or command-line interface to run scripts

### Running the Swing Implementation

The provided implementation in this repository demonstrates how to use the engine with a Java Swing-based interface.

To run the Swing example, follow these steps:

1. Clone the repository:
   ```bash
   git clone <repository-url>
   cd turquoise-graphics
   ```

2. Compile and run the Swing example:
   ```bash
   bash crGame.sh
   ```

   This will build and execute the game using the Swing implementation.

### Usage

Turquoise Graphics allows users to render 3D objects, manipulate them, and apply shaders. While this implementation demonstrates usage in Swing, the engine can be adapted to other GUIs, allowing flexibility.

#### Key Classes:

- **`GPanel`**: The main panel for rendering graphics.
- **`GFrame`**: A container for the `GPanel`, managing the Swing window.
- **`Game`**: The main game loop and logic.
- **`RenderObject`**: Represents 3D objects in the scene, responsible for transformations and rendering.
- **`Scene`**: Manages the collection of objects and their interactions in the 3D space.

### Customization

You can modify and extend the library to suit your needs. Some common customizations include:

- **Shaders:** Modify or create custom `ColourShaders` to adjust how objects are visually rendered.
- **Event Handling:** Handle user inputs (keyboard or mouse) to interact with the scene.
- **Scene Composition:** Add or remove objects dynamically, or adjust transformations (position, rotation, scaling) of objects during runtime.

## Example

Here’s a simple example of how to initialize a 3D object, apply a shader, and add it to a scene:

```java
// Create a new renderable object from an OBJ file
RenderObject obj = new RenderObject("path/to/model.obj");

// Apply a custom shader
obj.setShader(new ColourShader(Color.BLUE));

// Set object transformations
obj.setPosition(0, 0, -10);
obj.setScale(1.5);
obj.setRotation(45, 30, 0);

// Add object to the scene
scene.addObject(obj);
```
