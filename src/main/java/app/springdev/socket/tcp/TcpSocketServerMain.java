package app.springdev.socket.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketServerMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000)) {
            System.out.println("서버가 5000 포트에서 시작됨...");

            while (true) {
                Socket socket = serverSocket.accept();
                System.out.println("클라이언트 접속: " + socket.getInetAddress());

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    System.out.println("받은 메시지: " + inputLine);
                    out.write("서버 응답: " + inputLine + "\n");
                    out.flush();
                }
                socket.close();
                System.out.println("클라이언트 연결 종료");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
