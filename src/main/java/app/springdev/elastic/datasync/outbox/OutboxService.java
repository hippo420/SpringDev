package app.springdev.elastic.datasync.outbox;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import app.springdev.elastic.datasync.outbox.hook.NotiCreatedEvent;
import app.springdev.elastic.datasync.simple.Noti;
import app.springdev.elastic.datasync.simple.NotiRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@AllArgsConstructor
public class OutboxService {
    private final OutBoxRepository outBoxRepository;
    private final NotiRepository notiRepository;
    private final NotiElasticRepository notiElasticRepository;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher eventPublisher;

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

}
