package multiplayer.networking.tpackets.serverPackets;

public class ServerConfirmJoinPacket extends ServerConfirmPacket{
    public String toString() {
        return super.wrapString("JOIN");
    }
}
