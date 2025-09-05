package spring.securitystudy.verificationToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.verificationToken.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {

    @Query("SELECT t FROM VerificationToken t WHERE t.token = :token")
    Optional<VerificationToken> findByToken(@Param("token") String token);

    Optional<VerificationToken> findByUser(User user);
}
