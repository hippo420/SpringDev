package app.springdev.socket.tcp.throttle;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ThrottledTcpServer {
    private static final int PORT = 5000;
    private static final int MAX_MESSAGES_PER_SECOND = 5;

    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            System.out.println("유량제어 적용 TCP 서버 시작");

            while (true) {
                Socket clientSocket = serverSocket.accept();
                new Thread(new ThrottledClientHandler(clientSocket)).start();
            }
        } catch (IOException e) {
            System.err.println("서버 소켓 생성 실패: " + e.getMessage());
        }
    }

    static class ThrottledClientHandler implements Runnable {
        private Socket socket;
        private int messageCount = 0;
        private long startTime = System.currentTimeMillis();

        public ThrottledClientHandler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                 BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

                String line;
                while ((line = reader.readLine()) != null) {
                    long now = System.currentTimeMillis();

                    if (now - startTime >= 1000) {
                        messageCount = 0;
                        startTime = now;
                    }

                    if (messageCount >= MAX_MESSAGES_PER_SECOND) {
                        writer.write("유량제어: 초당 메시지 처리 제한 초과, 잠시 대기하세요.\n");
                        writer.flush();
                        Thread.sleep(200); // 200ms 대기
                        continue;
                    }

                    messageCount++;
                    System.out.println("수신: " + line);
                    writer.write("서버 응답: " + line + "\n");
                    writer.flush();
                }
            } catch (IOException | InterruptedException e) {
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
