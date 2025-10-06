package app.springdev.elastic.datasync.outbox.hook;

import app.springdev.elastic.NotiElasticRepository;
import app.springdev.elastic.NoticeDocument;
import app.springdev.elastic.datasync.Noti;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
@Slf4j
@Component
@RequiredArgsConstructor
public class EventHookHandler {
    private final NotiElasticRepository notiElasticRepository;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handleNotiCreated(NotiCreatedEvent event) {
        log.info("Async처리.... EventHookHandler - event: {}", event);
        Noti noti = event.getNoti();

        NoticeDocument doc = new NoticeDocument();
        doc.setId(noti.getId());
        doc.setTitle(noti.getTitle());
        doc.setContent(noti.getContent());
        doc.setWriter(noti.getWriter());
        doc.setCategory(noti.getCategory());
        doc.setViews(noti.getViews());
        doc.setCreatedAt(noti.getCreatedAt());

        notiElasticRepository.save(doc);
        log.info("Elasticsearch에 저장 완료 - ID: {}", doc.getId());
    }
}
