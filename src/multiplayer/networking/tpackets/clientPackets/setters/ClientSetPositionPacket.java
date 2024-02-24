package multiplayer.networking.tpackets.clientPackets.setters;

import graphics.objects.Vertex;

public class ClientSetPositionPacket extends ClientSetPacket {
    public Vertex position;

    public ClientSetPositionPacket(Vertex position) {
        this.position = position;
    }

    public String toString() {
        return wrapString("POSITION" + position.x + " " + position.y + " " + position.z);
    }

}
