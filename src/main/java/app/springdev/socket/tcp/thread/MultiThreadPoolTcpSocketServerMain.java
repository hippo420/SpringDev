package app.springdev.socket.tcp.thread;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadPoolTcpSocketServerMain {
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(5000);
                ExecutorService pool = Executors.newFixedThreadPool(2))
        {
            serverSocket.setReuseAddress(true);
            System.out.println("Server Start, Port: 5000");

            while (true) {
                Socket socket = serverSocket.accept();
                pool.execute(new ClientHandler(socket));
            }
        } catch (IOException e) {
            System.err.println("Failed start ServerSocket: " + e.getMessage());
        }
    }
}
