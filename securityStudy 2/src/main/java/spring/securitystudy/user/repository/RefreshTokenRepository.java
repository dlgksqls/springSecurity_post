package spring.securitystudy.user.repository;

import org.springframework.data.repository.CrudRepository;
import spring.securitystudy.util.jwt.RefreshToken;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {
}
