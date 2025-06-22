package app.springdev.jpa.ctl;

import app.springdev.jpa.repository.TeamRepository;
import app.springdev.jpa.svc.TeamService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequiredArgsConstructor
@RequestMapping("jpa")
public class TeamController {
    private final TeamService teamService;

    @RequestMapping("showNPlusOne")
    public void showNPlusOne() {
        teamService.initData();
        // N+1 확인
        log.info("▶▶ N+1 문제 발생 예시");
        teamService.showNPlusOne();
    }

    @RequestMapping("showWithFetchJoin")
    public void showWithFetchJoin() {
        teamService.initData();
        // Fetch Join
        log.info("▶▶ Fetch Join");
        teamService.showWithFetchJoin();
    }

    @RequestMapping("showWithEntityGraph")
    public void showWithEntityGraph() {
        teamService.initData();
        // Entity Graph
        log.info("▶▶ Entity Graph");
        teamService.showWithEntityGraph();
    }

    @RequestMapping("showWithDTO")
    public void showWithDTO() {
        teamService.initData();
        // DTO 조회
        log.info("▶▶ DTO 조회");
        teamService.showWithDTO();
    }
}
