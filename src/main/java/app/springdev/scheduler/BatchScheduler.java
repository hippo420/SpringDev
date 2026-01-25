package app.springdev.scheduler;

import app.springdev.scheduler.entity.BatchHistory;
import app.springdev.scheduler.entity.BatchJob;
import app.springdev.scheduler.entity.BatchStatus;
import app.springdev.scheduler.log.MdcTaskDecorator;
import app.springdev.scheduler.repository.BatchHistoryRepository;
import app.springdev.scheduler.repository.BatchJobRepository;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ScheduledFuture;
@Slf4j
@Configuration
public class BatchScheduler {

    private final ThreadPoolTaskScheduler taskScheduler;
    private final Map<Long, ScheduledFuture<?>> scheduledTasks = new ConcurrentHashMap<>();
    private final ApplicationContext applicationContext;
    private final BatchJobRepository jobRepository;     // JPA Repository
    private final BatchHistoryRepository historyRepository; // JPA Repository
    private final BatchHistoryService historyService;

    public BatchScheduler(ApplicationContext applicationContext,
                                 BatchJobRepository jobRepository,
                                 BatchHistoryRepository historyRepository,
                          BatchHistoryService historyService) {
        this.applicationContext = applicationContext;
        this.jobRepository = jobRepository;
        this.historyRepository = historyRepository;
        this.historyService = historyService;

        // 스케줄러 설정
        this.taskScheduler = new ThreadPoolTaskScheduler();
        this.taskScheduler.setPoolSize(5);
        this.taskScheduler.initialize();
    }

    // 1. 서버 시작 시 DB에 등록된 활성 배치들 스케줄링 등록
    @PostConstruct
    public void init() {
        log.info("배치 스케줄러 초기화 -[{}]", this.getClass().getSimpleName() );
        List<BatchJob> activeJobs = jobRepository.findByIsActiveTrue();

        for (BatchJob job : activeJobs) {
            log.info("배치 :{}" ,job);
            startJob(job);
        }
    }

    // 2. 배치 스케줄 등록 (등록/수정 시 호출)
    public void startJob(BatchJob job) {

        stopJob(job.getId());
        if (BatchConst.NO_USE.equals(job.getIsActive())) return;

        Runnable task = wrapWithMdc(
                job,
                () -> executeJobLogic(job, job.getParametersMap())
        );

        ScheduledFuture<?> future =
                taskScheduler.schedule(task, new CronTrigger(job.getCronExpression()));

        scheduledTasks.put(job.getId(), future);
    }

    // 3. 배치 스케줄 중지 (삭제/미사용 처리 시 호출)
    public void stopJob(Long jobId) {
        ScheduledFuture<?> future = scheduledTasks.get(jobId);
        if (future != null) {
            future.cancel(false);
            scheduledTasks.remove(jobId);
        }
    }

    // 4. 즉시 실행 / 재실행 (결과 화면에서 실패 건 재실행 시 사용)
    public void runManually(Long jobId, Map<String, Object> manualParams) {

        BatchJob job = jobRepository.findById(jobId).orElseThrow();

        taskScheduler.submit(
                wrapWithMdc(
                        job,
                        () -> executeJobLogic(job, manualParams)
                )
        );
    }

    // === 실제 실행 로직 (공통) ===
    private void executeJobLogic(BatchJob job, Map<String, Object> params) {

        long startTime = System.currentTimeMillis();

        try {
            boolean isRunning = historyRepository
                    .existsByBatchJobAndStatus(job, BatchStatus.STARTED);

            if (isRunning) {
                log.warn("BatchJob already running");
                return;
            }

            if (BatchConst.NO_USE.equals(job.getRunOnHoliday()) && isTodayHoliday()) {
                log.info("Batch skipped due to holiday");
                return;
            }

            BatchHistory history = historyService.saveStarted(job, params);

            try {
                CustomBatchJob batchBean =
                        (CustomBatchJob) applicationContext.getBean(job.getBeanName());

                log.info("Batch execution started");
                batchBean.execute(params);

                historyService.markSuccess(history);
                log.info("Batch execution success");

            } catch (Exception e) {
                historyService.markFailed(history, e);
                log.error("Batch execution failed", e);
            }

        } finally {
            long elapsed = System.currentTimeMillis() - startTime;
            log.info("Batch execution finished ({} ms)", elapsed);
        }
    }

    // 휴일 체크 더미 메소드
    private boolean isTodayHoliday() {
        // DB의 공휴일 테이블 조회 or 외부 API 연동
        return false;
    }

    private Runnable wrapWithMdc(BatchJob job, Runnable task) {
        return () -> {
            MDC.put("batchName", job.getBeanName());
            MDC.put("jobId", String.valueOf(job.getId()));

            try {
                task.run();
            } finally {
                MDC.clear();
            }
        };
    }
}
