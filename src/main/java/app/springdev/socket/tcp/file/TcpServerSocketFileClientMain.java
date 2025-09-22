package app.springdev.socket.tcp.file;

import java.io.*;
import java.net.Socket;

public class TcpServerSocketFileClientMain {
    public static void main(String[] args) {
        String serverAddress = "127.0.0.1";
        int port = 5000;

        try (Socket socket = new Socket(serverAddress, port)) {
            System.out.println("Connected to " + socket.getInetAddress().getHostAddress() + ":" + socket.getPort());

            File file = new File("D:\\workspace\\SpringDev\\src\\main\\resources\\data\\test.pdf");
            FileInputStream fis = new FileInputStream(file);
            BufferedInputStream bis = new BufferedInputStream(fis);

            OutputStream os = socket.getOutputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = bis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }

            os.flush();
            System.out.println("Successfully send file!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
