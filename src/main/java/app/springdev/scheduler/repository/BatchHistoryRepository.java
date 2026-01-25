package app.springdev.scheduler.repository;

import app.springdev.scheduler.entity.BatchHistory;
import app.springdev.scheduler.entity.BatchJob;
import app.springdev.scheduler.entity.BatchStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BatchHistoryRepository extends JpaRepository<BatchHistory,Long> {

    /*
     @Desc : 중복 실행 조회
     */
    boolean existsByBatchJobAndStatus(BatchJob batchJob, BatchStatus status);

    @Query("""
    select h from BatchHistory h
    where h.status = 'STARTED'
      and h.startTime <= :threshold
      and h.endTime is null
    """)
    List<BatchHistory> findStuckJobs(@Param("threshold") LocalDateTime threshold);


    @Query("""
    select avg(
        function('timestampdiff', SECOND, h.startTime, h.endTime)
    )
    from BatchHistory h
    where h.batchJob = :job
      and h.status = 'SUCCESS'
    """)
    Double getAverageDurationSeconds(@Param("job") BatchJob job);


    List<BatchHistory> findFirst50ByOrderByIdDesc();

    @Query("""
    select h
    from BatchHistory h
    where h.batchJob.jobName = :jobname
    order by id desc limit 50
    """)
    List<BatchHistory> findByBatchJob(String jobname);
}
