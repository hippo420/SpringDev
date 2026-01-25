package app.springdev.scheduler;

import app.springdev.scheduler.entity.BatchHistory;
import app.springdev.scheduler.repository.BatchHistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BatchWatchdog {

    private final BatchHistoryRepository historyRepository;
    private final AlertService alertService;

    @Scheduled(fixedDelay = 300000) // 5분마다
    public void detectStuckJobs() {


        LocalDateTime threshold = LocalDateTime.now().minusMinutes(30);

        List<BatchHistory> stuckJobs =
                historyRepository.findStuckJobs(threshold);

        for (BatchHistory h : stuckJobs) {
            alertService.notifyStuckJob(h);
        }

    }

}
