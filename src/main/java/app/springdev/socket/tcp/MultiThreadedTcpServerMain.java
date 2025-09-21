package app.springdev.socket.tcp;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedTcpServerMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(6000)) {
            System.out.println("멀티스레드 TCP 서버 시작 (포트 6000)");
            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
