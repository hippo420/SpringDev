package app.springdev.socket.udp;


import java.io.*;
import java.net.*;

public class UdpClientMain {
    public static void main(String[] args) throws Exception {
        DatagramSocket clientSocket = new DatagramSocket();
        InetAddress serverAddress = InetAddress.getByName("localhost");
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        System.out.println("UDP Client started. Type messages to send:");

        while (true) {
            String msg = reader.readLine(); // 콘솔 입력
            if (msg == null || msg.equalsIgnoreCase("exit")) {
                break; // 종료 조건
            }

            byte[] sendBuffer = msg.getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendBuffer, sendBuffer.length, serverAddress, 5000);
            clientSocket.send(sendPacket);

            // 서버 응답 수신
            byte[] receiveBuffer = new byte[1024];
            DatagramPacket receivePacket = new DatagramPacket(receiveBuffer, receiveBuffer.length);
            clientSocket.receive(receivePacket);

            String response = new String(receivePacket.getData(), 0, receivePacket.getLength());
            System.out.println("Server response: " + response);
        }

        clientSocket.close();
        System.out.println("UDP Client terminated.");
    }
}
