package app.springdev.scheduler.ctl;

import app.springdev.scheduler.entity.BatchJob;
import app.springdev.scheduler.svc.ScheduleService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/schedule")
public class ViewController {


    @GetMapping("/list")
    public String batchList() {
        return "/schedule/list";
    }

    @GetMapping("/register")
    public String batchRegister() {
        return "/schedule/register";
    }

    @GetMapping("/results")
    public String batchResult() {
        return "/schedule/result";
    }
}
