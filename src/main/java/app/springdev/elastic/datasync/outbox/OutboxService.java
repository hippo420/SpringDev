package app.springdev.elastic.datasync.outbox;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import app.springdev.elastic.datasync.outbox.hook.NotiCreatedEvent;
import app.springdev.elastic.datasync.Noti;
import app.springdev.elastic.datasync.NotiRepository;
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

@Slf4j
@Service
@AllArgsConstructor
public class OutboxService {
    private final OutBoxRepository outBoxRepository;
    private final NotiRepository notiRepository;
    private final NotiElasticRepository notiElasticRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;
    private final SyncDataPublisher syncDataPublisher;
    private final SyncDataConsumer syncDataConsumer;

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

        NoticeDocument notiDoc = new NoticeDocument();
        notiDoc.setId(saved.getId());
        notiDoc.setTitle(saved.getTitle());
        notiDoc.setContent(saved.getContent());
        notiDoc.setWriter(saved.getWriter());
        notiDoc.setCreatedAt(saved.getCreatedAt());
        notiDoc.setCategory(saved.getCategory());
        notiDoc.setViews(saved.getViews());

        String data = objectMapper.writeValueAsString(notiDoc);

        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                log.info("createNotiRabbitMq - afterCommit => {}", notiDoc);
                syncDataPublisher.sendMessage(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, data);
            }
        });
    }
}
