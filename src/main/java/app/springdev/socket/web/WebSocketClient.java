package app.springdev.socket.web;


import jakarta.websocket.*;

import java.net.URI;

@ClientEndpoint
public class WebSocketClient {

    private Session session;

    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        System.out.println("서버에 연결됨");
        sendMessage("Hello, WebSocket!");
    }

    @OnMessage
    public void onMessage(String message) {
        System.out.println("수신 메시지: " + message);
    }

    public void sendMessage(String message) {
        try {
            session.getBasicRemote().sendText(message);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        String uri = "ws://localhost:9090/ws";
        try {
            container.connectToServer(WebSocketClient.class, URI.create(uri));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
