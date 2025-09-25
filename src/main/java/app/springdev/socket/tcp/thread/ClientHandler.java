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
        String threadName = Thread.currentThread().getName();
        System.out.println("Socket-"+System.identityHashCode(this.socket)+" [" + threadName + "]  Process: "
                + socket.getInetAddress().getHostAddress());
        try (

                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true)

        ) {
            String msg;
            while ((msg = in.readLine()) != null) {

                System.out.println(" [" + threadName + "] Received: " + msg);

                out.println("Echo: " + msg);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                socket.close();
                System.out.println("Terminated From Client");
            } catch (IOException e) {
                System.err.println("Error on Terminated From Client: " + e.getMessage());
            }
        }
    }
}
