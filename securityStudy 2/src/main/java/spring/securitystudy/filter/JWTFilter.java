package spring.securitystudy.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.filter.OncePerRequestFilter;
import spring.securitystudy.user.CustomUserDetails;
import spring.securitystudy.util.JWTUtil;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;
    private final UserDetailsService userDetailsService;

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

        // 1. Access Token 이 없는 경우
        if (accessToken == null){
            filterChain.doFilter(request, response);
            return;
        }

        // 2. Access Token 이 만료된 경우
        if (jwtUtil.isExpired(accessToken)) {
            // 2-1. Refresh Token 이 있는지 확인, Refresh Token 이 유효한지 확인
            if (refreshToken == null || jwtUtil.isExpired(refreshToken)){
                // 없으면 재로그인 필요
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // 401
                filterChain.doFilter(request, response);
                return;
            }

            logger.debug("토큰 갱신");
            String usernameFromRefreshToken = jwtUtil.getUsername(refreshToken);
            CustomUserDetails userDetails;

            try {
                userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(usernameFromRefreshToken);
            } catch (UsernameNotFoundException e){
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                filterChain.doFilter(request, response);
                return;
            }

            String newAccessToken = jwtUtil.createAccessToken(
                    userDetails.getUsername(),
                    userDetails.getAuthorities().iterator().next().getAuthority()
            );

            Cookie accessTokenCookie = new Cookie("Authentication", newAccessToken);
            accessTokenCookie.setHttpOnly(true);
            accessTokenCookie.setSecure(false); // HTTPS 사용 시 true로 설정하기
            accessTokenCookie.setPath("/");
            accessTokenCookie.setMaxAge(jwtUtil.getAccessTokenExpiration() + 60);

            response.addCookie(accessTokenCookie);

            Authentication auth =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);

            response.sendRedirect(request.getRequestURI());
            return;
        }

        String username = jwtUtil.getUsername(accessToken);

        try {
            CustomUserDetails userDetails = (CustomUserDetails) userDetailsService.loadUserByUsername(username);
            Authentication auth =
                    new UsernamePasswordAuthenticationToken(userDetails,
                            null, userDetails.getAuthorities());

            SecurityContext context = SecurityContextHolder.createEmptyContext();
            context.setAuthentication(auth);
            SecurityContextHolder.setContext(context);
        } catch (UsernameNotFoundException e){
            // 유저 정보 없음 -> 인증 안함
            SecurityContextHolder.clearContext();
        }

        filterChain.doFilter(request, response);
    }
}