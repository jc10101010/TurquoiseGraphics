package multiplayer.networking.tpackets.serverPackets;

public abstract class ServerConfirmPacket extends ServerPacket{
    public String wrapString(String toWrap) {
        return super.wrapString("CONFIRM " + toWrap);
    }
}
