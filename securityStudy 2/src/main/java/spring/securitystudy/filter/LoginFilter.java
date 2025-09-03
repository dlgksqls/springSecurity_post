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
import spring.securitystudy.exception.UserNotFoundException;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.user.entity.User;
import spring.securitystudy.user.exception.EmailNotVerifiedException;
import spring.securitystudy.user.repository.RefreshTokenRepository;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.util.jwt.JWTUtil;
import spring.securitystudy.util.jwt.RefreshToken;

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;

public class LoginFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;
    private final JWTUtil jwtUtil;
    private final RefreshTokenRepository refreshTokenRepository;
    private final UserRepository userRepository;

    public LoginFilter(AuthenticationManager authenticationManager,
                       JWTUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository,
                       UserRepository userRepository) {
        super();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 계정입니다."));

        if (!user.isEnable()){
            throw new EmailNotVerifiedException("이메일 인증이 완료되지 않았습니다.");
        }

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

        // 2. Refresh Token 생셩
        String refreshToken = jwtUtil.createRefreshToken(username); // 2주
        RefreshToken redis = new RefreshToken(username, refreshToken);
        refreshTokenRepository.save(redis);

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
        refreshTokenCookie.setMaxAge(jwtUtil.getRefreshTokenExpiration() * 60);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 리다이렉트
        response.sendRedirect("/");
    }

    // filter는 스프링 mvc 앞에서 거르는 용도이므로 advice에서 예외 처리 불가
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

        // 이메일 인증 안 된 경우
        if (failed instanceof EmailNotVerifiedException){
            response.sendRedirect(request.getContextPath() + "/user/check-email?error=notVerified");
            return;
        }

        // 존재하지 않는 계정
        if (failed instanceof UserNotFoundException){
            response.sendRedirect(request.getContextPath() + "/user/login?error=true&message=Check Your Id Or Password");
            return;
        }

        response.sendRedirect(request.getContextPath() + "/user/login?error=true&message=Check Your Id Or Password");
    }

    @Override
    protected boolean requiresAuthentication(HttpServletRequest request, HttpServletResponse response) {
        return "POST".equalsIgnoreCase(request.getMethod()) &&
                "/user/login".equals(request.getServletPath());
    }
}
