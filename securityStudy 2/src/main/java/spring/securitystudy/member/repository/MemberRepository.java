package spring.securitystudy.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.member.entity.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Optional<Member> findByUsername(String username);

    @Query("SELECT m FROM Member m WHERE m.username LIKE %:username%")
    Optional<List<Member>> findByUsernamePrefix(@Param("username") String username);
}
