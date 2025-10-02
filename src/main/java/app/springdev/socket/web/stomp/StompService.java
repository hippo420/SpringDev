package app.springdev.socket.web.stomp;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class StompService {
    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void sendPrivateMessage(String username, ChatMessage msg) {
        messagingTemplate.convertAndSendToUser(username, "/queue/private", msg);
    }
}
