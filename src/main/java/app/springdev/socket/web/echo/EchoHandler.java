package app.springdev.socket.web.echo;

import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

public class EchoHandler extends TextWebSocketHandler {
    @Override
    public void handleMessage(WebSocketSession session, WebSocketMessage<?> message) throws Exception {
        String payload = message.getPayload().toString();

        if (payload.startsWith("chat:")) {
            session.sendMessage(new TextMessage("채팅 처리: " + payload));
        } else if (payload.startsWith("notice:")) {
            session.sendMessage(new TextMessage("공지 처리: " + payload));
        } else {
            session.sendMessage(new TextMessage("알 수 없는 요청"));
        }
    }
}
