package app.springdev.scheduler.repository;

import app.springdev.scheduler.entity.BatchJobLock;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface BatchJobLockRepository
        extends JpaRepository<BatchJobLock, Long> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select l from BatchJobLock l where l.jobId = :jobId")
    Optional<BatchJobLock> findForUpdate(@Param("jobId") Long jobId);
}
