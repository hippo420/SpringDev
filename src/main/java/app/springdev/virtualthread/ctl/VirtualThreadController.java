package app.springdev.virtualthread.ctl;

import app.springdev.virtualthread.svc.SimpleThreadService;
import app.springdev.virtualthread.svc.VirtualThreadService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/thread")
public class VirtualThreadController {

    private final VirtualThreadService threadService;

    @GetMapping("/virtual")
    public void execute()
    {
        threadService.execute();
    }
}
