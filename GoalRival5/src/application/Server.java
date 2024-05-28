package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Server {
    private DatagramSocket socket;

    public Server(int port) throws IOException {
        socket = new DatagramSocket(port);
    }

    public void listen() {
        new Thread(() -> {
            try {
                byte[] buffer = new byte[1024];
                while (true) {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    // Handle received message (e.g., relay to other players)
                    System.out.println("Received message: " + message);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void main(String[] args) {
        try {
            Server server = new Server(5000);
            server.listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}