package app.springdev.rabbit;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RestController
@RequestMapping("/rabbit")
public class RabbitController {

    @RequestMapping("/procude")
    public String procude(String msg) {
        log.info("procude");
        return "procude";
    }
}
