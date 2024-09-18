import colours.InverseSqrShadow;
import core.RenderObject;
import core.Scene;
import java.awt.Color;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import objects.Vertex;
import objects.Vertex2D;

public class Game {
    //Game objects
    private Scene scene; //The scene containing all renderable objects
    private JPanel panel; //The panel where the game is rendered

    private RenderObject enemy; //The enemy object in the scene
    private RenderObject plane; //The plane object in the scene

    //Player position and rotation vectors
    private Vertex playerPosition = new Vertex(0, 2, -10); //Initial position of the player
    private Vertex playerRotation = new Vertex(0, 0, 0); //Initial rotation of the player
    
    //Movement and mouse-related variables
    private Vertex moveDir = new Vertex(0, 0, 0); //Direction of movement
    private Vertex2D mousePosition = new Vertex2D(0, 0); //Position of the mouse

    //Game physics and controls
    private float moveSpeed = 8.0f; //Speed of movement
    private float rotationSpeed = 80.0f; //Speed of rotation
    private double lastFrameTime; //Tracks the time of the last frame
    private double timeSinceLast; //Time elapsed since the last frame

    //Booleans to track which movement keys are pressed
    private boolean isWDown = false;
    private boolean isADown = false;        
    private boolean isSDown = false;
    private boolean isDDown = false;

    //Actions for key events
    private Action wDown;
    private Action wUp;
    private Action aDown;
    private Action aUp;
    private Action sDown;
    private Action sUp;
    private Action dDown;
    private Action dUp;

    /**
     * Constructor to initialize the game.
     * @param scene The scene where objects will be rendered.
     * @param panel The JPanel to render the game and capture input events.
     */
    public Game(Scene scene, GPanel panel) {
        this.scene = scene;
        this.panel = panel;
        this.lastFrameTime = System.nanoTime(); //Set initial frame time
        
        //Hide cursor and replace with a blank 3x3 image
        panel.setCursor(panel.getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
            "null"));

