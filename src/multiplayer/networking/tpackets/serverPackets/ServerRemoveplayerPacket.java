package multiplayer.networking.tpackets.serverPackets;

public class ServerRemoveplayerPacket  extends ServerPacket{
    private String name;

    public ServerRemoveplayerPacket(String name) {
        this.name = name;
    }

    public String toString() {
        return super.wrapString("REMOVE " + name);
    }
}
