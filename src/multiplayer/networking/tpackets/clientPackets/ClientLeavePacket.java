package multiplayer.networking.tpackets.clientPackets;

public class ClientLeavePacket extends ClientPacket {
    public ClientLeavePacket() {
        super();
        dataType = clientDataType.LEAVE;
    }
}
