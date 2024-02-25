

import java.util.ArrayList;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.JPanel;
import javax.swing.KeyStroke;

import colours.InverseSqrShadow;
import core.RenderObject;
import core.Scene;

import java.awt.Color;
import java.awt.event.ActionEvent;

import objects.Vertex;

public class Game {
    private Scene scene;
    private JPanel panel;

    private RenderObject enemy; 
    private RenderObject plane; 
    private Vertex playerPosition = new Vertex(0, 2, -10);
    private Vertex playerRotation = new Vertex(0, 0, 0);
    
    private Vertex moveDir = new Vertex(0, 0, 0);
    private float moveSpeed = 8.0f;
    private double lastFrameTime = System.currentTimeMillis();

    public Game(Scene scene, GPanel panel) {
        this.scene = scene;
        this.panel = panel;

        enemy = RenderObject.loadObject("data/monkey.obj", "enemy", new InverseSqrShadow(new Color(0,238,238), scene), new Vertex(0,1.5f,0));
        plane  = RenderObject.loadObject("data/plane.obj", "enemy", new InverseSqrShadow(new Color(255, 0, 0), scene), new Vertex(0,0,0));

        float planeScale = 3;
        plane.setScale(new Vertex(planeScale, planeScale, planeScale));

        scene.addObject(enemy);
        scene.addObject(plane);
        
        setKeyListeners();
    }

    public void tick() {
        double timeSinceLast = ( (double) System.nanoTime() - lastFrameTime)/ (1000000000.0);

        float moveDirMagnitude = moveDir.magnitude();
        Vertex moveDirNormalized = null;

        if (moveDirMagnitude != 0) {
            moveDirNormalized = Vertex.divide(moveDir, moveDirMagnitude);
        } else {
            moveDirNormalized = new Vertex(0, 0, 0);
        }
        

        playerPosition.x += moveDirNormalized.x * moveSpeed * timeSinceLast;
        playerPosition.y += moveDirNormalized.y * moveSpeed * timeSinceLast;
        playerPosition.z += moveDirNormalized.z * moveSpeed * timeSinceLast;
        
        scene.setCamRot(playerRotation);
        scene.setCamPos(playerPosition);

        lastFrameTime = System.nanoTime();
    }

    private void setKeyListeners() {
        panel.getInputMap().put(KeyStroke.getKeyStroke("W"),
                            "pressedW");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released W"),
                                    "releasedW");
        panel.getInputMap().put(KeyStroke.getKeyStroke("A"),
                            "pressedA");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released A"),
                                    "releasedA");
        panel.getInputMap().put(KeyStroke.getKeyStroke("S"),
                            "pressedS");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released S"),
                                    "releasedS");
        panel.getInputMap().put(KeyStroke.getKeyStroke("D"),
                            "pressedD");
        panel.getInputMap().put(KeyStroke.getKeyStroke("released D"),
                                    "releasedD");
                                
        panel.getActionMap().put("pressedW",
                                    wDown);
        panel.getActionMap().put("releasedW",
                                    wUp);
        panel.getActionMap().put("pressedA",
                                    aDown);
        panel.getActionMap().put("releasedA",
                                    aUp);
        panel.getActionMap().put("pressedS",
                                    sDown);
        panel.getActionMap().put("releasedS",
                                    sUp);
        panel.getActionMap().put("pressedD",
                                    dDown);
        panel.getActionMap().put("releasedD",
                                    dUp);
    }

    private boolean isWDown = false;

    Action wDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isWDown) {
                moveDir.z += 1;
                isWDown = true;
            }
        } };

    Action wUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (isWDown) {
                moveDir.z -= 1;
                isWDown = false;
            }
        } };

    
    private boolean isADown = false;

    Action aDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isADown) {
                moveDir.x -= 1;
                isADown = true;
            }
        } };

    Action aUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (isADown) {
                moveDir.x += 1;
                isADown = false;
            }
        } };
    
    
    
    private boolean isSDown = false;

    Action sDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isSDown) {
                moveDir.z -= 1;
                isSDown = true;
            }
        } };

    Action sUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (isSDown) {
                moveDir.z += 1;
                isSDown = false;
            }
        } };


    
    private boolean isDDown = false;

    Action dDown = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (!isDDown) {
                moveDir.x += 1;
                isDDown = true;
            }
        } };

    Action dUp = new AbstractAction() {
        public void actionPerformed(ActionEvent e) {
            if (isDDown) {
                moveDir.x -= 1;
                isDDown = false;
            }
        } };

}


