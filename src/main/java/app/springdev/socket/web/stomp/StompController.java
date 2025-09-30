package app.springdev.socket.web.stomp;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
@MessageMapping("/stomp")
public class StompController {
    // 채팅 메시지 처리
    @MessageMapping("/chat.send")
    @SendTo("/topic/chat")
    public String sendChat(String message) {
        return "Chat: " + message;
    }

    // 알림 메시지 처리
    @MessageMapping("/notice.send")
    @SendTo("/topic/notice")
    public String sendNotice(String message) {
        return "Notice: " + message;
    }
}
