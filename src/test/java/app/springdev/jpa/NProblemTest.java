package app.springdev.jpa;

import app.springdev.jpa.svc.InitService;
import app.springdev.jpa.svc.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest
public class NProblemTest {
    @Autowired
    private InitService initService;

    @Autowired
    private TeamService teamService;

    @Test
    @DirtiesContext
    @DisplayName("N+1 테스트")
    public void testNPlusOne() {
        initService.initData();        // 트랜잭션 1: 초기화

        teamService.showNPlusOne(); // 트랜잭션 2: 조회
    }
}
