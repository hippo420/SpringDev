package app.springdev.jpa.svc;

import app.springdev.jpa.entity.Member;
import app.springdev.jpa.entity.Team;
import app.springdev.jpa.repository.MemberRepository;
import app.springdev.jpa.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class InitService {
    private final TeamRepository teamRepository;
    private final MemberRepository memberRepository;


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
                // 연관관계 주인 설정
                member.setTeam(team);
                // 역방향도 동기화 (중요!)
                team.getMembers().add(member);
                memberRepository.save(member);
            }
        }
    }
}
