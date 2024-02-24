package multiplayer.networking.tpackets.serverPackets;

public abstract class ServerStatePacket extends ServerPacket{
    
    @Override
    public String wrapString(String toWrap) {
        return super.wrapString("STATE " + toWrap);
    }
}