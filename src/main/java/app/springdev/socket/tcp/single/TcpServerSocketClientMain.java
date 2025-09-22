package app.springdev.socket.tcp.single;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpServerSocketClientMain {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg;
            while ((msg = input.readLine()) != null) {
                out.println(msg);
                System.out.println("서버 응답: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
