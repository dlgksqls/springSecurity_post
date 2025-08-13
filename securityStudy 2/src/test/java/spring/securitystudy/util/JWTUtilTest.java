package spring.securitystudy.util;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import spring.securitystudy.exception.user.TokenExpiredException;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
class JWTUtilTest {

    @Autowired
    JWTUtil jwtUtil;

    @Test
    public void access_Token_만료_테스트() throws InterruptedException{
        // given
        String username = "testUser";
        String role = "ROLE_USER";

        String accessToken = jwtUtil.createAccessToken(username, role);
        log.info("{}", "accessToken 생성");
        log.info("{}", accessToken);

        // when
        Thread.sleep(4000); // 4초

        // then
        assertThrows(TokenExpiredException.class, () -> {
            jwtUtil.validateToken(accessToken);
        });

        log.info("{}", "만료 후 검증");
    }

    @Test
    public void refreshToken_으로_새로운_accessToken_이_생성된다() throws InterruptedException{
        // given
        String username = "testUser";
        String role = "ROLE_USER";

        String accessToken = jwtUtil.createAccessToken(username, role);
        String refreshToken = jwtUtil.createRefreshToken(username);
        log.info("{}", "accessToken 생성");
        log.info("{}", accessToken);

        log.info("{}", "refreshToken 생성");
        log.info("{}", refreshToken);

        // when
        Thread.sleep(4000); // 4초
        assertThrows(TokenExpiredException.class, () -> {
            jwtUtil.validateToken(accessToken);
        });
        Thread.sleep(2000);

        // then
        assertThrows(TokenExpiredException.class, () -> {
            jwtUtil.validateToken(refreshToken);
        });

        log.info("{}", "만료 후 검증");
    }
}