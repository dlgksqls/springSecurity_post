package spring.securitystudy.verificationToken.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.securitystudy.verificationToken.entity.VerificationToken;

import java.util.Optional;

public interface VerificationTokenRepository extends JpaRepository<VerificationToken, Long> {
    Optional<VerificationToken> findByToken(String token);
}
