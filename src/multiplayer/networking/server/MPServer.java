package multiplayer.networking.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import graphics.objects.Vertex;
import multiplayer.networking.tpackets.Packet;
import multiplayer.networking.tpackets.clientPackets.ClientJoinPacket;
import multiplayer.networking.tpackets.clientPackets.ClientLeavePacket;
import multiplayer.networking.tpackets.clientPackets.ClientRequestPacket;
import multiplayer.networking.tpackets.clientPackets.setters.ClientSetPositionPacket;
import multiplayer.networking.tpackets.serverPackets.ServerConfirmJoinPacket;
import multiplayer.networking.tpackets.serverPackets.ServerConfirmSetPacket;
import multiplayer.networking.tpackets.serverPackets.ServerRegisterplayerPacket;
import multiplayer.networking.tpackets.serverPackets.ServerRemoveplayerPacket;
import multiplayer.networking.tpackets.serverPackets.ServerStatePositionPacket;

public class MPServer {
    // MAIN METHOD
    public static void main(String[] args) throws IOException {
        MPServer server = new MPServer();
        server.runServer();
        server.close();
    }

    // ATTRIBUTES
    private DatagramChannel dataChannel;
    private SocketAddress recentClient;

    private Packet recentPacket = null;
    private ArrayList<Packet> responsePackets = new ArrayList<>();
    private ArrayList<Packet> packetLog = new ArrayList<>();

    private HashMap<SocketAddress, MPServerPlayer> players = new HashMap<>();
    private int playerCount = 0;

    public static final Integer port = 4600;

    // Set up server channel listening
    public MPServer() throws IOException {
        // Setup networking

        dataChannel = DatagramChannel.open();
        InetSocketAddress address = new InetSocketAddress("localhost", port);
        dataChannel.bind(address);
        System.out.println("Server listening on port " + port + ": ...");

    }

    // Run the server
    public void runServer() throws IOException {
        while (true) {

            receivePacket();
            generateResponsePacket();
            if (responsePackets.size() != 0) {
                for (Packet packet : responsePackets) {
                    sendResponsePacket(packet);
                }
            }

            packetLog.add(recentPacket);
            if (recentPacket == null) {
                System.out.println("Client at " + recentClient + " did not send valid packet");
            } else {
                System.out.println(
                        "Client at " + recentClient + " sent packet of type: " + recentPacket.getClass().toString());
            }

        }
    }

    // Recieves a packet from the server
    public void receivePacket() throws IOException {
        // Receive data from client

        ByteBuffer buffer = ByteBuffer.allocate(1024);
        recentClient = dataChannel.receive(buffer);
        recentPacket = Packet.byteBufferToPacket(buffer);

    }

    // Interpret recent packet and form appropriate response
    public void generateResponsePacket() {
        responsePackets = new ArrayList<Packet>();
        if (recentPacket != null) {

            Class packetClass = recentPacket.getClass();
            Set<SocketAddress> ips = players.keySet();

            if (ips.contains(recentClient)) {
                responsePackets = players.get(recentClient).loadQueue();
                if (packetClass.equals(ClientLeavePacket.class)) {
                    ;
                    processLeavePacket();
                } else if (packetClass.equals(ClientSetPositionPacket.class)) {
                    processSetPositionPacket();
                } else if (packetClass.equals(ClientRequestPacket.class)) {
                    processRequestPacket();
                }

            } else {

                if (packetClass.equals(ClientJoinPacket.class)) {
                    processJoinPacket();
                }

            }

        }
    }

    // Process a client set state request operation
    public void processRequestPacket() {
        MPServerPlayer clientPlayer = players.get(recentClient);
        // For every player that is not the current player queue up a packet informing
        // current player of their position
        for (MPServerPlayer otherPlayer : players.values()) {

            if (!otherPlayer.equals(clientPlayer)) {

                responsePackets.add(new ServerStatePositionPacket(otherPlayer.getName(), otherPlayer.getPosition()));

            }

        }

    }

    // Process a client leave packet
    public void processLeavePacket() {
        playerCount--;

        // Inform all players of leaving
        MPServerPlayer clientPlayer = players.get(recentClient);
        sendPacketToAllOther(new ServerRemoveplayerPacket(clientPlayer.getName()), clientPlayer);

        // Remoge the player from the player ip Map
        players.remove(recentClient);
        ;
    }

    // Process a client set position packet and moves the MPServerPlayer
    public void processSetPositionPacket() {
        MPServerPlayer clientPlayer = players.get(recentClient);

        // Move the client's player object to the position they give
        ClientSetPositionPacket packet = (ClientSetPositionPacket) recentPacket;
        clientPlayer.setPosition(packet.position);

        // Send back a confirm set packet
        responsePackets.add(new ServerConfirmSetPacket());
    }

    // Process a client join packet
    public void processJoinPacket() {
        playerCount++;
        // Get the name we will assign to the new player
        String playerName = String.valueOf(playerCount);
        // Add the new player to the map of players to ips
        MPServerPlayer clientPlayer = new MPServerPlayer(playerName);
        players.put(recentClient, clientPlayer);

        responsePackets.add(new ServerConfirmJoinPacket());

        sendPacketToAllOther(new ServerRegisterplayerPacket(clientPlayer.getName()), clientPlayer);
        sendJoinPacketsForOthers(clientPlayer);
    }

    // Queues up a specific packet to all other players in game
    public void sendPacketToAllOther(Packet packet, MPServerPlayer currentPlayer) {
        for (MPServerPlayer otherPlayer : players.values()) {

            if (!otherPlayer.equals(currentPlayer)) {
                otherPlayer.queuePacket(packet);
            }

        }
    }

    public void sendJoinPacketsForOthers(MPServerPlayer currentPlayer) {
        for (MPServerPlayer otherPlayer : players.values()) {

            if (!otherPlayer.equals(currentPlayer)) {
                currentPlayer.queuePacket(new ServerRegisterplayerPacket(otherPlayer.getName()));
            }

        }
    }

    // Respond to a received message
    public void sendResponsePacket(Packet packet) throws IOException {
        // Receive data from client
        ByteBuffer buffer = ByteBuffer.wrap(packet.toString().getBytes());
        dataChannel.send(buffer, recentClient);
    }

    // Close the Server
    public void close() throws IOException {
        dataChannel.close();
    }

}
