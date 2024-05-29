package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    private DatagramSocket socket; // Datagram socket to receive and send packets

    // Constructor to initialize the server with a specific port
    public Server(int port) throws IOException {
        socket = new DatagramSocket(port); // Binds the socket to the specified port
    }

    // Method to start listening for incoming packets
    public void listen() {
        // Start a new thread to handle incoming packets
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024]; // Buffer to store incoming packet data
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length); // Create a new DatagramPacket to receive data
                    socket.receive(packet); // Receive packet from the socket
                    String message = new String(packet.getData(), 0, packet.getLength()); // Convert packet data to a string
                    // Handle received message (e.g., relay to other players)
                    System.out.println("Received message: " + message); // Print the received message to the console
                }
            } catch (IOException e) {
                e.printStackTrace(); // Print stack trace if an IOException occurs
            }
        }).start(); // Start the thread
    }

    // Main method to start the server
    public static void main(String[] args) {
        try {
            Server server = new Server(5000); // Create a new server instance on port 5000
            server.listen(); // Start listening for incoming packets
        } catch (IOException e) {
            e.printStackTrace(); // Print stack trace if an IOException occurs during server creation
        }
    }
}
