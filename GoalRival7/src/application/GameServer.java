package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private DatagramSocket socket; // Datagram socket for communication
    private List<ClientInfo> clients; // List to store client information

    // Constructor to initialize the server with the specified port
    public GameServer(int port) throws IOException {
        socket = new DatagramSocket(port); // Create a new datagram socket bound to the specified port
        clients = new ArrayList<>(); // Initialize the clients list
    }

    // Method to start the server
    public void start() {
        new Thread(() -> {
            byte[] buffer = new byte[1024]; // Buffer to store incoming data
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet); // Receive a packet from a client
                    String message = new String(packet.getData(), 0, packet.getLength());
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    ClientInfo client = new ClientInfo(address, port); // Create a new client info object

                    if (!clients.contains(client)) {
                        clients.add(client); // Add the new client to the list if not already present
                    } else {
                        // If the client is already connected, send an "AlreadyConnected" message
                        byte[] b1 = "AlreadyConnected".getBytes();
                        DatagramPacket p1 = new DatagramPacket(b1, b1.length, client.getAddress(), client.getPort());
                        socket.send(p1); // Send the packet to the client
                    }

                    broadcastMessage(message, address, port); // Broadcast the received message to other clients
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace if an error occurs
                }
            }
        }).start(); // Start the thread
    }

    // Method to broadcast a message to all clients except the sender
    private void broadcastMessage(String message, InetAddress senderAddress, int senderPort) {
        byte[] buffer = message.getBytes();
        for (ClientInfo client : clients) {
            if (!client.getAddress().equals(senderAddress) || client.getPort() != senderPort) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, client.getAddress(), client.getPort());
                    socket.send(packet); // Send the packet to the client
                } catch (IOException e) {
                    e.printStackTrace(); // Print the stack trace if an error occurs
                }
            }
        }
    }

    // Inner class to store client information
    private static class ClientInfo {
        private final InetAddress address; // Client's IP address
        private final int port; // Client's port number

        public ClientInfo(InetAddress address, int port) {
            this.address = address;
            this.port = port;
        }

        public InetAddress getAddress() {
            return address;
        }

        public int getPort() {
            return port;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null || getClass() != obj.getClass()) return false;
            ClientInfo that = (ClientInfo) obj;
            return port == that.port && address.equals(that.address);
        }

        @Override
        public int hashCode() {
            return address.hashCode() + port;
        }
    }
}
