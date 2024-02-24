package multiplayer.networking.server;

import java.util.ArrayList;

import graphics.objects.Vertex;
import multiplayer.networking.tpackets.Packet;

public class MPServerPlayer {
    private String name;
    private Vertex position;
    private ArrayList<Packet> queue = new ArrayList<>();

    public MPServerPlayer(String playerName) {
        name = playerName;
        position = new Vertex(0, 0, 0);
    }

    public Vertex getPosition() {
        return position;
    }
    public void setPosition(Vertex position) {
        this.position = position;
    }
    public String getName() {
        return name;
    }

    //Takes a packet object and adds it to the queue
    public void queuePacket(Packet packet) {
        queue.add(packet);
    }

    //Returns current packet queue and resets queue
    public ArrayList<Packet> loadQueue() {
        ArrayList<Packet> queueCopy = queue;
        
        queue = new ArrayList<Packet>(); 
        return queueCopy;
    }
}
