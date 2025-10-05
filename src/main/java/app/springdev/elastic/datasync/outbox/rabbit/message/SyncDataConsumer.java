package app.springdev.elastic.datasync.outbox.rabbit.message;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.outbox.rabbit.config.RabbitMQConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpRejectAndDontRequeueException;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
@Slf4j
@Component
@RequiredArgsConstructor
public class SyncDataConsumer {
    private final ObjectMapper objectMapper;
    private final NotiElasticRepository notiElasticRepository;

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void consume(String message) {
        log.info("consume - message: {}", message);
        try {
            // JSON → 객체 변환
            NoticeDocument document = objectMapper.readValue(message, NoticeDocument.class);

            // 공통 처리 함수 호출
            processMessage(document);

        } catch (Exception e) {
            // 실패 처리 (Dead Letter Queue로 보낼 수도 있음)
            log.info("메시지 처리 실패: {}", e.getMessage());
            throw new AmqpRejectAndDontRequeueException("메시지 처리 실패", e);
        }
    }

    private void processMessage(NoticeDocument document) {
        notiElasticRepository.save(document);
        log.info("처리 완료: {}", document);
    }
}
