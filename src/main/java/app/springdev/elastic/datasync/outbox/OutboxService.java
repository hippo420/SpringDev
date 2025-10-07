package app.springdev.elastic.datasync.outbox;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.outbox.annotation.EventPublish;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import app.springdev.elastic.datasync.outbox.hook.NotiCreatedEvent;
import app.springdev.elastic.datasync.Noti;
import app.springdev.elastic.datasync.NotiRepository;
import app.springdev.elastic.datasync.outbox.mapper.NoticeMapper;
import app.springdev.elastic.datasync.outbox.rabbit.config.RabbitMQConfig;
import app.springdev.elastic.datasync.outbox.rabbit.message.SyncDataConsumer;
import app.springdev.elastic.datasync.outbox.rabbit.message.SyncDataPublisher;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class OutboxService {
    private final OutBoxRepository outBoxRepository;
    private final NotiRepository notiRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final SyncDataPublisher syncDataPublisher;
    private final NoticeMapper noticeMapper;

    @Transactional
    public void createNoti (Noti noti) throws JsonProcessingException {

        notiRepository.save(noti);
        String payload = objectMapper.writeValueAsString(noti);
        OutboxEvent outboxEvent = new OutboxEvent();
        outboxEvent.setAggregateType("notice");
        outboxEvent.setAggregateId(noti.getId());
        outboxEvent.setType("NOTICE_CREATED");
        outboxEvent.setPayload(payload);
        outBoxRepository.save(outboxEvent);
    }

    @Transactional
    public Noti  createNotiByHook (Noti noti) throws JsonProcessingException {

        Noti saved = notiRepository.save(noti);
        eventPublisher.publishEvent(new NotiCreatedEvent(saved));
        return saved;
    }

    //RabbitMQ
    @Transactional
    public void createNotiRabbitMq(Noti noti) throws JsonProcessingException{
        Noti saved = notiRepository.save(noti);

        NoticeDocument notiDoc = noticeMapper.toNoticeDocument(noti);

        String data = objectMapper.writeValueAsString(notiDoc);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                log.info("createNotiRabbitMq - afterCommit => {}", notiDoc);
                syncDataPublisher.sendMessage(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, data);
            }
        });
    }



    //RabbitMQ
    @Transactional
    public void createNotiRelay(Noti noti) throws JsonProcessingException{
        Noti saved = notiRepository.save(noti);

        NoticeDocument notiDoc = noticeMapper.toNoticeDocument(saved);
        String payload = objectMapper.writeValueAsString(notiDoc);
        OutboxEvent outboxEvent = OutboxEvent.builder()
                .aggregateType("notice")
                .aggregateId(saved.getId())
                .payload(payload)
                .createdAt(LocalDateTime.now())
                .status("PENDING")
                .build();
        outBoxRepository.save(outboxEvent);

    }

    @Transactional
    @EventPublish(aggregateType = "notice",useReturnValue = true)
    public NoticeDocument createNotiRelayAOP(Noti noti, String param1) throws JsonProcessingException{
        Noti saved = notiRepository.save(noti);
        return noticeMapper.toNoticeDocument(saved);


    }
}
