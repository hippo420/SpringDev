package app.springdev.rabbit.message;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
@AllArgsConstructor
public class MessageProducer {
    private final RabbitTemplate rabbitTemplate;
    public static final String EXCHANGE_NAME = "trade.direct";
    public static final String ROUTING_KEY = "trade.key";

    @PostConstruct // 빈 생성 후 한 번만 실행
    public void init() {
        // 🌟 Publisher Confirm (Broker ACK) 콜백 설정
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                // 메시지가 브로커(Exchange)에 성공적으로 도착 (ACK)
                log.info("Broker ACK: Success. ID: {}", correlationData.getId());
            } else {
                // 메시지가 브로커에 도착 실패 또는 NACK
                log.error("Broker NACK: Fail. Cause: {}. ID: {}", cause, correlationData.getId());
                // 이곳에서 DB에 저장된 메시지를 재전송 시도하는 로직을 구현합니다.
            }
        });

        // 🌟 Return Callback (라우팅 실패 처리) 설정
        rabbitTemplate.setReturnsCallback(returned -> {
            // 메시지가 Exchange에 도착했으나, Binding Rule을 만족하는 큐가 없어 라우팅 실패
            log.error("Routing Failure: Message returned. Exchange: {}, Routing Key: {}, Reply Text: {}",
                    returned.getExchange(), returned.getRoutingKey(), returned.getReplyText());
            // 이 경우에도 메시지 유실을 방지하기 위해 별도의 처리 (예: DB 저장 후 재처리) 필요
        });
    }

    public void sendMessage(String message,final RouteKey  routeKey) {
        // 메시지 전송 시 CorrelationData를 설정하여 성공/실패 시 어떤 메시지인지 식별 가능하게 합니다.
        CorrelationData correlationData = new CorrelationData(UUID.randomUUID().toString());

        log.info("Sending message: {}", message);
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, routeKey.getRoutingKey(), message, correlationData);
    }
}
