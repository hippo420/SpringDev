package app.springdev.jpa.svc;

import app.springdev.jpa.entity.Member;
import app.springdev.jpa.entity.Team;
import app.springdev.jpa.repository.MemberRepository;
import app.springdev.jpa.repository.TeamRepository;
import app.springdev.jpa.vo.TeamMemberVo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class TeamService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;

    public void showNPlusOne() {
        List<Team> teams = teamRepository.findAll();
        for (Team team : teams) {
            log.info("Team: {}",team.getName());
            for (Member m : team.getMembers()) {
                log.info("=>   Member: {}",m.getName());
            }
        }
    }

    public void showWithFetchJoin() {

        List<Team> teams = teamRepository.findAllWithFetchJoin();
        for (Team team : teams) {
            log.info("Team: {}",team.getName());
            for (Member m : team.getMembers()) {
                log.info("=>   Member: {}",m.getName());
            }
        }
    }

    public void showWithEntityGraph() {
        List<Team> teams = teamRepository.findAllWithEntityGraph();
        for (Team team : teams) {
            log.info("Team: {}",  team.getName());
            for (Member m : team.getMembers()) {
                log.info("=>   Member: {}", m.getName());
            }
        }
    }

    public void showWithDTO() {
        List<TeamMemberVo> dtos = teamRepository.findTeamMemberVoList();
        for (TeamMemberVo dto : dtos) {
            log.info("TeamMemberVo => {}",dto);
        }
    }

    @Transactional
    public void initData(){
        teamRepository.deleteAll();
        memberRepository.deleteAll();
        for (int i = 1; i <= 5; i++) {
            Team team = new Team();
            team.setName("Team " + i);
            teamRepository.save(team);

            for (int j = 1; j <= 3; j++) {
                Member member = new Member();
                member.setName("Member " + i + "-" + j);
                member.setTeam(team);

                memberRepository.save(member);
            }
        }
    }
}
