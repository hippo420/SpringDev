package app.springdev.scheduler;

import app.springdev.scheduler.entity.BatchHistory;
import app.springdev.scheduler.entity.BatchJob;
import app.springdev.scheduler.entity.BatchStatus;
import app.springdev.scheduler.repository.BatchHistoryRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class BatchHistoryService {

    private final BatchHistoryRepository historyRepository;
    private final AlertService alertService;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public BatchHistory saveStarted(BatchJob job, Map<String, Object> params) {
        BatchHistory history = BatchHistory.builder()
                .batchJob(job)
                .startTime(LocalDateTime.now())
                .status(BatchStatus.STARTED)
                .build();
        history.setUsedParametersMap(params);
        return historyRepository.save(history);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSuccess(BatchHistory history) {
        history.setStatus(BatchStatus.SUCCESS);
        history.setEndTime(LocalDateTime.now());
        historyRepository.save(history);

        // 🔥 여기서 SLA 체크
        if (isSlaBreached(history)) {
            alertService.notifySlaBreach(history);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFailed(BatchHistory history, Exception e) {
        history.setStatus(BatchStatus.FAILED);
        history.setResultMessage(e.getMessage());
        history.setEndTime(LocalDateTime.now());
        historyRepository.save(history);
    }


    public boolean isSlaBreached(BatchHistory h) {
        Integer sla = h.getBatchJob().getSlaMinutes();
        if (sla == null || h.getEndTime() == null) return false;

        long durationMinutes = h.getDurationSeconds() / 60;
        return durationMinutes > sla;
    }

}