package app.springdev.virtualthread.ctl;

import app.springdev.virtualthread.svc.SimpleThreadService;
import app.springdev.virtualthread.svc.VirtualThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Controller
@RequiredArgsConstructor
@RequestMapping("/thread")
public class SimpleThreadController {

    private final SimpleThreadService threadService;

    @GetMapping("/simple")
    public String execute()
    {
        return threadService.execute();
    }
}
