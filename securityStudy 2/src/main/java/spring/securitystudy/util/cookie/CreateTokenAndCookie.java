package spring.securitystudy.util.cookie;

import jakarta.servlet.http.Cookie;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import spring.securitystudy.util.jwt.JWTUtil;

@RequiredArgsConstructor
@Component
public class CreateTokenAndCookie {

    private final JWTUtil jwtUtil;

    public String createToken(String username, String role, String type){
        String token = null;

        if (type.equals("access")) token = jwtUtil.createAccessToken(username, role);
        else if (type.equals("refresh")) token = jwtUtil.createRefreshToken(username);

        return token;
    }

    public Cookie createCookie(String name, String token) {
        Cookie tokenCookie = new Cookie(name, token);
        tokenCookie.setHttpOnly(true);
        tokenCookie.setSecure(false); // HTTPS 사용 시 true로 설정하기
        tokenCookie.setPath("/");

        if (name.equals("Authentication")) tokenCookie.setMaxAge(jwtUtil.getAccessTokenExpiration() + 60);
        else if (name.equals("RefreshToken")) tokenCookie.setMaxAge(jwtUtil.getRefreshTokenExpiration() + 60);
        return tokenCookie;
    }
}
