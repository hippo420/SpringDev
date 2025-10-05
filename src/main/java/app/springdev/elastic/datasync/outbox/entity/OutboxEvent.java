package app.springdev.elastic.datasync.outbox.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OutboxEvent {
    @Id
    @GeneratedValue
    private Long id;

    // Debezium에서 Kafka 토픽 이름으로 사용할 필드 (예: 'product')
    private String aggregateType;

    // Kafka Key로 사용할 필드 (예: RDB PK)
    private Long aggregateId;

    private String type;                 // 이벤트 유형 (예: "PRODUCT_CREATED", "PRODUCT_UPDATED")

    @Column(columnDefinition = "json") // PostgreSQL JSONB 타입 또는 MySQL JSON 타입
    private String payload;             // 실제 Elasticsearch에 전달할 데이터 (JSON 문자열)

    private LocalDateTime createdAt = LocalDateTime.now();

    private String status = "N";

    @Override
    public String toString() {
        return "OutboxEvent{" +
                "id=" + id +
                ", aggregateType='" + aggregateType + '\'' +
                ", aggregateId=" + aggregateId +
                ", type='" + type + '\'' +
                ", payload='" + payload + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