        //Set up the game scene and input listeners
        setupScene();
        setKeyListeners();
        setMouseListener();
    }

    /**
     * Sets up the game scene by loading objects and adding them to the scene.
     */
    private void setupScene() {
        //Load enemy and plane models into the scene with Inverse Square Shadow effects
        enemy = RenderObject.loadObject("data/teapot.obj", "enemy", new InverseSqrShadow(new Color(255, 0, 0), scene), new Vertex(0, 0f, 0));
        plane  = RenderObject.loadObject("data/plane.obj", "enemy", new InverseSqrShadow(new Color(255, 255, 255), scene), new Vertex(0, 0, 0));

        //Scale the plane
        float planeScale = 3;
        plane.setScale(new Vertex(planeScale, planeScale, planeScale));

        //Add the objects to the scene
        scene.addObject(enemy);
        scene.addObject(plane);

        //Define actions for key inputs (WASD)
        defineAction();
    }

    /**
     * Defines actions for each movement key (WASD) for moving the player.
     */
    public void defineAction() {
        //Action for pressing 'W'
        wDown = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!isWDown) { //If 'W' is not already pressed
                    moveDir.z += 1; //Move forward
                    isWDown = true;
                }
            } };
    
        //Action for releasing 'W'
        wUp = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (isWDown) { //If 'W' is pressed
                    moveDir.z -= 1; //Stop moving forward
                    isWDown = false;
                }
            } };
    
        //Action for pressing 'A' (Move left)
        aDown = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!isADown) { //If 'A' is not already pressed
                    moveDir.x -= 1; //Move left
                    isADown = true;
                }
            } };
    
        //Action for releasing 'A' (Stop moving left)
        aUp = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (isADown) { //If 'A' is pressed
                    moveDir.x += 1; //Stop moving left
                    isADown = false;
                }
            } };
    
        //Action for pressing 'S' (Move backward)
        sDown = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!isSDown) { //If 'S' is not already pressed
                    moveDir.z -= 1; //Move backward
                    isSDown = true;
                }
            } };
    
        //Action for releasing 'S' (Stop moving backward)
        sUp = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (isSDown) { //If 'S' is pressed
                    moveDir.z += 1; //Stop moving backward
                    isSDown = false;
                }
            } };
    
        //Action for pressing 'D' (Move right)
        dDown = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (!isDDown) { //If 'D' is not already pressed
                    moveDir.x += 1; //Move right
                    isDDown = true;
                }
            } };
    
        //Action for releasing 'D' (Stop moving right)
        dUp = new AbstractAction() {
            public void actionPerformed(ActionEvent e) {
                if (isDDown) { //If 'D' is pressed
                    moveDir.x -= 1; //Stop moving right
                    isDDown = false;
                }
            } };
    }

    /**
     * Updates the game state every frame.
     * Calculates the time elapsed since the last frame and updates the player's position and rotation.
     */
    public void tick() {
        //Calculate time elapsed since the last frame
        timeSinceLast = ( (double) System.nanoTime() - lastFrameTime)/ (1000000000.0);

        //Update player's rotation and position based on input
        setRotation();
        setPosition();

        //Update the camera position and rotation based on the player's position and rotation
        scene.setCamRot(playerRotation);
        scene.setCamPos(playerPosition);

        //Update the last frame time
        lastFrameTime = System.nanoTime();
    }

    /**
     * Updates the player's rotation based on mouse movement.
     * Restricts the pitch (up/down rotation) to avoid extreme angles.
     */
    private void setRotation() {
        //Rotate based on mouse position
        playerRotation.y = mousePosition.x * rotationSpeed * 0.1f;
        playerRotation.x = mousePosition.y * rotationSpeed * 0.1f;

        //Restrict pitch rotation to avoid looking directly up or down
        if (playerRotation.x > Math.PI / 2) {
            playerRotation.x = (float) Math.PI / 2;
        } else if (playerRotation.x < -Math.PI / 2) {
            playerRotation.x = (float) -Math.PI / 2;
        }
    }

    /**
     * Updates the player's position based on the current movement direction and speed.
     */
    private void setPosition() {
        //Calculate the magnitude (length) of the movement direction vector
        float moveDirMagnitude = moveDir.magnitude();
        Vertex moveDirNormalized = null;

        //Normalize the movement direction (to make it a unit vector)
        if (moveDirMagnitude != 0) {
            moveDirNormalized = Vertex.divide(moveDir, moveDirMagnitude);
        } else {
            moveDirNormalized = new Vertex(0, 0, 0); //No movement if no direction
        }

        //Create a rotation vector based on player's current Y rotation (yaw)
        Vertex playerRotationY = new Vertex(0, playerRotation.y, 0);
        Vertex moveDirNormalRotated = Vertex.rotate(moveDirNormalized, playerRotationY);

        //Update player position based on movement direction and speed
        playerPosition.x += moveDirNormalRotated.x * moveSpeed * timeSinceLast;
        playerPosition.z += moveDirNormalRotated.z * moveSpeed * timeSinceLast;
    }

    /**
     * Sets up the mouse listener to capture mouse movements and clicks.
     */
    private void setMouseListener() {
        //Adds listener events for when the mouse is moved and when it is clicked
        panel.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent mouseEvent) { 
                //Placeholder for mouse click actions
            }
        });

        panel.addMouseMotionListener(new MouseAdapter() {
            public void mouseMoved(MouseEvent mouseEvent) { 
                mouseListener(mouseEvent); //Capture mouse movements
            }
        });
    }

    /**
     * Updates the stored mouse position when the mouse is moved.
     * @param mouseEvent The mouse event containing the current mouse position.
     */
    private void mouseListener(MouseEvent mouseEvent) {
        //Store normalized mouse position, centered at (0, 0)
        mousePosition = new Vertex2D(mouseEvent.getX() / ((float) panel.getWidth()) - 0.5f,
                                     mouseEvent.getY() / ((float) panel.getHeight()) - 0.5f);
    }

    /**
     * Sets up key listeners to handle WASD input for player movement.
     */
    private void setKeyListeners() {
        //Map WASD key presses and releases to corresponding actions
        panel.getInputMap().put(KeyStroke.getKeyStroke("W"), "pressedW");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released W"), "releasedW");
        panel.getInputMap().put(KeyStroke.getKeyStroke("A"), "pressedA");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released A"), "releasedA");
        panel.getInputMap().put(KeyStroke.getKeyStroke("S"), "pressedS");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released S"), "releasedS");
        panel.getInputMap().put(KeyStroke.getKeyStroke("D"), "pressedD");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released D"), "releasedD");
        
        //Assign actions for each key press and release
        panel.getActionMap().put("pressedW", wDown);
        panel.getActionMap().put("releasedW", wUp);
        panel.getActionMap().put("pressedA", aDown);
        panel.getActionMap().put("releasedA", aUp);
        panel.getActionMap().put("pressedS", sDown);
        panel.getActionMap().put("releasedS", sUp);
        panel.getActionMap().put("pressedD", dDown);
        panel.getActionMap().put("releasedD", dUp);
    }

}
