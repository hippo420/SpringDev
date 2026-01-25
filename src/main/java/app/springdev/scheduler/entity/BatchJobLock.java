package app.springdev.scheduler.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
@Table(name = "batch_lock")
public class BatchJobLock {

    @Id
    @Column(name = "job_id")
    private Long jobId;

    @Column(name = "locked", nullable = false)
    private Boolean locked;

    @Column(name = "locked_at")
    private LocalDateTime lockedAt;

    @Column(name = "locked_by", length = 100)
    private String lockedBy;

    protected BatchJobLock() {
        // JPA 기본 생성자
    }

    public BatchJobLock(Long jobId, Boolean locked) {
        this.jobId = jobId;
        this.locked = locked;
    }


    public boolean isLocked() {
        if(this.locked.equals(true))
            return true;
        else
            return false;
    }
}
