package multiplayer.networking.tpackets;

import java.net.SocketAddress;
import java.nio.ByteBuffer;

import graphics.objects.Vertex;
import multiplayer.networking.tpackets.clientPackets.*;
import multiplayer.networking.tpackets.clientPackets.setters.*;
import multiplayer.networking.tpackets.serverPackets.ServerConfirmJoinPacket;
import multiplayer.networking.tpackets.serverPackets.ServerConfirmSetPacket;
import multiplayer.networking.tpackets.serverPackets.ServerRegisterplayerPacket;
import multiplayer.networking.tpackets.serverPackets.ServerRemoveplayerPacket;
import multiplayer.networking.tpackets.serverPackets.ServerStatePacket;
import multiplayer.networking.tpackets.serverPackets.ServerStatePositionPacket;

//A Packet is an abstract Object that represents an packet object sent between the server and the client
//Packets contain the data relevant to their purpose.
//A packet can be parsed from text with the stringToPacket() function
//A packet can be transformed into text by calling the toString() function

public abstract class Packet {
    // Abstract class

    // Used to change behaviour of super methods
    public static enum role {
        SERVER,
        CLIENT
    }

    // This is set as described above
    public role packetRole;

    // Wraps a string in the data passed in, turns into chain where subclasses call
    // wrapString of their super classes
    public String wrapString(String toWrap) {
        return "START " + packetRole.name() + " " + toWrap + " END";
    }

    // Requires that all proper Packet subclaases implement a toString method which
    // converts from a packet to a string to send
    public abstract String toString();

    // Convert a byte buffer into a packet object
    public static Packet byteBufferToPacket(ByteBuffer buffer) {
        String msg = byteBufferToString(buffer);
        Packet packet = null;
        try {
            packet = stringToPacket(msg);
        } catch (IndexOutOfBoundsException e) {
            System.out.println(e.getMessage());
        }
        return packet;
    }

    // Convert byte buffer to a string
    public static String byteBufferToString(ByteBuffer buffer) {
        buffer.flip();
        int limits = buffer.limit();
        byte bytes[] = new byte[limits];
        buffer.get(bytes, 0, limits);
        return new String(bytes);
    }

    // Converts a string to a packet object, through checking the token indexes
    public static Packet stringToPacket(String msg) throws IndexOutOfBoundsException {
        Packet packet = null;
        String[] tokens = msg.split(" ");
        if (hasStartAndEnd(tokens)) {
            if (tokens[1].equals("CLIENT")) {
                if (tokens[2].equals("REQUEST")) {

                    packet = new ClientRequestPacket();

                } else if (tokens[2].equals("SET")) {
                    if (tokens[3].equals("POSITION")) {

                        Vertex vertex = tokensToVertex(tokens[4], tokens[5], tokens[6]);
                        packet = new ClientSetPositionPacket(vertex);

                    }
                } else if (tokens[2].equals("JOIN")) {

                    packet = new ClientJoinPacket();

                } else if (tokens[2].equals("LEAVE")) {

                    packet = new ClientLeavePacket();

                }
            } else if (tokens[1].equals("SERVER")) {
                if (tokens[2].equals("STATE")) {
                    if (tokens[3].equals("POSITION")) {

                        String name = tokens[4];
                        Vertex vertex = tokensToVertex(tokens[5], tokens[6], tokens[7]);
                        packet = new ServerStatePositionPacket(name, vertex);

                    }
                } else if (tokens[2].equals("CONFIRM")) {
                    if (tokens[3].equals("JOIN")) {

                        packet = new ServerConfirmJoinPacket();

                    } else if (tokens[3].equals("SET")) {

                        packet = new ServerConfirmSetPacket();

                    }
                } else if (tokens[2].equals("ADD")) {

                    packet = new ServerRegisterplayerPacket(tokens[3]);

                } else if (tokens[2].equals("REMOVE")) {

                    packet = new ServerRemoveplayerPacket(tokens[3]);

                }
            }
        }
        return packet;
    }

    private static boolean hasStartAndEnd(String[] tokens) {
        if (tokens.length < 2) {
            return false;
        }
        return (tokens[0].equals("START") && tokens[tokens.length - 1].equals("END"));
    }

    private static Vertex tokensToVertex(String val1, String val2, String val3) {
        Vertex vertex = null;
        try {
            vertex = new Vertex(0, 0, 0);
            vertex.x = Float.parseFloat(val1);
            vertex.y = Float.parseFloat(val2);
            vertex.z = Float.parseFloat(val3);
        } catch (NumberFormatException e) {
            vertex = null;
            System.out.println(e.getMessage());
        }

        return vertex;
    }

}
