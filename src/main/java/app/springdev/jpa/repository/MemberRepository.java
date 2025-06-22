package app.springdev.jpa.repository;

import app.springdev.jpa.entity.Member;
import app.springdev.jpa.entity.Team;
import app.springdev.jpa.vo.TeamMemberVo;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

}
