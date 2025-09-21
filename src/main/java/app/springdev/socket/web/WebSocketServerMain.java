package app.springdev.socket.web;

import jakarta.websocket.*;
import jakarta.websocket.server.ServerEndpoint;
import org.springframework.boot.actuate.endpoint.annotation.Endpoint;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@ServerEndpoint("/chat")
public class WebSocketServerMain {
    private static Set<Session> clients = Collections.synchronizedSet(new HashSet<>());
    @OnOpen
    public void onOpen(Session session) {
        clients.add(session);
        System.out.println("새 클라이언트 접속: " + session.getId());
    }

    @OnMessage
    public void onMessage(String message, Session session) throws Exception {
        System.out.println("[" + session.getId() + "] " + message);

        // 모든 클라이언트에게 메시지 전송
        synchronized (clients) {
            for (Session client : clients) {
                client.getBasicRemote().sendText("[" + session.getId() + "] " + message);
            }
        }
    }

    @OnClose
    public void onClose(Session session) {
        clients.remove(session);
        System.out.println("클라이언트 종료: " + session.getId());
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        System.err.println("에러: " + throwable.getMessage());
    }
}
