package app.springdev.socket.udp.thread;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadPoolUdpServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT);
            ExecutorService pool = Executors.newFixedThreadPool(2)) {

            System.out.println("MultiThreadUdpServer started...");
            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                UDPClientHandler handler = new UDPClientHandler(socket, packet);
                pool.submit(handler);
            }
        }
    }
}
