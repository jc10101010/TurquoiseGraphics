package multiplayer.networking.tpackets.clientPackets;

public class ClientRequestPacket extends ClientPacket {

    public ClientRequestPacket() {
        super();
        dataType = clientDataType.REQUEST;
    }
}
