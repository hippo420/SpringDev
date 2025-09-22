package app.springdev.socket.tcp.single;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpServerSocketMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("서버 시작 (단일 쓰레드)");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    InputStream in = clientSocket.getInputStream();
                    OutputStream out = clientSocket.getOutputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("수신: " + line);
                        writer.write("서버 응답: " + line + "\n");
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.err.println("클라이언트 처리 중 오류: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("서버 소켓 생성 실패: " + e.getMessage());
        }
    }

}
