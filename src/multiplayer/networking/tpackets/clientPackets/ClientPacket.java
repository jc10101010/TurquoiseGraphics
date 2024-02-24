package multiplayer.networking.tpackets.clientPackets;

import multiplayer.networking.tpackets.Packet;

public abstract class ClientPacket extends Packet {
    public static enum clientDataType {
        JOIN,
        LEAVE,
        REQUEST,
        POSITION, // SET PACKETS TAKE CARE
        SET
    }

    public static clientDataType packetType;

    public ClientPacket() {
        super();
        packetRole = role.CLIENT;
    }

    public String toString() {
        return wrapString(packetType.name());
    }

}