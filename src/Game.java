

import java.util.ArrayList;

import colours.ColourShader;
import colours.HorizontalShader;
import core.RenderObject;
import core.Scene;

import java.awt.Color;

import objects.Vertex;

public class Game {
    private ArrayList<RenderObject> objects = new ArrayList<>();
    private RenderObject enemy; 
    private RenderObject plane; 
    private Vertex playerPosition = new Vertex(-4, 2, -5);
    private Scene renderScene;

    public Game(Scene scene) {
        enemy = RenderObject.loadObject("data/dodecahedron.obj", "enemy", new HorizontalShader(new Color(255, 0, 0)), new Vertex(0,1.5f,0));
        plane  = RenderObject.loadObject("data/plane.obj", "enemy", new HorizontalShader(new Color(255, 255, 255)), new Vertex(0,0,0));
        objects.add(enemy);
        objects.add(plane);
        renderScene = scene;
    }

    public void refreshObjects() {
        playerPosition.x += 0.04;
        renderScene.setCamPos(playerPosition);
    }

    public ArrayList<RenderObject> getObjects() {return objects;}
    public Vertex getPlayerPosition() {return playerPosition;}
}
