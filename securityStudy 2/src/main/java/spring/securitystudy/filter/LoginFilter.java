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
import spring.securitystudy.user.repository.RefreshTokenRepository;
import spring.securitystudy.user.repository.UserRepository;
import spring.securitystudy.util.cookie.CreateTokenAndCookie;
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
    private final CreateTokenAndCookie createTokenAndCookie;

    public LoginFilter(AuthenticationManager authenticationManager,
                       JWTUtil jwtUtil,
                       RefreshTokenRepository refreshTokenRepository,
                       UserRepository userRepository) {
        super();
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.refreshTokenRepository = refreshTokenRepository;
        this.userRepository = userRepository;
        this.createTokenAndCookie = new CreateTokenAndCookie(jwtUtil);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("존재하지 않는 계정입니다."));

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

        // 이메일 인증을 하지 않았다면
        if (!userDetails.getUser().isEnable()){
            String token = createTokenAndCookie.createToken(username, "UNVERIFIED", "access");
            String refreshToken = createTokenAndCookie.createToken(username, null, "refresh");

            // 쿠키 발급
            Cookie unverifiedToken = createTokenAndCookie.createCookie("Authentication", token);

            response.addCookie(unverifiedToken);

            response.sendRedirect(request.getContextPath() + "/user/check-email?error=notVerified");
            return;
        }

        // 1. Access Token 생성
        String token = createTokenAndCookie.createToken(username, role, "access");

        // 2. Refresh Token 생셩 (2주)
        String refreshToken = createTokenAndCookie.createToken(username, null, "refresh");
        RefreshToken redis = new RefreshToken(username, refreshToken);
        refreshTokenRepository.save(redis);

        // Access Token 쿠키 설정
        Cookie accessTokenCookie = createTokenAndCookie.createCookie("Authentication", token);

        // Refresh Token 쿠키 설정 (새롭게 추가)
        Cookie refreshTokenCookie = createTokenAndCookie.createCookie("RefreshToken", refreshToken);

        response.addCookie(accessTokenCookie);
        response.addCookie(refreshTokenCookie);

        // 로그인 성공 후 이메일 인증을 했다면 index로 redirect
        response.sendRedirect("/");
    }

    // filter는 스프링 mvc 앞에서 거르는 용도이므로 advice에서 예외 처리 불가
    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request,
                                              HttpServletResponse response,
                                              AuthenticationException failed)
            throws IOException, ServletException {

        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401 Unauthorized

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
