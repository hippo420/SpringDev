package app.springdev.elastic.datasync.outbox.rabbit.message;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncDataPublisher {
    private final RabbitTemplate rabbitTemplate;

    public void sendMessage(String exchange, String routingKey, Object payload) {
        log.info("sendMessage - exchange: {} routingKey: {} payload: {}", exchange,routingKey, payload);

        rabbitTemplate.convertAndSend(exchange, routingKey, payload.toString());
    }
}
