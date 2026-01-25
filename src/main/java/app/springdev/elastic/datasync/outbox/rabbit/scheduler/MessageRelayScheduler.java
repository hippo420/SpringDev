package app.springdev.elastic.datasync.outbox.rabbit.scheduler;

import app.springdev.elastic.datasync.outbox.OutBoxRepository;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import app.springdev.elastic.datasync.outbox.rabbit.config.RabbitMQConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
@Slf4j
@Component
@ConditionalOnProperty(
        value = "scheduler.outbox-relay.enabled", // 확인할 설정 속성 이름
        havingValue = "true",                      // 해당 속성 값이 "true"일 때만 활성화
        matchIfMissing = false                      // 속성 자체가 없으면 기본적으로 활성화 (선택 사항)
)
public class MessageRelayScheduler {
    @Autowired
    private OutBoxRepository outboxRepository;

    @Autowired
    private RabbitTemplate rabbitTemplate;

    // 주기적으로 Outbox 테이블을 폴링
    @Scheduled(fixedDelay = 2000)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void sendPendingEvents() {

        //TODO 미사용시 트랜잭션 줄이기 위해 주석처리, 필요시 해제
        // PENDING 상태의 이벤트 조회 (LOCK을 사용하여 동시성 제어 필요)
        //List<OutboxEvent> pendingEvents = outboxRepository.findByAggregateTypeAndStatus("notice","PENDING");
        List<OutboxEvent> pendingEvents = new ArrayList<>();
        log.info("Pending Events({})...started!!",pendingEvents.size());

        for (OutboxEvent event : pendingEvents) {
            try {
                // RabbitMQ로 전송
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, event.getPayload());

                event.setStatus("COMPLETED");
                outboxRepository.save(event);

            } catch (AmqpException e) {
                // 전송 실패 시 로그 기록 후 다음 재시도를 위해 상태를 PENDING/RETRY로 유지
                log.error("RabbitMQ 전송 실패: {}", event.getId(), e);
            }
        }
    }
}
