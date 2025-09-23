package app.springdev.socket.tcp.single;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.StandardSocketOptions;

public class TcpServerSocketMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000,50)) {
            serverSocket.setReuseAddress(true);           //서버 재시작 시, 같은 포트를 바로 다시 바인딩
            serverSocket.setReceiveBufferSize(64 * 1024); //수신 버퍼 크기
            serverSocket.setSoTimeout(5000);              //수신 최대시간

            /**
             * setOption
             */
            // 포트 재사용 허용
            //serverSocket.setOption(StandardSocketOptions.SO_REUSEADDR, true);
            // 수신 버퍼 크기 설정
            //serverSocket.setOption(StandardSocketOptions.SO_RCVBUF, 64 * 1024);

            //특정 NIC,IP에만 바인딩처리시
            //ServerSocket serverSocket = new ServerSocket();
            //serverSocket.bind(new InetSocketAddress("192.168.0.100", 5000));
            System.out.println("Server Started - PORT 5000");

            while (true) {
                try (Socket clientSocket = serverSocket.accept()) {
                    InputStream in = clientSocket.getInputStream();
                    OutputStream out = clientSocket.getOutputStream();

                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(out));

                    String line;
                    while ((line = reader.readLine()) != null) {
                        System.out.println("Receive: " + line);
                        writer.write("Server Response: " + line + "\n");
                        writer.flush();
                    }
                } catch (IOException e) {
                    System.err.println("Client Error: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            System.err.println("Failed ServerSocket: " + e.getMessage());
        }
    }

}
