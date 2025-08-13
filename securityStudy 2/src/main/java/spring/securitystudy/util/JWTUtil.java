package spring.securitystudy.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import spring.securitystudy.exception.user.TokenExpiredException;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@Component
public class JWTUtil {
    private SecretKey secretKey;
    private int accessTokenExpiration;
    private int refreshTokenExpiration;

    public JWTUtil(@Value("${spring.jwt.secret}") String secret,
                   @Value("${jwt.access-token-expiration}") int accessTokenExpiration,
                   @Value("${jwt.refresh-token-expiration}") int refreshTokenExpiration) {
        this.secretKey = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
        this.accessTokenExpiration = accessTokenExpiration;
        this.refreshTokenExpiration = refreshTokenExpiration;
    }

    public String getUsername(String token){
        return Jwts
                .parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("username", String.class);
    }

    public String getRole(String token){
        return Jwts
                .parser()
                .verifyWith(secretKey).build()
                .parseSignedClaims(token).getPayload().get("role", String.class);
    }

    public void validateToken(String token){
        try {
            Date expiration = Jwts
                    .parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload().getExpiration();

            // 토큰 만료 시
            if (expiration.before(new Date())) throw new TokenExpiredException("세션이 만료되었습니다. 다시 로그인하세요.");
        } catch (ExpiredJwtException e){
            throw new TokenExpiredException("세션이 만료되었습니다. 다시 로그인하세요.");
        } catch (Exception e){
            throw new RuntimeException("유효하지 않은 토큰입니다.");
        }
    }

    public String createAccessToken(String username, String role){
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (long)accessTokenExpiration * 1000L))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public String createRefreshToken(String username){
        return Jwts.builder()
                .claim("username", username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + (long)refreshTokenExpiration * 1000L))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public int getAccessTokenExpiration() {
        return accessTokenExpiration;
    }

    public int getRefreshTokenExpiration() {
        return refreshTokenExpiration;
    }
}
