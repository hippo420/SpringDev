package app.springdev.socket.udp.single;

import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class SingleThreadUdpServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("싱글 쓰레드 UDP 서버 시작");

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("수신 메시지: " + msg);

                String response = "서버 응답: " + msg;
                byte[] sendData = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length, packet.getAddress(), packet.getPort());
                socket.send(sendPacket);
            }
        }
    }
}
