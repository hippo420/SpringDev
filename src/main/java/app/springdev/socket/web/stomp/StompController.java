package app.springdev.socket.web.stomp;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class StompController {
    // 채팅 메시지 처리
    @MessageMapping("/chat")
    @SendTo("/chat/room/{roomId}")
    public String sendChat(@DestinationVariable String roomId, String message) {
        return "Room " + roomId + ": " + message;
    }

    // 채팅 메시지 처리
    @MessageMapping("/private")
    public String sendChatPrivate(String message) {
        return "Private-Chat: " + message;
    }

    // 알림 메시지 처리
    @MessageMapping("/notice")
    @SendTo("/notice/board")
    public String sendNotice(String message) {
        return "Notice: " + message;
    }


}
