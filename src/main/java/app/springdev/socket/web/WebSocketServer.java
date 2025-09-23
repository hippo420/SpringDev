package app.springdev.socket.web;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.apache.catalina.Server;

import java.io.BufferedReader;
import java.io.InputStreamReader;

@ServerEndpoint("/chat")
public class WebSocketServer {

    @OnOpen
    public void onOpen(Session session) {
        System.out.println("새 연결: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("수신: " + message);
        // 연결된 모든 클라이언트에게 브로드캐스트
        for (Session s : session.getOpenSessions()) {
            if (s.isOpen()) {
                s.getBasicRemote().sendText("[" + session.getId() + "] " + message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        System.out.println("연결 종료: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("에러: " + throwable.getMessage());
    }

    // 메인 메서드로 서버 실행
    public static void main(String[] args) throws Exception {
//        Server server = new Server("localhost", 9090, "/ws", WebSocketServer.class);
//        try {
//            server.start();
//            System.out.println("WebSocket 서버 실행 중... ws://localhost:8080/ws/chat");
//            new BufferedReader(new InputStreamReader(System.in)).readLine();
//        } finally {
//            server.stop();
//        }
    }
}
