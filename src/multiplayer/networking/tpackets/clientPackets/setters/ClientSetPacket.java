package multiplayer.networking.tpackets.clientPackets.setters;

import multiplayer.networking.tpackets.clientPackets.ClientPacket;

public abstract class ClientSetPacket extends ClientPacket {// subclasses do not have datatypes for now
    public ClientSetPacket() {
        dataType = clientDataType.SET;
    }

    @Override
    public String toString() {
        return wrapString("SET" + dataType.name());
    }
}
