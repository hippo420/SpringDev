package app.springdev.rabbit.message;

import app.springdev.rabbit.config.RabbitConfig;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Service;
import com.rabbitmq.client.Channel;
import java.io.IOException;

@Slf4j
@Service
@AllArgsConstructor
public class MessageConsumer {
    // 🌟 수동 ACK(Manual Acknowledgement) 모드 사용: 실무에서 필수
    // spring.rabbitmq.listener.simple.acknowledge-mode: manual 설정이 필요
    @RabbitListener(queues = RabbitConfig.QUORUM_QUEUE_NAME)
    public void receiveMessage(Object message, Channel channel, @Header(AmqpHeaders.DELIVERY_TAG) long tag) {
        try {
            log.info("Received message: {}", message);
            // 1. 메시지 처리 로직 (DB 저장, 외부 API 호출 등)

            // 2. 처리 완료 시 RabbitMQ에 ACK 전송 (성공적으로 처리했으니 큐에서 삭제해라)
            channel.basicAck(tag, false); // false: 이 메시지만 ACK 처리

        } catch (Exception e) {
            log.error("Message processing failed: {}", e.getMessage());
            try {
                // 3. 처리 실패 시 NACK 전송
                // false: 재큐잉 하지 마라 (DLX가 설정되어 있다면 DLX로 이동)
                channel.basicNack(tag, false, false);
            } catch (IOException ioException) {
                log.error("NACK failed: {}", ioException.getMessage());
            }
        }
    }
}
