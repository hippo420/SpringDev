package app.springdev.socket.udp.file;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.*;

public class FileUdpServer {
    private static final int PORT = 5000;
    private static final int BUFFER_SIZE = 1024;
    private static final String FILE_PATH = "received_udp_messages.txt";

    public static void main(String[] args) throws Exception {
        try (DatagramSocket socket = new DatagramSocket(PORT);
             BufferedWriter fileWriter = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            System.out.println("파일 처리 UDP 서버 시작");

            byte[] buffer = new byte[BUFFER_SIZE];

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                socket.receive(packet);

                String msg = new String(packet.getData(), 0, packet.getLength());
                System.out.println("수신 메시지: " + msg);

                // 파일에 저장
                fileWriter.write(msg);
                fileWriter.newLine();
                fileWriter.flush();

                String response = "서버 응답: " + msg;
                byte[] sendData = response.getBytes();

                DatagramPacket sendPacket = new DatagramPacket(
                        sendData, sendData.length, packet.getAddress(), packet.getPort());
                socket.send(sendPacket);
            }
        }
    }
}
