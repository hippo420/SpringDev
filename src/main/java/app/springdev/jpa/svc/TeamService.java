package app.springdev.jpa.svc;

import app.springdev.jpa.entity.Member;
import app.springdev.jpa.entity.Team;
import app.springdev.jpa.repository.MemberRepository;
import app.springdev.jpa.repository.TeamRepository;
import app.springdev.jpa.vo.TeamMemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    @Transactional(readOnly = true, propagation = Propagation.REQUIRES_NEW)
    public void showNPlusOne() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            log.info("Team: {} ",team.getName());
            for (Member m : team.getMembers()) {
                log.info("=>   Member: {}",m.getName());
            }
        }
    }

    @Transactional(readOnly = true)
    public void showWithFetchJoin() {

        List<Team> teams = teamRepository.findAllWithFetchJoin();
        for (Team team : teams) {
            log.info("Team: {}",team.getName());
            for (Member m : team.getMembers()) {
                log.info("=>   Member: {}",m.getName());
            }
        }
    }

    @Transactional(readOnly = true)
    public void showWithEntityGraph() {
        List<Team> teams = teamRepository.findAllWithEntityGraph();
        for (Team team : teams) {
            log.info("Team: {}",  team.getName());
            for (Member m : team.getMembers()) {
                log.info("=>   Member: {}", m.getName());
            }
        }
    }

    @Transactional(readOnly = true)
    public void showWithDTO() {
        List<TeamMemberVo> dtos = teamRepository.findTeamMemberVoList();
        for (TeamMemberVo dto : dtos) {
            log.info("TeamMemberVo => {}",dto);
        }
    }


}
