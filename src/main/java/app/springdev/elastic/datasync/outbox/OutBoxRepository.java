package app.springdev.elastic.datasync.outbox;

import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutboxEvent,Long> {
    List<OutboxEvent> findByAggregateTypeAndStatus(String aggregateType, String status);
}
