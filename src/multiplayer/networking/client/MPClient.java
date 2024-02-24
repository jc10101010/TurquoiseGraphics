package multiplayer.networking.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;

import multiplayer.networking.server.MPServer;
import multiplayer.networking.tpackets.Packet;

public class MPClient {
    // MAIN METHOD
    public static void main(String[] args) throws IOException {
        // Setup input reader and client
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        MPClient client = new MPClient();
        // While input is not done. read it and send it
        while (true) {
            String input = reader.readLine();
            client.sendString(input);
            client.receivePackets();
            while (client.isPacketAvailable()) {
                System.out.println("Server said: " + client.getFirstPacket().toString());
            }
        }
        // client.close();
    }

    // ATTRIBUTES
    private DatagramChannel dataChannel;
    private ArrayList<Packet> unhandledPackets = new ArrayList<>();
    private Packet currentPacket = null;
    public static Integer port = 4600;

    // NORMAL METHODS
    // Sets up a client that can send messgaes to the server, hooks up to localhost
    public MPClient() throws IOException {
        // Open datagram channel
        dataChannel = DatagramChannel.open();
        dataChannel.bind(null);
        dataChannel.configureBlocking(false);
    }

    // Takes in string and sends it then sends it to server
    public void sendPacket(Packet packet) throws IOException {
        sendString(packet.toString());
    }

    public void sendString(String value) throws IOException {
        InetSocketAddress serverAddress = new InetSocketAddress("localhost", port);
        ByteBuffer buffer = ByteBuffer.wrap(value.getBytes());
        dataChannel.send(buffer, serverAddress);
        buffer.clear();
    }

    // Recursive read function that reads all messages available to be read
    public void receivePackets() throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        SocketAddress add = dataChannel.receive(buffer);
        if (add != null) {
            unhandledPackets.add(Packet.byteBufferToPacket(buffer));
            receivePackets();
        }
    }

    public boolean isPacketAvailable() {
        if (unhandledPackets.size() == 0) {
            return false;
        } else {
            currentPacket = unhandledPackets.get(0);
            unhandledPackets.remove(0);
            return true;
        }
    }

    public Packet getFirstPacket() {
        return currentPacket;
    }

    public void close() throws IOException {
        dataChannel.close();
    }
}
