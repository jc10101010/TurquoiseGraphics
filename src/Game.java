

import java.util.ArrayList;

import colours.ColourShader;
import colours.HorizontalShader;
import colours.InverseSqrShadow;
import core.RenderObject;
import core.Scene;

import java.awt.Color;

import objects.Vertex;

public class Game {
    private RenderObject enemy; 
    private RenderObject plane; 
    private Vertex playerPosition = new Vertex(0, 2, -10);
    private Scene renderScene;

    public Game(Scene scene) {
        enemy = RenderObject.loadObject("data/monkey.obj", "enemy", new InverseSqrShadow(new Color(0,238,238), scene), new Vertex(0,1.5f,0));
        plane  = RenderObject.loadObject("data/plane.obj", "enemy", new InverseSqrShadow(new Color(255, 0, 0), scene), new Vertex(0,0,0));
        scene.addObject(enemy);
        scene.addObject(plane);
        renderScene = scene;
    }

    public void tick() {
        playerPosition.z += 0.04;
        renderScene.setCamPos(playerPosition);
    }
}
