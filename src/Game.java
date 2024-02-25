

import java.util.ArrayList;

import colours.ColourShader;
import colours.HorizontalShader;
import colours.InverseSqrShadow;
import core.RenderObject;
import core.Scene;

import java.awt.AWTEvent;
import java.awt.Color;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import objects.Vertex;

public class Game {
    private RenderObject enemy; 
    private RenderObject plane; 
    private Vertex playerPosition = new Vertex(0, 2, -10);
    private Vertex playerRotation = new Vertex(0, 0, 0);
    private Scene renderScene;
    private Vertex moveDir = new Vertex(0, 0, 0);
    private float moveSpeed = 4.0f;
    private double lastFrameTime = System.currentTimeMillis();

    public Game(Scene scene) {
        
        ArrayList<RenderObject> enemies = new ArrayList<>();
        for (int i = 0; i < 1; i++) {
            enemy = RenderObject.loadObject("data/monkey.obj", "enemy", new InverseSqrShadow(new Color(0,238,238), scene), new Vertex(0,1.5f,0));
            enemies.add(enemy);
        }
        setKeyListeners();
        scene.setObjects(enemies);
        
        plane  = RenderObject.loadObject("data/plane.obj", "enemy", new InverseSqrShadow(new Color(255, 0, 0), scene), new Vertex(0,0,0));
        plane.setScale(new Vertex(3, 3, 3));
        scene.addObject(plane);
        renderScene = scene;
    }

    public void tick() {
        double timeSinceLast = ( (double) System.nanoTime() - lastFrameTime)/ (1000000000.0);

        playerPosition.x += moveDir.x * moveSpeed * timeSinceLast;
        playerPosition.y += moveDir.y * moveSpeed * timeSinceLast;
        playerPosition.z += moveDir.z * moveSpeed * timeSinceLast;
        
        renderScene.setCamRot(playerRotation);
        renderScene.setCamPos(playerPosition);

        lastFrameTime = System.nanoTime();
    }

    private void setKeyListeners() {
        Toolkit.getDefaultToolkit().addAWTEventListener(new AWTEventListener() {

            @Override
            public void eventDispatched(AWTEvent event) {
                if (event instanceof KeyEvent) {
                    KeyEvent ke = (KeyEvent) event;
                    boolean released = false;
                    switch (ke.getID()) {
                        case KeyEvent.KEY_PRESSED:
                            break;
                        case KeyEvent.KEY_RELEASED:
                            released = true;
                            break;
                    }
                    switch (ke.getKeyCode()) {
                        case KeyEvent.VK_UP:
                            moveDir.z = 1;
                            if (released) {
                                moveDir.z = 0;
                            }
                            break;
                        case KeyEvent.VK_DOWN:
                            moveDir.z = -1;
                            if (released) {
                                moveDir.z = 0;
                            }
                            break;
                        case KeyEvent.VK_LEFT:
                            moveDir.x = -1;
                            if (released) {
                                moveDir.x = 0;
                            }
                            break;
                        case KeyEvent.VK_RIGHT:
                            moveDir.x = 1;
                            if (released) {
                                moveDir.x = 0;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }, AWTEvent.KEY_EVENT_MASK);
    }
}


