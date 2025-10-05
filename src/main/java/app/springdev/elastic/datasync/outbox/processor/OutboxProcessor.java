package app.springdev.elastic.datasync.outbox.processor;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.outbox.OutBoxRepository;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import app.springdev.elastic.datasync.simple.Noti;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxProcessor {
    private final OutBoxRepository outboxRepository;
    private final NotiElasticRepository notiElasticRepository; // ES Repository
    private final ObjectMapper objectMapper;


    @Scheduled(fixedDelay = 2000) // 5초마다 실행
    public void processOutboxEvents() {
        log.info("Processing outbox events Start!!!!");
        OutboxEvent cond = new OutboxEvent();
        cond.setAggregateType("notice");
        cond.setStatus("N");

        List<OutboxEvent> events = outboxRepository.findByAggregateTypeAndStatus("notice","N");
        for (OutboxEvent e : events) {
            try {
                Noti noti = objectMapper.readValue(e.getPayload(), Noti.class);

                NoticeDocument doc = new NoticeDocument();
                doc.setId(noti.getId());
                doc.setTitle(noti.getTitle());
                doc.setContent(noti.getContent());
                doc.setWriter(noti.getWriter());
                doc.setCategory(noti.getCategory());
                doc.setViews(noti.getViews());
                doc.setCreatedAt(noti.getCreatedAt());

                // 2. Elasticsearch에 저장
                notiElasticRepository.save(doc);

                // 3. 상태 변경
                e.setStatus("Y");
                outboxRepository.save(e);
            } catch (Exception ex) {
                log.error("Failed to process outbox event: {}", e.getId(), ex);
            }
        }
    }
}
