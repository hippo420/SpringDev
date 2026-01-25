package app.springdev.scheduler.entity;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashMap;
import java.util.Map;

@Entity
@Table(
        name = "batch_job",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_batch_job_name", columnNames = "job_name")
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class BatchJob {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * 배치명 (Unique)
     */
    @Column(name = "job_name", nullable = false, length = 100)
    private String jobName;

    /**
     * 설명
     */
    @Column(name = "description", length = 500)
    private String description;

    /**
     * 실행 주기 (Cron 표현식)
     * 예: 0 0 12 * * *
     */
    @Column(name = "cron_expression", nullable = false, length = 100)
    private String cronExpression;

    /**
     * 기본 파라미터 (JSON)
     */
    @Column(name = "parameters", columnDefinition = "TEXT")
    private String parameters;

    /**
     * 휴일 실행 여부 (Y/N)
     */
    @Column(name = "run_on_holiday", nullable = false, length = 1)
    private String runOnHoliday;

    /**
     * 사용 여부 (Y/N)
     */
    @Column(name = "is_active", nullable = false, length = 1)
    private String isActive;

    /**
     * 실제 실행할 Spring Bean 이름
     */
    @Column(name = "bean_name", nullable = false, length = 200)
    private String beanName;

    @Column(name = "sla_minutes")
    private Integer slaMinutes; // null이면 SLA 미적용

    // ===============================
    // JSON ↔ Map 변환 헬퍼
    // ===============================
    @Transient
    private static final ObjectMapper objectMapper = new ObjectMapper();

    @Transient
    public Map<String, Object> getParametersMap() {
        if (parameters == null || parameters.isBlank()) {
            return new HashMap<>();
        }
        try {
            return objectMapper.readValue(
                    parameters,
                    new TypeReference<Map<String, Object>>() {}
            );
        } catch (Exception e) {
            throw new IllegalStateException("Invalid parameters JSON", e);
        }
    }

    @Transient
    public void setParametersMap(Map<String, Object> map) {
        try {
            this.parameters = objectMapper.writeValueAsString(map);
        } catch (Exception e) {
            throw new IllegalStateException("Failed to serialize parameters", e);
        }
    }

    // boolean처럼 쓰기 좋게
    @Transient
    public boolean isActive() {
        return "Y".equalsIgnoreCase(isActive);
    }

    @Transient
    public boolean isRunOnHoliday() {
        return "Y".equalsIgnoreCase(runOnHoliday);
    }
}
