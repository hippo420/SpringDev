package app.springdev.socket.udp.thread;

import app.springdev.socket.tcp.thread.ClientHandler;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class MultiThreadUdpServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("멀티쓰레드 UDP 서버 시작");
            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                new Thread(new UDPClientHandler(socket, packet)).start();
            }
        }
    }
}
