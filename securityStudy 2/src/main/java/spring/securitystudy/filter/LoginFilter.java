package spring.securitystudy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.util.JWTUtil;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;

    public LoginFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
        super();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password);

        return authenticationManager.authenticate(authToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request,
                                            HttpServletResponse response,
                                            FilterChain chain, Authentication authentication)
            throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String username = userDetails.getUsername();

        Collection<? extends GrantedAuthority> auth = authentication.getAuthorities();
        Iterator<? extends GrantedAuthority> iterator = auth.iterator();
        GrantedAuthority authority = iterator.next();

        String role = authority.getAuthority();

        // 1. Access Token 생성
        String token = jwtUtil.createAccessToken(username, role); // 15분

        // 2. Refresh Token 상셩
        String refreshToken = jwtUtil.createRefreshToken(username); // 2주

        // Access Token 쿠키 설정
        Cookie accessTokenCookie = new Cookie("Authentication", token);
        accessTokenCookie.setHttpOnly(true);
        accessTokenCookie.setSecure(false); // HTTPS 사용 시 true로 설정하기
        accessTokenCookie.setPath("/");
        accessTokenCookie.setMaxAge(jwtUtil.getAccessTokenExpiration() + 60);

        // Refresh Token 쿠키 설정 (새롭게 추가)
        Cookie refreshTokenCookie = new Cookie("RefreshToken", refreshToken);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setSecure(false);
        refreshTokenCookie.setPath("/");
        refreshTokenCookie.setMaxAge(jwtUtil.getRefreshTokenExpiration());

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 리다이렉트
        response.sendRedirect("/");
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

        // 로그인 창으로 리다이렉트
        String redirectUrl = request.getContextPath() + "/user/login?error";

        response.sendRedirect(redirectUrl);
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return "POST".equalsIgnoreCase(request.getMethod()) &&
                "/user/login".equals(request.getServletPath());
    }
}
