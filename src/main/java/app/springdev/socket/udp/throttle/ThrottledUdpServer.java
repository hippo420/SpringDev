package app.springdev.socket.udp.throttle;

import java.net.*;

public class ThrottledUdpServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;
    private static final int MAX_MSG_PER_SECOND = 5;

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT)) {
            System.out.println("유량제어 UDP 서버 시작");

            byte[] buffer = new byte[BUFFER_SIZE];
            int msgCount = 0;
            long startTime = System.currentTimeMillis();

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                long now = System.currentTimeMillis();
                if (now - startTime > 1000) {
                    msgCount = 0;
                    startTime = now;
                }

                if (msgCount >= MAX_MSG_PER_SECOND) {
                    String limitMsg = "유량제어: 메시지 처리 제한 초과";
                    byte[] limitData = limitMsg.getBytes();
                    DatagramPacket limitPacket = new DatagramPacket(
                            limitData, limitData.length, packet.getAddress(), packet.getPort());
                    socket.send(limitPacket);
                    continue;
                }

                msgCount++;

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
