package spring.securitystudy.util;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
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
        this.secretKey =
                new SecretKeySpec(secret.getBytes(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().getAlgorithm());
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

    public boolean isExpired(String token){
        try {
            return Jwts
                    .parser()
                    .verifyWith(secretKey).build()
                    .parseSignedClaims(token).getPayload().getExpiration().before(new Date());
        } catch (ExpiredJwtException e){
            // 토큰 만료 시
            return true;
        } catch (Exception e){
            return true;
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

    public String createRefreshToken(String username, String role){
        return Jwts.builder()
                .claim("username", username)
                .claim("role", role)
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
