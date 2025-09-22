package app.springdev.socket.tcp;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class TcpSocketClientMain {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

            BufferedReader keyboard = new BufferedReader(new InputStreamReader(System.in));
            String userInput;
            System.out.println("서버에 메시지 전송, 종료하려면 'exit' 입력");

            while (!(userInput = keyboard.readLine()).equalsIgnoreCase("exit")) {
                out.write(userInput + "\n");
                out.flush();

                String response = in.readLine();
                System.out.println("서버로부터 응답: " + response);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
