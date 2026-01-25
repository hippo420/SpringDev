package app.springdev.scheduler.ctl;

import app.springdev.scheduler.entity.BatchHistory;
import app.springdev.scheduler.entity.BatchJob;
import app.springdev.scheduler.svc.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/schedule")
@RequiredArgsConstructor
public class ScheduleController {

    private final ScheduleService service;

    @GetMapping("/list")
    public List<BatchJob> batchList() {
        return service.batchList();
    }

    @PostMapping("/save")
    public void batchRegister(@RequestBody BatchJob job) {
        service.save(job);
    }

    @GetMapping("/result")
    public List<BatchHistory> batchResult() {
        return service.batchHistories();
    }

    @PostMapping("/update")
    public void update(@RequestBody BatchJob job) {
        service.save(job);
    }
}
