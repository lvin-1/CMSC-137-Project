package application;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

public class GameServer {
    private DatagramSocket socket;
    private List<ClientInfo> clients;

    public GameServer(int port) throws IOException {
        socket = new DatagramSocket(port);
        clients = new ArrayList<>();
    }

    public void start() {
        new Thread(() -> {
            byte[] buffer = new byte[1024];
            while (true) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                    socket.receive(packet);
                    String message = new String(packet.getData(), 0, packet.getLength());
                    InetAddress address = packet.getAddress();
                    int port = packet.getPort();
                    ClientInfo client = new ClientInfo(address, port);
                    if (!clients.contains(client)) {
                        clients.add(client);
                    }else {
                    	byte[] b1 = "AlreadyConnected".getBytes();
                    	DatagramPacket p1 = new DatagramPacket(b1, b1.length, client.getAddress(), client.getPort());
                        socket.send(p1);
                    }
                    broadcastMessage(message, address, port);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void broadcastMessage(String message, InetAddress senderAddress, int senderPort) {
        byte[] buffer = message.getBytes();
        for (ClientInfo client : clients) {
            if (!client.getAddress().equals(senderAddress) || client.getPort() != senderPort) {
                try {
                    DatagramPacket packet = new DatagramPacket(buffer, buffer.length, client.getAddress(), client.getPort());
                    socket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    
    private static class ClientInfo {
        private final InetAddress address;
        private final int port;

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
