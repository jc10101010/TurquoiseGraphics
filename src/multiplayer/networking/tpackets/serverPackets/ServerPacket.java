package multiplayer.networking.tpackets.serverPackets;

import multiplayer.networking.tpackets.Packet;

public abstract class ServerPacket extends Packet {
    public ServerPacket() {
        super();
        packetType = role.SERVER;
    }
}