package app.springdev.jpa.repository;

import app.springdev.jpa.entity.Team;
import app.springdev.jpa.vo.TeamMemberVo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface TeamRepository extends JpaRepository<Team, Long> {
    // 1. 기본 N+1 발생
    List<Team> findAll();

    // 2. Fetch Join
    @Query("SELECT t FROM Team t JOIN FETCH t.members")
    List<Team> findAllWithFetchJoin();

    // 3. EntityGraph
    @EntityGraph(attributePaths = "members")
    @Query("SELECT t FROM Team t")
    List<Team> findAllWithEntityGraph();

    // 4. DTO 조회
    @Query("SELECT new app.springdev.jpa.vo.TeamMemberVo(t.name, m.name) " +
            "FROM Team t JOIN t.members m")
    List<TeamMemberVo> findTeamMemberVoList();
}
