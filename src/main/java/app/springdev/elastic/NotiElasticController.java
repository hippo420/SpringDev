package app.springdev.elastic;

import app.springdev.elastic.datasync.outbox.OutboxService;
import app.springdev.elastic.datasync.Noti;
import app.springdev.elastic.datasync.NotiRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Slf4j
@RequestMapping("/elastic")
@RestController
@AllArgsConstructor
public class NotiElasticController {

    private final NotiElasticRepository repository;
    private final NotiRepository postRepository;
    private final OutboxService outboxService;


    @RequestMapping("ins")
    public void save(@RequestBody NoticeDocument noti) {
        log.info("ins -> {}" , noti);
        repository.save(noti);
    }

    @RequestMapping("upd")
    public void update(@RequestBody NoticeDocument noti) {
        repository.save(noti);
    }

    @RequestMapping("lst")
    public String search(@RequestParam Long id) {
        return repository.findById(id).toString();
    }

    @RequestMapping("lstAll")
    public void lstAll() {
        repository.findAll();
    }

    @RequestMapping("del")
    public void delete(@RequestParam Long id) {
        repository.deleteById(id);
    }

    @Transactional
    @RequestMapping("insNotiNonSync")
    public void insNotiNonSync(@RequestBody Noti noti) {
        postRepository.save(noti);
    }

    @Transactional
    @RequestMapping("insNotiSync")
    public void insNotiSync(@RequestBody Noti noti) {
        postRepository.save(noti);
        NoticeDocument notiDoc = new NoticeDocument();
        notiDoc.setId(noti.getId());
        notiDoc.setTitle(noti.getTitle());
        notiDoc.setContent(noti.getContent());
        notiDoc.setWriter(noti.getWriter());
        notiDoc.setCreatedAt(noti.getCreatedAt());
        notiDoc.setCategory(noti.getCategory());
        notiDoc.setViews(noti.getViews());
        repository.save(notiDoc);
    }


    @RequestMapping("insOutBox")
    public void insOutBox(@RequestBody Noti noti) {
        try {
            outboxService.createNoti(noti);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("insOutBoxByHook")
    public void insOutBoxByHook(@RequestBody Noti noti) {
        try {
            outboxService.createNotiByHook(noti);

        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @RequestMapping("insOutBoxByRabbitMQ")
    public void insOutBoxByRabbitMQ(@RequestBody Noti noti) {
        try {
            outboxService.createNotiRabbitMq(noti);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }
}
