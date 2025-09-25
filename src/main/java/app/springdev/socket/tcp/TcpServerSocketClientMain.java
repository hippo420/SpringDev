package app.springdev.socket.tcp;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class TcpServerSocketClientMain {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 5000)) {
            System.out.println("Client Joined: !!");
            socket.setSoTimeout(5000);   //최대 대기 시간 (기본값 :0 - 무한대기)
            /** Nagle 알고리즘 사용 여부
              * false(기본): 작은 패킷 여러 개를 묶어서 전송 → 효율↑, 지연 발생 가능.
              * true: 즉시 전송 → 속도↑, 트래픽↑
             **/
            socket.setTcpNoDelay(true);
            socket.setKeepAlive(true);                 //유휴 상태시 Health체크
            socket.setSendBufferSize(128 * 1024);      //송신 버퍼 크기 조정
            socket.setReceiveBufferSize(128 * 1024);   //수신 버퍼 크기 조정
            socket.setReuseAddress(true);              //동일 포트 재사용 가능 여부

            //잘사용안하는 것들
            //socket.setOOBInline(true); //긴급 데이터(Urgent Data, OOB) 를 일반 데이터 스트림에 포함시킬지 여부.
            //socket.setTrafficClass(0x10); //IP 헤더의 TOS 필드 설정 [0x10-저지연, 0x08-높은 처리량, 0x04-신뢰성]
            //socket.setPerformancePreferences(1, 2, 0); //연결시간, 지연시간, 대역폭 우선순위 지정.
            //socket.shutdownOutput(); // 더 이상 데이터 보낼 수 없음
            //socket.shutdownInput();  // 더 이상 데이터 받을 수 없음

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

            String msg;
            while ((msg = input.readLine()) != null) {
                out.println(msg);
                System.out.println("Client Receive: " + in.readLine());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
