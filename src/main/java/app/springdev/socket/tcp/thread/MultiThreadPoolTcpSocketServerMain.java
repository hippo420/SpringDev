package app.springdev.socket.tcp.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadPoolTcpSocketServerMain {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(5000);
        ExecutorService pool = Executors.newFixedThreadPool(10); // 최대 10명 동시 처리
        System.out.println("서버 시작, 포트 5000");

        while (true) {
            Socket socket = serverSocket.accept();
            pool.execute(new ClientHandler(socket));
        }
    }
}
