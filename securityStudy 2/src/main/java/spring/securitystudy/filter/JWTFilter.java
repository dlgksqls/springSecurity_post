package spring.securitystudy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.securitystudy.user.exception.TokenExpiredException;
import spring.securitystudy.util.JWTUtil;

import java.io.IOException;

@RequiredArgsConstructor
@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();
        return path.startsWith("/user/login") || path.startsWith("/css/") || path.startsWith("/js/") || path.startsWith("/favicon.ico");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String accessToken = null;
        String refreshToken = null;

        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("Authentication".equals(cookie.getName())) {
                    accessToken = cookie.getValue();
                } else if ("RefreshToken".equals(cookie.getName())) {
                    refreshToken = cookie.getValue();
                }
            }
        }

        // AT 없으면 다음 필터로
        if (accessToken == null) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            // 1. AT 검증
            jwtUtil.validateToken(accessToken);
            setAuthentication(accessToken);

        } catch (TokenExpiredException e) {
            log.info("AccessToken 만료");

            // 2. RT 없으면 401
            if (refreshToken == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(request, response);
                return;
            }

            // 3. RT 검증 후 새 AT 발급
            try {
                jwtUtil.validateToken(refreshToken);
                String username = jwtUtil.getUsername(refreshToken);
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                String newAccessToken = jwtUtil.createAccessToken(
                        userDetails.getUsername(),
                        userDetails.getAuthorities().iterator().next().getAuthority()
                );

                // 쿠키만 갱신
                Cookie cookie = new Cookie("Authentication", newAccessToken);
                cookie.setHttpOnly(true);
                cookie.setSecure(false);
                cookie.setPath("/");
                cookie.setMaxAge(jwtUtil.getAccessTokenExpiration() + 60);
                response.addCookie(cookie);

                setAuthentication(newAccessToken);

            } catch (TokenExpiredException ex) {
                log.info("RefreshToken 만료, 재로그인 필요");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.sendRedirect(request.getContextPath() + "/user/login?error=true&message=SessionDeny");
                filterChain.doFilter(request, response);
                return;
            }
        }

        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        String username = jwtUtil.getUsername(token);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        UsernamePasswordAuthenticationToken auth =
                new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(auth);
    }
}
