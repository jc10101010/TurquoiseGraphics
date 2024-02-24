package multiplayer.networking.tpackets.clientPackets;

import graphics.objects.Vertex;

public class ClientJoinPacket extends ClientPacket {
    public ClientJoinPacket() {
        super();
        dataType = clientDataType.JOIN;
    }
}
