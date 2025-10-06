package app.springdev.elastic.datasync.outbox.processor;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.outbox.OutBoxRepository;
import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import app.springdev.elastic.datasync.Noti;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
@Slf4j
@Service
@RequiredArgsConstructor
@ConditionalOnProperty(
        value = "scheduler.outbox-processor.enabled", // 확인할 설정 속성 이름
        havingValue = "true",                      // 해당 속성 값이 "true"일 때만 활성화
        matchIfMissing = false                      // 속성 자체가 없으면 기본적으로 활성화 (선택 사항)
)
public class OutboxProcessor {
    private final OutBoxRepository outboxRepository;
    private final NotiElasticRepository notiElasticRepository; // ES Repository
    private final ObjectMapper objectMapper;


    @Scheduled(fixedDelay = 2000) // 5초마다 실행
    public void processOutboxEvents() {
        log.info("Processing outbox events Start!!!!");

        List<OutboxEvent> events = outboxRepository.findByAggregateTypeAndStatus("notice","PENDING");
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
