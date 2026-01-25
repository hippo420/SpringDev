package app.springdev.scheduler.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "batch_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BatchHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 배치 작업 FK
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "job_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_batch_history_job")
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private BatchJob batchJob;

    /**
     * 시작 시간
     */
    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    /**
     * 종료 시간
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;

    /**
     * 상태 (STARTED, SUCCESS, FAILED)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private BatchStatus status;

    /**
     * 성공/실패 메시지
     */
    @Column(name = "result_message", columnDefinition = "TEXT")
    private String resultMessage;

    /**
     * 실제 실행 시 사용된 파라미터 (JSON)
     */
    @Column(name = "used_parameters", columnDefinition = "TEXT")
    private String usedParameters;

    // ===============================
    // JSON ↔ Map 변환 헬퍼 (추가)
    // ===============================
    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Transient
    public Map<String, Object> getUsedParametersMap() {
        if (usedParameters == null || usedParameters.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(
                    usedParameters,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            throw new IllegalStateException("Invalid used_parameters JSON", e);
        }
    }

    @Transient
    public void setUsedParametersMap(Map<String, Object> map) {
        try {
            this.usedParameters = objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize used_parameters", e);
        }
    }

    //BatchHistory에 실행 시간 계산 헬퍼
    @Transient
    public long getDurationSeconds() {
        if (startTime == null || endTime == null) return 0;
        return Duration.between(startTime, endTime).getSeconds();
    }
}
