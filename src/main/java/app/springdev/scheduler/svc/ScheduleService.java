package app.springdev.scheduler.svc;

import app.springdev.scheduler.entity.BatchHistory;
import app.springdev.scheduler.entity.BatchJob;
import app.springdev.scheduler.repository.BatchHistoryRepository;
import app.springdev.scheduler.repository.BatchJobRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class ScheduleService {
    private final BatchJobRepository repository;
    private final BatchHistoryRepository repository1;

    public List<BatchJob> batchList() {
        return repository.findAll();
    }


    public void save(BatchJob job) {
        repository.save(job);
    }

    public List<BatchHistory> batchHistories() {
        return repository1.findFirst50ByOrderByIdDesc();
    }

    public List<BatchHistory> batchHistoryByJob(String jobname) {
        return repository1.findByBatchJob(jobname);
    }
}
