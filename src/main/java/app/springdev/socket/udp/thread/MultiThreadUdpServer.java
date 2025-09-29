package app.springdev.socket.udp.thread;

import app.springdev.socket.tcp.thread.ClientHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MultiThreadUdpServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("MultiThreadUdpServer started...");
            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                UDPClientHandler handler = new UDPClientHandler(socket, packet);
                new Thread(handler).start();
            }
        }
    }
}
