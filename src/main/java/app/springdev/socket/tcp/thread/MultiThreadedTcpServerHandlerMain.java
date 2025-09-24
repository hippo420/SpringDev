package app.springdev.socket.tcp.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedTcpServerHandlerMain {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("멀티쓰레드 TCP 서버 시작");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                // 클라이언트 연결 수락 후 ClientHandler 인스턴스를 새 쓰레드에서 실행
                ClientHandler clientHandler = new ClientHandler(clientSocket);
                new Thread(clientHandler).start();
            }
        } catch (IOException e) {
            System.err.println("서버 소켓 생성 실패: " + e.getMessage());
        }
    }

}
