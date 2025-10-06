package app.springdev.elastic.datasync.outbox;

import app.springdev.elastic.datasync.outbox.entity.OutboxEvent;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;

import java.util.List;

public interface OutBoxRepository extends JpaRepository<OutboxEvent,Long> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    List<OutboxEvent> findByAggregateTypeAndStatus(String aggregateType, String status);
}
