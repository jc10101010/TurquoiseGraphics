package multiplayer.networking.tpackets.serverPackets;

public class ServerConfirmSetPacket extends ServerConfirmPacket{
    public String toString() {
        return super.wrapString("SET");
    }
}
