package app.springdev.socket.tcp;

import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpFileServerMain {
    public static void main(String[] args) {
        int port = 5000;
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            System.out.println("Server is running Now: " + port);

            Socket socket = serverSocket.accept();
            System.out.println("Connected Client: " + socket.getInetAddress());

            // 파일 저장할 경로
            File file = new File("D:\\workspace\\SpringDev\\src\\main\\resources\\data\\received_file.pdf");

            FileOutputStream fos = new FileOutputStream(file);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            InputStream is = socket.getInputStream();

            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.close();
            System.out.println("Successfully received file!");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
