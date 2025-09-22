package app.springdev.socket.tcp.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class MultiThreadedTcpServerMain {
    private static final int PORT = 5000;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("멀티쓰레드 TCP 서버 시작");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("서버 소켓 생성 실패: " + e.getMessage());
        }
    }

    static class ClientHandler implements Runnable {
        private Socket socket;

        public ClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println("수신: " + line);
                    writer.write("서버 응답: " + line + "\n");
                    writer.flush();
                }
            } catch (IOException e) {
                System.err.println("클라이언트 처리 중 오류: " + e.getMessage());
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    System.err.println("소켓 닫기 실패: " + e.getMessage());
                }
            }
        }
    }
}
