package app.springdev.socket.tcp.thread;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class ClientHandler implements Runnable{
    private Socket socket;

    public ClientHandler(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try (
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)
        ) {
            String msg;
            while ((msg = in.readLine()) != null) {
                System.out.println("[" + socket.getInetAddress() + "] " + msg);
                out.println("Echo: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                System.out.println("클라이언트 연결 종료");
            } catch (IOException e) {
                System.err.println("소켓 종료 중 오류: " + e.getMessage());
            }
        }
    }
}
