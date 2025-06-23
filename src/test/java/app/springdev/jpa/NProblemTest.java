package app.springdev.jpa;

import app.springdev.jpa.svc.InitService;
import app.springdev.jpa.svc.TeamService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.TestPropertySource;

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
        //initService.initData();        // 트랜잭션 1: 초기화

        teamService.showNPlusOne(); // 트랜잭션 2: 조회
    }

    @Test
    @DisplayName("N+1 FetchJoin 테스트")
    public void showWithFetchJoin() {
        teamService.showWithFetchJoin(); // 트랜잭션 2: 조회
    }

    @Test
    @DisplayName("N+1 batchSize 테스트")
    public void batchSize() {
        teamService.batchSize(); // 트랜잭션 2: 조회
    }

    @Test
    @DisplayName("N+1 EntityGraph 테스트")
    public void showWithEntityGraph() {
        teamService.showWithEntityGraph(); // 트랜잭션 2: 조회
    }

    @Test
    @DisplayName("N+1 showWithDTO 테스트")
    public void showWithDTO() {
        teamService.showWithDTO(); // 트랜잭션 2: 조회
    }

    @Test
    @DisplayName("N+1 fetchPaging 테스트")
    public void fetchPaging() {
        teamService.fetchPaging(); // 트랜잭션 2: 조회
    }

    //Team.members에 BatchSize적용
    @Test
    @DisplayName("N+1 pagingWithBatchSize 테스트")
    public void pagingWithBatchSize() {
        teamService.pagingWithBatchSize(); // 트랜잭션 2: 조회
    }

}
